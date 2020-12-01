package com.dili.ia.glossary;

/**
 * 业务流程常量
 */
public interface BpmConstants {

    // =======================    租赁流程定义    ================================
    /**
     * 租赁审批流程KEY
     */
    String PK_RENTAL_APPROVAL_PROCESS = "rentalApprovalProcess";

    /**
     * 摊位租赁业务流程KEY
     */
    String PK_BOOTH_LEASE_ORDER_PROCESS = "boothLeaseOrderBpm";

    /**
     * 冷库租赁业务流程KEY
     */
    String PK_LOCATION_LEASE_ORDER_PROCESS = "locationLeaseOrderBpm";

    /**
     * 公寓租赁业务流程KEY
     */
    String PK_LODGING_LEASE_ORDER_PROCESS = "lodgingLeaseOrderBpm";

    // =======================    退款流程定义    ================================
    /**
     * 退款审批流程KEY
     */
    String PK_REFUND_APPROVAL_PROCESS = "rentalRefundApprovalProcess";
    /**
     * 退款业务流程KEY
     */
    String PK_BOOTH_LEASE_REFUND_ORDER_PROCESS = "boothLeaseRefundOrderBpm";
    /**
     * 冷库租赁业务流程KEY
     */
    String PK_LOCATION_LEASE_REFUND_ORDER_PROCESS = "locationLeaseRefundOrderBpm";

    /**
     * 公寓租赁业务流程KEY
     */
    String PK_LODGING_LEASE_REFUND_ORDER_PROCESS = "lodgingLeaseRefundOrderBpm";

}
