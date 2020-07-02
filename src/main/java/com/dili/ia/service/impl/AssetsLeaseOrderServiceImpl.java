package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.*;
import com.dili.ia.domain.dto.*;
import com.dili.ia.domain.dto.printDto.LeaseOrderPrintDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.AssetsLeaseOrderMapper;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.rpc.UidFeignRpc;
import com.dili.ia.service.*;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.ia.util.ResultCodeConst;
import com.dili.ia.util.SpringUtil;
import com.dili.logger.sdk.component.MsgService;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleWayDetail;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 14:40:05.
 */
@Service
public class AssetsLeaseOrderServiceImpl extends BaseServiceImpl<AssetsLeaseOrder, Long> implements AssetsLeaseOrderService {
    private final static Logger LOG = LoggerFactory.getLogger(AssetsLeaseOrderServiceImpl.class);
    public AssetsLeaseOrderMapper getActualDao() {
        return (AssetsLeaseOrderMapper)getDao();
    }

    @Autowired
    private DepartmentRpc departmentRpc;
    @Autowired
    private AssetsLeaseOrderItemService assetLeaseOrderItemService;
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
    private RefundOrderService refundOrderService;
    @Autowired
    private CustomerRpc customerRpc;
    @Autowired
    private MsgService msgService;
    @Autowired
    private BusinessChargeItemService businessChargeItemService;
    @Autowired
    private RefundFeeItemService refundFeeItemService;
    @Autowired
    private TransferDeductionItemService transferDeductionItemService;

    @Autowired @Lazy
    private List<AssetsLeaseService> assetLeaseServices;
    private Map<Integer,AssetsLeaseService> assetLeaseServiceMap = new HashMap<>();
    @PostConstruct
    public void init() {
        for(AssetsLeaseService service : assetLeaseServices) {
            this.assetLeaseServiceMap.put(service.getAssetsType(), service);
        }
    }

    @Override
    @Transactional
    public BaseOutput saveLeaseOrder(AssetsLeaseOrderListDto dto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }

        //检查客户状态
        checkCustomerState(dto.getCustomerId(),userTicket.getFirmId());
        AssetsLeaseService assetLeaseService = assetLeaseServiceMap.get(dto.getAssetsType());
        dto.getLeaseOrderItems().forEach(o->{
            //检查资产状态
            assetLeaseService.checkAssetState(o.getAssetsId());
        });

        dto.setMarketId(userTicket.getFirmId());
        dto.setMarketCode(userTicket.getFirmCode());
        dto.setCreatorId(userTicket.getId());
        dto.setCreator(userTicket.getRealName());
        if(CollectionUtils.isNotEmpty(dto.getCategorys())){
            dto.setCategoryId(dto.getCategorys().stream().map(o->o.getId()).collect(Collectors.joining(",")));
            dto.setCategoryName(dto.getCategorys().stream().map(o->o.getText()).collect(Collectors.joining(",")));
        }

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
            AssetsLeaseOrder oldLeaseOrder = get(dto.getId());
            if(!LeaseOrderStateEnum.CREATED.getCode().equals(oldLeaseOrder.getState())){
                throw new BusinessException(ResultCode.DATA_ERROR, "租赁单编号【" + oldLeaseOrder.getCode() + "】 状态已变更，不可以进行修改操作");
            }
            dto.setVersion(oldLeaseOrder.getVersion());
            dto.setWaitAmount(dto.getPayAmount());
            SpringUtil.copyPropertiesIgnoreNull(dto,oldLeaseOrder);
            oldLeaseOrder.setContractNo(dto.getContractNo());
            oldLeaseOrder.setNotes(dto.getNotes());
            oldLeaseOrder.setCategoryId(dto.getCategoryId());
            oldLeaseOrder.setCategoryName(dto.getCategoryName());
            if (update(oldLeaseOrder) == 0) {
                LOG.info("摊位租赁单修改异常,乐观锁生效 【租赁单编号:{}】", dto.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }

            deleteLeaseOrderItems(dto.getId());
            insertLeaseOrderItems(dto);
        }
        return BaseOutput.success().setData(dto);
    }

    /**
     * 删除订单项
     * @param leaseOrderId
     */
    private void deleteLeaseOrderItems(Long leaseOrderId){
        AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
        condition.setLeaseOrderId(leaseOrderId);
        List<AssetsLeaseOrderItem> assetLeaseOrderItems = assetLeaseOrderItemService.listByExample(condition);
        assetLeaseOrderItemService.deleteByExample(condition);
        assetLeaseOrderItems.forEach(o->{
            BusinessChargeItem bciCondition = new BusinessChargeItem();
            bciCondition.setBusinessId(o.getId());
            bciCondition.setBizType(AssetsTypeEnum.getAssetsTypeEnum(o.getAssetsType()).getCode());
            businessChargeItemService.deleteByExample(bciCondition);
        });

    }


    /**
     * 批量插入租赁单项
     *
     * @param dto
     */
    private void insertLeaseOrderItems(AssetsLeaseOrderListDto dto) {
        dto.getLeaseOrderItems().forEach(o -> {
            o.setLeaseOrderId(dto.getId());
            o.setLeaseOrderCode(dto.getCode());
            o.setCustomerId(dto.getCustomerId());
            o.setCustomerName(dto.getCustomerName());
            o.setState(LeaseOrderStateEnum.CREATED.getCode());
            o.setPayState(PayStateEnum.NOT_PAID.getCode());
            o.setStopRentState(StopRentStateEnum.NO_APPLY.getCode());
            assetLeaseOrderItemService.insertSelective(o);

            //收费项新增
            if(CollectionUtils.isNotEmpty(o.getBusinessChargeItems())){
                o.getBusinessChargeItems().forEach(bci->{
                    bci.setBusinessId(o.getId());
                    businessChargeItemService.insertSelective(bci);
                });
            }
        });
    }

    /**
     * 合同编号验重
     * @param leaseOrderId 待修改的租赁单Id
     * @param contractNo
     * @param isAdd
     */
    private void checkContractNo(Long leaseOrderId,String contractNo,Boolean isAdd){
        if(StringUtils.isNotBlank(contractNo)){
            AssetsLeaseOrder condition = new AssetsLeaseOrder();
            condition.setContractNo(contractNo);
            List<AssetsLeaseOrder> leaseOrders = list(condition);
            if(isAdd && CollectionUtils.isNotEmpty(leaseOrders)){
                throw new BusinessException(ResultCode.DATA_ERROR,"合同编号不允许重复使用，请修改");
            }else {
                if(leaseOrders.size() == 1){
                    AssetsLeaseOrder leaseOrder = leaseOrders.get(0);
                    if(!leaseOrder.getId().equals(leaseOrderId)){
                        throw new BusinessException(ResultCode.DATA_ERROR,"合同编号不允许重复使用，请修改");
                    }
                }
            }
        }
    }


    /**
     * 检查客户状态
     * @param customerId
     * @param marketId
     */
    @Override
    public void checkCustomerState(Long customerId,Long marketId){
        BaseOutput<Customer> output = customerRpc.get(customerId,marketId);
        if(!output.isSuccess()){
            throw new BusinessException(ResultCode.DATA_ERROR,"客户接口调用异常 "+output.getMessage());
        }
        Customer customer = output.getData();
        if(null == customer){
            throw new BusinessException(ResultCode.DATA_ERROR,"客户不存在，请重新修改后保存");
        }else if(EnabledStateEnum.DISABLED.getCode().equals(customer.getState())){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户【" + customer.getName() + "】已禁用，请重新修改后保存");
        }else if(YesOrNoEnum.YES.getCode().equals(customer.getIsDelete())){
            throw new BusinessException(ResultCode.DATA_ERROR,"客户【" + customer.getName() + "】已删除，请重新修改后保存");
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

        AssetsLeaseOrder leaseOrder = get(id);
        AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
        condition.setLeaseOrderId(leaseOrder.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetLeaseOrderItemService.listByExample(condition);

        AssetsLeaseService assetLeaseService = assetLeaseServiceMap.get(leaseOrder.getAssetsType());
        /***************************检查是否可以提交付款 begin*********************/
        if(leaseOrder.getState().equals(LeaseOrderStateEnum.CREATED.getCode())){
            //检查客户状态
            checkCustomerState(leaseOrder.getCustomerId(),leaseOrder.getMarketId());
            leaseOrderItems.forEach(o->{
                //检查资产状态
                assetLeaseService.checkAssetState(o.getAssetsId());
            });
        }
        //检查是否可以进行提交付款
        checkSubmitPayment(id, amount, waitAmount, leaseOrder);
        /***************************检查是否可以提交付款 end*********************/

        //新增缴费单
        PaymentOrder paymentOrder = buildPaymentOrder(leaseOrder);
        paymentOrder.setAmount(amount);
        paymentOrderService.insertSelective(paymentOrder);

        if (leaseOrder.getState().equals(LeaseOrderStateEnum.CREATED.getCode())) {//第一次发起付款，相关业务实现
            //冻结定金和转抵
            BaseOutput customerAccountOutput = customerAccountService.submitLeaseOrderCustomerAmountFrozen(
                    leaseOrder.getId(), leaseOrder.getCode(), leaseOrder.getCustomerId(),
                    leaseOrder.getEarnestDeduction(), leaseOrder.getTransferDeduction(),
                    leaseOrder.getMarketId(),userTicket.getId(),userTicket.getRealName());
            if(!customerAccountOutput.isSuccess()){
                LOG.info("冻结定金和转抵异常【编号：{}】", leaseOrder.getCode());
                if(ResultCodeConst.EARNEST_ERROR.equals(customerAccountOutput.getCode())){
                    throw new BusinessException(ResultCode.DATA_ERROR,"客户定金可用金额不足，请核实修改后重新保存");
                }else if(ResultCodeConst.TRANSFER_ERROR.equals(customerAccountOutput.getCode())){
                    throw new BusinessException(ResultCode.DATA_ERROR,"客户转抵可用金额不足，请核实修改后重新保存");
                }else{
                    throw new BusinessException(ResultCode.DATA_ERROR,customerAccountOutput.getMessage());
                }
            }
            //冻结摊位
            assetLeaseService.frozenAsset(leaseOrder, leaseOrderItems);
            leaseOrder.setState(LeaseOrderStateEnum.SUBMITTED.getCode());
            leaseOrder.setPaymentId(paymentOrder.getId());
            //更新摊位租赁单状态
            cascadeUpdateLeaseOrderState(leaseOrder, true, LeaseOrderItemStateEnum.SUBMITTED);
        } else {//非第一次付款，相关业务实现
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
     * 检查是否可以进行提交付款
     * @param id
     * @param amount
     * @param waitAmount
     * @param leaseOrder
     */
    private void checkSubmitPayment(Long id, Long amount, Long waitAmount, AssetsLeaseOrder leaseOrder) {
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
     * 构建缴费单数据
     *
     * @param leaseOrder
     * @return
     */
    private PaymentOrder buildPaymentOrder(AssetsLeaseOrder leaseOrder) {
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
        paymentOrder.setIsSettle(YesOrNoEnum.NO.getCode());
        paymentOrder.setBizType(BizTypeEnum.BOOTH_LEASE.getCode());
        return paymentOrder;
    }

    /**
     * 构造结算单数据
     *
     * @param leaseOrder
     * @return
     */
    private SettleOrderDto buildSettleOrderDto(AssetsLeaseOrder leaseOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new BusinessException(ResultCode.DATA_ERROR,"未登录");
        }
        SettleOrderDto settleOrder = new SettleOrderDto();
        settleOrder.setAppId(settlementAppId);
        settleOrder.setBusinessCode(leaseOrder.getCode());
        settleOrder.setBusinessDepId(leaseOrder.getDepartmentId());
        settleOrder.setBusinessDepName(leaseOrder.getDepartmentName());
        settleOrder.setBusinessType(Integer.valueOf(BizTypeEnum.BOOTH_LEASE.getCode()));
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
     * 级联更新摊位租赁订单状态 订单项状态级联发生变化
     *
     * @param leaseOrder
     * @param isCascade  false不级联更新订单项 true 级联更新订单项
     * @param stateEnum  isCascade为false时，此处可以传null
     */
    @Transactional
    public void cascadeUpdateLeaseOrderState(AssetsLeaseOrder leaseOrder, boolean isCascade, LeaseOrderItemStateEnum stateEnum) {
        if (updateSelective(leaseOrder) == 0) {
            LOG.info("摊位租赁单提交状态更新失败 乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试");
        }

        if (isCascade) {
            AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
            condition.setLeaseOrderId(leaseOrder.getId());
            List<AssetsLeaseOrderItem> leaseOrderItems = assetLeaseOrderItemService.listByExample(condition);
            leaseOrderItems.stream().forEach(o -> o.setState(stateEnum.getCode()));
            if (assetLeaseOrderItemService.batchUpdateSelective(leaseOrderItems) != leaseOrderItems.size()) {
                LOG.info("级联更新摊位租赁订单状态失败,乐观锁生效 【租赁单编号:{},变更目标状态:{}】", leaseOrder.getCode(), stateEnum.getName());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }
        }
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
        AssetsLeaseOrder leaseOrder = get(id);
        if (!LeaseOrderStateEnum.CREATED.getCode().equals(leaseOrder.getState())) {
            String stateName = LeaseOrderStateEnum.getLeaseOrderStateEnum(leaseOrder.getState()).getName();
            LOG.info("租赁单【编号：{}】状态为【{}】，不可以进行取消操作", leaseOrder.getCode(), stateName);
            throw new BusinessException(ResultCode.DATA_ERROR,"租赁单状态为【" + stateName + "】，不可以进行取消操作");
        }
        leaseOrder.setState(LeaseOrderStateEnum.CANCELD.getCode());
        leaseOrder.setCancelerId(userTicket.getId());
        leaseOrder.setCanceler(userTicket.getRealName());

        String formatNow = DateUtils.format(new Date(),"yyyyMMddHHmmssSSS");
        if(StringUtils.isNotBlank(leaseOrder.getContractNo())){
            leaseOrder.setContractNo(leaseOrder.getContractNo() + "_" + formatNow);
        }

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
        AssetsLeaseOrder leaseOrder = get(id);
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

        //Todo 撤回保证金单

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
        AssetsLeaseService assetLeaseService = assetLeaseServiceMap.get(leaseOrder.getAssetsType());
        assetLeaseService.unFrozenAllAsset(leaseOrder.getId());
        //日志上下文构建
        LoggerUtil.buildLoggerContext(leaseOrder.getId(),leaseOrder.getCode(),userTicket.getId(),userTicket.getRealName(),userTicket.getFirmId(),null);
        return BaseOutput.success();
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
        AssetsLeaseOrder leaseOrder = get(paymentOrderPO.getBusinessId());
        if (PaymentOrderStateEnum.PAID.getCode().equals(paymentOrderPO.getState())) {
            return BaseOutput.success().setData(true);
        }

        paymentOrderPO.setState(PaymentOrderStateEnum.PAID.getCode());
        paymentOrderPO.setSettlementWay(settleOrder.getWay());
        paymentOrderPO.setSettlementOperator(settleOrder.getOperatorName());
        paymentOrderPO.setPayedTime(DateUtils.localDateTimeToUdate(settleOrder.getOperateTime()));
        if(leaseOrder.getWaitAmount().equals(paymentOrderPO.getAmount())){
            paymentOrderPO.setIsSettle(YesOrNoEnum.YES.getCode());
        }
        if (paymentOrderService.updateSelective(paymentOrderPO) == 0) {
            LOG.info("结算成功，缴费单同步数据异常,乐观锁生效 【缴费单编号:{}】", settleOrder.getBusinessCode());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
        /*****************************更新缴费单相关字段 end*************************/


        //第一次消费，需要抵扣保证金、定金、转抵金
        if (LeaseOrderStateEnum.SUBMITTED.getCode().equals(leaseOrder.getState())) {
            //消费定金、转抵
            BaseOutput customerAccountOutput = customerAccountService.paySuccessLeaseOrderCustomerAmountConsume(
                    leaseOrder.getId(), leaseOrder.getCode(),
                    leaseOrder.getCustomerId(), leaseOrder.getEarnestDeduction(),
                    leaseOrder.getTransferDeduction(),
                    leaseOrder.getMarketId(),settleOrder.getOperatorId(),settleOrder.getOperatorName());
            if(!customerAccountOutput.isSuccess()){
                LOG.info("结算成功，消费定金、转抵接口异常 【租赁单编号:{},定金:{},转抵:{}】", leaseOrder.getCode(),leaseOrder.getEarnestDeduction(),leaseOrder.getTransferDeduction());
                throw new BusinessException(ResultCode.DATA_ERROR,customerAccountOutput.getMessage());
            }
            //解冻出租摊位
            AssetsLeaseService assetLeaseService = assetLeaseServiceMap.get(leaseOrder.getAssetsType());
            assetLeaseService.leaseAsset(leaseOrder);
        }

        /***************************更新租赁单及其订单项相关字段 begin*********************/
        //根据租赁时间和当前时间比对，单子是未生效、已生效、还是已过期
        LocalDateTime now = LocalDateTime.now();
        if (!now.isBefore(leaseOrder.getStartTime()) && !now.isAfter(leaseOrder.getEndTime())) {
            leaseOrder.setState(LeaseOrderStateEnum.EFFECTIVE.getCode());
        } else if (now.isBefore(leaseOrder.getStartTime())) {
            leaseOrder.setState(LeaseOrderStateEnum.NOT_ACTIVE.getCode());
        } else if (now.isAfter(leaseOrder.getEndTime())) {
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
        AssetsLeaseOrderItem leaseOrderItemCondition = DTOUtils.newInstance(AssetsLeaseOrderItem.class);
        leaseOrderItemCondition.setLeaseOrderId(leaseOrder.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetLeaseOrderItemService.listByExample(leaseOrderItemCondition);
        leaseOrderItems.stream().forEach(o -> {
            o.setState(leaseOrder.getState());
            o.setPayState(leaseOrder.getPayState());
        });
        if (assetLeaseOrderItemService.batchUpdateSelective(leaseOrderItems) != leaseOrderItems.size()) {
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
        /***************************更新租赁单及其订单项相关字段 end*********************/

        msgService.sendBusinessLog(recordPayLog(settleOrder, leaseOrder));
        return BaseOutput.success().setData(true);
    }

    /**
     * 记录交费日志
     * @param settleOrder
     * @param leaseOrder
     */
    public BusinessLog recordPayLog(SettleOrder settleOrder, AssetsLeaseOrder leaseOrder) {
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
     * 租赁单生效处理
     *
     * @param o
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void leaseOrderEffectiveHandler(AssetsLeaseOrder o) {
        o.setState(LeaseOrderStateEnum.EFFECTIVE.getCode());
        if (updateSelective(o) == 0) {
            LOG.info("租赁单生效处理异常,乐观锁生效 【租赁单ID {}】", o.getId());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试");
        }

        AssetsLeaseOrderItem itemCondition = new AssetsLeaseOrderItem();
        itemCondition.setLeaseOrderId(o.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetLeaseOrderItemService.listByExample(itemCondition);

        List<AssetsLeaseOrderItem> waitItems = new ArrayList<>();
        leaseOrderItems.stream().forEach(leaseOrderItem -> {
            //只更新待生效的订单项
            if (LeaseOrderItemStateEnum.NOT_ACTIVE.getCode().equals(leaseOrderItem.getState())) {
                leaseOrderItem.setState(LeaseOrderItemStateEnum.EFFECTIVE.getCode());
                waitItems.add(leaseOrderItem);
            }
        });
        if (assetLeaseOrderItemService.batchUpdateSelective(waitItems) != waitItems.size()) {
            LOG.info("租赁单生效处理异常,级联批量处理摊位订单项乐观锁生效【订单ID:{}", o.getId());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
    }

    /**
     * 租赁单到期处理
     *
     * @param o
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void leaseOrderExpiredHandler(AssetsLeaseOrder o) {
        o.setState(LeaseOrderStateEnum.EXPIRED.getCode());
        if (updateSelective(o) == 0) {
            LOG.info("租赁单到期处理异常 乐观锁生效 【租赁单ID {}】", o.getId());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试");
        }

        AssetsLeaseOrderItem itemCondition = new AssetsLeaseOrderItem();
        itemCondition.setLeaseOrderId(o.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetLeaseOrderItemService.listByExample(itemCondition);

        List<AssetsLeaseOrderItem> waitItems = new ArrayList<>();
        leaseOrderItems.stream().forEach(leaseOrderItem -> {
            //只更新待到期的订单项
            if (LeaseOrderItemStateEnum.EFFECTIVE.getCode().equals(leaseOrderItem.getState())) {
                leaseOrderItem.setState(LeaseOrderItemStateEnum.EXPIRED.getCode());
                waitItems.add(leaseOrderItem);
            }
        });
        if (assetLeaseOrderItemService.batchUpdateSelective(waitItems) != waitItems.size()) {
            LOG.info("租赁单到期处理异常,级联批量处理摊位订单项乐观锁生效【订单ID:{}", o.getId());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }
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

        AssetsLeaseOrder leaseOrder = get(paymentOrder.getBusinessId());
        PrintDataDto<LeaseOrderPrintDto> printDataDto = new PrintDataDto<LeaseOrderPrintDto>();
        LeaseOrderPrintDto leaseOrderPrintDto = new LeaseOrderPrintDto();
        leaseOrderPrintDto.setPrintTime(LocalDate.now());
        leaseOrderPrintDto.setReprint(reprint == 2 ? "(补打)" : "");
        leaseOrderPrintDto.setLeaseOrderCode(leaseOrder.getCode());
        if (YesOrNoEnum.YES.getCode().equals(paymentOrder.getIsSettle())) {
            leaseOrderPrintDto.setBusinessType(BizTypeEnum.BOOTH_LEASE.getName());
            printDataDto.setName(PrintTemplateEnum.BOOTH_LEASE_PAID.getCode());
        } else {
            leaseOrderPrintDto.setBusinessType(BizTypeEnum.EARNEST.getName());
            printDataDto.setName(PrintTemplateEnum.BOOTH_LEASE_NOT_PAID.getCode());
        }
        leaseOrderPrintDto.setCustomerName(leaseOrder.getCustomerName());
        leaseOrderPrintDto.setCustomerCellphone(leaseOrder.getCustomerCellphone());
        leaseOrderPrintDto.setStartTime(leaseOrder.getStartTime().toLocalDate());
        leaseOrderPrintDto.setEndTime(leaseOrder.getEndTime().toLocalDate());
        leaseOrderPrintDto.setIsRenew(YesOrNoEnum.getYesOrNoEnum(leaseOrder.getIsRenew()).getName());
        leaseOrderPrintDto.setCategoryName(leaseOrder.getCategoryName());
        leaseOrderPrintDto.setNotes(leaseOrder.getNotes());
        leaseOrderPrintDto.setTotalAmount(MoneyUtils.centToYuan(leaseOrder.getTotalAmount()));

        Long totalPayAmountExcludeLast = 0L;
        //已结清时  定金需要加前几次支付金额
        if(YesOrNoEnum.YES.getCode().equals(paymentOrder.getIsSettle())){
            PaymentOrder paymentOrderConditions = DTOUtils.newInstance(PaymentOrder.class);
            paymentOrderConditions.setBusinessId(paymentOrder.getBusinessId());
            paymentOrderConditions.setBizType(BizTypeEnum.BOOTH_LEASE.getCode());
            List<PaymentOrder> paymentOrders = paymentOrderService.list(paymentOrderConditions);

            for (PaymentOrder order : paymentOrders) {
                if (!order.getCode().equals(orderCode) && order.getState().equals(PaymentOrderStateEnum.PAID.getCode())) {
                    totalPayAmountExcludeLast += order.getAmount();
                }
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

        //组合支付需要显示结算详情
        StringBuffer settleWayDetails = new StringBuffer();
        settleWayDetails.append("【");
        if (paymentOrder.getSettlementWay().equals(SettleWayEnum.MIXED_PAY.getCode())){
            BaseOutput<List<SettleWayDetail>> output = settlementRpc.listSettleWayDetailsByCode(paymentOrder.getSettlementCode());
            if (output.isSuccess() && CollectionUtils.isNotEmpty(output.getData())){
                output.getData().forEach(o -> {
                    //此循环字符串拼接顺序不可修改，样式 微信  150.00，4237458467568870，备注：微信付款150元
                    settleWayDetails.append(SettleWayEnum.getNameByCode(o.getWay())).append("  ").append(MoneyUtils.centToYuan(o.getAmount()));
                    if (StringUtils.isNotEmpty(o.getSerialNumber())){
                        settleWayDetails.append(",").append(o.getSerialNumber());
                    }
                    if (StringUtils.isNotEmpty(o.getNotes())){
                        settleWayDetails.append(",").append("备注：").append(o.getNotes());
                    }
                    settleWayDetails.append("\r\n");
                });
            }else {
                LOGGER.info("查询结算微服务组合支付，支付详情失败；原因：{}",output.getMessage());
            }
        }else{
            BaseOutput<SettleOrder> output = settlementRpc.getByCode(paymentOrder.getSettlementCode());
            if(output.isSuccess()){
                SettleOrder settleOrder = output.getData();
                if(StringUtils.isNotBlank(settleOrder.getSerialNumber())){
                    settleWayDetails.append(settleOrder.getSerialNumber());
                    if (StringUtils.isNotBlank(settleOrder.getNotes())){
                        settleWayDetails.append(",").append(settleOrder.getNotes());
                    }
                }else {
                    if (StringUtils.isNotBlank(settleOrder.getNotes())){
                        settleWayDetails.append(settleOrder.getNotes());
                    }
                }
            }else {
                LOGGER.info("查询结算微服务非组合支付，支付详情失败；原因：{}",output.getMessage());
            }
        }
        settleWayDetails.append("】");
        leaseOrderPrintDto.setSettleWayDetails(settleWayDetails.toString());

        List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.queryBusinessChargeItemConfig(leaseOrder.getMarketId(), AssetsTypeEnum.getAssetsTypeEnum(leaseOrder.getAssetsType()).getBizType(), null);
        leaseOrderPrintDto.setChargeItems(chargeItemDtos);

        AssetsLeaseOrderItem leaseOrderItemCondition = new AssetsLeaseOrderItem();
        leaseOrderItemCondition.setLeaseOrderId(leaseOrder.getId());
        List<AssetsLeaseOrderItemListDto> leaseOrderItems = assetLeaseOrderItemService.leaseOrderItemListToDto(assetLeaseOrderItemService.list(leaseOrderItemCondition), AssetsTypeEnum.getAssetsTypeEnum(leaseOrder.getAssetsType()).getBizType(), chargeItemDtos);
        List<LeaseOrderItemPrintDto> leaseOrderItemPrintDtos = new ArrayList<>();
        leaseOrderItems.forEach(o -> {
            leaseOrderItemPrintDtos.add(LeaseOrderRefundOrderServiceImpl.leaseOrderItem2PrintDto(o));
        });
        leaseOrderPrintDto.setLeaseOrderItems(leaseOrderItemPrintDtos);
        printDataDto.setItem(leaseOrderPrintDto);
        return BaseOutput.success().setData(printDataDto);
    }

    @Override
    @Transactional
    @GlobalTransactional
    public BaseOutput createRefundOrder(RefundOrderDto refundOrderDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        //订单项退款申请
        AssetsLeaseOrderItem leaseOrderItem = assetLeaseOrderItemService.get(refundOrderDto.getBusinessItemId());
        //摊位订单项退款申请条件检查
        checkRufundApplyWithLeaseOrderItem(refundOrderDto, leaseOrderItem,userTicket);

        if(null == leaseOrderItem.getStopTime()){
            leaseOrderItem.setStopTime(refundOrderDto.getStopTime());
        }
        leaseOrderItem.setRefundState(RefundStateEnum.REFUNDING.getCode());
        if(assetLeaseOrderItemService.updateSelective(leaseOrderItem) == 0){
            LOG.info("摊位租赁订单项退款申请异常 更新乐观锁生效 【租赁单项ID {}】", leaseOrderItem.getId());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
        }

        refundOrderDto.setBizType(AssetsTypeEnum.getAssetsTypeEnum(leaseOrderItem.getAssetsType()).getBizType());
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

        //退款费用项设置
        refundOrderDto.getRefundFeeItems().forEach(o->{
            o.setRefundOrderId(refundOrderDto.getId());
            o.setRefundOrderCode(refundOrderDto.getCode());
            refundFeeItemService.insertSelective(o);
        });

        if(CollectionUtils.isNotEmpty(refundOrderDto.getTransferDeductionItems())){
            refundOrderDto.getTransferDeductionItems().forEach(o->{
                o.setRefundOrderId(refundOrderDto.getId());
                transferDeductionItemService.insertSelective(o);
            });
        }
        return BaseOutput.success();
    }

    /**
     * 摊位订单项退款申请条件检查
     * @param refundOrderDto
     * @param leaseOrderItem
     */
    private void checkRufundApplyWithLeaseOrderItem(RefundOrderDto refundOrderDto, AssetsLeaseOrderItem leaseOrderItem, UserTicket userTicket) {
        //收款人和转抵扣收款人客户状态验证
        checkCustomerState(refundOrderDto.getPayeeId(), userTicket.getFirmId());
        List<TransferDeductionItem> transferDeductionItems = refundOrderDto.getTransferDeductionItems();
        if(CollectionUtils.isNotEmpty(transferDeductionItems)){
            for (TransferDeductionItem transferDeductionItem : transferDeductionItems
            ) {
                checkCustomerState(transferDeductionItem.getPayeeId(), userTicket.getFirmId());
            }
        }

        if(!RefundStateEnum.WAIT_APPLY.getCode().equals(leaseOrderItem.getRefundState())){
            throw new BusinessException(ResultCode.DATA_ERROR,"摊位项状态已发生变更，不能发起退款申请");
        }

        BusinessChargeItem chargeItemCondition = new BusinessChargeItem();
        chargeItemCondition.setBusinessId(leaseOrderItem.getId());
        Map<Long,Long> businessChargeItemMap = businessChargeItemService.list(chargeItemCondition).stream().collect(Collectors.toMap(BusinessChargeItem::getChargeItemId,BusinessChargeItem::getWaitAmount));
        Map<Long,Long> refundFeeItemMap = refundOrderDto.getRefundFeeItems().stream().collect(Collectors.toMap(RefundFeeItem::getChargeItemId,RefundFeeItem::getAmount));
        for(Long chargeItemId : refundFeeItemMap.keySet()){
            if(refundFeeItemMap.get(chargeItemId) > businessChargeItemMap.get(chargeItemId)){
                throw new BusinessException(ResultCode.DATA_ERROR,"存在收费项退款额大于可退款额");
            }
        }
    }

    @Override
    @Transactional
    public BaseOutput cancelRefundOrderHandler(Long leaseOrderId,Long leaseOrderItemId) {
        if(null == leaseOrderItemId){
            AssetsLeaseOrder leaseOrder = get(leaseOrderId);
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
            AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
            condition.setLeaseOrderId(leaseOrder.getId());
            List<AssetsLeaseOrderItem> leaseOrderItems = assetLeaseOrderItemService.listByExample(condition);
            leaseOrderItems.stream().forEach(o -> o.setRefundState(RefundStateEnum.WAIT_APPLY.getCode()));
            if (assetLeaseOrderItemService.batchUpdateSelective(leaseOrderItems) != leaseOrderItems.size()) {
                LOG.info("摊位租赁单取消退款申请异常 级联批量更新租赁单订单项乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }
        }else{
            //订单项退款申请
            AssetsLeaseOrderItem leaseOrderItem = assetLeaseOrderItemService.get(leaseOrderItemId);
            if(!RefundStateEnum.REFUNDING.getCode().equals(leaseOrderItem.getRefundState())){
                LOG.info("租赁单【编号：{}】退款状态已发生变更，不能取消退款",leaseOrderItem.getLeaseOrderCode());
                throw new BusinessException(ResultCode.DATA_ERROR,"退款状态已发生变更，不能取消退款");
            }

            leaseOrderItem.setRefundState(RefundStateEnum.WAIT_APPLY.getCode());
            if(assetLeaseOrderItemService.updateSelective(leaseOrderItem) == 0){
                LOG.info("摊位租赁单订单项取消退款申请异常 更新租赁单订单项乐观锁生效 【摊位租赁订单项ID {}】", leaseOrderItem.getId());
                throw new BusinessException(ResultCode.DATA_ERROR,"多人操作，请重试！");
            }
        }
        return BaseOutput.success();
    }

    @Override
    @GlobalTransactional
    @Transactional
    public BaseOutput settleSuccessRefundOrderHandler(RefundOrder refundOrder) {
        AssetsLeaseOrder leaseOrder = get(refundOrder.getBusinessId());
        //订单项退款申请
        AssetsLeaseOrderItem leaseOrderItem = assetLeaseOrderItemService.get(refundOrder.getBusinessItemId());
        if (RefundStateEnum.REFUNDED.getCode().equals(leaseOrderItem.getRefundState())) {
            LOG.info("此单已退款【leaseOrderItemId={}】", refundOrder.getBusinessItemId());
            return BaseOutput.success();
        }
        leaseOrderItem.setRefundState(RefundStateEnum.REFUNDED.getCode());
        leaseOrderItem.setState(LeaseOrderItemStateEnum.REFUNDED.getCode());
        if (assetLeaseOrderItemService.updateSelective(leaseOrderItem) == 0) {
            LOG.info("摊位租赁单订单项退款申请结算退款成功 更新租赁单订单项乐观锁生效 【租赁单订单项ID {}】", leaseOrderItem.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        //停止租赁 释放时间段
        assetLeaseOrderItemService.stopBoothRent(leaseOrderItem, leaseOrder.getStartTime(), leaseOrderItem.getStopTime());
        //级联检查其他订单项状态，如果全部为已退款，则需联动更新订单状态为已退款
        AssetsLeaseOrderItem condition = new AssetsLeaseOrderItem();
        condition.setLeaseOrderId(leaseOrder.getId());
        List<AssetsLeaseOrderItem> leaseOrderItems = assetLeaseOrderItemService.listByExample(condition);
        boolean isUpdateLeaseOrderState = true;
        for (AssetsLeaseOrderItem orderItem : leaseOrderItems) {
            if (orderItem.getId().equals(refundOrder.getBusinessItemId())) {
                continue;
            } else if (!LeaseOrderItemStateEnum.REFUNDED.getCode().equals(orderItem.getState())) {
                isUpdateLeaseOrderState = false;
                break;
            }
        }
        if (isUpdateLeaseOrderState) {
            leaseOrder.setState(LeaseOrderStateEnum.REFUNDED.getCode());
            leaseOrder.setRefundState(RefundStateEnum.REFUNDED.getCode());
            if (updateSelective(leaseOrder) == 0) {
                LOG.info("摊位租赁单订单项退款申请结算退款成功 级联更新租赁单乐观锁生效 【租赁单ID {}】", leaseOrder.getId());
                throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
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
                    LOG.info("退款单转抵异常，【退款编号:{},收款人:{},收款金额:{},msg:{}】",refundOrder.getCode(),o.getPayee(),o.getPayeeAmount(),accountOutput.getMessage());
                    throw new BusinessException(ResultCode.DATA_ERROR,accountOutput.getMessage());
                }
            });
        }
        //记录退款日志
        msgService.sendBusinessLog(recordRefundLog(refundOrder, leaseOrder));
        return BaseOutput.success();
    }

    /**
     * 记录退款日志
     * @param refundOrder
     * @param leaseOrder
     */
    public BusinessLog recordRefundLog(RefundOrder refundOrder, AssetsLeaseOrder leaseOrder) {
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

    @Override
    public BaseOutput supplement(AssetsLeaseOrder leaseOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        checkContractNo(leaseOrder.getId(),leaseOrder.getContractNo(),false);
        AssetsLeaseOrder oldLeaseOrder = get(leaseOrder.getId());
        leaseOrder.setVersion(oldLeaseOrder.getVersion());
        if (updateSelective(leaseOrder) == 0) {
            return BaseOutput.failure("多人操作，请稍后重试");
        }
        LoggerUtil.buildLoggerContext(oldLeaseOrder.getId(),oldLeaseOrder.getCode(),userTicket.getId(),userTicket.getRealName(),userTicket.getFirmId(),null);
        return BaseOutput.success();
    }
}