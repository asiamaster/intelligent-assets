package com.dili.ia.api;

import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.service.PrintService;
import com.dili.ss.domain.BaseOutput;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
     * 租赁单缴费打印数据查询
     * @return
     */
    @RequestMapping(value="/leaseOrder/queryPrintData")
    public @ResponseBody BaseOutput<PrintDataDto> queryPrintLeaseOrderData(String orderCode, Integer reprint){
        try{
            if(StringUtils.isBlank(orderCode) || null == reprint){
                return BaseOutput.failure("参数错误");
            }
            return printService.queryPrintLeaseOrderData(orderCode,reprint);
        }catch (Exception e){
            LOG.error("租赁单缴费打印异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 合同签订单打印数据查询
     *
     * @return
     */
    @RequestMapping(value = "/queryPrintContractSigningData")
    public @ResponseBody
    BaseOutput<PrintDataDto> queryPrintContractSigningData(String code) {
        try {
            if (StringUtils.isBlank(code)) {
                return BaseOutput.failure("参数错误");
            }
            return printService.queryPrintContractSigningData(code);
        } catch (Exception e) {
            LOG.error("合同签订单打印异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

}
