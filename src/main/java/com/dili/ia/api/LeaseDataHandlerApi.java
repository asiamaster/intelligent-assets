package com.dili.ia.api;

import com.dili.ia.service.AssetsLeaseDataHandlerService;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 摊位租赁数据处理api
 */
@RestController
@RequestMapping("/api/leaseDataHandler")
public class LeaseDataHandlerApi {
    private final static Logger LOG = LoggerFactory.getLogger(LeaseDataHandlerApi.class);
    @Autowired
    AssetsLeaseDataHandlerService assetsLeaseDataHandlerService;


    /**
     * 去掉保证金后数据处理
     *
     * @return
     */
    @GetMapping(value = "/leaseOrderDataHandler")
    public @ResponseBody
    BaseOutput leaseOrderDataHandler() {
        try {
            return assetsLeaseDataHandlerService.leaseOrderDataHandler();
        } catch (Exception e) {
            LOG.error("跑订单数据异常 FAIL！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 迁移保证金后数据处理
     *
     * @return
     */
    @GetMapping(value = "/moveDepositAmount")
    public @ResponseBody
    BaseOutput moveDepositAmount() {
        try {
            return assetsLeaseDataHandlerService.moveDepositAmount();
        } catch (Exception e) {
            LOG.error("迁移保证金后数据处理异常 FAIL！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 跑订单项及收费项数据
     *
     * @return
     */
    @GetMapping(value = "/leaseOrderChargeItemDataHandler")
    public @ResponseBody
    BaseOutput leaseOrderChargeItemDataHandler() {
        try {
            return assetsLeaseDataHandlerService.leaseOrderChargeItemDataHandler();
        } catch (Exception e) {
            LOG.error("跑订单项及收费项数据异常 FAIL！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 跑退款单金额及退款收费项数据
     *
     * @return
     */
    @GetMapping(value = "/refundOrderChargeItemDataHandler")
    public @ResponseBody
    BaseOutput refundOrderChargeItemDataHandler() {
        try {
            return assetsLeaseDataHandlerService.refundOrderChargeItemDataHandler();
        } catch (Exception e) {
            LOG.error("跑退款单金额及退款收费项数据异常 FAIL！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }


}
