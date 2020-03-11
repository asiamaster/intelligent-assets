package com.dili.ia.api;

import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.service.LeaseOrderItemService;
import com.dili.ia.service.LeaseOrderService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
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
    @RequestMapping(value="/settlementDealHandler", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput<Boolean> settlementDealHandler(SettleOrder settleOrder){
        try{
            return leaseOrderService.updateLeaseOrderBySettleInfo(settleOrder);
        }catch (Exception e){
            LOG.error("摊位租赁结算成功回调异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 扫描已生效但状态未变更的单子，更改其状态
     * cron 0 0 0 * * ?
     * @return
     */
    @RequestMapping(value="/scanNotActiveLeaseOrder")
    public @ResponseBody BaseOutput<Boolean> scanNotActiveLeaseOrder(){
        try{
            return leaseOrderService.scanNotActiveLeaseOrder();
        }catch (Exception e){
            LOG.error("扫描已生效但状态未变更的单子异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 扫描已到期但状态未变更的单子，更改其状态
     * cron 0 0 0 * * ?
     * @return
     */
    @RequestMapping(value="/scanExpiredLeaseOrder")
    public @ResponseBody BaseOutput<Boolean> scanExpiredLeaseOrder(){
        try{
            return leaseOrderService.scanExpiredLeaseOrder();
        }catch (Exception e){
            LOG.error("扫描已到期但状态未变更的单子异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 扫描等待停租的摊位
     * cron 0 0 0 * * ?
     * @return
     */
    @RequestMapping(value="/scanWaitStopRentLeaseOrder")
    public @ResponseBody BaseOutput<Boolean> scanWaitStopRentLeaseOrder(){
        try{
            return leaseOrderItemService.scanWaitStopRentLeaseOrder();
        }catch (Exception e){
            LOG.error("扫描等待停租的摊位异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 扫描等待停租的摊位
     * cron 0 0 0 * * ?
     * @return
     */
    @RequestMapping(value="/queryPrintData")
    public @ResponseBody BaseOutput<PrintDataDto> queryPrintData(String businessCode, Integer reprint){
        try{
            if(StringUtils.isBlank(businessCode) || null == reprint){
                return BaseOutput.failure("参数错误");
            }
            return leaseOrderService.queryPrintData(businessCode,reprint);
        }catch (Exception e){
            LOG.error("扫描等待停租的摊位异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }
}
