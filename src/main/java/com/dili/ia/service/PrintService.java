package com.dili.ia.service;

import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ss.domain.BaseOutput;

/**
 * 打印服务
 */
public interface PrintService {
    /**
     * 查询打印结算票据数据
     * @param orderCode 缴费单编号
     * @param reprint 是否补打
     * @return
     */
    BaseOutput<PrintDataDto> queryPrintLeaseSettlementBillData(String orderCode, Integer reprint);

    /**
     * 查询打印合同签订单数据（包含保证金）
     *
     * @param leaseOrderId 租赁单ID
     * @return
     */
    BaseOutput<PrintDataDto> queryPrintLeaseContractSigningBillData(Long leaseOrderId);

    /**
     * 租赁定金单打印数据查询（包含保证金）
     *
     * @param leaseOrderId 租赁单ID
     * @return
     */
    BaseOutput<PrintDataDto> queryPrintLeasePaymentBillData(Long leaseOrderId);
}
