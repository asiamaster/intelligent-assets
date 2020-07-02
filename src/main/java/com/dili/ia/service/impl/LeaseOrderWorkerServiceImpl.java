package com.dili.ia.service.impl;

import com.dili.ia.domain.AssetsLeaseOrder;
import com.dili.ia.domain.AssetsLeaseOrderItem;
import com.dili.ia.domain.dto.AssetsLeaseOrderItemListDto;
import com.dili.ia.domain.dto.AssetsLeaseOrderListDto;
import com.dili.ia.glossary.LeaseOrderStateEnum;
import com.dili.ia.glossary.StopRentStateEnum;
import com.dili.ia.service.AssetsLeaseOrderItemService;
import com.dili.ia.service.AssetsLeaseOrderService;
import com.dili.ia.service.LeaseOrderWorkerService;
import com.dili.ss.domain.BaseOutput;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaseOrderWorkerServiceImpl implements LeaseOrderWorkerService {
    private final static Logger LOG = LoggerFactory.getLogger(LeaseOrderWorkerServiceImpl.class);
    @Autowired
    private AssetsLeaseOrderService assetLeaseOrderService;
    @Autowired
    private AssetsLeaseOrderItemService assetLeaseOrderItemService;

    /**
     * 扫描待生效的订单，做生效处理
     *
     * @return
     */
    @Override
    public BaseOutput<Boolean> scanEffectiveLeaseOrder() {
        LOG.info("=========================摊位租赁生效处理调度执行 begin====================================");
        while (true) {
            AssetsLeaseOrderListDto condition = new AssetsLeaseOrderListDto();
            condition.setStartTimeLT(LocalDateTime.now());
            condition.setState(LeaseOrderStateEnum.NOT_ACTIVE.getCode());
            condition.setRows(100);
            condition.setPage(1);
            List<AssetsLeaseOrder> leaseOrders = assetLeaseOrderService.listByExample(condition);
            if (CollectionUtils.isEmpty(leaseOrders)) {
                break;
            }

            leaseOrders.stream().forEach(o -> {
                try {
                    assetLeaseOrderService.leaseOrderEffectiveHandler(o);
                } catch (Exception e) {
                    LOG.error("租赁单【编号：{}】变更生效异常。{}", o.getCode(), e.getMessage());
                    LOG.error("租赁单变更生效异常", e);
                }
            });
        }
        LOG.info("=========================摊位租赁生效处理调度执行 end====================================");
        return BaseOutput.success().setData(true);
    }

    /**
     * 扫描待到期的订单，做到期处理
     *
     * @return
     */
    @Override
    public BaseOutput<Boolean> scanExpiredLeaseOrder() {
        LOG.info("=========================摊位租赁到期处理调度执行 begin====================================");
        while (true) {
            AssetsLeaseOrderListDto condition = new AssetsLeaseOrderListDto();
            condition.setEndTimeLT(LocalDateTime.now());
            condition.setState(LeaseOrderStateEnum.EFFECTIVE.getCode());
            condition.setRows(100);
            condition.setPage(1);
            List<AssetsLeaseOrder> leaseOrders = assetLeaseOrderService.listByExample(condition);
            if (CollectionUtils.isEmpty(leaseOrders)) {
                break;
            }

            leaseOrders.stream().forEach(o -> {
                try {
                    assetLeaseOrderService.leaseOrderExpiredHandler(o);
                } catch (Exception e) {
                    LOG.error("租赁单【编号：{}】变更到期异常。{}", o.getCode(), e.getMessage());
                    LOG.error("租赁单变更到期异常", e);
                }
            });
        }
        LOG.info("=========================摊位租赁到期处理调度执行 end====================================");
        return BaseOutput.success().setData(true);
    }

    /**
     * 扫描等待停租的摊位
     * @return
     */
    @Override
    public BaseOutput<Boolean> scanWaitStopRentLeaseOrder() {
        LOG.info("=========================摊位租赁停租处理调度执行 begin====================================");
        while (true) {
            AssetsLeaseOrderItemListDto condition = new AssetsLeaseOrderItemListDto();
            condition.setStopRentState(StopRentStateEnum.WAIT_TIMER_EXE.getCode());
            condition.setStopTimeLet(LocalDateTime.now());
            condition.setRows(100);
            condition.setPage(1);
            List<AssetsLeaseOrderItem> leaseOrderItems = assetLeaseOrderItemService.listByExample(condition);
            if (CollectionUtils.isEmpty(leaseOrderItems)) {
                break;
            }

            leaseOrderItems.stream().forEach(o -> {
                try {
                    assetLeaseOrderItemService.stopRentLeaseOrderItemFromTimer(o);
                } catch (Exception e) {
                    LOG.error("租赁订单项【id:{}】执行停租异常。{}", o.getId(), e.getMessage());
                    LOG.error("租赁单执行停租异常", e);
                }
            });
        }
        LOG.info("=========================摊位租赁停租处理调度执行 end====================================");
        return BaseOutput.success().setData(true);
    }
}
