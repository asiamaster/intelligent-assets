package com.dili.ia.service.impl;

import com.dili.ia.domain.LeaseOrder;
import com.dili.ia.domain.LeaseOrderItem;
import com.dili.ia.domain.dto.LeaseOrderItemListDto;
import com.dili.ia.domain.dto.LeaseOrderListDto;
import com.dili.ia.glossary.LeaseOrderStateEnum;
import com.dili.ia.glossary.StopRentStateEnum;
import com.dili.ia.service.LeaseOrderItemService;
import com.dili.ia.service.LeaseOrderService;
import com.dili.ia.service.LeaseOrderWorkerService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class LeaseOrderWorkerServiceImpl implements LeaseOrderWorkerService {
    private final static Logger LOG = LoggerFactory.getLogger(LeaseOrderWorkerServiceImpl.class);
    @Autowired
    private LeaseOrderService leaseOrderService;
    @Autowired
    private LeaseOrderItemService leaseOrderItemService;

    /**
     * 扫描待生效的订单，做生效处理
     *
     * @return
     */
    @Override
    @Transactional
    public BaseOutput<Boolean> scanEffectiveLeaseOrder() {
        LOG.info("=========================摊位租赁生效处理调度执行 begin====================================");
        while (true) {
            LeaseOrderListDto condition = DTOUtils.newInstance(LeaseOrderListDto.class);
            condition.setStartTimeLT(new Date());
            condition.setState(LeaseOrderStateEnum.NOT_ACTIVE.getCode());
            condition.setRows(100);
            condition.setPage(1);
            List<LeaseOrder> leaseOrders = leaseOrderService.listByExample(condition);
            if (CollectionUtils.isEmpty(leaseOrders)) {
                break;
            }

            leaseOrders.stream().forEach(o -> {
                try {
                    leaseOrderService.leaseOrderEffectiveHandler(o);
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
    @Transactional
    public BaseOutput<Boolean> scanExpiredLeaseOrder() {
        LOG.info("=========================摊位租赁到期处理调度执行 begin====================================");
        while (true) {
            LeaseOrderListDto condition = DTOUtils.newInstance(LeaseOrderListDto.class);
            condition.setEndTimeLT(new Date());
            condition.setState(LeaseOrderStateEnum.EFFECTIVE.getCode());
            condition.setRows(100);
            condition.setPage(1);
            List<LeaseOrder> leaseOrders = leaseOrderService.listByExample(condition);
            if (CollectionUtils.isEmpty(leaseOrders)) {
                break;
            }

            leaseOrders.stream().forEach(o -> {
                try {
                    leaseOrderService.leaseOrderExpiredHandler(o);
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
    @Transactional
    public BaseOutput<Boolean> scanWaitStopRentLeaseOrder() {
        LOG.info("=========================摊位租赁停租处理调度执行 begin====================================");
        while (true) {
            LeaseOrderItemListDto condition = DTOUtils.newInstance(LeaseOrderItemListDto.class);
            condition.setStopRentState(StopRentStateEnum.WAIT_TIMER_EXE.getCode());
            condition.setStopTimeLet(new Date());
            condition.setRows(100);
            condition.setPage(1);
            List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(condition);
            if (CollectionUtils.isEmpty(leaseOrderItems)) {
                break;
            }

            leaseOrderItems.stream().forEach(o -> {
                try {
                    leaseOrderItemService.stopRentLeaseOrderItemFromTimer(o);
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
