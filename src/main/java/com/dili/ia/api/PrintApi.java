package com.dili.ia.api;

import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.service.PrintService;
import com.dili.ss.domain.BaseOutput;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 打印api
 */
@RestController
@RequestMapping("/api")
public class PrintApi {
    private final static Logger LOG = LoggerFactory.getLogger(PrintApi.class);
    @Autowired
    private PrintService printService;

    /**
     * 租赁单缴费打印数据查询(杭州)
     *
     * @return
     */
    @GetMapping(value = "/leaseOrder/queryPrintData")
    public @ResponseBody
    BaseOutput<PrintDataDto> queryPrintLeaseSettlementBillData(String orderCode, Integer reprint) {
        try {
            if (StringUtils.isBlank(orderCode) || null == reprint) {
                return BaseOutput.failure("参数错误");
            }
            return printService.queryPrintLeaseSettlementBillData(orderCode, reprint);
        } catch (Exception e) {
            LOG.error("租赁单缴费打印异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 合同签订单打印数据查询(杭州)
     *
     * @param leaseOrderId
     * @return
     */
    @GetMapping(value = "/leaseOrder/queryPrintLeaseContractSigningBillData")
    public @ResponseBody
    BaseOutput<PrintDataDto> queryPrintLeaseContractSigningBillData(@RequestParam Long leaseOrderId) {
        try {
            return printService.queryPrintLeaseContractSigningBillData(leaseOrderId);
        } catch (Exception e) {
            LOG.error("合同签订单打印异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 合同签订单打印数据查询(杭州)
     *
     * @return
     */
    @GetMapping(value = "/leaseOrder/queryPrintLeasePaymentBillData")
    public @ResponseBody
    BaseOutput<PrintDataDto> queryPrintLeasePaymentBillData(@RequestParam Long leaseOrderId) {
        try {
            return printService.queryPrintLeasePaymentBillData(leaseOrderId);
        } catch (Exception e) {
            LOG.error("合同签订单打印异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

}
