package com.dili.ia.glossary;

/**
 * 业务流程常量
 */
public interface BpmConstants {
    /**
     * 租赁审批流程KEY
     */
    String PK_RENTAL_APPROVAL_PROCESS = "rentalApprovalProcess";
    /**
     * 退款审批流程KEY
     */
    String PK_REFUND_APPROVAL_PROCESS = "rentalRefundApprovalProcess";

    /**
     * 市场负责人审批，任务定义key
     */
    String TK_MANAGER_APPROVAL = "managerApproval";

    /**
     * 分管领导审批，任务定义key
     */
    String TK_VICE_GENERAL_MANAGER_APPROVAL = "viceGeneralManagerApproval";

    /**
     * 总经理审批，任务定义key
     */
    String TK_GENERAL_MANAGER_APPROVAL = "generalManagerApproval";


}
