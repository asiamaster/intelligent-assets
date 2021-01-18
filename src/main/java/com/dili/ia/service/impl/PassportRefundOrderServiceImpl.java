package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.Passport;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.printDto.PassportPrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.ChargeItemCodeEnum;
import com.dili.ia.glossary.PassportStateEnum;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.PassportService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.ia.util.BeanMapUtil;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.logger.sdk.component.MsgService;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
@Service
public class PassportRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService {

    private final static Logger logger = LoggerFactory.getLogger(PassportRefundOrderServiceImpl.class);

    @Autowired
    MsgService msgService;

    @Autowired
    private PassportService passportService;

    @Autowired
    private CustomerAccountService customerAccountService;
    @Autowired
    private BusinessChargeItemService businessChargeItemService;

    /**
     * 退款单 -- 提交(无需改变通行证缴费单的状态)
     * 
     * @param  refundOrder
     * @return BaseOutput
     * @date   2020/7/21
     */
    @Override
    public BaseOutput submitHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 撤回(无需改变通行证缴费单的状态)
     *
     * @param  refundOrder
     * @return BaseOutput
     * @date   2020/7/21
     */
    @Override
    public BaseOutput withdrawHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 退款成功回调(将通行证缴费单的状态由退款中修改为已退款)
     *
     * @param  settleOrder
     * @param  refundOrder
     * @return BaseOutput
     * @date   2020/7/21
     */
    @Override
    public BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
        Passport passportInfo = passportService.getPassportByCode(refundOrder.getBusinessCode());
        if (passportInfo != null ){
            if (!PassportStateEnum.SUBMITTED_REFUND.getCode().equals(passportInfo.getState())) {
                throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
            }

            this.updateState(refundOrder.getBusinessCode(), passportInfo.getVersion(), PassportStateEnum.REFUNDED);
        }

        //记录退款日志
        msgService.sendBusinessLog(recordRefundLog(refundOrder));

        return BaseOutput.success();
    }

    /**
     * 退款单 -- 取消(将通行证缴费单的状态由退款中还原原状态 - 未生效/已生效/已过期)
     *
     * @param  refundOrder
     * @return BaseOutput
     * @date   2020/7/21
     */
    @Override
    public BaseOutput cancelHandler(RefundOrder refundOrder) {
        Passport passportInfo = passportService.getPassportByCode(refundOrder.getBusinessCode());
        if (passportInfo != null ) {
            if (!PassportStateEnum.SUBMITTED_REFUND.getCode().equals(passportInfo.getState())) {
                throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
            }

            PassportStateEnum state = PassportStateEnum.NOT_START;
            if (passportInfo.getStartTime() != null && passportInfo.getEndTime() != null && passportInfo.getStartTime().isBefore(LocalDateTime.now())
                    && LocalDateTime.now().isBefore(passportInfo.getEndTime())) {
                // 已生效
                state = PassportStateEnum.IN_FORCE;
            } else if (passportInfo.getEndTime() != null && passportInfo.getEndTime().isBefore(LocalDateTime.now())) {
                // 已过期
                state = PassportStateEnum.EXPIRED;
            }

            this.updateState(refundOrder.getBusinessCode(), passportInfo.getVersion(), state);
        }

        return BaseOutput.success();
    }

    /**
     * 退款单 -- 业务数据加载
     *
     * @param  refundOrder
     * @return BaseOutput
     * @date   2020/7/21
     */
    @Override
    public BaseOutput<Map<String, Object>> buildBusinessPrintData(RefundOrder refundOrder) {
        PrintDataDto<PassportPrintDto> reprint = passportService.receiptRefundPrintData(refundOrder.getCode(), "reprint");
        Map<String, Object> resultMap = new HashMap<>();
        //已交清退款单打印数据
        resultMap.put("printTemplateCode",reprint.getName());
        //根据要求拼装订单项
        resultMap.putAll(BeanMapUtil.beanToMap(reprint.getItem()));
        return BaseOutput.success().setData(resultMap);
    }

    /**
     * 退款单 -- 获取业务类型
     *
     * @return Set
     * @date   2020/7/21
     */
    @Override
    public Set<String> getBizType() {
        return Sets.newHashSet(BizTypeEnum.PASSPORT.getCode());
    }

    /**
     * 具体的修改操作
     */
    private void updateState(String code, Integer version, PassportStateEnum state) {

        Passport domain = new Passport();
        domain.setVersion(version + 1);
        domain.setState(state.getCode());
        domain.setModifyTime(LocalDateTime.now());

        Passport condition = new Passport();
        condition.setCode(code);
        condition.setVersion(version);

        // 修改通行证交费单状态
        int row = passportService.updateSelectiveByExample(domain, condition);
        if (row != 1) {
            throw new BusinessException(ResultCode.DATA_ERROR, "业务繁忙,稍后再试");
        }
    }

    /**
     * 退款时,组装收费项
     *
     * @param
     * @return
     * @date   2020/12/17
     */
    @Override
    public List<SettleFeeItem> buildSettleFeeItem(RefundOrder refundOrder) {
        //组装费用项
        Passport passport = passportService.get(refundOrder.getBusinessId());
        if (passport == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "通行证业务单已删除");
        }
        //组装费用项
        List<SettleFeeItem> settleFeeItemList = new ArrayList<>();
        SettleFeeItem sfItem = new SettleFeeItem();
        List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.queryFixedBusinessChargeItemConfig(passport.getMarketId(), BizTypeEnum.PASSPORT.getCode(), YesOrNoEnum.YES.getCode(), YesOrNoEnum.YES.getCode(), ChargeItemCodeEnum.PASSPORT.getCode());
        BusinessChargeItemDto chargeItemDto = chargeItemDtos.stream().findFirst().orElse(null);
        if (null == chargeItemDto){
            logger.info("业务没有查询到固定的收费项，code={}", ChargeItemCodeEnum.PASSPORT.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "业务没有查询到固定的收费项");
        }
        //静态收费项来源于基础数据中心收费项配置
        sfItem.setChargeItemId(chargeItemDto.getId());
        //静态收费项名称
        sfItem.setChargeItemName(chargeItemDto.getChargeItem());
        sfItem.setAmount(refundOrder.getTotalRefundAmount());
        settleFeeItemList.add(sfItem);

        return settleFeeItemList;
    }

    /**
     * 记录退款日志
     *
     * @param refundOrder 退款单
     */
    private BusinessLog recordRefundLog(RefundOrder refundOrder) {
        BusinessLog businessLog = new BusinessLog();
        businessLog.setBusinessId(refundOrder.getBusinessId());
        businessLog.setBusinessCode(refundOrder.getBusinessCode());
        businessLog.setContent(refundOrder.getSettlementCode());
        businessLog.setOperationType("refund");
        businessLog.setMarketId(refundOrder.getMarketId());
        businessLog.setOperatorId(refundOrder.getRefundOperatorId());
        businessLog.setOperatorName(refundOrder.getRefundOperator());
        businessLog.setBusinessType(LogBizTypeConst.PASSPORT);
        businessLog.setSystemCode("IA");
        return businessLog;
    }
}
