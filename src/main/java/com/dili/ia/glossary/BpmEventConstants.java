package com.dili.ia.glossary;

/**
 * 流程事件常量
 */
public interface BpmEventConstants {
    /**
     * 修改事件
     */
    String UPDATE_EVENT = "updateEvent";
    /**
     * 取消事件
     */
    String CANCEL_EVENT = "cancelEvent";
    /**
     * 提交付款事件
     */
    String SUBMIT_EVENT = "submitEvent";
    /**
     * 撤回事件
     */
    String WITHDRAW_EVENT = "withdrawEvent";
    /**
     * 到期事件
     */
    String EXPIRED_EVENT = "expiredEvent";
    /**
     * 补录事件
     */
    String SUPPLEMENT_EVENT = "supplementEvent";
    /**
     * 确认付款事件
     */
    String PAID_EVENT = "paidEvent";
    /**
     * 提交审批事件
     */
    String SUBMIT_APPROVAL_EVENT = "submitApprovalEvent";
    /**
     * 作废事件
     */
    String OBSOLETE_EVENT = "obsoleteEvent";
    /**
     * 续租事件
     */
    String RELET_EVENT = "reletEvent";
    /**
     * 开票事件
     */
    String INVOICE_EVENT = "invoiceEvent";

    /**
     * java接收任务: 提交
     */
    String SUBMITTED_RECEIVE_TASK = "submitted";

    /**
     * java接收任务: 未生效
     */
    String NOT_ACTIVE_RECEIVE_TASK = "notActive";

    /**
     * java接收任务: 已生效
     */
    String ACTIVE_RECEIVE_TASK = "active";

    /**
     * 确认退款事件
     */
    String REFUND_EVENT = "refundEvent";

}
