package com.dili.ia.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.ia.controller.LeaseOrderController;
import com.dili.ia.domain.LeaseOrder;
import com.dili.ia.domain.LeaseOrderItem;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.dto.LeaseOrderItemListDto;
import com.dili.ia.domain.dto.LeaseOrderListDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.LeaseOrderMapper;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.rpc.UidFeignRpc;
import com.dili.ia.service.LeaseOrderItemService;
import com.dili.ia.service.LeaseOrderService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
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
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private UidFeignRpc uidFeignRpc;

    /**
     * 摊位租赁单保存
     *
     * @param dto
     * @return
     */
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
        dto.setMarketCode(userTicket.getFirmCode());
        dto.setCreatorId(userTicket.getId());
        dto.setCreator(userTicket.getRealName());

        if (null == dto.getId()) {
            BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.LEASE_ORDER.getCode());
            if (!bizNumberOutput.isSuccess()) {
                throw new RuntimeException("编号生成器微服务异常");
            }
            dto.setCode(bizNumberOutput.getData());
            dto.setState(LeaseOrderStateEnum.CREATED.getCode());
            dto.setDepartmentId(userTicket.getDepartmentId());
            dto.setPayState(PayStateEnum.NOT_PAID.getCode());
            dto.setWaitAmount(dto.getPayAmount());
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

    /**
     * 结算成功，同步更新租赁单相关信息
     *
     * @param settleOrder
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BaseOutput<Boolean> updateLeaseOrderBySettleInfo(SettleOrder settleOrder) {
        PaymentOrder condition = DTOUtils.newInstance(PaymentOrder.class);
        condition.setCode(settleOrder.getBusinessCode());
        PaymentOrder paymentOrderPO = paymentOrderService.listByExample(condition).stream().findFirst().orElse(null);
        if (PaymentOrderStateEnum.PAID.getCode().equals(paymentOrderPO.getState())) {
            return BaseOutput.success().setData(true);
        }

        paymentOrderPO.setState(PaymentOrderStateEnum.PAID.getCode());
        paymentOrderPO.setPayedTime(DateUtils.localDateTimeToUdate(settleOrder.getOperateTime()));
        if (paymentOrderService.updateSelective(paymentOrderPO) == 0) {
            throw new RuntimeException("多人操作，请重试！");
        }

        //摊位租赁摊位租赁单及摊位租赁单项相关信息改动
        LeaseOrder leaseOrder = get(paymentOrderPO.getBusinessId());
        if (LeaseOrderStateEnum.SUBMITTED.getCode().equals(leaseOrder.getState())) {
            //TODO 解冻并消费保证金、定金、转低
            //第一笔消费抵扣保证金
            deductionLeaseOrderItemDepositAmount(leaseOrder.getId());
        }
        if ((leaseOrder.getWaitAmount() - paymentOrderPO.getAmount()) == 0L) {
            leaseOrder.setPayState(PayStateEnum.PAID.getCode());
            Date now = new Date();
            if (now.getTime() >= leaseOrder.getStartTime().getTime() &&
                    now.getTime() <= leaseOrder.getEndTime().getTime()) {
                leaseOrder.setState(LeaseOrderStateEnum.EFFECTIVE.getCode());
            } else if (now.getTime() < leaseOrder.getStartTime().getTime()) {
                leaseOrder.setState(LeaseOrderStateEnum.NOT_ACTIVE.getCode());
            } else if (now.getTime() > leaseOrder.getEndTime().getTime()) {
                leaseOrder.setState(LeaseOrderStateEnum.EXPIRED.getCode());
            }

        }
        leaseOrder.setWaitAmount(leaseOrder.getWaitAmount() - paymentOrderPO.getAmount());
        leaseOrder.setPaidAmount(leaseOrder.getPaidAmount() + paymentOrderPO.getAmount());
        leaseOrder.setPaymentId(0L);
        //缴清后 级联修改子摊位租赁单项状态
        cascadeUpdateLeaseOrderState(leaseOrder, true, LeaseOrderItemStateEnum.getLeaseOrderItemStateEnum(leaseOrder.getState()));

        return BaseOutput.success().setData(true);
    }

    /**
     * 提交付款
     *
     * @param id         租赁单ID
     * @param amount     交费金额
     * @param waitAmount 待缴费金额
     * @return
     */
    @Override
    @Transactional
    public BaseOutput submitPayment(Long id, Long amount, Long waitAmount) {
        //更新摊位租赁单状态
        LeaseOrder leaseOrder = get(id);
        //提交付款条件：已创建状态或已提交状态
        if (!LeaseOrderStateEnum.CREATED.getCode().equals(leaseOrder.getState()) &&
                !LeaseOrderStateEnum.SUBMITTED.getCode().equals(leaseOrder.getState())) {
            String stateName = LeaseOrderStateEnum.getLeaseOrderStateEnum(leaseOrder.getState()).getName();
            LOG.info("租赁单编号【{}】 状态为【{}】，不可以进行提交付款操作", leaseOrder.getCode(), stateName);
            throw new RuntimeException("租赁单状态为【" + stateName + "】，不可以进行提交付款操作");
        }
        if (leaseOrder.getWaitAmount().equals(0L)) {
            throw new RuntimeException("摊位租赁单费用已结清");
        }
        if (amount > leaseOrder.getWaitAmount()) {
            LOG.info("摊位租赁单【ID {}】 支付金额【{}】大于待付金额【{}】", id, amount, leaseOrder.getWaitAmount());
            throw new RuntimeException("支付金额大于待付金额");
        }
        if (!waitAmount.equals(leaseOrder.getWaitAmount())) {
            LOG.info("摊位租赁单待缴费金额已发生变更，请重试【ID {}】 旧金额【{}】新金额【{}】", id, waitAmount, leaseOrder.getWaitAmount());
            throw new RuntimeException("摊位租赁单待缴费金额已发生变更，请重试");
        }
        //@TODO 摊位 定金 保证金 转低冻结

        //冻结保证金
        frozenLeaseOrderItemDepositAmount(id);

        //新增缴费单
        PaymentOrder paymentOrder = buildPaymentOrder(leaseOrder);
        paymentOrder.setAmount(amount);
        paymentOrderService.insertSelective(paymentOrder);

        if (leaseOrder.getState().equals(LeaseOrderStateEnum.CREATED.getCode())) {
            leaseOrder.setState(LeaseOrderStateEnum.SUBMITTED.getCode());
            leaseOrder.setPaymentId(paymentOrder.getId());
            cascadeUpdateLeaseOrderState(leaseOrder, true, LeaseOrderItemStateEnum.SUBMITTED);
        } else if (leaseOrder.getState().equals(LeaseOrderStateEnum.SUBMITTED.getCode())) {
            //判断缴费单是否需要撤回 需要撤回则撤回
            if (null != leaseOrder.getPaymentId() && 0 != leaseOrder.getPaymentId()) {
                withdrawPaymentOrder(leaseOrder.getPaymentId());
            }
            leaseOrder.setPaymentId(paymentOrder.getId());
            if (updateSelective(leaseOrder) == 0) {
                LOG.info("摊位租赁单提交状态更新失败 乐观锁生效 【租赁单ID {}】", id);
                throw new RuntimeException("摊位租赁单提交状态更新失败");
            }
        }

        //新增结算单
        SettleOrderDto settleOrder = buildSettleOrderDto(leaseOrder);
        settleOrder.setAmount(amount);
        settleOrder.setBusinessCode(paymentOrder.getCode());
        BaseOutput<SettleOrder> settlementOutput = settlementRpc.submit(settleOrder);
        if (settlementOutput.isSuccess()) {
            //冗余结算编号 另起事务使其不影响原有事务
            try {
                saveSettlementCode(paymentOrder.getId(), settlementOutput.getData().getCode());
            } catch (Exception e) {
                LOG.error("结算编号冗余异常 租赁单【code:{}】缴费单【code:{}】 异常信息{}", leaseOrder.getCode(), paymentOrder.getCode(), e.getMessage());
            }
        } else {
            throw new RuntimeException(settlementOutput.getMessage());
        }
        return settlementOutput;
    }

    /**
     * 摊位租赁单保证金抵扣
     *
     * @param id
     */
    private void deductionLeaseOrderItemDepositAmount(Long id) {
        LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
        condition.setLeaseOrderId(id);
        List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(condition);
        List<Long> depositAmountSourceIds = leaseOrderItems.stream().map(LeaseOrderItem::getDepositAmountSourceId).collect(Collectors.toList());

        LeaseOrderItemListDto depositAmountSourceCondition = DTOUtils.newInstance(LeaseOrderItemListDto.class);
        depositAmountSourceCondition.setIds(depositAmountSourceIds);
        List<LeaseOrderItem> depositAmountSourceItems = leaseOrderItemService.listByExample(depositAmountSourceCondition);
        depositAmountSourceItems.stream().forEach(o -> {
            if (DepositAmountFlagEnum.FROZEN.getCode().equals(o.getDepositAmountFlag())) {
                o.setDepositAmountFlag(DepositAmountFlagEnum.DEDUCTION.getCode());
            } else {
                LOG.info("{}保证金已被抵扣,不可重复操作", o.getBoothName());
                throw new RuntimeException(o.getBoothName() + "保证金已被抵扣,不可重复操作");
            }
        });
        if (leaseOrderItemService.batchUpdateSelective(depositAmountSourceItems) != depositAmountSourceItems.size()) {
            LOG.info("源摊位租赁单项抵扣失败 【租赁单id={}】", id);
            throw new RuntimeException("多人操作，请重试！");
        }

    }

    /**
     * 摊位租赁单保证金冻结
     *
     * @param id
     */
    private void frozenLeaseOrderItemDepositAmount(Long id) {
        LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
        condition.setLeaseOrderId(id);
        List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(condition);
        List<Long> depositAmountSourceIds = leaseOrderItems.stream().map(LeaseOrderItem::getDepositAmountSourceId).collect(Collectors.toList());

        LeaseOrderItemListDto depositAmountSourceCondition = DTOUtils.newInstance(LeaseOrderItemListDto.class);
        depositAmountSourceCondition.setIds(depositAmountSourceIds);
        List<LeaseOrderItem> depositAmountSourceItems = leaseOrderItemService.listByExample(depositAmountSourceCondition);
        depositAmountSourceItems.stream().forEach(o -> {
            if (DepositAmountFlagEnum.TRANSFERRED.getCode().equals(o.getDepositAmountFlag())) {
                o.setDepositAmountFlag(DepositAmountFlagEnum.FROZEN.getCode());
            } else {
                String operateName = DepositAmountFlagEnum.getDepositAmountFlagEnum(o.getDepositAmountFlag()).getOperateName();
                LOG.info("{}保证金已被{},保证金总抵扣额已发生变化不可进行抵扣，请修改后重试", o.getBoothName(), operateName);
                throw new RuntimeException(o.getBoothName() + "保证金已被" + operateName + ",保证金总抵扣额已发生变化不可进行抵扣，请修改后重试");
            }
        });
        if (leaseOrderItemService.batchUpdateSelective(depositAmountSourceItems) != depositAmountSourceItems.size()) {
            LOG.info("源摊位租赁单项冻结失败 【租赁单id={}】", id);
            throw new RuntimeException("多人操作，请重试！");
        }

    }

    /**
     * 摊位租赁单保证金解冻
     *
     * @param id
     */
    private void unFrozenLeaseOrderItemDepositAmount(Long id) {
        LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
        condition.setLeaseOrderId(id);
        List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(condition);
        List<Long> depositAmountSourceIds = leaseOrderItems.stream().map(LeaseOrderItem::getDepositAmountSourceId).collect(Collectors.toList());

        LeaseOrderItemListDto depositAmountSourceCondition = DTOUtils.newInstance(LeaseOrderItemListDto.class);
        depositAmountSourceCondition.setIds(depositAmountSourceIds);
        List<LeaseOrderItem> depositAmountSourceItems = leaseOrderItemService.listByExample(depositAmountSourceCondition);
        depositAmountSourceItems.stream().forEach(o -> {
            if (DepositAmountFlagEnum.FROZEN.getCode().equals(o.getDepositAmountFlag())) {
                o.setDepositAmountFlag(DepositAmountFlagEnum.TRANSFERRED.getCode());
            } else {
                String operateName = DepositAmountFlagEnum.getDepositAmountFlagEnum(o.getDepositAmountFlag()).getOperateName();
                LOG.info("{}保证金已被解冻,不可重复操作", o.getBoothName());
                throw new RuntimeException(o.getBoothName() + "保证金已被解冻,不可重复操作");
            }
        });
        if (leaseOrderItemService.batchUpdateSelective(depositAmountSourceItems) != depositAmountSourceItems.size()) {
            LOG.info("源摊位租赁单项解冻失败 【租赁单id={}】", id);
            throw new RuntimeException("多人操作，请重试！");
        }

    }

    /**
     * 级联更新摊位租赁订单状态 订单项状态级联发生变化
     *
     * @param leaseOrder
     * @param isCascade  false不级联更新订单项 true 级联更新订单项
     * @param stateEnum  isCascade为false时，此处可以传null
     */
    @Transactional
    void cascadeUpdateLeaseOrderState(LeaseOrder leaseOrder, boolean isCascade, LeaseOrderItemStateEnum stateEnum) {
        if (updateSelective(leaseOrder) == 0) {
            LOG.info("摊位租赁单提交状态更新失败 乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
            throw new RuntimeException("多人操作，请重试");
        }

        if (isCascade) {
            LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
            condition.setLeaseOrderId(leaseOrder.getId());
            List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(condition);
            leaseOrderItems.stream().forEach(o -> o.setState(stateEnum.getCode()));
            if (leaseOrderItemService.batchUpdateSelective(leaseOrderItems) != leaseOrderItems.size()) {
                throw new RuntimeException("多人操作，请重试！");
            }
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
        PaymentOrder paymentOrderPo = paymentOrderService.get(paymentId);
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
        BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.PAYMENT_ORDER.getCode());
        if (!bizNumberOutput.isSuccess()) {
            throw new RuntimeException("编号生成器微服务异常");
        }
        paymentOrder.setCode(bizNumberOutput.getData());
        paymentOrder.setBusinessCode(leaseOrder.getCode());
        paymentOrder.setBusinessId(leaseOrder.getId());
        paymentOrder.setMarketId(userTicket.getFirmId());
        paymentOrder.setMarketCode(userTicket.getFirmCode());
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
    private SettleOrderDto buildSettleOrderDto(LeaseOrder leaseOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        SettleOrderDto settleOrder = new SettleOrderDto();
        settleOrder.setAppId(settlementAppId);
        settleOrder.setBusinessCode(leaseOrder.getCode());
        settleOrder.setBusinessDepId(leaseOrder.getDepartmentId());
        settleOrder.setBusinessDepName(leaseOrder.getDepartmentName());
        settleOrder.setBusinessType(BizTypeEnum.BOOTH_LEASE.getCode());
        settleOrder.setCustomerId(leaseOrder.getCustomerId());
        settleOrder.setCustomerName(leaseOrder.getCustomerName());
        settleOrder.setCustomerPhone(leaseOrder.getCustomerCellphone());
        settleOrder.setMarketId(userTicket.getFirmId());
        settleOrder.setMarketCode(userTicket.getFirmCode());
        settleOrder.setReturnUrl("http://ia.diligrp.com:8381/api/leaseOrder/settlementDealHandler");
        settleOrder.setSubmitterDepId(userTicket.getDepartmentId());
        settleOrder.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        settleOrder.setSubmitterId(userTicket.getId());
        settleOrder.setSubmitterName(userTicket.getRealName());
        settleOrder.setSubmitTime(LocalDateTime.now());
        settleOrder.setType(SettleTypeEnum.PAY.getCode());
        settleOrder.setState(SettleStateEnum.WAIT_DEAL.getCode());
        return settleOrder;
    }

    /**
     * 取消摊位租赁订单
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public BaseOutput cancelOrder(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        LeaseOrder leaseOrder = get(id);
        if (!LeaseOrderStateEnum.CREATED.getCode().equals(leaseOrder.getState())) {
            String stateName = LeaseOrderStateEnum.getLeaseOrderStateEnum(leaseOrder.getState()).getName();
            LOG.info("租赁单【code:{}】状态为【{}】，不可以进行取消操作", leaseOrder.getCode(), stateName);
            throw new RuntimeException("租赁单状态为【" + stateName + "】，不可以进行取消操作");
        }
        leaseOrder.setState(LeaseOrderStateEnum.CANCELD.getCode());
        leaseOrder.setCancelerId(userTicket.getId());
        leaseOrder.setCanceler(userTicket.getRealName());

        //联动摊位租赁单项状态 取消
        cascadeUpdateLeaseOrderState(leaseOrder, true, LeaseOrderItemStateEnum.CANCELD);

        return BaseOutput.success();
    }

    /**
     * 撤回摊位租赁订单
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public BaseOutput withdrawOrder(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        LeaseOrder leaseOrder = get(id);
        if (!LeaseOrderStateEnum.SUBMITTED.getCode().equals(leaseOrder.getState())) {
            String stateName = LeaseOrderStateEnum.getLeaseOrderStateEnum(leaseOrder.getState()).getName();
            LOG.info("租赁单【code:{}】状态为【{}】，不可以进行撤回操作", leaseOrder.getCode(), stateName);
            throw new RuntimeException("租赁单状态为【" + stateName + "】，不可以进行撤回操作");
        }
        if (null != leaseOrder.getPaymentId() && 0L != leaseOrder.getPaymentId()) {
            withdrawPaymentOrder(leaseOrder.getPaymentId());
            leaseOrder.setPaymentId(0L);
        }
        leaseOrder.setState(LeaseOrderStateEnum.CREATED.getCode());
        cascadeUpdateLeaseOrderState(leaseOrder, true, LeaseOrderItemStateEnum.CREATED);

        //TODO 摊位、保证金、定金、转低解冻
        //解冻摊位保证金
        unFrozenLeaseOrderItemDepositAmount(id);

        return BaseOutput.success();
    }

    /**
     * 撤回缴费单 判断缴费单是否需要撤回 需要撤回则撤回
     * 如果撤回时发现缴费单状态为及时同步结算状态 则抛出异常 提示用户带结算同步后再操作
     *
     * @param paymentId
     */
    private void withdrawPaymentOrder(Long paymentId) {
        PaymentOrder payingOrder = paymentOrderService.get(paymentId);
        if (PaymentOrderStateEnum.NOT_PAID.getCode().equals(payingOrder.getState())) {
            String paymentCode = payingOrder.getCode();
            Map<String, String> paramMap = Map.of("appId", String.valueOf(settlementAppId), "businessCode", paymentCode);
            BaseOutput<SettleOrder> settleOrderBaseOutput = settlementRpc.get(paramMap);
            if (!settleOrderBaseOutput.isSuccess()) {
                LOG.info("结算单查询异常 【缴费单CODE {}】", paymentCode);
                throw new RuntimeException("结算单查询异常");
            }
            SettleOrder settleOrder = settleOrderBaseOutput.getData();
            //缴费单对应的结算单未处理
            if (settleOrder.getState().equals(SettleStateEnum.WAIT_DEAL.getCode())) {
                if (!settlementRpc.cancelByCode(paymentCode).isSuccess()) {
                    LOG.info("结算单撤回异常 【缴费单CODE {}】", paymentCode);
                    throw new RuntimeException("结算单撤回异常");
                }
            } else {
                String stateName = SettleStateEnum.getNameByCode(settleOrder.getState());
                throw new RuntimeException("状态已发生变更，目前状态【" + stateName + "】不能进行撤回，等结算数据同步后再操作");
            }
        }
    }

    /**
     * 扫描待生效的订单，做生效处理
     * @return
     */
    @Override
    public BaseOutput<Boolean> scanNotActiveLeaseOrder() {
        while (true) {
            LeaseOrderListDto condition = DTOUtils.newInstance(LeaseOrderListDto.class);
            condition.setStartTimeLT(new Date());
            condition.setState(LeaseOrderStateEnum.NOT_ACTIVE.getCode());
            condition.setRows(100);
            condition.setPage(1);
            List<LeaseOrder> leaseOrders = listByExample(condition);
            if (CollectionUtils.isEmpty(leaseOrders)) {
                break;
            }

            leaseOrders.stream().forEach(o -> {
                try {
                    leaseOrderEffectiveHandler(o);
                } catch (Exception e) {
                    LOG.error("租赁单【code:{}】变更生效异常。{}", o.getCode(), e.getMessage());
                    LOG.error("租赁单变更生效异常", e);
                }
            });
        }
        return BaseOutput.success().setData(true);
    }

    /**
     * 租赁单生效处理
     * @param o
     */
    @Transactional
    void leaseOrderEffectiveHandler(LeaseOrder o) {
        o.setState(LeaseOrderStateEnum.EFFECTIVE.getCode());
        if (updateSelective(o) == 0) {
            LOG.info("摊位租赁单提交状态更新失败 乐观锁生效 【租赁单ID {}】", o.getId());
            throw new RuntimeException("多人操作，请重试");
        }

        LeaseOrderItem itemCondition = DTOUtils.newInstance(LeaseOrderItem.class);
        itemCondition.setLeaseOrderId(o.getId());
        List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(itemCondition);

        List<LeaseOrderItem> waitItems = new ArrayList<>();
        leaseOrderItems.stream().forEach(leaseOrderItem -> {
            //只更新待生效的订单项
            if (LeaseOrderItemStateEnum.NOT_ACTIVE.getCode().equals(leaseOrderItem.getState())) {
                leaseOrderItem.setState(LeaseOrderItemStateEnum.EFFECTIVE.getCode());
                waitItems.add(leaseOrderItem);
            }
        });
        if (leaseOrderItemService.batchUpdateSelective(waitItems) != waitItems.size()) {
            throw new RuntimeException("多人操作，请重试！");
        }
    }

    /**
     * 扫描待到期的订单，做到期处理
     * @return
     */
    @Override
    public BaseOutput<Boolean> scanExpiredLeaseOrder() {
        while (true) {
            LeaseOrderListDto condition = DTOUtils.newInstance(LeaseOrderListDto.class);
            condition.setEndTimeLT(new Date());
            condition.setState(LeaseOrderStateEnum.EFFECTIVE.getCode());
            condition.setRows(100);
            condition.setPage(1);
            List<LeaseOrder> leaseOrders = listByExample(condition);
            if (CollectionUtils.isEmpty(leaseOrders)) {
                break;
            }

            leaseOrders.stream().forEach(o -> {
                try {
                    leaseOrderExpiredHandler(o);
                } catch (Exception e) {
                    LOG.error("租赁单【code:{}】变更到期异常。{}", o.getCode(), e.getMessage());
                    LOG.error("租赁单变更到期异常", e);
                }
            });
        }
        return BaseOutput.success().setData(true);
    }

    /**
     * 租赁单到期处理
     * @param o
     */
    @Transactional
    void leaseOrderExpiredHandler(LeaseOrder o) {
        o.setState(LeaseOrderStateEnum.EXPIRED.getCode());
        if (updateSelective(o) == 0) {
            LOG.info("摊位租赁单提交状态更新失败 乐观锁生效 【租赁单ID {}】", o.getId());
            throw new RuntimeException("多人操作，请重试");
        }

        LeaseOrderItem itemCondition = DTOUtils.newInstance(LeaseOrderItem.class);
        itemCondition.setLeaseOrderId(o.getId());
        List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(itemCondition);

        List<LeaseOrderItem> waitItems = new ArrayList<>();
        leaseOrderItems.stream().forEach(leaseOrderItem -> {
            //只更新待到期的订单项
            if (LeaseOrderItemStateEnum.EFFECTIVE.getCode().equals(leaseOrderItem.getState())) {
                leaseOrderItem.setState(LeaseOrderItemStateEnum.EXPIRED.getCode());
                waitItems.add(leaseOrderItem);
            }
        });
        if (leaseOrderItemService.batchUpdateSelective(waitItems) != waitItems.size()) {
            throw new RuntimeException("多人操作，请重试！");
        }
    }
}