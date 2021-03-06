package com.dili.ia.service;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.ApprovalParam;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-09 19:34:40.
 */
public interface RefundOrderService extends BaseService<RefundOrder, Long> {


    /**
     * 退款单 --提交
     * @param refundOrder 退款单
     * @return BaseOutput
     * */
    BaseOutput doSubmitDispatcher(RefundOrder refundOrder);
    /**
     * 退款单 --撤回
     * @param refundOrder 退款单
     * @return BaseOutput
     * */
    BaseOutput doWithdrawDispatcher(RefundOrder refundOrder);
    /**
     * 退款单 --撤回
     * @param refundOrder 退款单
     * @return BaseOutput
     * */
    BaseOutput doCancelDispatcher(RefundOrder refundOrder);

    /**
     * 退款单 --修改
     * @param refundOrder 退款单
     * @return BaseOutput
     * */
    BaseOutput<RefundOrder> doUpdatedHandler(RefundOrder refundOrder);

    /**
     * 退款单 --撤回
     * @param settleOrder 退款结算单
     * @return BaseOutput
     * */
    BaseOutput<RefundOrder> doRefundSuccessHandler(SettleOrder settleOrder);

    /**
     * 退款单 --新增
     * @param refundOrder 退款单
     * @return BaseOutput
     * */
    BaseOutput doAddHandler(RefundOrder refundOrder);
    /**
     * 票据打印数据加载
     * @param orderCode 订单号
     * @param reprint 是否补打标记
     * @return BaseOutput<PrintDataDto>
     */
    BaseOutput<PrintDataDto> queryPrintData(String orderCode, Integer reprint);

    /**
     * 提交审批
     * @param id 租赁单ID
     */
    void submitForApproval(Long id);

    /**
     * 审批通过
     * @param approvalParam
     */
    void approvedHandler(ApprovalParam approvalParam);

    /**
     * 审批拒绝
     * @param approvalParam
     */
    void approvedDeniedHandler (ApprovalParam approvalParam);

}