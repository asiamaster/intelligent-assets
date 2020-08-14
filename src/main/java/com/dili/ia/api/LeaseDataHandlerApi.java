package com.dili.ia.api;

import com.dili.ia.service.AssetsLeaseDataHandlerService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 跑租赁单数据
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
     * 跑退款单数据
     *
     * @return
     */
    @GetMapping(value = "/refundOrderDataHandler")
    public @ResponseBody
    BaseOutput refundOrderDataHandler() {
        try {
            return assetsLeaseDataHandlerService.refundOrderDataHandler();
        } catch (Exception e) {
            LOG.error("跑退款单金额及退款收费项数据异常 FAIL！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }


    /**
     * 删除租赁单
     * 有退款转抵的不能删除
     * @return
     */
    @PostMapping(value = "/deleteLeaseOrder")
    public @ResponseBody
    BaseOutput deleteLeaseOrder(@RequestBody List<Long> leaseOrderIds) {
        try {
            return assetsLeaseDataHandlerService.deleteLeaseOrder(leaseOrderIds);
        } catch (Exception e) {
            LOG.error("删除租赁单数据异常 FAIL！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }


}
