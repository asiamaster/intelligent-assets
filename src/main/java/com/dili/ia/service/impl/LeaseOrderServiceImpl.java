package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BoothDTO;
import com.dili.assets.sdk.dto.BoothRentDTO;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.*;
import com.dili.ia.domain.dto.*;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.LeaseOrderMapper;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.rpc.UidFeignRpc;
import com.dili.ia.service.*;
import com.dili.ia.util.BeanMapUtil;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.ia.util.ResultCodeConst;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Value("${settlement.handler.url}")
    private String settlerHandlerUrl;
    @Autowired
    private UidFeignRpc uidFeignRpc;
    @Autowired
    private CustomerAccountService customerAccountService;
    @Autowired
    private AssetsRpc assetsRpc;
    @Autowired
    private RefundOrderService refundOrderService;
    @Autowired
    private CustomerRpc customerRpc;
    @Autowired
    private BusinessLogRpc businessLogRpc;
    @Autowired
    private TransactionDetailsService transactionDetailsService;

    /**
     * 摊位租赁单保存
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public BaseOutput saveLeaseOrder(LeaseOrderListDto dto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }

        //检查客户状态
        checkCustomerState(dto.getCustomerId(),userTicket.getFirmId());
        dto.getLeaseOrderItems().forEach(o->{
            //检查摊位状态
            checkBoothState(o.getBoothId());
        });

        dto.setMarketId(userTicket.getFirmId());
        dto.setMarketCode(userTicket.getFirmCode());
        dto.setCreatorId(userTicket.getId());
        dto.setCreator(userTicket.getRealName());

        if (null == dto.getId()) {
            //租赁单新增
            checkContractNo(null, dto.getContractNo(), true);//合同编号验证重复
            BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.LEASE_ORDER.getCode());
            if (!bizNumberOutput.isSuccess()) {
                LOG.info("租赁单编号生成异常");
                throw new BusinessException(ResultCode.DATA_ERROR,"编号生成器微服务异常");
            }
            dto.setCode(userTicket.getFirmCode().toUpperCase() + bizNumberOutput.getData());
            dto.setState(LeaseOrderStateEnum.CREATED.getCode());
            dto.setPayState(PayStateEnum.NOT_PAID.getCode());
            dto.setWaitAmount(dto.getPayAmount());
            insertSelective(dto);
            insertLeaseOrderItems(dto);
        } else {
            //租赁单修改
            checkContractNo(dto.getId(), dto.getContractNo(), false);//合同编号验证重复
            LeaseOrder oldLeaseOrder = get(dto.getId());
            if(!LeaseOrderStateEnum.CREATED.getCode().equals(oldLeaseOrder.getState())){
                throw new BusinessException(ResultCode.DATA_ERROR, "租赁单编号【" + oldLeaseOrder.getCode() + "】 状态已变更，不可以进行修改操作");
            }
            dto.setVersion(oldLeaseOrder.getVersion());
            dto.setWaitAmount(dto.getPayAmount());
            if (updateExact(dto) == 0) {
                LOG.info("摊位租赁单修改异常,乐观锁生效 【租赁单编号:{}】", dto.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }

            LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
            condition.setLeaseOrderId(dto.getId());
            leaseOrderItemService.deleteByExample(condition);
            insertLeaseOrderItems(dto);
        }
        return BaseOutput.success().setData(dto);
    }

    /**
     * 批量插入租赁单项
     *
     * @param dto
     */
    private void insertLeaseOrderItems(LeaseOrderListDto dto) {
        dto.getLeaseOrderItems().forEach(o -> {
            o.setLeaseOrderId(dto.getId());
            o.setLeaseOrderCode(dto.getCode());
            o.setCustomerId(dto.getCustomerId());
            o.setCustomerName(dto.getCustomerName());
            o.setState(LeaseOrderStateEnum.CREATED.getCode());
            o.setDepositAmountFlag(DepositAmountFlagEnum.PRE_TRANSFER.getCode());
            o.setPayState(PayStateEnum.NOT_PAID.getCode());
            o.setStopRentState(StopRentStateEnum.NO_APPLY.getCode());
            leaseOrderItemService.insertSelective(o);
        });
    }

    @Override
    public BaseOutput supplement(LeaseOrder leaseOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        checkContractNo(leaseOrder.getId(),leaseOrder.getContractNo(),false);
        LeaseOrder oldLeaseOrder = get(leaseOrder.getId());
        leaseOrder.setVersion(oldLeaseOrder.getVersion());
        if (updateSelective(leaseOrder) == 0) {
            return BaseOutput.failure("多人操作，请稍后重试");
        }
        LoggerUtil.buildLoggerContext(oldLeaseOrder.getId(),oldLeaseOrder.getCode(),userTicket.getId(),userTicket.getRealName(),userTicket.getFirmId(),null);
        return BaseOutput.success();
    }

    /**
     * 合同编号验重
     * @param leaseOrderId 待修改的租赁单Id
     * @param contractNo
     * @param isAdd
     */
    private void checkContractNo(Long leaseOrderId,String contractNo,Boolean isAdd){
        if(StringUtils.isNotBlank(contractNo)){
            LeaseOrder condition = DTOUtils.newInstance(LeaseOrder.class);
            condition.setContractNo(contractNo);
            List<LeaseOrder> leaseOrders = list(condition);
            if(isAdd && CollectionUtils.isNotEmpty(leaseOrders)){
                throw new BusinessException(ResultCode.DATA_ERROR,"合同编号不允许重复使用，请修改");
            }else {
                if(leaseOrders.size() == 1){
                    LeaseOrder leaseOrder = leaseOrders.get(0);
                    if(!leaseOrder.getId().equals(leaseOrderId)){
                        throw new BusinessException(ResultCode.DATA_ERROR,"合同编号不允许重复使用，请修改");
                    }
                }
            }
        }
    }

    /**
     * 检查摊位状态
     * @param boothId
     */
    private void checkBoothState(Long boothId){
        BaseOutput<BoothDTO> output = assetsRpc.getBoothById(boothId);
        if(!output.isSuccess()){
            throw new BusinessException(ResultCode.DATA_ERROR,"摊位接口调用异常 "+output.getMessage());
        }
        BoothDTO booth = output.getData();
        if(null == booth){
            throw new BusinessException(ResultCode.DATA_ERROR,"摊位不存在，请重新修改后保存");
        }else if(EnabledStateEnum.DISABLED.getCode().equals(booth.getState())){
            throw new BusinessException(ResultCode.DATA_ERROR,"摊位"+booth.getName()+"已禁用，请重新修改后保存");
        }else if(YesOrNoEnum.YES.getCode().equals(booth.getIsDelete())){
            throw new BusinessException(ResultCode.DATA_ERROR,"摊位"+booth.getName()+"已删除，请重新修改后保存");
        }
    }

    /**
     * 检查客户状态
     * @param customerId
     * @param marketId
     */
    private void checkCustomerState(Long customerId,Long marketId){
        BaseOutput<Customer> output = customerRpc.get(customerId,marketId);
        if(!output.isSuccess()){
            throw new BusinessException(ResultCode.DATA_ERROR,"客户接口调用异常 "+output.getMessage());
        }
        Customer customer = output.getData();
        if(null == customer){
            throw new BusinessException(ResultCode.DATA_ERROR,"客户不存在，请重新修改后保存");
        }else if(EnabledStateEnum.DISABLED.getCode().equals(customer.getState())){
            throw new BusinessException(ResultCode.DATA_ERROR,"客户已禁用，请重新修改后保存");
        }else if(YesOrNoEnum.YES.getCode().equals(customer.getIsDelete())){
            throw new BusinessException(ResultCode.DATA_ERROR,"客户已删除，请重新修改后保存");
        }
    }


    /**
     * 结算成功，同步更新租赁单相关信息
     *
     * @param settleOrder
     * @return
     */
    @Override
    @Transactional
    @GlobalTransactional
    public BaseOutput<Boolean> updateLeaseOrderBySettleInfo(SettleOrder settleOrder) {
        /*****************************更新缴费单相关字段 begin***********************/
        PaymentOrder condition = DTOUtils.newInstance(PaymentOrder.class);
        condition.setCode(settleOrder.getOrderCode());
        PaymentOrder paymentOrderPO = paymentOrderService.listByExample(condition).stream().findFirst().orElse(null);
        if (PaymentOrderStateEnum.PAID.getCode().equals(paymentOrderPO.getState())) {
            return BaseOutput.success().setData(true);
        }

        paymentOrderPO.setState(PaymentOrderStateEnum.PAID.getCode());
        paymentOrderPO.setSettlementWay(settleOrder.getWay());
        paymentOrderPO.setSettlementOperator(settleOrder.getOperatorName());
        paymentOrderPO.setPayedTime(DateUtils.localDateTimeToUdate(settleOrder.getOperateTime()));
        if (paymentOrderService.updateSelective(paymentOrderPO) == 0) {
            LOG.info("结算成功，缴费单同步数据异常,乐观锁生效 【缴费单编号:{}】", settleOrder.getBusinessCode());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
        /*****************************更新缴费单相关字段 end*************************/

        LeaseOrder leaseOrder = get(paymentOrderPO.getBusinessId());
        //第一次消费，需要抵扣保证金、定金、转低金
        if (LeaseOrderStateEnum.SUBMITTED.getCode().equals(leaseOrder.getState())) {
            //第一笔消费抵扣保证金
            if(leaseOrder.getDepositDeduction() > 0L){
                deductionLeaseOrderItemDepositAmount(leaseOrder);
            }
            //消费定金、转低
            BaseOutput customerAccountOutput = customerAccountService.paySuccessLeaseOrderCustomerAmountConsume(
                    leaseOrder.getId(), leaseOrder.getCode(),
                    leaseOrder.getCustomerId(), leaseOrder.getEarnestDeduction(),
                    leaseOrder.getTransferDeduction(),
                    leaseOrder.getMarketId(),settleOrder.getOperatorId(),settleOrder.getOperatorName());
            if(!customerAccountOutput.isSuccess()){
                LOG.info("结算成功，消费定金、转低接口异常 【租赁单编号:{},定金:{},转抵:{}】", leaseOrder.getCode(),leaseOrder.getEarnestDeduction(),leaseOrder.getTransferDeduction());
                throw new BusinessException(ResultCode.DATA_ERROR,customerAccountOutput.getMessage());
            }
            //解冻出租摊位
            leaseBooth(leaseOrder);
        }

        /***************************更新租赁单及其订单项相关字段 begin*********************/
        //根据租赁时间和当前时间比对，单子是未生效、已生效、还是已过期
        Date now = new Date();
        if (now.getTime() >= leaseOrder.getStartTime().getTime() &&
                now.getTime() <= leaseOrder.getEndTime().getTime()) {
            leaseOrder.setState(LeaseOrderStateEnum.EFFECTIVE.getCode());
        } else if (now.getTime() < leaseOrder.getStartTime().getTime()) {
            leaseOrder.setState(LeaseOrderStateEnum.NOT_ACTIVE.getCode());
        } else if (now.getTime() > leaseOrder.getEndTime().getTime()) {
            leaseOrder.setState(LeaseOrderStateEnum.EXPIRED.getCode());
        }

        if ((leaseOrder.getWaitAmount() - paymentOrderPO.getAmount()) == 0L) {
            leaseOrder.setPayState(PayStateEnum.PAID.getCode());
        }
        leaseOrder.setWaitAmount(leaseOrder.getWaitAmount() - paymentOrderPO.getAmount());
        leaseOrder.setPaidAmount(leaseOrder.getPaidAmount() + paymentOrderPO.getAmount());
        leaseOrder.setPaymentId(0L);
        if (updateSelective(leaseOrder) == 0) {
            LOG.info("摊位租赁单提交状态更新失败 乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试");
        }
        LeaseOrderItem leaseOrderItemCondition = DTOUtils.newInstance(LeaseOrderItem.class);
        leaseOrderItemCondition.setLeaseOrderId(leaseOrder.getId());
        List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(leaseOrderItemCondition);
        leaseOrderItems.stream().forEach(o -> {
            o.setState(leaseOrder.getState());
            o.setPayState(leaseOrder.getPayState());
            //只有租赁单费用交清后，摊位项保证金才真正划入摊位上，才可用于其他订单保证金抵扣
            if(PayStateEnum.PAID.getCode().equals(leaseOrder.getPayState()) && o.getDepositAmount() > 0L){
                o.setDepositAmountFlag(DepositAmountFlagEnum.TRANSFERRED.getCode());
            }
        });
        if (leaseOrderItemService.batchUpdateSelective(leaseOrderItems) != leaseOrderItems.size()) {
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
        /***************************更新租赁单及其订单项相关字段 end*********************/

        businessLogRpc.save(recordPayLog(settleOrder,leaseOrder));
        return BaseOutput.success().setData(true);
    }

    /**
     * 记录交费日志
     * @param settleOrder
     * @param leaseOrder
     */
    public BusinessLog recordPayLog(SettleOrder settleOrder, LeaseOrder leaseOrder) {
        BusinessLog businessLog = new BusinessLog();
        businessLog.setBusinessId(leaseOrder.getId());
        businessLog.setBusinessCode(leaseOrder.getCode());
        businessLog.setContent(settleOrder.getCode());
        businessLog.setOperationType("pay");
        businessLog.setMarketId(settleOrder.getMarketId());
        businessLog.setOperatorId(settleOrder.getOperatorId());
        businessLog.setOperatorName(settleOrder.getOperatorName());
        businessLog.setBusinessType(LogBizTypeConst.BOOTH_LEASE);
        businessLog.setSystemCode("INTELLIGENT_ASSETS");
        return businessLog;
    }

    /**
     * 解冻消费摊位
     * @param leaseOrder
     */
    private void leaseBooth(LeaseOrder leaseOrder) {
        BoothRentDTO boothRentDTO = new BoothRentDTO();
        boothRentDTO.setOrderId(leaseOrder.getId().toString());
        BaseOutput assetsOutput = assetsRpc.rentBoothRent(boothRentDTO);
        if(!assetsOutput.isSuccess()){
            LOG.info("摊位解冻出租异常{}",assetsOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR,assetsOutput.getMessage());
        }
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
    @GlobalTransactional
    public BaseOutput submitPayment(Long id, Long amount, Long waitAmount) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new BusinessException(ResultCode.DATA_ERROR,"未登录");
        }

        LeaseOrder leaseOrder = get(id);
        LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
        condition.setLeaseOrderId(leaseOrder.getId());
        List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(condition);

        /***************************检查是否可以提交付款 begin*********************/
        if(leaseOrder.getState().equals(LeaseOrderStateEnum.CREATED.getCode())){
            //检查客户状态
            checkCustomerState(leaseOrder.getCustomerId(),leaseOrder.getMarketId());
            leaseOrderItems.forEach(o->{
                //检查摊位状态
                checkBoothState(o.getBoothId());
            });
        }
        //检查是否可以进行提交付款
        checkSubmitPayment(id, amount, waitAmount, leaseOrder,leaseOrderItems);
        /***************************检查是否可以提交付款 end*********************/

        //新增缴费单
        PaymentOrder paymentOrder = buildPaymentOrder(leaseOrder);
        paymentOrder.setAmount(amount);
        paymentOrderService.insertSelective(paymentOrder);

        if (leaseOrder.getState().equals(LeaseOrderStateEnum.CREATED.getCode())) {//第一次发起付款，相关业务实现
            //冻结保证金
            if(leaseOrder.getDepositDeduction() > 0L){
                frozenLeaseOrderItemDepositAmount(leaseOrder);
            }
            //冻结定金和转低
            BaseOutput customerAccountOutput = customerAccountService.submitLeaseOrderCustomerAmountFrozen(
                    leaseOrder.getId(), leaseOrder.getCode(), leaseOrder.getCustomerId(),
                    leaseOrder.getEarnestDeduction(), leaseOrder.getTransferDeduction(),
                    leaseOrder.getMarketId(),userTicket.getId(),userTicket.getRealName());
            if(!customerAccountOutput.isSuccess()){
                LOG.info("冻结定金和转低异常【编号：{}】", leaseOrder.getCode());
                if(ResultCodeConst.EARNEST_ERROR.equals(customerAccountOutput.getCode())){
                    throw new BusinessException(ResultCode.DATA_ERROR,"客户定金可用金额不足，请核实修改后重新保存");
                }else if(ResultCodeConst.TRANSFER_ERROR.equals(customerAccountOutput.getCode())){
                    throw new BusinessException(ResultCode.DATA_ERROR,"客户转低可用金额不足，请核实修改后重新保存");
                }else{
                    throw new BusinessException(ResultCode.DATA_ERROR,customerAccountOutput.getMessage());
                }
            }
            //冻结摊位
            frozenBooth(leaseOrder,leaseOrderItems);
            leaseOrder.setState(LeaseOrderStateEnum.SUBMITTED.getCode());
            leaseOrder.setPaymentId(paymentOrder.getId());
            //更新摊位租赁单状态
            cascadeUpdateLeaseOrderState(leaseOrder, true, LeaseOrderItemStateEnum.SUBMITTED);
        } else if (leaseOrder.getState().equals(LeaseOrderStateEnum.SUBMITTED.getCode())) {//非第一次付款，相关业务实现
            //判断缴费单是否需要撤回 需要撤回则撤回
            if (null != leaseOrder.getPaymentId() && 0 != leaseOrder.getPaymentId()) {
                withdrawPaymentOrder(leaseOrder.getPaymentId());
            }
            leaseOrder.setPaymentId(paymentOrder.getId());
            //更新摊位租赁单状态
            if (updateSelective(leaseOrder) == 0) {
                LOG.info("摊位租赁单提交状态更新失败 乐观锁生效 【租赁单ID {}】", id);
                throw new BusinessException(ResultCode.DATA_ERROR,"摊位租赁单提交状态更新失败");
            }
        }

        //新增结算单
        SettleOrderDto settleOrder = buildSettleOrderDto(leaseOrder);
        settleOrder.setAmount(amount);
        settleOrder.setOrderCode(paymentOrder.getCode());//订单号
        settleOrder.setBusinessCode(paymentOrder.getBusinessCode());//业务单号
        BaseOutput<SettleOrder> settlementOutput = settlementRpc.submit(settleOrder);
        if (settlementOutput.isSuccess()) {
            try {
                saveSettlementCode(paymentOrder.getId(), settlementOutput.getData().getCode());
            } catch (Exception e) {
                LOG.error("结算编号冗余异常 租赁单【编号：{}】缴费单【编号：{}】 异常信息{}", leaseOrder.getCode(), paymentOrder.getCode(), e.getMessage());
            }
        } else {
            LOG.info("提交付款调用结算异常【编号：{}】", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR,settlementOutput.getMessage());
        }

        //日志上下文构建
        LoggerUtil.buildLoggerContext(leaseOrder.getId(),leaseOrder.getCode(),userTicket.getId(),userTicket.getRealName(),leaseOrder.getMarketId(),null);
        return settlementOutput;
    }

    /**
     * 冻结摊位
     * @param leaseOrderItems
     */
    private void frozenBooth(LeaseOrder leaseOrder,List<LeaseOrderItem> leaseOrderItems) {
        leaseOrderItems.forEach(o->{
            BoothRentDTO boothRentDTO = new BoothRentDTO();
            boothRentDTO.setBoothId(o.getBoothId());
            boothRentDTO.setStart(leaseOrder.getStartTime());
            boothRentDTO.setEnd(leaseOrder.getEndTime());
            boothRentDTO.setOrderId(leaseOrder.getId().toString());
            BaseOutput assetsOutput = assetsRpc.addBoothRent(boothRentDTO);
            if(!assetsOutput.isSuccess()){
                LOG.info("冻结摊位异常【编号：{}】", leaseOrder.getCode());
                if(assetsOutput.getCode().equals("2500")){
                    throw new BusinessException(ResultCode.DATA_ERROR,o.getBoothName()+"选择的时间期限重复，请修改后重新保存");
                }else{
                    throw new BusinessException(ResultCode.DATA_ERROR,assetsOutput.getMessage());
                }
            }
        });
    }

    /**
     * 解冻租赁订单所有摊位
     * @param leaseOrderId
     */
    public void unFrozenAllBooth(Long leaseOrderId) {
        BoothRentDTO boothRentDTO = new BoothRentDTO();
        boothRentDTO.setOrderId(leaseOrderId.toString());
        BaseOutput assetsOutput = assetsRpc.deleteBoothRent(boothRentDTO);
        if(!assetsOutput.isSuccess()){
            LOG.info("解冻租赁订单【leaseOrderId:{}】所有摊位异常{}", leaseOrderId, assetsOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR,assetsOutput.getMessage());
        }
    }

    /**
     * 解冻租赁订单单个摊位
     * @param leaseOrderId
     * @param boothId
     */
    public void unFrozenSingleBooth(Long leaseOrderId,Long boothId) {
        BoothRentDTO boothRentDTO = new BoothRentDTO();
        boothRentDTO.setOrderId(leaseOrderId.toString());
        boothRentDTO.setBoothId(boothId);
        BaseOutput assetsOutput = assetsRpc.deleteBoothRent(boothRentDTO);
        if(!assetsOutput.isSuccess()){
            LOG.info("解冻租赁订单【leaseOrderId:{},boothId:{}】单个摊位异常{}", leaseOrderId, boothId, assetsOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR,assetsOutput.getMessage());
        }
    }

    /**
     * 检查是否可以进行提交付款
     * @param id
     * @param amount
     * @param waitAmount
     * @param leaseOrder
     * @param leaseOrderItems
     */
    private void checkSubmitPayment(Long id, Long amount, Long waitAmount, LeaseOrder leaseOrder,List<LeaseOrderItem> leaseOrderItems) {
        //服务器端再次验证保证金抵扣额
        if(LeaseOrderStateEnum.CREATED.getCode().equals(leaseOrder.getState())){
            LeaseOrderItemListDto condition = DTOUtils.newInstance(LeaseOrderItemListDto.class);
            condition.setCustomerId(leaseOrder.getCustomerId());
            condition.setBoothIds(leaseOrderItems.stream().map(o->o.getBoothId()).collect(Collectors.toList()));
            Map<Long,List<LeaseOrderItem>> depositAmountAvailableItemMap = leaseOrderItemService.queryDepositAmountAvailableItem(condition);
            Long availableDepositAmount = 0L;
            for (Long key : depositAmountAvailableItemMap.keySet()) {
                List<LeaseOrderItem> depositAmountItems = depositAmountAvailableItemMap.get(key);
                for (LeaseOrderItem item:depositAmountItems
                ) {
                    availableDepositAmount += item.getDepositAmount();
                }
            }
            if(!leaseOrder.getDepositDeduction().equals(availableDepositAmount)){
                LOG.info("租赁单编号【{}】 保证金可抵扣额发生变化，需要重新修改后进行订单提交操作", leaseOrder.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR, "租赁单编号【" + leaseOrder.getCode() + "】 保证金可抵扣额发生变化，需要重新修改后进行订单提交操作");
            }
        }
        //提交付款条件：已交清或退款中、已退款不能进行提交付款操作
        if (PayStateEnum.PAID.getCode().equals(leaseOrder.getPayState())) {
            LOG.info("租赁单编号【{}】 已交清，不可以进行提交付款操作", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "租赁单编号【" + leaseOrder.getCode() + "】 已交清，不可以进行提交付款操作");
        }
        if(!RefundStateEnum.WAIT_APPLY.getCode().equals(leaseOrder.getRefundState())){
            LOG.info("租赁单编号【{}】已发起退款，不可以进行提交付款操作", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "租赁单编号【" + leaseOrder.getCode() + "】 已发起退款，不可以进行提交付款操作");
        }
        if(LeaseOrderStateEnum.CANCELD.getCode().equals(leaseOrder.getState())){
            LOG.info("租赁单编号【{}】已取消，不可以进行提交付款操作", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "租赁单编号【" + leaseOrder.getCode() + "】 已取消，不可以进行提交付款操作");
        }
        if (amount.equals(0L) && !waitAmount.equals(0L)) {
            throw new BusinessException(ResultCode.DATA_ERROR,"摊位租赁单费用已结清");
        }
        if (amount > leaseOrder.getWaitAmount()) {
            LOG.info("摊位租赁单【ID {}】 支付金额【{}】大于待付金额【{}】", id, amount, leaseOrder.getWaitAmount());
            throw new BusinessException(ResultCode.DATA_ERROR,"支付金额大于待付金额");
        }
        if (!waitAmount.equals(leaseOrder.getWaitAmount())) {
            LOG.info("摊位租赁单待缴费金额已发生变更，请重试【ID {}】 旧金额【{}】新金额【{}】", id, waitAmount, leaseOrder.getWaitAmount());
            throw new BusinessException(ResultCode.DATA_ERROR,"摊位租赁单待缴费金额已发生变更，请重试");
        }
    }

    /**
     * 摊位租赁单保证金抵扣
     *
     * @param leaseOrder
     */
    private void deductionLeaseOrderItemDepositAmount(LeaseOrder leaseOrder) {
        List<LeaseOrderItem> depositAmountSourceItems = collectAllDepositAmountSource(leaseOrder.getId());
        depositAmountSourceItems.stream().forEach(o -> {
            if (DepositAmountFlagEnum.FROZEN.getCode().equals(o.getDepositAmountFlag())) {
                o.setDepositAmountFlag(DepositAmountFlagEnum.DEDUCTION.getCode());
            } else {
                LOG.info("摊位租赁单保证金已被抵扣,不可重复操作,【源租赁单编号:{}，新租赁单编号:{}，摊位:{}】", o.getLeaseOrderCode(), leaseOrder.getCode(), o.getBoothName());
                throw new BusinessException(ResultCode.DATA_ERROR,o.getBoothName() + "保证金已被抵扣,不可重复操作");
            }
        });
        if (leaseOrderItemService.batchUpdateSelective(depositAmountSourceItems) != depositAmountSourceItems.size()) {
            LOG.info("源摊位租赁单项抵扣失败，乐观锁生效 【新租赁单编号={}】", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
        //记录保证金流水
        recordDepositAmountDetails(depositAmountSourceItems,leaseOrder,TransactionSceneTypeEnum.DEDUCT_USE);

    }

    /**
     * 摊位租赁单保证金冻结
     *
     * @param leaseOrder
     */
    private void frozenLeaseOrderItemDepositAmount(LeaseOrder leaseOrder) {
        List<LeaseOrderItem> depositAmountSourceItems = collectAllDepositAmountSource(leaseOrder.getId());
        depositAmountSourceItems.stream().forEach(o -> {
            //保证金为已转入且退款待申请且 费用已交清 方可冻结抵扣
            if (DepositAmountFlagEnum.TRANSFERRED.getCode().equals(o.getDepositAmountFlag())
                    && RefundStateEnum.WAIT_APPLY.getCode().equals(o.getRefundState())
                    && PayStateEnum.PAID.getCode().equals(o.getPayState())) {
                o.setDepositAmountFlag(DepositAmountFlagEnum.FROZEN.getCode());
            } else {
                String operateName = DepositAmountFlagEnum.getDepositAmountFlagEnum(o.getDepositAmountFlag()).getOperateName();
                LOG.info("【源租赁单编号:{}，新租赁单编号:{}】{}保证金已被{},保证金总抵扣额已发生变化不可进行抵扣，请修改后重试", o.getLeaseOrderCode(), leaseOrder.getCode(), o.getBoothName(), operateName);
                throw new BusinessException(ResultCode.DATA_ERROR,o.getBoothName() + "保证金已被" + operateName + ",保证金总抵扣额已发生变化不可进行抵扣，请修改后重试");
            }
        });
        if (leaseOrderItemService.batchUpdateSelective(depositAmountSourceItems) != depositAmountSourceItems.size()) {
            LOG.info("源摊位租赁单项冻结失败，乐观锁生效 【新租赁单编号={}】", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
        //记录保证金流水
        recordDepositAmountDetails(depositAmountSourceItems,leaseOrder,TransactionSceneTypeEnum.FROZEN);

    }

    /**
     * 摊位租赁单保证金解冻
     *
     * @param leaseOrder
     */
    private void unFrozenLeaseOrderItemDepositAmount(LeaseOrder leaseOrder) {
        List<LeaseOrderItem> depositAmountSourceItems = collectAllDepositAmountSource(leaseOrder.getId());
        depositAmountSourceItems.stream().forEach(o -> {
            if (DepositAmountFlagEnum.FROZEN.getCode().equals(o.getDepositAmountFlag())) {
                o.setDepositAmountFlag(DepositAmountFlagEnum.TRANSFERRED.getCode());
            } else {
                String operateName = DepositAmountFlagEnum.getDepositAmountFlagEnum(o.getDepositAmountFlag()).getOperateName();
                LOG.info("摊位保证金已被解冻,不可重复操作,【源租赁单编号:{}，新租赁单编号:{}，摊位:{}】", o.getLeaseOrderCode(), leaseOrder.getCode(), o.getId(), o.getBoothName());
                throw new BusinessException(ResultCode.DATA_ERROR,o.getBoothName() + "保证金已被解冻,不可重复操作");
            }
        });
        if (leaseOrderItemService.batchUpdateSelective(depositAmountSourceItems) != depositAmountSourceItems.size()) {
            LOG.info("源摊位租赁单项解冻失败,乐观锁生效 【新租赁单编号={}】", leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
        //记录保证金流水
        recordDepositAmountDetails(depositAmountSourceItems,leaseOrder,TransactionSceneTypeEnum.UNFROZEN);
    }

    /**
     * 记录保证金流水
     * @param depositAmountSourceItems
     * @param leaseOrder
     * @param type
     */
    private void recordDepositAmountDetails(List<LeaseOrderItem> depositAmountSourceItems, LeaseOrder leaseOrder,TransactionSceneTypeEnum type) {
        List<TransactionDetails> transactionDetailsList = new ArrayList<>();
        for (LeaseOrderItem item :
                depositAmountSourceItems) {
            TransactionDetails transactionDetails = transactionDetailsService.buildByConditions(type.getCode()
                    ,BizTypeEnum.BOOTH_LEASE.getCode(),TransactionItemTypeEnum.DEPOSIT.getCode()
                    , item.getDepositAmount(),item.getId(),item.getBoothName()
                    ,leaseOrder.getCustomerId(),leaseOrder.getCode(),leaseOrder.getMarketId(),null,null);
            transactionDetails.setCreateTime(new Date());
            transactionDetails.setModifyTime(new Date());
            transactionDetailsList.add(transactionDetails);
        }
        transactionDetailsService.batchInsert(transactionDetailsList);
    }

    /**
     * 查询所有源保证金项
     * @param id
     * @return
     */
    private List<LeaseOrderItem> collectAllDepositAmountSource(Long id) {
        LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
        condition.setLeaseOrderId(id);
        List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(condition);
        List<String> depositAmountSourceIds = leaseOrderItems.stream().filter(o-> null != o.getDepositAmountSourceId()).map(LeaseOrderItem::getDepositAmountSourceId).collect(Collectors.toList());
        List<Long> allSourceIds = new ArrayList<>();
        for (String depositAmountSourceId:depositAmountSourceIds
        ) {
            allSourceIds.addAll(Arrays.asList(depositAmountSourceId.split(",")).stream().map(o->Long.valueOf(o)).collect(Collectors.toList()));
        }
        LeaseOrderItemListDto depositAmountSourceCondition = DTOUtils.newInstance(LeaseOrderItemListDto.class);
        depositAmountSourceCondition.setIds(allSourceIds);
        return leaseOrderItemService.listByExample(depositAmountSourceCondition);
    }

    /**
     * 级联更新摊位租赁订单状态 订单项状态级联发生变化
     *
     * @param leaseOrder
     * @param isCascade  false不级联更新订单项 true 级联更新订单项
     * @param stateEnum  isCascade为false时，此处可以传null
     */
    @Transactional
    public void cascadeUpdateLeaseOrderState(LeaseOrder leaseOrder, boolean isCascade, LeaseOrderItemStateEnum stateEnum) {
        if (updateSelective(leaseOrder) == 0) {
            LOG.info("摊位租赁单提交状态更新失败 乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试");
        }

        if (isCascade) {
            LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
            condition.setLeaseOrderId(leaseOrder.getId());
            List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(condition);
            leaseOrderItems.stream().forEach(o -> o.setState(stateEnum.getCode()));
            if (leaseOrderItemService.batchUpdateSelective(leaseOrderItems) != leaseOrderItems.size()) {
                LOG.info("级联更新摊位租赁订单状态失败,乐观锁生效 【租赁单编号:{},变更目标状态:{}】", leaseOrder.getCode(), stateEnum.getName());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }
        }
    }

    /**
     * 冗余结算编号到缴费单
     *
     * @param paymentId
     * @param settlementCode
     */
    public void saveSettlementCode(Long paymentId, String settlementCode) {
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
            throw new BusinessException(ResultCode.DATA_ERROR,"未登录");
        }
        PaymentOrder paymentOrder = DTOUtils.newInstance(PaymentOrder.class);
        BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.PAYMENT_ORDER.getCode());
        if (!bizNumberOutput.isSuccess()) {
            LOG.info("租赁单【编号：{}】,缴费单编号生成异常",leaseOrder.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR,"编号生成器微服务异常");
        }
        paymentOrder.setCode(userTicket.getFirmCode().toUpperCase() + bizNumberOutput.getData());
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
            throw new BusinessException(ResultCode.DATA_ERROR,"未登录");
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
        settleOrder.setReturnUrl(settlerHandlerUrl);
        settleOrder.setSubmitterDepId(userTicket.getDepartmentId());
        settleOrder.setSubmitterDepName(null == userTicket.getDepartmentId() ? null : departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
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
            LOG.info("租赁单【编号：{}】状态为【{}】，不可以进行取消操作", leaseOrder.getCode(), stateName);
            throw new BusinessException(ResultCode.DATA_ERROR,"租赁单状态为【" + stateName + "】，不可以进行取消操作");
        }
        leaseOrder.setState(LeaseOrderStateEnum.CANCELD.getCode());
        leaseOrder.setCancelerId(userTicket.getId());
        leaseOrder.setCanceler(userTicket.getRealName());

        String formatNow = DateUtils.format(new Date(),"yyyyMMddHHmmssSSS");
        leaseOrder.setContractNo(leaseOrder.getContractNo() + "_" + formatNow);

        //联动摊位租赁单项状态 取消
        cascadeUpdateLeaseOrderState(leaseOrder, true, LeaseOrderItemStateEnum.CANCELD);
        //日志上下文构建
        LoggerUtil.buildLoggerContext(leaseOrder.getId(),leaseOrder.getCode(),userTicket.getId(),userTicket.getRealName(),userTicket.getFirmId(),null);
        return BaseOutput.success();
    }

    /**
     * 撤回摊位租赁订单
     *
     * @param id
     * @return
     */
    @Transactional
    @GlobalTransactional
    @Override
    public BaseOutput withdrawOrder(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        LeaseOrder leaseOrder = get(id);
        if (!LeaseOrderStateEnum.SUBMITTED.getCode().equals(leaseOrder.getState())) {
            String stateName = LeaseOrderStateEnum.getLeaseOrderStateEnum(leaseOrder.getState()).getName();
            LOG.info("租赁单【编号：{}】状态为【{}】，不可以进行撤回操作", leaseOrder.getCode(), stateName);
            throw new BusinessException(ResultCode.DATA_ERROR,"租赁单状态为【" + stateName + "】，不可以进行撤回操作");
        }
        if (null != leaseOrder.getPaymentId() && 0L != leaseOrder.getPaymentId()) {
            withdrawPaymentOrder(leaseOrder.getPaymentId());
            leaseOrder.setPaymentId(0L);
        }
        leaseOrder.setState(LeaseOrderStateEnum.CREATED.getCode());
        cascadeUpdateLeaseOrderState(leaseOrder, true, LeaseOrderItemStateEnum.CREATED);

        //解冻摊位保证金
        if(leaseOrder.getDepositDeduction() > 0L){
            unFrozenLeaseOrderItemDepositAmount(leaseOrder);
        }
        //解冻定金、转抵
        BaseOutput customerAccountOutput = customerAccountService.withdrawLeaseOrderCustomerAmountUnFrozen(
                leaseOrder.getId(), leaseOrder.getCode(), leaseOrder.getCustomerId(),
                leaseOrder.getEarnestDeduction(), leaseOrder.getTransferDeduction(),
                leaseOrder.getMarketId(),userTicket.getId(),userTicket.getRealName());
        if(!customerAccountOutput.isSuccess()){
            LOG.info("租赁单撤回 解冻定金、转抵异常【编号：{},MSG:{}】", leaseOrder.getCode(), customerAccountOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR,customerAccountOutput.getMessage());
        }
        //解冻摊位
        unFrozenAllBooth(leaseOrder.getId());
        //日志上下文构建
        LoggerUtil.buildLoggerContext(leaseOrder.getId(),leaseOrder.getCode(),userTicket.getId(),userTicket.getRealName(),userTicket.getFirmId(),null);
        return BaseOutput.success();
    }

    /**
     * 撤回缴费单 判断缴费单是否需要撤回 需要撤回则撤回
     * 如果撤回时发现缴费单状态为及时同步结算状态 则抛出异常 提示用户带结算同步后再操作
     *
     * @param paymentId
     */
    public void withdrawPaymentOrder(Long paymentId) {
        PaymentOrder payingOrder = paymentOrderService.get(paymentId);
        if (PaymentOrderStateEnum.NOT_PAID.getCode().equals(payingOrder.getState())) {
            String paymentCode = payingOrder.getCode();
            BaseOutput output = settlementRpc.cancel(settlementAppId,paymentCode);
            if (!output.isSuccess()) {
                LOG.info("结算单撤回异常 【缴费单CODE {}】", paymentCode);
                throw new BusinessException(ResultCode.DATA_ERROR,output.getMessage());
            }

            payingOrder.setState(PaymentOrderStateEnum.CANCEL.getCode());
            if(paymentOrderService.updateSelective(payingOrder) == 0){
                LOG.info("撤回缴费单异常，乐观锁生效，【缴费单编号:{}】",payingOrder.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }
        }
    }

    /**
     * 扫描待生效的订单，做生效处理
     *
     * @return
     */
    @Override
    public BaseOutput<Boolean> scanEffectiveLeaseOrder() {
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
                    LOG.error("租赁单【编号：{}】变更生效异常。{}", o.getCode(), e.getMessage());
                    LOG.error("租赁单变更生效异常", e);
                }
            });
        }
        LOG.info("摊位租赁生效处理调度执行完毕");
        return BaseOutput.success().setData(true);
    }

    /**
     * 租赁单生效处理
     *
     * @param o
     */
    @Transactional
    public void leaseOrderEffectiveHandler(LeaseOrder o) {
        o.setState(LeaseOrderStateEnum.EFFECTIVE.getCode());
        if (updateSelective(o) == 0) {
            LOG.info("租赁单生效处理异常,乐观锁生效 【租赁单ID {}】", o.getId());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试");
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
            LOG.info("租赁单生效处理异常,级联批量处理摊位订单项乐观锁生效【订单ID:{}", o.getId());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
    }

    /**
     * 扫描待到期的订单，做到期处理
     *
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
                    LOG.error("租赁单【编号：{}】变更到期异常。{}", o.getCode(), e.getMessage());
                    LOG.error("租赁单变更到期异常", e);
                }
            });
        }
        LOG.info("摊位租赁到期处理调度执行完毕");
        return BaseOutput.success().setData(true);
    }

    /**
     * 租赁单到期处理
     *
     * @param o
     */
    @Transactional
    public void leaseOrderExpiredHandler(LeaseOrder o) {
        o.setState(LeaseOrderStateEnum.EXPIRED.getCode());
        if (updateSelective(o) == 0) {
            LOG.info("租赁单到期处理异常 乐观锁生效 【租赁单ID {}】", o.getId());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试");
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
            LOG.info("租赁单到期处理异常,级联批量处理摊位订单项乐观锁生效【订单ID:{}", o.getId());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
    }

    @Override
    @Transactional
    @GlobalTransactional
    public BaseOutput createRefundOrder(RefundOrderDto refundOrderDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        if(null == refundOrderDto.getBusinessItemId()){
            //主单上退款申请
            LeaseOrder leaseOrder = get(refundOrderDto.getBusinessId());
            LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
            condition.setLeaseOrderId(leaseOrder.getId());
            List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(condition);

            if(!RefundStateEnum.WAIT_APPLY.getCode().equals(leaseOrder.getRefundState())){
                throw new BusinessException(ResultCode.DATA_ERROR,"租赁单状态已发生变更，不能发起退款申请");
            }
            if(PayStateEnum.PAID.getCode().equals(leaseOrder.getPayState())){
                throw new BusinessException(ResultCode.DATA_ERROR,"租赁单费用已交清不能，只能在租赁摊位上进行退款");
            }
            //退款总金额不能大于未交清可退金额 （已交金额+所有抵扣项）
            if (refundOrderDto.getTotalRefundAmount() > (leaseOrder.getPaidAmount() + leaseOrder.getDepositDeduction() + leaseOrder.getEarnestDeduction() + leaseOrder.getTransferDeduction())) {
                throw new BusinessException(ResultCode.DATA_ERROR,"退款总金额不能大于可退金额");
            }

            //判断缴费单是否需要撤回 需要撤回则撤回
            if (null != leaseOrder.getPaymentId() && 0 != leaseOrder.getPaymentId()) {
                withdrawPaymentOrder(leaseOrder.getPaymentId());
                leaseOrder.setPaymentId(0L);
            }

            leaseOrder.setRefundState(RefundStateEnum.REFUNDING.getCode());
            leaseOrder.setRefundAmount(refundOrderDto.getTotalRefundAmount());
            if(updateSelective(leaseOrder) == 0){
                LOG.info("租赁单退款申请异常 租赁单更新乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }

            //级联订单项退款状态
            leaseOrderItems.stream().forEach(o -> o.setRefundState(RefundStateEnum.REFUNDING.getCode()));
            if (leaseOrderItemService.batchUpdateSelective(leaseOrderItems) != leaseOrderItems.size()) {
                LOG.info("租赁单退款申请异常 级联批量摊位租赁单项更新乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }
        }else{
            //订单项退款申请
            LeaseOrderItem leaseOrderItem = leaseOrderItemService.get(refundOrderDto.getBusinessItemId());
            //已到期或已停租状态才能发起退款申请
            if (LeaseOrderItemStateEnum.EXPIRED.getCode().equals(leaseOrderItem.getState()) && LeaseOrderItemStateEnum.RENTED_OUT.getCode().equals(leaseOrderItem.getState())) {
                throw new BusinessException(ResultCode.DATA_ERROR,"摊位项状态已发生变更，不能发起退款申请");
            }
            if(!RefundStateEnum.WAIT_APPLY.getCode().equals(leaseOrderItem.getRefundState())){
                throw new BusinessException(ResultCode.DATA_ERROR,"摊位项状态已发生变更，不能发起退款申请");
            }
            //保证金已转入状态才可退
            if(refundOrderDto.getDepositRefundAmount() > 0 && !leaseOrderItem.getDepositAmountFlag().equals(DepositAmountFlagEnum.TRANSFERRED.getCode())){
                throw new BusinessException(ResultCode.DATA_ERROR,"摊位保证金状态已发生变更，不能进行退款，请修改");
            }
            if(refundOrderDto.getRentRefundAmount() > leaseOrderItem.getRentAmount()){
                throw new BusinessException(ResultCode.DATA_ERROR,"租金退款额大于可退款额");
            }
            if(refundOrderDto.getDepositRefundAmount() > leaseOrderItem.getDepositAmount()){
                throw new BusinessException(ResultCode.DATA_ERROR,"保证金退款额大于可退款额");
            }
            if(refundOrderDto.getManageRefundAmount() > leaseOrderItem.getManageAmount()){
                throw new BusinessException(ResultCode.DATA_ERROR,"物管费退款额大于可退款额");
            }
            if(!refundOrderDto.getTotalRefundAmount().equals(refundOrderDto.getRentRefundAmount() + refundOrderDto.getDepositRefundAmount() + refundOrderDto.getManageRefundAmount())){
                throw new BusinessException(ResultCode.DATA_ERROR,"退款金额分配错误，请重新修改再保存");
            }

            leaseOrderItem.setRefundState(RefundStateEnum.REFUNDING.getCode());
            leaseOrderItem.setRefundAmount(refundOrderDto.getTotalRefundAmount());
            leaseOrderItem.setDepositRefundAmount(refundOrderDto.getDepositRefundAmount());
            leaseOrderItem.setRentRefundAmount(refundOrderDto.getRentRefundAmount());
            leaseOrderItem.setManageRefundAmount(refundOrderDto.getManageRefundAmount());
            if(leaseOrderItemService.updateSelective(leaseOrderItem) == 0){
                LOG.info("摊位租赁订单项退款申请异常 更新乐观锁生效 【租赁单项ID {}】", leaseOrderItem.getId());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }
        }

        refundOrderDto.setBizType(BizTypeEnum.BOOTH_LEASE.getCode());
        BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.LEASE_REFUND_ORDER.getCode());
        if (!bizNumberOutput.isSuccess()) {
            LOG.info("租赁单【编号：{}】退款单编号生成异常",refundOrderDto.getBusinessCode());
            throw new BusinessException(ResultCode.DATA_ERROR,"编号生成器微服务异常");
        }
        refundOrderDto.setCode(userTicket.getFirmCode().toUpperCase() + bizNumberOutput.getData());
        if(!refundOrderService.doAddHandler(refundOrderDto).isSuccess()){
            LOG.info("租赁单【编号：{}】退款申请接口异常",refundOrderDto.getBusinessCode());
            throw new BusinessException(ResultCode.DATA_ERROR,"退款申请接口异常");
        }

        if(CollectionUtils.isNotEmpty(refundOrderDto.getTransferDeductionItems())){
            refundOrderDto.getTransferDeductionItems().forEach(o->{
                o.setRefundOrderId(refundOrderDto.getId());
                transferDeductionItemService.insertSelective(o);
            });
        }
        return BaseOutput.success();
    }

    @Override
    public BaseOutput<PrintDataDto> queryPrintData(String orderCode, Integer reprint) {
        PaymentOrder paymentOrderCondition = DTOUtils.newInstance(PaymentOrder.class);
        paymentOrderCondition.setCode(orderCode);
        PaymentOrder paymentOrder = paymentOrderService.list(paymentOrderCondition).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            LOG.info("租赁单打印异常 【businessCode:{}】无效",orderCode);
            throw new BusinessException(ResultCode.DATA_ERROR,"businessCode无效");
        }
        if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
            LOG.info("租赁单打印异常 【businessCode:{}】此单未支付",orderCode);
            return BaseOutput.failure("此单未支付");
        }

        LeaseOrder leaseOrder = get(paymentOrder.getBusinessId());
        PrintDataDto printDataDto = new PrintDataDto();
        LeaseOrderPrintDto leaseOrderPrintDto = new LeaseOrderPrintDto();
        leaseOrderPrintDto.setPrintTime(new Date());
        leaseOrderPrintDto.setReprint(reprint == 2 ? "(补打)" : "");
        leaseOrderPrintDto.setLeaseOrderCode(leaseOrder.getCode());
        if (PayStateEnum.PAID.getCode().equals(leaseOrder.getPayState())) {
            leaseOrderPrintDto.setBusinessType(BizTypeEnum.BOOTH_LEASE.getName());
            printDataDto.setName(PrintTemplateEnum.BOOTH_LEASE_PAID.getCode());
        } else {
            leaseOrderPrintDto.setBusinessType(BizTypeEnum.EARNEST.getName());
            printDataDto.setName(PrintTemplateEnum.BOOTH_LEASE_NOT_PAID.getCode());
        }
        leaseOrderPrintDto.setCustomerName(leaseOrder.getCustomerName());
        leaseOrderPrintDto.setCustomerCellphone(leaseOrder.getCustomerCellphone());
        leaseOrderPrintDto.setStartTime(leaseOrder.getStartTime());
        leaseOrderPrintDto.setEndTime(leaseOrder.getEndTime());
        leaseOrderPrintDto.setIsRenew(IsRenewEnum.getIsRenewEnum(leaseOrder.getIsRenew()).getName());
        leaseOrderPrintDto.setCategoryName(leaseOrder.getCategoryName());
        leaseOrderPrintDto.setNotes(leaseOrder.getNotes());
        leaseOrderPrintDto.setTotalAmount(MoneyUtils.centToYuan(leaseOrder.getTotalAmount()));
        leaseOrderPrintDto.setDepositDeduction(MoneyUtils.centToYuan(leaseOrder.getDepositDeduction()));

        PaymentOrder paymentOrderConditions = DTOUtils.newInstance(PaymentOrder.class);
        paymentOrderConditions.setBusinessId(paymentOrder.getBusinessId());
        List<PaymentOrder> paymentOrders = paymentOrderService.list(paymentOrderConditions);
        Long totalPayAmountExcludeLast = 0L;
        for (PaymentOrder order : paymentOrders) {
            if (!order.getCode().equals(orderCode) && order.getState().equals(PaymentOrderStateEnum.PAID.getCode())) {
                totalPayAmountExcludeLast += order.getAmount();
            }
        }
        //除最后一次所交费用+定金抵扣 之和未总定金
        leaseOrderPrintDto.setEarnestDeduction(MoneyUtils.centToYuan(leaseOrder.getEarnestDeduction() + totalPayAmountExcludeLast));
        leaseOrderPrintDto.setTransferDeduction(MoneyUtils.centToYuan(leaseOrder.getTransferDeduction()));
        leaseOrderPrintDto.setPayAmount(MoneyUtils.centToYuan(leaseOrder.getPayAmount()));
        leaseOrderPrintDto.setAmount(MoneyUtils.centToYuan(paymentOrder.getAmount()));
        leaseOrderPrintDto.setSettlementWay(SettleWayEnum.getNameByCode(paymentOrder.getSettlementWay()));
        leaseOrderPrintDto.setSettlementOperator(paymentOrder.getSettlementOperator());
        leaseOrderPrintDto.setSubmitter(paymentOrder.getCreator());

        LeaseOrderItem leaseOrderItemCondition = DTOUtils.newInstance(LeaseOrderItem.class);
        leaseOrderItemCondition.setLeaseOrderId(leaseOrder.getId());
        List<LeaseOrderItemPrintDto> leaseOrderItemPrintDtos = new ArrayList<>();
        leaseOrderItemService.list(leaseOrderItemCondition).forEach(o -> {
            leaseOrderItemPrintDtos.add(LeaseOrderRefundOrderServiceImpl.leaseOrderItem2PrintDto(o));
        });
        leaseOrderPrintDto.setLeaseOrderItems(leaseOrderItemPrintDtos);
        printDataDto.setItem(BeanMapUtil.beanToMap(leaseOrderPrintDto));
        return BaseOutput.success().setData(printDataDto);
    }

    @Autowired
    private TransferDeductionItemService transferDeductionItemService;

    @Override
    @Transactional
    public BaseOutput cancelRefundOrderHandler(Long leaseOrderId,Long leaseOrderItemId) {
        if(null == leaseOrderItemId){
            LeaseOrder leaseOrder = get(leaseOrderId);
            if(!RefundStateEnum.REFUNDING.getCode().equals(leaseOrder.getRefundState())){
                LOG.info("租赁单【编号：{}】退款状态已发生变更，不能取消退款",leaseOrder.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR,"退款状态已发生变更，不能取消退款");
            }
            leaseOrder.setRefundState(RefundStateEnum.WAIT_APPLY.getCode());
            leaseOrder.setRefundAmount(0L);
            if(updateSelective(leaseOrder) == 0){
                LOG.info("摊位租赁单取消退款申请异常 更新租赁单乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }

            //级联订单项退款状态
            LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
            condition.setLeaseOrderId(leaseOrder.getId());
            List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(condition);
            leaseOrderItems.stream().forEach(o -> o.setRefundState(RefundStateEnum.WAIT_APPLY.getCode()));
            if (leaseOrderItemService.batchUpdateSelective(leaseOrderItems) != leaseOrderItems.size()) {
                LOG.info("摊位租赁单取消退款申请异常 级联批量更新租赁单订单项乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }
        }else{
            //订单项退款申请
            LeaseOrderItem leaseOrderItem = leaseOrderItemService.get(leaseOrderItemId);
            if(!RefundStateEnum.REFUNDING.getCode().equals(leaseOrderItem.getRefundState())){
                LOG.info("租赁单【编号：{}】退款状态已发生变更，不能取消退款",leaseOrderItem.getLeaseOrderCode());
                throw new BusinessException(ResultCode.DATA_ERROR,"退款状态已发生变更，不能取消退款");
            }

            leaseOrderItem.setRefundState(RefundStateEnum.WAIT_APPLY.getCode());
            leaseOrderItem.setRefundAmount(0L);
            leaseOrderItem.setDepositRefundAmount(0L);
            leaseOrderItem.setRentRefundAmount(0L);
            leaseOrderItem.setManageRefundAmount(0L);
            if(leaseOrderItemService.updateSelective(leaseOrderItem) == 0){
                LOG.info("摊位租赁单订单项取消退款申请异常 更新租赁单订单项乐观锁生效 【摊位租赁订单项ID {}】", leaseOrderItem.getId());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }
        }
        return BaseOutput.success();
    }

    @Override
    @Transactional
    public BaseOutput settleSuccessRefundOrderHandler(RefundOrder refundOrder) {
        LeaseOrder leaseOrder = get(refundOrder.getBusinessId());
        if(null == refundOrder.getBusinessItemId()){
            if(RefundStateEnum.REFUNDED.getCode().equals(leaseOrder.getRefundState())){
                LOG.info("此单已退款【leaseOrderId={}】",refundOrder.getBusinessId());
                return BaseOutput.success();
            }
            //解冻租赁订单所有摊位租赁
            unFrozenAllBooth(leaseOrder.getId());
            leaseOrder.setRefundState(RefundStateEnum.REFUNDED.getCode());
            leaseOrder.setState(LeaseOrderStateEnum.REFUNDED.getCode());
            if(updateSelective(leaseOrder) == 0){
                LOG.info("租赁单退款申请结算退款成功 更新租赁单乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }

            //级联订单项退款状态
            LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
            condition.setLeaseOrderId(leaseOrder.getId());
            List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(condition);
            leaseOrderItems.stream().forEach(o -> {
                o.setRefundState(RefundStateEnum.REFUNDED.getCode());
                o.setState(LeaseOrderItemStateEnum.REFUNDED.getCode());
            });
            if (leaseOrderItemService.batchUpdateSelective(leaseOrderItems) != leaseOrderItems.size()) {
                LOG.info("租赁单退款申请结算退款成功 级联批量更新租赁单订单项乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }
        }else{
            //订单项退款申请
            LeaseOrderItem leaseOrderItem = leaseOrderItemService.get(refundOrder.getBusinessItemId());
            if(RefundStateEnum.REFUNDED.getCode().equals(leaseOrderItem.getRefundState())){
                LOG.info("此单已退款【leaseOrderItemId={}】",refundOrder.getBusinessItemId());
                return BaseOutput.success();
            }

            //解冻单个摊位租赁
            unFrozenSingleBooth(leaseOrderItem.getLeaseOrderId(),leaseOrderItem.getBoothId());
            leaseOrderItem.setRefundState(RefundStateEnum.REFUNDED.getCode());
            leaseOrderItem.setState(LeaseOrderItemStateEnum.REFUNDED.getCode());
            if(leaseOrderItemService.updateSelective(leaseOrderItem) == 0){
                LOG.info("摊位租赁单订单项退款申请结算退款成功 更新租赁单订单项乐观锁生效 【租赁单订单项ID {}】", leaseOrderItem.getId());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }

            //级联检查其他订单项状态，如果全部为已退款，则需联动更新订单状态为已退款
            LeaseOrderItem condition = DTOUtils.newInstance(LeaseOrderItem.class);
            condition.setLeaseOrderId(leaseOrder.getId());
            List<LeaseOrderItem> leaseOrderItems = leaseOrderItemService.listByExample(condition);
            boolean isUpdateLeaseOrderState = true;
            for (LeaseOrderItem orderItem : leaseOrderItems) {
                if (orderItem.getId().equals(refundOrder.getBusinessItemId())) {
                    continue;
                } else if (!LeaseOrderItemStateEnum.REFUNDED.getCode().equals(orderItem.getState())) {
                    isUpdateLeaseOrderState = false;
                    break;
                }
            }
            if(isUpdateLeaseOrderState){
                leaseOrder.setState(LeaseOrderStateEnum.REFUNDED.getCode());
                leaseOrder.setRefundState(RefundStateEnum.REFUNDED.getCode());
                if(updateSelective(leaseOrder) == 0){
                    LOG.info("摊位租赁单订单项退款申请结算退款成功 级联更新租赁单乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
                    throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
                }
            }

            if(leaseOrderItem.getDepositRefundAmount() > 0L){
                recordDepositAmountRefundDetails(leaseOrder, leaseOrderItem);
            }
        }

        //转抵扣充值
        TransferDeductionItem transferDeductionItemCondition = DTOUtils.newInstance(TransferDeductionItem.class);
        transferDeductionItemCondition.setRefundOrderId(refundOrder.getId());
        List<TransferDeductionItem> transferDeductionItems = transferDeductionItemService.list(transferDeductionItemCondition);
        if(CollectionUtils.isNotEmpty(transferDeductionItems)){
            transferDeductionItems.forEach(o->{
                BaseOutput accountOutput = customerAccountService.leaseOrderRechargTransfer(
                        refundOrder.getId(),refundOrder.getCode(),o.getPayeeId(),o.getPayeeAmount(),
                        refundOrder.getMarketId(),refundOrder.getRefundOperatorId(),refundOrder.getRefundOperator());
                if(!accountOutput.isSuccess()){
                    LOG.info("退款单转低异常，【退款编号:{},收款人:{},收款金额:{},msg:{}】",refundOrder.getCode(),o.getPayee(),o.getPayeeAmount(),accountOutput.getMessage());
                    throw new BusinessException(ResultCode.DATA_ERROR,accountOutput.getMessage());
                }
            });
        }
        //记录退款日志
        businessLogRpc.save(recordRefundLog(refundOrder,leaseOrder));
        return BaseOutput.success();
    }

    /**
     *记录保证金流水（退款）
     * @param leaseOrder
     * @param leaseOrderItem
     */
    public void recordDepositAmountRefundDetails(LeaseOrder leaseOrder, LeaseOrderItem leaseOrderItem) {
        TransactionDetails transactionDetails = transactionDetailsService.buildByConditions(TransactionSceneTypeEnum.REFUND.getCode()
                , BizTypeEnum.BOOTH_LEASE.getCode(), TransactionItemTypeEnum.DEPOSIT.getCode()
                , leaseOrderItem.getDepositRefundAmount(),leaseOrderItem.getId(),leaseOrderItem.getBoothName()
                ,leaseOrder.getCustomerId(),null,leaseOrder.getMarketId(),null,null);
        transactionDetailsService.insertSelective(transactionDetails);
    }

    /**
     * 记录退款日志
     * @param refundOrder
     * @param leaseOrder
     */
    public BusinessLog recordRefundLog(RefundOrder refundOrder, LeaseOrder leaseOrder) {
        BusinessLog businessLog = new BusinessLog();
        businessLog.setBusinessId(leaseOrder.getId());
        businessLog.setBusinessCode(leaseOrder.getCode());
        businessLog.setContent(refundOrder.getSettlementCode());
        businessLog.setOperationType("refund");
        businessLog.setMarketId(refundOrder.getMarketId());
        businessLog.setOperatorId(refundOrder.getRefundOperatorId());
        businessLog.setOperatorName(refundOrder.getRefundOperator());
        businessLog.setBusinessType(LogBizTypeConst.BOOTH_LEASE);
        businessLog.setSystemCode("INTELLIGENT_ASSETS");
        return businessLog;
    }
}