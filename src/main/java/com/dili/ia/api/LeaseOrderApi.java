package com.dili.ia.api;

import com.dili.assets.sdk.dto.CategoryDTO;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.ia.glossary.AssetsTypeEnum;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.service.AssetsLeaseOrderItemService;
import com.dili.ia.service.AssetsLeaseOrderService;
import com.dili.ia.service.AssetsLeaseService;
import com.dili.ia.service.LeaseOrderWorkerService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.AppException;
import com.dili.ss.exception.BusinessException;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 摊位租赁api
 */
@RestController
@RequestMapping("/api/leaseOrder")
public class LeaseOrderApi {
    private final static Logger LOG = LoggerFactory.getLogger(LeaseOrderApi.class);

    @Autowired
    AssetsLeaseOrderService assetsLeaseOrderService;
    @Autowired
    AssetsLeaseOrderItemService assetsLeaseOrderItemService;
    @Autowired
    LeaseOrderWorkerService leaseOrderWorkerService;

    @Autowired
    SettlementRpc settlementRpc;

    @Autowired
    AssetsRpc assetsRpc;
    /**
     * 摊位租赁结算成功回调
     * @param settleOrder
     * @return
     */
    @RequestMapping(value="/settlementDealHandler", method = {RequestMethod.POST})
    public BaseOutput<Boolean> settlementDealHandler(@RequestBody SettleOrder settleOrder){
        try{
            return assetsLeaseOrderService.updateLeaseOrderBySettleInfo(settleOrder);
        }catch (BusinessException e){
            LOG.info("摊位租赁结算成功回调异常！", e);
            return BaseOutput.failure(e.getMessage());
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
    @RequestMapping(value="/scanEffectiveLeaseOrder")
    public @ResponseBody BaseOutput<Boolean> scanEffectiveLeaseOrder(){
        try{
            return leaseOrderWorkerService.scanEffectiveLeaseOrder();
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
            return leaseOrderWorkerService.scanExpiredLeaseOrder();
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
            return leaseOrderWorkerService.scanWaitStopRentLeaseOrder();
        }catch (Exception e){
            LOG.error("扫描等待停租的摊位异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

}
