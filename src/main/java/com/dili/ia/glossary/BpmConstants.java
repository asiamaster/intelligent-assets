package com.dili.ia.glossary;

/**
 * 业务流程常量
 */
public interface BpmConstants {
    // =======================    通用流程定义    ================================
    /**
     * 通用租赁业务流程KEY
     */
    String PK_LEASE_ORDER_PROCESS = "leaseOrderBpm";

    /**
     * 通用退款业务流程KEY，现在使用和公寓一样的业务流程
     */
    String PK_REFUND_ORDER_PROCESS = "refundOrderBpm";

    /**
     * 通用租赁审批流程KEY
     */
    String PK_LEASE_APPROVAL_PROCESS = "leaseApprovalProcess";
    /**
     * 通用退款审批流程KEY
     */
    String PK_REFUND_APPROVAL_PROCESS = "refundApprovalProcess";
    // =======================    租赁流程定义    ================================
    /**
     * 摊位租赁审批流程KEY
     */
    String PK_BOOTH_LEASE_APPROVAL_PROCESS = PK_LEASE_APPROVAL_PROCESS;

    /**
     * 摊位租赁业务流程KEY
     */
    String PK_BOOTH_LEASE_ORDER_PROCESS = PK_LEASE_ORDER_PROCESS;
    /**
     * 公寓租赁业务流程KEY
     */
    String PK_LODGING_LEASE_ORDER_PROCESS = PK_LEASE_ORDER_PROCESS;
    /**
     * 冷库租赁业务流程KEY，现在使用和公寓一样的业务流程
     */
    String PK_LOCATION_LEASE_ORDER_PROCESS = PK_LEASE_ORDER_PROCESS;

    // =======================    退款流程定义    ================================
    /**
     * 摊位退款审批流程KEY
     */
    String PK_BOOTH_REFUND_APPROVAL_PROCESS = PK_REFUND_APPROVAL_PROCESS;
    /**
     * 摊位退款业务流程KEY
     */
    String PK_BOOTH_LEASE_REFUND_ORDER_PROCESS = PK_REFUND_ORDER_PROCESS;
    /**
     * 公寓租赁业务流程KEY
     */
    String PK_LODGING_LEASE_REFUND_ORDER_PROCESS = PK_REFUND_ORDER_PROCESS;
    /**
     * 冷库租赁业务流程KEY，现在使用和公寓一样的业务流程
     */
    String PK_LOCATION_LEASE_REFUND_ORDER_PROCESS = PK_REFUND_ORDER_PROCESS;
    /**
     * 定金业务流程KEY，现在使用和公寓一样的业务流程
     */
    String PK_EARNEST_REFUND_ORDER_PROCESS = PK_REFUND_ORDER_PROCESS;
    /**
     * 保证金业务流程KEY，现在使用和公寓一样的业务流程
     */
    String PK_DEPOSIT_ORDER_REFUND_ORDER_PROCESS = PK_REFUND_ORDER_PROCESS;

}
