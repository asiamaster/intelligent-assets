package com.dili.ia.service;

import com.dili.ia.domain.AssetsLeaseOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.AssetsLeaseOrderListDto;
import com.dili.ia.domain.dto.AssetsLeaseSubmitPaymentDto;
import com.dili.ia.domain.dto.ApprovalParam;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.domain.dto.LeaseRefundOrderDto;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 14:40:05.
 */
public interface AssetsLeaseOrderService extends BaseService<AssetsLeaseOrder, Long> {
    /**
     * 摊位租赁订单保存
     * @param dto
     * @return
     */
    BaseOutput saveLeaseOrder(AssetsLeaseOrderListDto dto);

    /**
     * 客户状态检查
     * @param customerId
     * @param marketId
     */
    void checkCustomerState(Long customerId,Long marketId);

    /**
     * 提交付款
     * @param assetsLeaseSubmitPaymentDto
     * @return
     */
    BaseOutput submitPayment(AssetsLeaseSubmitPaymentDto assetsLeaseSubmitPaymentDto);

    /**
     * 提交审批
     * @param id 租赁单ID
     */
    void submitForApproval(Long id);

    /**
     * 摊位租赁订单取消
     * @param id
     * @return
     */
    BaseOutput cancelOrder(Long id);

    /**
     * 摊位租赁订单作废
     * @param id
     * @return
     */
    BaseOutput invalidOrder(Long id, String invalidReason);

    /**
     * 摊位租赁订单撤回
     * @param id
     * @return
     */
    BaseOutput withdrawOrder(Long id);

    /**
     * 根据结算信息修改摊位租赁相关信息
     * @param settleOrder
     * @return
     */
    BaseOutput<Boolean> updateLeaseOrderBySettleInfo(SettleOrder settleOrder);

    /**
     * 租赁单生效处理
     * @param o
     */
    void leaseOrderEffectiveHandler(AssetsLeaseOrder o);

    /**
     * 租赁单到期处理
     * @param o
     */
    void leaseOrderExpiredHandler(AssetsLeaseOrder o);



    /**
     * 退款申请
     * @param refundOrderDto
     * @return
     */
    BaseOutput createOrUpdateRefundOrder(LeaseRefundOrderDto refundOrderDto);

    /**
     * 取消退款单回调处理
     * @param leaseOrderItemId
     * @return
     */
    BaseOutput cancelRefundOrderHandler(Long leaseOrderItemId);

    /**
     * 结算退款单成功回调处理
     * @param refundOrder
     * @return
     */
    BaseOutput settleSuccessRefundOrderHandler(RefundOrder refundOrder);

    /**
     * 补录
     * @param leaseOrder
     * @return
     */
    BaseOutput supplement(AssetsLeaseOrder leaseOrder);

    /**
     * 审批通过处理
     * @param approvalParam 审批参数
     * @return
     */
    void approvedHandler(ApprovalParam approvalParam);

    /**
     * 审批拒绝处理
     * @param approvalParam 审批参数
     * @return
     */
    void approvedDeniedHandler(ApprovalParam approvalParam);
}