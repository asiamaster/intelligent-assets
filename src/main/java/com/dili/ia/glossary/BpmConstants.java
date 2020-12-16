package com.dili.ia.glossary;

/**
 * 业务流程常量
 */
public interface BpmConstants {

    // =======================    租赁流程定义    ================================
    /**
     * 摊位租赁审批流程KEY
     */
    String PK_RENTAL_APPROVAL_PROCESS = "rentalApprovalProcess";

    /**
     * 摊位租赁业务流程KEY
     */
    String PK_BOOTH_LEASE_ORDER_PROCESS = "boothLeaseOrderBpm";
    /**
     * 公寓租赁业务流程KEY
     */
    String PK_LODGING_LEASE_ORDER_PROCESS = "lodgingLeaseOrderBpm";
    /**
     * 冷库租赁业务流程KEY，现在使用和公寓一样的业务流程
     */
//    String PK_LOCATION_LEASE_ORDER_PROCESS = "locationLeaseOrderBpm";
    String PK_LOCATION_LEASE_ORDER_PROCESS = "lodgingLeaseOrderBpm";

    // =======================    退款流程定义    ================================
    /**
     * 摊位退款审批流程KEY
     */
    String PK_REFUND_APPROVAL_PROCESS = "rentalRefundApprovalProcess";
    /**
     * 摊位退款业务流程KEY
     */
    String PK_BOOTH_LEASE_REFUND_ORDER_PROCESS = "boothLeaseRefundOrderBpm";
    /**
     * 公寓租赁业务流程KEY
     */
    String PK_LODGING_LEASE_REFUND_ORDER_PROCESS = "lodgingLeaseRefundOrderBpm";
    /**
     * 冷库租赁业务流程KEY，现在使用和公寓一样的业务流程
     */
//    String PK_LOCATION_LEASE_REFUND_ORDER_PROCESS = "locationLeaseRefundOrderBpm";
    String PK_LOCATION_LEASE_REFUND_ORDER_PROCESS = "lodgingLeaseRefundOrderBpm";
    /**
     * 定金业务流程KEY，现在使用和公寓一样的业务流程
     */
    String PK_EARNEST_REFUND_ORDER_PROCESS = "lodgingLeaseRefundOrderBpm";
    /**
     * 保证金业务流程KEY，现在使用和公寓一样的业务流程
     */
    String PK_DEPOSIT_ORDER_REFUND_ORDER_PROCESS = "lodgingLeaseRefundOrderBpm";

}
