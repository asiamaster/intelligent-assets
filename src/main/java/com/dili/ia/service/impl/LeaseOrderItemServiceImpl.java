package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BoothRentDTO;
import com.dili.ia.controller.LeaseOrderItemController;
import com.dili.ia.domain.LeaseOrder;
import com.dili.ia.domain.LeaseOrderItem;
import com.dili.ia.domain.dto.LeaseOrderItemListDto;
import com.dili.ia.domain.dto.LeaseOrderListDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.LeaseOrderItemMapper;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ia.service.LeaseOrderItemService;
import com.dili.ia.service.LeaseOrderService;
import com.dili.ia.util.LoggerUtil;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.DateUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-11 15:54:49.
 */
@Service
public class LeaseOrderItemServiceImpl extends BaseServiceImpl<LeaseOrderItem, Long> implements LeaseOrderItemService {

    public LeaseOrderItemMapper getActualDao() {
        return (LeaseOrderItemMapper)getDao();
    }
    private final static Logger LOG = LoggerFactory.getLogger(LeaseOrderItemServiceImpl.class);

    @Autowired
    private LeaseOrderService leaseOrderService;
    @Autowired
    private AssetsRpc assetsRpc;

    /**
     * 停租操作
     * @param leaseOrderItem
     * @return
     */
    @Override
    @Transactional
    @GlobalTransactional
    public BaseOutput stopRent(LeaseOrderItem leaseOrderItem) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        leaseOrderItem.setStopOperatorId(userTicket.getId());
        leaseOrderItem.setStopOperatorName(userTicket.getRealName());
        LeaseOrderItem leaseOrderItemOld = get(leaseOrderItem.getId());
        if(!StopRentStateEnum.NO_APPLY.getCode().equals(leaseOrderItemOld.getStopRentState())){
            throw new BusinessException(ResultCode.DATA_ERROR,"已发起过停租，不能多次发起停租");
        }
        if(!PayStateEnum.PAID.getCode().equals(leaseOrderItemOld.getPayState())){
            throw new BusinessException(ResultCode.DATA_ERROR,"只有费用已交清后才能停租");
        }
        if(!(LeaseOrderItemStateEnum.NOT_ACTIVE.getCode().equals(leaseOrderItemOld.getState()) || LeaseOrderItemStateEnum.EFFECTIVE.getCode().equals(leaseOrderItemOld.getState()))){
            throw new BusinessException(ResultCode.DATA_ERROR,"状态已变更，不能进行停租操作");
        }
        if(StopWayEnum.IMMEDIATELY.getCode().equals(leaseOrderItem.getStopWay())){//立即停租
            leaseOrderItem.setStopTime(new Date());
            leaseOrderItem.setState(LeaseOrderItemStateEnum.RENTED_OUT.getCode());
            leaseOrderItem.setVersion(leaseOrderItemOld.getVersion());
            leaseOrderItem.setStopRentState(StopRentStateEnum.RENTED_OUT.getCode());

            //检查子项状态，看是否需要联动订单状态
            stopRentCascadeLeaseOrderState(leaseOrderItemOld);
        }else{//未来停租
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(leaseOrderItem.getStopTime());
            calendar.add(Calendar.HOUR_OF_DAY,23);
            calendar.add(Calendar.MINUTE,59);
            calendar.add(Calendar.SECOND,59);
            leaseOrderItem.setStopTime(calendar.getTime());
            leaseOrderItem.setStopRentState(StopRentStateEnum.WAIT_TIMER_EXE.getCode());
            leaseOrderItem.setVersion(leaseOrderItemOld.getVersion());
        }

        //修改摊位租赁时间段
        BoothRentDTO boothRentDTO = new BoothRentDTO();
        boothRentDTO.setBoothId(leaseOrderItemOld.getBoothId());
        boothRentDTO.setOrderId(leaseOrderItemOld.getLeaseOrderId().toString());
        boothRentDTO.setEnd(leaseOrderItem.getStopTime());
        BaseOutput assetsOutput = assetsRpc.updateEndBoothRent(boothRentDTO);
        if(!assetsOutput.isSuccess()){
            LOG.info("摊位订单项停租异常，【订单项ID:{},摊位名称:{},异常MSG:{}】", leaseOrderItemOld.getId(), leaseOrderItemOld.getBoothName(), assetsOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR,assetsOutput.getMessage());
        }
        if(updateSelective(leaseOrderItem) == 0){
            LOG.info("摊位订单项停租异常,乐观锁生效【订单项ID:{},摊位名称:{}】", leaseOrderItemOld.getId(), leaseOrderItemOld.getBoothName());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
        LoggerUtil.buildLoggerContext(leaseOrderItemOld.getLeaseOrderId(),leaseOrderItemOld.getLeaseOrderCode(),userTicket.getId(),userTicket.getRealName(),userTicket.getFirmId(),leaseOrderItem.getStopReason());
        return BaseOutput.success();
    }

    /**
     * 客户摊位保证金可用查询
     * @param leaseOrderItem
     * @return
     */
    @Override
    public Map<Long,List<LeaseOrderItem>> queryDepositAmountAvailableItem(LeaseOrderItemListDto leaseOrderItem) {
        leaseOrderItem.setDepositAmountFlag(DepositAmountFlagEnum.TRANSFERRED.getCode());
        leaseOrderItem.setRefundState(RefundStateEnum.WAIT_APPLY.getCode());
        leaseOrderItem.setPayState(PayStateEnum.PAID.getCode());
        leaseOrderItem.setDepositAmountGt(0L);
        List<LeaseOrderItem> leaseOrderItems = listByExample(leaseOrderItem);
        return leaseOrderItems.stream().collect(Collectors.groupingBy(LeaseOrderItem::getBoothId));
    }

    /**
     * 级联更新租赁单状态（停租摊位操作）
     * @param leaseOrderItemOld
     */
    private void stopRentCascadeLeaseOrderState(LeaseOrderItem leaseOrderItemOld) {
        LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
        condition.setLeaseOrderId(leaseOrderItemOld.getLeaseOrderId());
        List<LeaseOrderItem> leaseOrderItems = list(condition);

        LeaseOrder leaseOrder = leaseOrderService.get(leaseOrderItemOld.getLeaseOrderId());
        boolean isUpdateLeaseOrderState = true;
        if(leaseOrderItems.size() > 1){
            isUpdateLeaseOrderState = true;
            for (LeaseOrderItem orderItem : leaseOrderItems) {
                if (orderItem.getId().equals(leaseOrderItemOld.getId())) {
                    continue;
                } else if (LeaseOrderItemStateEnum.NOT_ACTIVE.getCode().equals(orderItem.getState())
                        || LeaseOrderItemStateEnum.EFFECTIVE.getCode().equals(orderItem.getState())
                        || LeaseOrderItemStateEnum.EXPIRED.getCode().equals(orderItem.getState())) {
                    isUpdateLeaseOrderState = false;
                    break;
                }
            }
        }
        if(isUpdateLeaseOrderState){
            leaseOrder.setState(LeaseOrderStateEnum.RENTED_OUT.getCode());
            //租赁单状态不更新为停租也需要版本号+1，状态的联动需要 各订单项需要共同一把锁
            if(leaseOrderService.updateSelective(leaseOrder) == 0){
                LOG.info("级联更新租赁单状态异常,乐观锁生效 【租赁单编号:{},摊位名称:{}】", leaseOrderItemOld.getLeaseOrderCode(), leaseOrderItemOld.getBoothName());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }
        }

    }

    /**
     * 定时停租处理
     * @param o
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void stopRentLeaseOrderItemFromTimer(LeaseOrderItem o) {
        o.setStopTime(new Date());
        o.setState(LeaseOrderItemStateEnum.RENTED_OUT.getCode());
        o.setVersion(o.getVersion());
        o.setStopRentState(StopRentStateEnum.RENTED_OUT.getCode());
        if(updateSelective(o) == 0){
            LOG.info("租赁订单项执行停租异常,乐观锁生效 【id:{},摊位:{}】。", o.getId(), o.getBoothName());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
        stopRentCascadeLeaseOrderState(o);
    }
}