package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BoothRentDTO;
import com.dili.ia.domain.AssetLeaseOrder;
import com.dili.ia.domain.AssetLeaseOrderItem;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.AssetLeaseOrderItemMapper;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ia.service.AssetLeaseOrderItemService;
import com.dili.ia.service.AssetLeaseOrderService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 14:40:05.
 */
@Service
public class AssetLeaseOrderItemServiceImpl extends BaseServiceImpl<AssetLeaseOrderItem, Long> implements AssetLeaseOrderItemService {

    public AssetLeaseOrderItemMapper getActualDao() {
        return (AssetLeaseOrderItemMapper)getDao();
    }
    private final static Logger LOG = LoggerFactory.getLogger(AssetLeaseOrderItemServiceImpl.class);

    @Autowired
    private AssetLeaseOrderService assetLeaseOrderService;
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
    public BaseOutput stopRent(AssetLeaseOrderItem leaseOrderItem) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        leaseOrderItem.setStopOperatorId(userTicket.getId());
        leaseOrderItem.setStopOperatorName(userTicket.getRealName());
        AssetLeaseOrderItem leaseOrderItemOld = get(leaseOrderItem.getId());
        AssetLeaseOrder leaseOrder = assetLeaseOrderService.get(leaseOrderItemOld.getLeaseOrderId());
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

        //修改摊位租赁时间段
        BoothRentDTO boothRentDTO = new BoothRentDTO();
        boothRentDTO.setBoothId(leaseOrderItemOld.getAssetId());
        boothRentDTO.setOrderId(leaseOrderItemOld.getLeaseOrderId().toString());
        BaseOutput assetsOutput;
        if(leaseOrderItem.getStopTime().isBefore(leaseOrder.getStartTime())){
            //未生效停租 结束时间比开始时间小 直接释放时间段
            assetsOutput = assetsRpc.deleteBoothRent(boothRentDTO);
        }else{
            boothRentDTO.setEnd(DateUtils.localDateTimeToUdate(leaseOrderItem.getStopTime()));
            assetsOutput = assetsRpc.updateEndBoothRent(boothRentDTO);
        }
        if(!assetsOutput.isSuccess()){
            LOG.info("摊位订单项停租异常，【订单项ID:{},摊位名称:{},异常MSG:{}】", leaseOrderItemOld.getId(), leaseOrderItemOld.getAssetName(), assetsOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR,assetsOutput.getMessage());
        }
        if(updateSelective(leaseOrderItem) == 0){
            LOG.info("摊位订单项停租异常,乐观锁生效【订单项ID:{},摊位名称:{}】", leaseOrderItemOld.getId(), leaseOrderItemOld.getAssetName());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
        LoggerUtil.buildLoggerContext(leaseOrderItemOld.getLeaseOrderId(),leaseOrderItemOld.getLeaseOrderCode(),userTicket.getId(),userTicket.getRealName(),userTicket.getFirmId(),leaseOrderItem.getStopReason());
        return BaseOutput.success();
    }

    /**
     * 级联更新租赁单状态（停租摊位操作）
     * @param leaseOrderItemOld
     */
    private void stopRentCascadeLeaseOrderState(AssetLeaseOrderItem leaseOrderItemOld) {
        AssetLeaseOrderItem condition = DTOUtils.newInstance(AssetLeaseOrderItem.class);
        condition.setLeaseOrderId(leaseOrderItemOld.getLeaseOrderId());
        List<AssetLeaseOrderItem> leaseOrderItems = list(condition);

        AssetLeaseOrder leaseOrder = assetLeaseOrderService.get(leaseOrderItemOld.getLeaseOrderId());
        boolean isUpdateLeaseOrderState = true;
        if(leaseOrderItems.size() > 1){
            isUpdateLeaseOrderState = true;
            for (AssetLeaseOrderItem orderItem : leaseOrderItems) {
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
            if(assetLeaseOrderService.updateSelective(leaseOrder) == 0){
                LOG.info("级联更新租赁单状态异常,乐观锁生效 【租赁单编号:{},摊位名称:{}】", leaseOrderItemOld.getLeaseOrderCode(), leaseOrderItemOld.getAssetName());
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
    public void stopRentLeaseOrderItemFromTimer(AssetLeaseOrderItem o) {
        o.setStopTime(LocalDateTime.now());
        o.setState(LeaseOrderItemStateEnum.RENTED_OUT.getCode());
        o.setVersion(o.getVersion());
        o.setStopRentState(StopRentStateEnum.RENTED_OUT.getCode());
        if(updateSelective(o) == 0){
            LOG.info("租赁订单项执行停租异常,乐观锁生效 【id:{},摊位:{}】。", o.getId(), o.getAssetName());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
        stopRentCascadeLeaseOrderState(o);
    }
}