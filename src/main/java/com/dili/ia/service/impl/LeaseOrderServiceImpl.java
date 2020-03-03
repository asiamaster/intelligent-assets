package com.dili.ia.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.ia.controller.LeaseOrderController;
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
import com.dili.ss.util.DateUtils;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Maps;
import net.sf.cglib.beans.BeanMap;
import okhttp3.internal.http2.ErrorCode;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger LOG = LoggerFactory.getLogger(LeaseOrderServiceImpl.class);

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
        if (depOut.isSuccess()) {
            dto.setDepartmentName(depOut.getData().getName());
        }
        dto.setMarketId(userTicket.getFirmId());
        dto.setCreatorId(userTicket.getId());
        dto.setCreator(userTicket.getRealName());

        if (null == dto.getId()) {
            // TODO: 2020/3/3 编号生成器取编码
//            dto.setCode();
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
            // TODO: 2020/3/3 编号生成器取编码
//            o.setCode();
            o.setLeaseOrderId(dto.getId());
            o.setCustomerId(dto.getCustomerId());
            o.setCustomerName(dto.getCustomerName());
            o.setState(LeaseOrderStateEnum.CREATED.getCode());
            o.setDepositAmountFlag(DepositAmountFlagEnum.PRE_TRANSFER.getCode());
            leaseOrderItemService.insertSelective(o);
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BaseOutput<Boolean> updateLeaseOrderBySettleInfo(SettleOrder settleOrder) {
        PaymentOrder condition = DTOUtils.newInstance(PaymentOrder.class);
        condition.setCode(settleOrder.getBusinessCode());
        PaymentOrder paymentOrderPO = paymentOrderService.listByExample(condition).stream().findFirst().orElse(null);
        if(PaymentOrderStateEnum.PAID.getCode().equals(paymentOrderPO.getState())){
            return BaseOutput.success().setData(true);
        }

        paymentOrderPO.setState(PaymentOrderStateEnum.PAID.getCode());
        paymentOrderPO.setPayedTime(DateUtils.localDateTimeToUdate(settleOrder.getOperateTime()));
        if (paymentOrderService.updateSelective(paymentOrderPO) == 0) {
            throw new RuntimeException("多人操作，请重试！");
        }

        //摊位租赁订单及订单项相关信息改动
        LeaseOrder leaseOrder = get(paymentOrderPO.getBusinessId());
        if(LeaseOrderStateEnum.SUBMITTED.getCode().equals(leaseOrder.getState())){
            //TODO 解冻并消费保证金、定金、转低

        }
        if((leaseOrder.getWaitAmount()-paymentOrderPO.getAmount()) == 0L){
            leaseOrder.setPayState(PayStateEnum.PAID.getCode());
            Date now = new Date();
            if(now.getTime() >= leaseOrder.getStartTime().getTime() &&
            now.getTime() <= leaseOrder.getEndTime().getTime()){
                leaseOrder.setState(LeaseOrderStateEnum.EFFECTIVE.getCode());
            }else if(now.getTime() < leaseOrder.getStartTime().getTime()){
                leaseOrder.setState(LeaseOrderStateEnum.NOT_ACTIVE.getCode());
            }else if(now.getTime() > leaseOrder.getEndTime().getTime()){
                leaseOrder.setState(LeaseOrderStateEnum.EXPIRED.getCode());
            }
            //缴清后 级联修改子订单项状态
            cascadeUpdateLeaseOrderItemState(leaseOrder.getId(),LeaseOrderItemStateEnum.getLeaseOrderItemStateEnum(leaseOrder.getState()));
        }
        leaseOrder.setWaitAmount(leaseOrder.getWaitAmount()-paymentOrderPO.getAmount());
        leaseOrder.setPaidAmount(leaseOrder.getPaidAmount() + paymentOrderPO.getAmount());
        leaseOrder.setPaymentId(0L);
        if (updateSelective(leaseOrder) == 0) {
            throw new RuntimeException("多人操作，请重试！");
        }

        return BaseOutput.success().setData(true);
    }

    @Override
    @Transactional
    public BaseOutput submitPayment(Long id, Long amount) {
        //更新订单状态
        LeaseOrder leaseOrder = get(id);
        if (leaseOrder.getWaitAmount().equals(0L)) {
            throw new RuntimeException("订单费用已结清");
        }
        if (amount > leaseOrder.getWaitAmount()) {
            LOG.error("租赁单【ID {}】 支付金额【{}】大于待付金额【{}】",id,amount,leaseOrder.getWaitAmount());
            throw new RuntimeException("支付金额大于待付金额");
        }
        if (leaseOrder.getState().equals(LeaseOrderStateEnum.CREATED.getCode())) {
            leaseOrder.setState(LeaseOrderStateEnum.SUBMITTED.getCode());
            if (updateSelective(leaseOrder) == 0) {
                LOG.error("订单提交状态更新失败 乐观锁生效 【租赁单ID {}】",id);
                throw new RuntimeException("订单提交状态更新失败");
            }
            cascadeUpdateLeaseOrderItemState(id, LeaseOrderItemStateEnum.SUBMITTED);
        } else if(null != leaseOrder.getPaymentId() && 0L != leaseOrder.getPaymentId()) {
            PaymentOrder payingOrder = paymentOrderService.get(leaseOrder.getPaymentId());
            if (PaymentOrderStateEnum.NOT_PAID.getCode().equals(payingOrder.getState())) {
                String paymentCode = payingOrder.getCode();
                Map<String, String> paramMap = Map.of("appId", String.valueOf(settlementAppId), "businessCode", paymentCode);
                BaseOutput<SettleOrder> settleOrderBaseOutput = settlementRpc.get(paramMap);
                if (!settleOrderBaseOutput.isSuccess()) {
                    LOG.error("结算单查询异常 【缴费单CODE {}】",paymentCode);
                    throw new RuntimeException("结算单查询异常");
                }
                SettleOrder settleOrder = settleOrderBaseOutput.getData();
                //缴费单对应的结算单未处理
                if (settleOrder.getState().equals(SettleStateEnum.WAIT_DEAL.getCode())) {
                    if(!settlementRpc.cancelByCode(paymentCode).isSuccess()){
                        LOG.error("结算单撤回异常 【缴费单CODE {}】",paymentCode);
                        throw new RuntimeException("结算单撤回异常");
                    }
                }else if(settleOrder.getState().equals(SettleStateEnum.DEAL.getCode())){
                    //缴费单对应的结算单已处理 同步状态及数据 调用结算处理回调
                    updateLeaseOrderBySettleInfo(settleOrder);
                }
            }

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
        if (settlementOutput.isSuccess()) {
            //冗余结算编号 另起事务使其不影响原有事务
            saveSettlementCode(paymentOrder.getId(), settlementOutput.getData().getCode());
        }else{
            throw new RuntimeException(settlementOutput.getMessage());
        }
        return settlementOutput;
    }

    /**
     * 级联更新订单项状态
     *
     * @param id
     * @param stateEnum
     */
    private void cascadeUpdateLeaseOrderItemState(Long id, LeaseOrderItemStateEnum stateEnum) {
        LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
        condition.setLeaseOrderId(id);
        List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(condition);
        leaseOrderItems.stream().forEach(o -> o.setState(stateEnum.getCode()));
        if (leaseOrderItemService.batchUpdateSelective(leaseOrderItems) != leaseOrderItems.size()) {
            throw new RuntimeException("订单项提交状态更新失败");
        }
    }

    /**
     * 冗余结算编号到缴费单
     *
     * @param paymentId
     * @param settlementCode
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveSettlementCode(Long paymentId, String settlementCode) {
        PaymentOrder paymentOrderPo = DTOUtils.newInstance(PaymentOrder.class);
        paymentOrderPo.setId(paymentId);
        paymentOrderPo.setSettlementCode(settlementCode);
        paymentOrderService.updateSelective(paymentOrderPo);
    }

    /**
     * 构建缴费单数据
     *
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
        paymentOrder.setState(PaymentOrderStateEnum.NOT_PAID.getCode());
        paymentOrder.setBizType(BizTypeEnum.BOOTH_LEASE.getCode());
        return paymentOrder;
    }

    /**
     * 构造结算单数据
     *
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
        settleOrder.setReturnUrl("http://ia.diligrp.com:8381/api/leaseOrder/settlementDealHandler");
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
        if(!LeaseOrderStateEnum.CREATED.getCode().equals(leaseOrder.getState())){
            String stateName = LeaseOrderStateEnum.getLeaseOrderStateEnum(leaseOrder.getState()).getName();
            LOG.error("租赁单【code:{}】状态为【{}】，不可以进行取消操作",leaseOrder.getCode(),stateName);
            throw new RuntimeException("租赁单状态为【"+stateName+"】，不可以进行取消操作");
        }
        leaseOrder.setState(LeaseOrderStateEnum.CANCELD.getCode());
        leaseOrder.setCancelerId(userTicket.getId());
        leaseOrder.setCanceler(userTicket.getRealName());
        int rows = updateSelective(leaseOrder);
        if (rows == 0) {
            throw new RuntimeException("多人操作，请重试！");
        }

        //联动订单项状态 取消
        cascadeUpdateLeaseOrderItemState(id, LeaseOrderItemStateEnum.CANCELD);

        return BaseOutput.success();
    }
}