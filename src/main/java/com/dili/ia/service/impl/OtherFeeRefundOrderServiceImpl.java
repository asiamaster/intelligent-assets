package com.dili.ia.service.impl;

import com.dili.ia.domain.OtherFee;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.printDto.OtherFeePrintDto;
import com.dili.ia.domain.dto.printDto.PassportPrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.OtherFeeStateEnum;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.OtherFeeService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.ia.util.BeanMapUtil;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.logger.sdk.component.MsgService;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.enums.ChargeItemEnum;
import com.dili.settlement.enums.FeeTypeEnum;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class OtherFeeRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService {

    private final static Logger logger = LoggerFactory.getLogger(OtherFeeRefundOrderServiceImpl.class);

    @Autowired
    MsgService msgService;

    @Autowired
    private OtherFeeService otherFeeService;

    @Autowired
    private CustomerAccountService customerAccountService;

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
        OtherFee otherFeeInfo = otherFeeService.getOtherFeeByCode(refundOrder.getBusinessCode());
        if (otherFeeInfo != null) {
            if (!OtherFeeStateEnum.REFUNDING.getCode().equals(otherFeeInfo.getState())) {
                throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
            }

            OtherFee domain = new OtherFee();
            domain.setVersion(refundOrder.getVersion() + 1);
            domain.setState(OtherFeeStateEnum.REFUND.getCode());
            domain.setModifyTime(LocalDateTime.now());
            // 将退款金额修改
            domain.setRefundAmount(refundOrder.getPayeeAmount() + otherFeeInfo.getRefundAmount());

            OtherFee condition = new OtherFee();
            condition.setCode(refundOrder.getBusinessCode());

            // 修改通行证交费单状态
            int row = otherFeeService.updateSelectiveByExample(domain, condition);
            if (row != 1) {
                throw new BusinessException(ResultCode.DATA_ERROR, "业务繁忙,稍后再试");
            }
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
        OtherFee otherFee = new OtherFee();
        otherFee.setCode(refundOrder.getBusinessCode());
        OtherFee otherFeeInfo = otherFeeService.list(otherFee).get(0);
        if (otherFeeInfo != null) {
            if (!OtherFeeStateEnum.REFUNDING.getCode().equals(otherFeeInfo.getState())) {
                throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
            }

            this.updateState(refundOrder.getBusinessCode(), otherFeeInfo.getVersion(), OtherFeeStateEnum.PAID);
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
        PrintDataDto<OtherFeePrintDto> reprint = otherFeeService.receiptRefundPrintData(refundOrder.getCode(), "reprint");
        Map<String, Object> resultMap = new HashMap<>();
        //已交清退款单打印数据
        resultMap.put("printTemplateCode",reprint.getName());
        //根据要求拼装订单项
        resultMap.putAll(BeanMapUtil.beanToMap(reprint.getItem()));
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 获取业务类型
     *
     * @return Set
     * @date   2020/7/21
     */
    @Override
    public Set<String> getBizType() {
        return Sets.newHashSet(BizTypeEnum.OTHER_FEE.getCode());
    }

    /**
     * 具体的修改操作
     */
    private void updateState(String code, Integer version, OtherFeeStateEnum state) {

        OtherFee domain = new OtherFee();
        domain.setVersion(version + 1);
        domain.setState(state.getCode());
        domain.setModifyTime(LocalDateTime.now());

        OtherFee condition = new OtherFee();
        condition.setCode(code);
        condition.setVersion(version);

        // 修改通行证交费单状态
        int row = otherFeeService.updateSelectiveByExample(domain, condition);
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
        OtherFee otherFee = otherFeeService.get(refundOrder.getBusinessId());
        if (otherFee == null) {
            throw new BusinessException(ResultCode.DATA_ERROR, "其他收费业务单已删除");
        }
        //组装费用项
        List<SettleFeeItem> settleFeeItemList = new ArrayList<>();
        SettleFeeItem sfItem = new SettleFeeItem();
        sfItem.setChargeItemId(otherFee.getChargeItemId());
        sfItem.setChargeItemName(otherFee.getChargeItemName());
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
        businessLog.setBusinessType(LogBizTypeConst.OTHER_FEE);
        businessLog.setSystemCode("IA");
        return businessLog;
    }
}
