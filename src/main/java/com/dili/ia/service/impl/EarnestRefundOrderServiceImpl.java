package com.dili.ia.service.impl;

import com.dili.ia.domain.*;
import com.dili.ia.domain.dto.EarnestOrderPrintDto;
import com.dili.ia.domain.dto.EarnestRefundOrderPrintDto;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.RefundOrderMapper;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.util.BeanMapUtil;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-09 19:34:40.
 */
@Service
public class EarnestRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderService {

    public RefundOrderMapper getActualDao() {
        return (RefundOrderMapper)getDao();
    }
    @Autowired
    SettlementRpc settlementRpc;
    @Autowired
    DepartmentRpc departmentRpc;
    @Autowired
    CustomerAccountService customerAccountService;

    @Value("${settlement.app-id}")
    private Long settlementAppId;
    @Override
    public Set<Integer> getBizType() {
        return Sets.newHashSet(BizTypeEnum.EARNEST.getCode());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput submitRefundOrder(RefundOrder refundOrder) {
        //@TODO改状态，提交到结算中心,冻结定金
        if (!refundOrder.getState().equals(RefundOrderStateEnum.CREATED.getCode())){
            return BaseOutput.failure("提交失败，状态已变更！");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        refundOrder.setState(RefundOrderStateEnum.SUBMITTED.getCode());
        refundOrder.setSubmitTime(new Date());
        refundOrder.setSubmitterId(userTicket.getId());
        refundOrder.setSubmitter(userTicket.getRealName());
        this.update(refundOrder);

        customerAccountService.frozenEarnest(refundOrder.getCustomerId(), refundOrder.getMarketId(), refundOrder.getTotalRefundAmount());
        //提交到结算中心 --- 执行顺序不可调整！！因为异常只能回滚自己系统，无法回滚其它远程系统
        BaseOutput<SettleOrder> out= settlementRpc.submit(buildSettleOrderDto(userTicket, refundOrder));
        if (!out.isSuccess()){
            LOGGER.info("提交到结算中心失败！" + out.getMessage() + out.getErrorData());
            throw new RuntimeException("提交到结算中心失败！" + out.getMessage());
        }

        return BaseOutput.success();
    }

    //组装 -- 结算中心缴费单 SettleOrder
    private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, RefundOrder ro){
        SettleOrderDto settleOrder = new SettleOrderDto();
        //以下是提交到结算中心的必填字段
        settleOrder.setMarketId(ro.getMarketId()); //市场ID
        settleOrder.setMarketCode(userTicket.getFirmCode());
        settleOrder.setBusinessCode(ro.getCode()); //业务单号
        settleOrder.setCustomerId(ro.getCustomerId());//客户ID
        settleOrder.setCustomerName(ro.getCustomerName());// "客户姓名
        settleOrder.setCustomerPhone(ro.getCustomerCellphone());//"客户手机号
        settleOrder.setAmount(ro.getTotalRefundAmount()); //金额

        //@TODO部门待处理
        settleOrder.setBusinessDepId(1L); //"业务部门ID
        settleOrder.setBusinessDepName("测试部门");//"业务部门名称
        settleOrder.setSubmitterId(userTicket.getId());// "提交人ID
        settleOrder.setSubmitterName(userTicket.getRealName());// "提交人姓名
        settleOrder.setSubmitterDepId(userTicket.getDepartmentId()); //"提交人部门ID
        settleOrder.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        settleOrder.setSubmitTime(LocalDateTime.now());
        settleOrder.setAppId(settlementAppId);//应用ID
        settleOrder.setBusinessType(BizTypeEnum.EARNEST.getCode()); // 业务类型
        settleOrder.setType(SettleTypeEnum.REFUND.getCode());// "结算类型  -- 退款
        settleOrder.setState(SettleStateEnum.WAIT_DEAL.getCode());
//        settleOrder.setAccountNumber();
//        settleOrder.setBankName();
//        settleOrder.setBankCardHolder();
//        settleOrder.setWay();
//        settleOrder.setRetryRecordId();

        String returnUrl = "http://ia.diligrp.com/earnestOrder/paySuccess";
        settleOrder.setReturnUrl(returnUrl); // 结算-- 缴费成功后回调路径

        return settleOrder;
    }

    @Override
    public BaseOutput<PrintDataDto> queryPrintData(RefundOrder refundOrder, Integer reprint) {
        if (!RefundOrderStateEnum.REFUNDED.getCode().equals(refundOrder.getState())) {
            return BaseOutput.failure("此单未退款");
        }
        PrintDataDto printDataDto = new PrintDataDto();
        EarnestRefundOrderPrintDto erPrintDto = new EarnestRefundOrderPrintDto();
        erPrintDto.setPrintTime(new Date());
        erPrintDto.setReprint(reprint == 2 ? "(补打)" : "");
        erPrintDto.setCode(refundOrder.getCode());

        erPrintDto.setCustomerName(refundOrder.getCustomerName());
        erPrintDto.setCustomerCellphone(refundOrder.getCustomerCellphone());
        erPrintDto.setRefundReason(refundOrder.getRefundReason());
        erPrintDto.setAmount(MoneyUtils.centToYuan(refundOrder.getTotalRefundAmount()));
        //@TODO退款单冗余结算员
        erPrintDto.setSettlementOperator(refundOrder.getSubmitter());
        erPrintDto.setSubmitter(refundOrder.getSubmitter());
        erPrintDto.setBusinessType(BizTypeEnum.EARNEST.getName());
        erPrintDto.setPayee(refundOrder.getPayee());
        erPrintDto.setBank(refundOrder.getBank());
        erPrintDto.setBankCardNo(refundOrder.getBankCardNo());
        //@TODO 退款方式定义
        erPrintDto.setRefundType(SettleWayEnum.getNameByCode(refundOrder.getRefundType()));

        printDataDto.setItem(BeanMapUtil.beanToMap(erPrintDto));
        printDataDto.setName(PrintTemplateEnum.EARNEST_REFUND_ORDER.getCode());
        return BaseOutput.success().setData(printDataDto);
    }

    @Override
    public BaseOutput withdrawRefundOrder(RefundOrder refundOrder) {
        return BaseOutput.success("未实现，待做");
    }

    @Override
    public BaseOutput refundSuccessHandler(Long refundOrderId) {
        return null;
    }

}