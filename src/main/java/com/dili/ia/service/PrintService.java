package com.dili.ia.service;

import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ss.domain.BaseOutput;

public interface PrintService {
    /**
     * 查询打印数据
     * @param orderCode
     * @param reprint
     * @return
     */
    BaseOutput<PrintDataDto> queryPrintLeaseOrderData(String orderCode, Integer reprint);

    /**
     * 查询打印合同签订单数据
     *
     * @param code
     * @return
     */
    BaseOutput<PrintDataDto> queryPrintContractSigningData(String code);
}
