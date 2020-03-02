package com.dili.ia.api;

import com.dili.ia.controller.LeaseOrderController;
import com.dili.ia.domain.LeaseOrder;
import com.dili.ia.service.LeaseOrderItemService;
import com.dili.ia.service.LeaseOrderService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 摊位租赁api
 */
@Api("/api/leaseOrder")
@RestController
@RequestMapping("/api/leaseOrder")
public class LeaseOrderApi {
    private final static Logger LOG = LoggerFactory.getLogger(LeaseOrderApi.class);

    @Autowired
    LeaseOrderService leaseOrderService;
    @Autowired
    LeaseOrderItemService leaseOrderItemService;

    /**
     * 摊位租赁结算成功回调
     * @param settleOrder
     * @return
     */
    @RequestMapping(value="/paymentSuccess", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput paymentSuccess(SettleOrder settleOrder){

        return BaseOutput.success();
    }
}
