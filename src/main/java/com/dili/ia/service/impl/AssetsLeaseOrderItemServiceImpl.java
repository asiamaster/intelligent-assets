package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BoothRentDTO;
import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.ia.domain.AssetsLeaseOrder;
import com.dili.ia.domain.AssetsLeaseOrderItem;
import com.dili.ia.domain.dto.AssetsLeaseOrderItemListDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.AssetsLeaseOrderItemMapper;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ia.service.AssetsLeaseOrderItemService;
import com.dili.ia.service.AssetsLeaseOrderService;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.util.LoggerUtil;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.DateUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 14:40:05.
 */
@Service
public class AssetsLeaseOrderItemServiceImpl extends BaseServiceImpl<AssetsLeaseOrderItem, Long> implements AssetsLeaseOrderItemService {

    public AssetsLeaseOrderItemMapper getActualDao() {
        return (AssetsLeaseOrderItemMapper)getDao();
    }
    private final static Logger LOG = LoggerFactory.getLogger(AssetsLeaseOrderItemServiceImpl.class);

    @Autowired
    private AssetsLeaseOrderService assetsLeaseOrderService;
    @Autowired
    private AssetsRpc assetsRpc;

    @Autowired private BusinessChargeItemService businessChargeItemService;

    /**
     * 停租操作
     * @param leaseOrderItem
     * @return
     */
    @Override
    @Transactional
    @GlobalTransactional
    public BaseOutput stopRent(AssetsLeaseOrderItem leaseOrderItem) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        leaseOrderItem.setStopOperatorId(userTicket.getId());
        leaseOrderItem.setStopOperatorName(userTicket.getRealName());
        AssetsLeaseOrderItem leaseOrderItemOld = get(leaseOrderItem.getId());
        AssetsLeaseOrder leaseOrder = assetsLeaseOrderService.get(leaseOrderItemOld.getLeaseOrderId());

        if(!LeaseRefundStateEnum.WAIT_APPLY.getCode().equals(leaseOrder.getRefundState())){
            throw new BusinessException(ResultCode.DATA_ERROR,"已发起过退款申请，不能发起停租");
        }

        if(!StopRentStateEnum.NO_APPLY.getCode().equals(leaseOrderItemOld.getStopRentState())){
            throw new BusinessException(ResultCode.DATA_ERROR,"已发起过停租，不能多次发起停租");
        }
        if(!(LeaseOrderItemStateEnum.NOT_ACTIVE.getCode().equals(leaseOrderItemOld.getState()) || LeaseOrderItemStateEnum.EFFECTIVE.getCode().equals(leaseOrderItemOld.getState()))){
            throw new BusinessException(ResultCode.DATA_ERROR,"状态已变更，不能进行停租操作");
        }
        if(StopWayEnum.IMMEDIATELY.getCode().equals(leaseOrderItem.getStopWay())){//立即停租
            leaseOrderItem.setStopTime(LocalDateTime.now());
            leaseOrderItem.setState(LeaseOrderItemStateEnum.RENTED_OUT.getCode());
            leaseOrderItem.setVersion(leaseOrderItemOld.getVersion());
            leaseOrderItem.setStopRentState(StopRentStateEnum.RENTED_OUT.getCode());

            //检查子项状态，看是否需要联动订单状态
            stopRentCascadeLeaseOrderState(leaseOrderItemOld);
        }else{//未来停租
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59");
            leaseOrderItem.setStopTime(LocalDateTime.parse(leaseOrderItem.getStopTime().format(formatter), formatter));
            leaseOrderItem.setStopRentState(StopRentStateEnum.WAIT_TIMER_EXE.getCode());
            leaseOrderItem.setVersion(leaseOrderItemOld.getVersion());
        }

        stopBoothRent(leaseOrderItemOld,leaseOrder.getStartTime(),leaseOrderItem.getStopTime());
        if(updateSelective(leaseOrderItem) == 0){
            LOG.info("摊位订单项停租异常,乐观锁生效【订单项ID:{},摊位名称:{}】", leaseOrderItemOld.getId(), leaseOrderItemOld.getAssetsName());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
        LoggerUtil.buildLoggerContext(leaseOrderItemOld.getLeaseOrderId(),leaseOrderItemOld.getLeaseOrderCode(),userTicket.getId(),userTicket.getRealName(),userTicket.getFirmId(),leaseOrderItem.getStopReason());
        return BaseOutput.success();
    }

    /**
     * 停租摊位租赁
     * @param assetsLeaseOrderItem
     * @param startTime
     * @param stopTime
     */
    @Override
    public void stopBoothRent(AssetsLeaseOrderItem assetsLeaseOrderItem, LocalDateTime startTime, LocalDateTime stopTime) {
        //修改摊位租赁时间段
        BoothRentDTO boothRentDTO = new BoothRentDTO();
        boothRentDTO.setBoothId(assetsLeaseOrderItem.getAssetsId());
        boothRentDTO.setOrderId(assetsLeaseOrderItem.getLeaseOrderId().toString());
        BaseOutput assetsOutput;
        if (stopTime.isBefore(startTime)) {
            //未生效停租 结束时间比开始时间小 直接释放时间段
            assetsOutput = assetsRpc.deleteBoothRent(boothRentDTO);
        } else {
            boothRentDTO.setEnd(DateUtils.localDateTimeToUdate(stopTime));
            assetsOutput = assetsRpc.updateEndBoothRent(boothRentDTO);
        }
        if (!assetsOutput.isSuccess()) {
            LOG.info("摊位订单项停租异常，【订单项ID:{},摊位名称:{},异常MSG:{}】", assetsLeaseOrderItem.getId(), assetsLeaseOrderItem.getAssetsName(), assetsOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, assetsOutput.getMessage());
        }
    }

    /**
     * 级联更新租赁单状态（停租摊位操作）
     * @param leaseOrderItemOld
     */
    private void stopRentCascadeLeaseOrderState(AssetsLeaseOrderItem leaseOrderItemOld) {
        AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
        condition.setLeaseOrderId(leaseOrderItemOld.getLeaseOrderId());
        List<AssetsLeaseOrderItem> leaseOrderItems = list(condition);

        AssetsLeaseOrder leaseOrder = assetsLeaseOrderService.get(leaseOrderItemOld.getLeaseOrderId());
        boolean isUpdateLeaseOrderState = true;
        if(leaseOrderItems.size() > 1){
            isUpdateLeaseOrderState = true;
            for (AssetsLeaseOrderItem orderItem : leaseOrderItems) {
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
            if(assetsLeaseOrderService.updateSelective(leaseOrder) == 0){
                LOG.info("级联更新租赁单状态异常,乐观锁生效 【租赁单编号:{},摊位名称:{}】", leaseOrderItemOld.getLeaseOrderCode(), leaseOrderItemOld.getAssetsName());
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
    public void stopRentLeaseOrderItemFromTimer(AssetsLeaseOrderItem o) {
        o.setStopTime(LocalDateTime.now());
        o.setState(LeaseOrderItemStateEnum.RENTED_OUT.getCode());
        o.setVersion(o.getVersion());
        o.setStopRentState(StopRentStateEnum.RENTED_OUT.getCode());
        if(updateSelective(o) == 0){
            LOG.info("租赁订单项执行停租异常,乐观锁生效 【id:{},摊位:{}】。", o.getId(), o.getAssetsName());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
        stopRentCascadeLeaseOrderState(o);
    }

    @Override
    public List<AssetsLeaseOrderItemListDto> leaseOrderItemListToDto(List<AssetsLeaseOrderItem> assetsLeaseOrderItems, String bizType, List<BusinessChargeItemDto> chargeItemDtos) {
        List<Map<String, String>> businessChargeItems = businessChargeItemService.queryBusinessChargeItem(bizType, assetsLeaseOrderItems.stream().map(o -> o.getId()).collect(Collectors.toList()), chargeItemDtos);
        Map<Long,Map<String,String>> businessChargeItemMap = new HashMap<>();
        businessChargeItems.forEach(bct->{
            businessChargeItemMap.put(Long.valueOf(bct.get("businessId")),bct);
        });

        List<AssetsLeaseOrderItemListDto> assetsLeaseOrderItemListDtos = new ArrayList<>();
        assetsLeaseOrderItems.forEach(o->{
            AssetsLeaseOrderItemListDto assetsLeaseOrderItemListDto = new AssetsLeaseOrderItemListDto();
            try {
                BeanUtils.copyProperties(assetsLeaseOrderItemListDto,o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            assetsLeaseOrderItemListDto.setBusinessChargeItem(businessChargeItemMap.get(o.getId()));
            assetsLeaseOrderItemListDtos.add(assetsLeaseOrderItemListDto);
        });

        return assetsLeaseOrderItemListDtos;
    }
}