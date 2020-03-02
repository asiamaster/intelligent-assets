package com.dili.ia.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.ia.domain.LeaseOrder;
import com.dili.ia.domain.LeaseOrderItem;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.dto.LeaseOrderListDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.LeaseOrderMapper;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.service.LeaseOrderItemService;
import com.dili.ia.service.LeaseOrderService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Maps;
import net.sf.cglib.beans.BeanMap;
import okhttp3.internal.http2.ErrorCode;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-11 15:54:49.
 */
@Service
public class LeaseOrderServiceImpl extends BaseServiceImpl<LeaseOrder, Long> implements LeaseOrderService {

    public LeaseOrderMapper getActualDao() {
        return (LeaseOrderMapper) getDao();
    }
    @Autowired
    private DepartmentRpc departmentRpc;

    @Autowired
    private LeaseOrderItemService leaseOrderItemService;

    @Autowired
    private SettlementRpc settlementRpc;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Value("${settlement.app-id}")
    private Long settlementAppId;

    @Override
    @Transactional
    public BaseOutput saveLeaseOrder(LeaseOrderListDto dto) {
        //@TODO 摊位是否可租赁，接口验证

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }

        BaseOutput<Department> depOut = departmentRpc.get(userTicket.getDepartmentId());
        if(depOut.isSuccess()){
            dto.setDepartmentName(depOut.getData().getName());
        }
        dto.setMarketId(userTicket.getFirmId());
        dto.setCreatorId(userTicket.getId());
        dto.setCreator(userTicket.getRealName());

        if (null == dto.getId()) {
            dto.setState(LeaseOrderStateEnum.CREATED.getCode());
            dto.setDepartmentId(userTicket.getDepartmentId());
            dto.setPayState(PayStateEnum.NOT_PAID.getCode());
            insertSelective(dto);
            insertLeaseOrderItems(dto);
        } else {
            LeaseOrder oldLeaseOrder = get(dto.getId());
            dto.setVersion(oldLeaseOrder.getVersion());
            int rows = updateSelective(dto);
            if (rows == 0) {
                throw new RuntimeException("多人操作，请重试！");
            }

            LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
            condition.setLeaseOrderId(dto.getId());
            leaseOrderItemService.deleteByExample(condition);
            insertLeaseOrderItems(dto);
        }
        return BaseOutput.success();
    }

    /**
     * 批量插入租赁单项
     *
     * @param dto
     */
    private void insertLeaseOrderItems(LeaseOrderListDto dto) {
        dto.getLeaseOrderItems().forEach(o -> {
            o.setLeaseOrderId(dto.getId());
            o.setCustomerId(dto.getCustomerId());
            o.setCustomerName(dto.getCustomerName());
            o.setState(LeaseOrderStateEnum.CREATED.getCode());
            o.setDepositAmountFlag(DepositAmountFlagEnum.PRE_TRANSFER.getCode());
            leaseOrderItemService.insertSelective(o);
        });
    }

    @Override
    @Transactional
    public BaseOutput submitPayment(Long id, Long amount) {

        //更新订单状态
        LeaseOrder leaseOrder = get(id);
        if(leaseOrder.getWaitAmount().equals(0L)){
            throw new RuntimeException("订单费用已结清");
        }
        if(leaseOrder.getState().equals(LeaseOrderStateEnum.CREATED.getCode())){
            leaseOrder.setState(LeaseOrderStateEnum.SUBMITTED.getCode());
            if(updateSelective(leaseOrder) == 0){
                throw new RuntimeException("订单提交状态更新失败");
            }
            cascadeUpdateLeaseOrderItemState(id, LeaseOrderItemStateEnum.SUBMITTED);
        }

        //@TODO 定金 保证金 转低冻结

        //新增缴费单
        PaymentOrder paymentOrder = buildPaymentOrder(leaseOrder);
        paymentOrder.setAmount(amount);
        paymentOrderService.insertSelective(paymentOrder);

        //新增结算单
        SettleOrder settleOrder = buildSettleOrder(leaseOrder);
        settleOrder.setAmount(amount);
        settleOrder.setBusinessCode(paymentOrder.getCode());
        BaseOutput<SettleOrder> settlementOutput = settlementRpc.submit(settleOrder);
        if(settlementOutput.isSuccess()){
            saveSettlementCode(paymentOrder.getId(),settlementOutput.getData().getCode());
        }
        if(!settlementOutput.isSuccess()){
            throw new RuntimeException(settlementOutput.getMessage());
        }
        return settlementOutput;
    }

    /**
     * 级联更新订单项状态
     * @param id
     * @param submitted
     */
    private void cascadeUpdateLeaseOrderItemState(Long id, LeaseOrderItemStateEnum submitted) {
        LeaseOrderItem condition= DTOUtils.newInstance(LeaseOrderItem.class);
        condition.setLeaseOrderId(id);
        List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(condition);
        leaseOrderItems.stream().forEach(o->o.setState(submitted.getCode()));
        if(leaseOrderItemService.batchUpdateSelective(leaseOrderItems) != leaseOrderItems.size()){
            throw new RuntimeException("订单项提交状态更新失败");
        }
    }

    /**
     * 冗余结算编号到缴费单
     * @param paymentId
     * @param settlementCode
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveSettlementCode(Long paymentId, String settlementCode){
        PaymentOrder paymentOrderPo = DTOUtils.newInstance(PaymentOrder.class);
        paymentOrderPo.setId(paymentId);
        paymentOrderPo.setSettlementCode(settlementCode);
        paymentOrderService.updateSelective(paymentOrderPo);
    }

    /**
     * 构建缴费单数据
     * @param leaseOrder
     * @return
     */
    private PaymentOrder buildPaymentOrder(LeaseOrder leaseOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        PaymentOrder paymentOrder = DTOUtils.newInstance(PaymentOrder.class);
        //TODO 编号生成器 取
        paymentOrder.setCode(leaseOrder.getCode());
        paymentOrder.setBusinessCode(leaseOrder.getCode());
        paymentOrder.setBusinessId(leaseOrder.getId());
        paymentOrder.setMarketId(userTicket.getFirmId());
        paymentOrder.setCreatorId(userTicket.getId());
        paymentOrder.setCreator(userTicket.getRealName());
        paymentOrder.setState(PayStateEnum.NOT_PAID.getCode());
        paymentOrder.setBizType(BizTypeEnum.BOOTH_LEASE.getCode());
        return paymentOrder;
    }

    /**
     * 构造结算单数据
     * @param leaseOrder
     * @return
     */
    private SettleOrder buildSettleOrder(LeaseOrder leaseOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        SettleOrder settleOrder = new SettleOrder();
        settleOrder.setAppId(settlementAppId);
        settleOrder.setBusinessCode(leaseOrder.getCode());
        settleOrder.setBusinessDepId(leaseOrder.getDepartmentId());
        settleOrder.setBusinessDepName(leaseOrder.getDepartmentName());
        settleOrder.setBusinessType(BizTypeEnum.BOOTH_LEASE.getCode());
        settleOrder.setCustomerId(leaseOrder.getCustomerId());
        settleOrder.setCustomerName(leaseOrder.getCustomerName());
        settleOrder.setCustomerPhone(leaseOrder.getCustomerCellphone());
        settleOrder.setMarketId(userTicket.getFirmId());
        settleOrder.setReturnUrl("http://ia.diligrp.com:8381/api/leaseOrder/paymentSuccess");
        settleOrder.setSubmitterDepId(userTicket.getDepartmentId());
        settleOrder.setSubmitterId(userTicket.getId());
        settleOrder.setSubmitterName(userTicket.getRealName());
        settleOrder.setSubmitTime(LocalDateTime.now());
        settleOrder.setType(SettleTypeEnum.PAY.getCode());
        settleOrder.setState(SettleStateEnum.WAIT_DEAL.getCode());
        return settleOrder;
    }

    @Override
    @Transactional
    public BaseOutput cancelOrder(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        LeaseOrder leaseOrder = get(id);
        leaseOrder.setState(LeaseOrderStateEnum.CANCELD.getCode());
        leaseOrder.setCancelerId(userTicket.getId());
        leaseOrder.setCanceler(userTicket.getRealName());
        int rows = updateSelective(leaseOrder);
        if(rows == 0){
            throw new RuntimeException("多人操作，请重试！");
        }

        //联动订单项状态 取消
        cascadeUpdateLeaseOrderItemState(id, LeaseOrderItemStateEnum.CANCELD);

        return BaseOutput.success();
    }
}