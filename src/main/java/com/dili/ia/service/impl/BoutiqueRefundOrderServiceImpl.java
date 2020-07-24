package com.dili.ia.service.impl;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.BoutiqueOrderStateEnum;
import com.dili.ia.glossary.StockInStateEnum;
import com.dili.ia.service.BoutiqueEntranceRecordService;
import com.dili.ia.service.BoutiqueFeeOrderService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

public class BoutiqueRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService {

    @Autowired
    private BoutiqueFeeOrderService boutiqueFeeOrderService;

    @Autowired
    private BoutiqueEntranceRecordService boutiqueEntranceRecordService;

    /**
     * 退款单 -- 提交
     * 
     * @param
     * @return 
     * @date   2020/7/21
     */
    @Override
    public BaseOutput submitHandler(RefundOrder refundOrder) {

        BoutiqueFeeOrderDto orderDto = boutiqueEntranceRecordService.getBoutiqueAndOrderByCode(refundOrder.getCode());
        if (!BoutiqueOrderStateEnum.PAID.getCode().equals(orderDto.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
        }

        this.updateState(refundOrder.getCode(), orderDto.getVersion(), BoutiqueOrderStateEnum.SUBMITTED_REFUND);
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 撤回
     *
     * @param
     * @return
     * @date   2020/7/21
     */
    @Override
    public BaseOutput withdrawHandler(RefundOrder refundOrder) {

        BoutiqueFeeOrderDto orderDto = boutiqueEntranceRecordService.getBoutiqueAndOrderByCode(refundOrder.getCode());
        if (!BoutiqueOrderStateEnum.SUBMITTED_REFUND.getCode().equals(orderDto.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
        }

        this.updateState(refundOrder.getCode(), orderDto.getVersion(), BoutiqueOrderStateEnum.PAID);
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 退款成功回调
     *
     * @param
     * @return
     * @date   2020/7/21
     */
    @Override
    public BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {

        BoutiqueFeeOrderDto orderDto = boutiqueEntranceRecordService.getBoutiqueAndOrderByCode(refundOrder.getCode());
        if (!BoutiqueOrderStateEnum.PAID.getCode().equals(orderDto.getState())) {
            throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
        }

        this.updateState(refundOrder.getCode(), orderDto.getVersion(), BoutiqueOrderStateEnum.REFUNDED);

        return BaseOutput.success();
    }

    /**
     * 退款单 -- 取消
     *
     * @param
     * @return
     * @date   2020/7/21
     */
    @Override
    public BaseOutput cancelHandler(RefundOrder refundOrder) {
        this.withdrawHandler(refundOrder);

        return BaseOutput.success();
    }

    /**
     * 退款单 -- 业务数据加载
     *
     * @param
     * @return
     * @date   2020/7/21
     */
    @Override
    public BaseOutput<Map<String, Object>> buildBusinessPrintData(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 获取业务类型
     *
     * @param
     * @return
     * @date   2020/7/21
     */
    @Override
    public Set<String> getBizType() {
        return Sets.newHashSet(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode());
    }

    private void updateState(String code, Integer version, BoutiqueOrderStateEnum state) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BoutiqueFeeOrder domain = new BoutiqueFeeOrder();
        domain.setVersion(version + 1);
        domain.setState(state.getCode());
        domain.setOperatorId(userTicket.getId());
        domain.setOperatorName(userTicket.getRealName());

        BoutiqueFeeOrder condition = new BoutiqueFeeOrder();
        condition.setCode(code);
        condition.setVersion(version);

        // 修改精品停车交费单状态
        int row = boutiqueFeeOrderService.updateSelectiveByExample(domain, condition);
        if (row != 1) {
            throw new BusinessException(ResultCode.DATA_ERROR, "业务繁忙,稍后再试");
        }
    }
}
