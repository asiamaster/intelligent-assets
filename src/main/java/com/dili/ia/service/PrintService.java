package com.dili.ia.service;

import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ss.domain.BaseOutput;

/**
 * 打印服务
 */
public interface PrintService {
    /**
     * 查询打印数据
     * @param orderCode
     * @param reprint
     * @return
     */
    BaseOutput<PrintDataDto> queryPrintLeaseSettlementBillData(String orderCode, Integer reprint);

    /**
     * 查询打印合同签订单数据
     *
     * @param leaseOrderId
     * @return
     */
    BaseOutput<PrintDataDto> queryPrintLeaseContractSigningBillData(Long leaseOrderId);

    /**
     * 租赁定金单打印数据查询
     *
     * @param leaseOrderId
     * @return
     */
    BaseOutput<PrintDataDto> queryPrintLeasePaymentBillData(Long leaseOrderId);
}
