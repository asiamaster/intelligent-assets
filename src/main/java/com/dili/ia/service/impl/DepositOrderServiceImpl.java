package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BoothDTO;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.Customer;
import com.dili.ia.domain.DepositOrder;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.DepositOrderQuery;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.printDto.DepositOrderPrintDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.DepositOrderMapper;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.rpc.UidFeignRpc;
import com.dili.ia.service.DepositOrderService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.util.BeanMapUtil;
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
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-22 17:54:56.
 */
@Service
public class DepositOrderServiceImpl extends BaseServiceImpl<DepositOrder, Long> implements DepositOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(DepositOrderServiceImpl.class);
    @Autowired
    DepartmentRpc departmentRpc;
    @Autowired
    CustomerRpc customerRpc;
    @Autowired
    AssetsRpc assetsRpc;
    @Autowired
    SettlementRpc settlementRpc;
    @Autowired
    PaymentOrderService paymentOrderService;
    @Autowired
    UidFeignRpc uidFeignRpc;
    @Autowired
    RefundOrderService refundOrderService;

    public DepositOrderMapper getActualDao() {
        return (DepositOrderMapper)getDao();
    }

    @Value("${settlement.app-id}")
    private Long settlementAppId;
    @Value("${depositOrder.settlement.handler.url}")
    private String settlerHandlerUrl;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput<DepositOrder> addDepositOrder(DepositOrderQuery depositOrderQuery) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(null == userTicket){
            return BaseOutput.failure("未登录");
        }
        //检查客户状态
        checkCustomerState(depositOrderQuery.getCustomerId(),userTicket.getFirmId());
        if(depositOrderQuery.getAssetsId() != null){
//            if (depositOrderListDto.getAssetsType().equals(AssetsTypeEnum.BOOTH.getTypeCode())){
                //检查摊位状态
                checkBoothState(depositOrderQuery.getAssetsId());
//            }
            // @TODO 检查冷库，公寓状态
        }
        BaseOutput<Department> depOut = departmentRpc.get(depositOrderQuery.getDepartmentId());
        if(!depOut.isSuccess()){
            LOGGER.info("获取部门失败！" + depOut.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, "获取部门失败！");
        }

        depositOrderQuery.setCode(userTicket.getFirmCode().toUpperCase() + this.getBizNumber(userTicket.getFirmCode() + "_" + BizNumberTypeEnum.DEPOSIT_ORDER.getCode()));
        depositOrderQuery.setCreatorId(userTicket.getId());
        depositOrderQuery.setCreator(userTicket.getRealName());
        depositOrderQuery.setMarketId(userTicket.getFirmId());
        depositOrderQuery.setDepartmentName(depOut.getData().getName());
        depositOrderQuery.setState(DepositOrderStateEnum.CREATED.getCode());
        depositOrderQuery.setVersion(0L);
        this.insertSelective(depositOrderQuery);
        return BaseOutput.success().setData(depositOrderQuery);
    }

    private String getBizNumber(String type){
        BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(type);
        if(!bizNumberOutput.isSuccess()){
            LOGGER.info("编号生成失败!" + bizNumberOutput.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, "编号生成失败!");
        }
        if (bizNumberOutput.getData() == null){
            LOGGER.info("未获取到有效编号！检查是否配置编号类型type{}" + bizNumberOutput.getMessage(), type);
            throw new BusinessException(ResultCode.DATA_ERROR, "未获取到有效编号！"+ bizNumberOutput.getMessage());
        }

        return bizNumberOutput.getData();
    }

    /**
     * 检查摊位状态
     * @param boothId
     */
    private void checkBoothState(Long boothId){
        BaseOutput<BoothDTO> output = assetsRpc.getBoothById(boothId);
        if(!output.isSuccess()){
            throw new BusinessException(ResultCode.DATA_ERROR, "摊位接口调用异常 "+output.getMessage());
        }
        BoothDTO booth = output.getData();
        if(null == booth){
            throw new BusinessException(ResultCode.DATA_ERROR, "摊位不存在，请核实和修改后再保存");
        }else if(EnabledStateEnum.DISABLED.getCode().equals(booth.getState())){
            throw new BusinessException(ResultCode.DATA_ERROR, "摊位已禁用，请核实和修改后再保存");
        }else if(YesOrNoEnum.YES.getCode().equals(booth.getIsDelete())){
            throw new BusinessException(ResultCode.DATA_ERROR, "摊位已删除，请核实和修改后再保存");
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
            throw new BusinessException(ResultCode.DATA_ERROR, "客户接口调用异常 "+output.getMessage());
        }
        Customer customer = output.getData();
        if(null == customer){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户不存在，请核实和修改后再保存");
        }else if(EnabledStateEnum.DISABLED.getCode().equals(customer.getState())){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户已禁用，请核实和修改后再保存");
        }else if(YesOrNoEnum.YES.getCode().equals(customer.getIsDelete())){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户已删除，请核实和修改后再保存");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput<DepositOrder> updateDepositOrder(DepositOrder depositOrder) {
        if (depositOrder.getId() == null){
            return BaseOutput.failure("Id不能为空！");
        }

        //修改有清空修改，所以使用update
        if (this.update(this.buildUpdateDto(depositOrder)) == 0){
            LOG.info("修改保证金单失败,乐观锁生效【客户名称：{}】 【保证金单ID:{}】", depositOrder.getCustomerName(), depositOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        return BaseOutput.success("修改成功！").setData(depositOrder);
    }

    private DepositOrder buildUpdateDto(DepositOrder dto){
        DepositOrder depositOrder = this.get(dto.getId());
        BaseOutput<Department> depOut = departmentRpc.get(dto.getDepartmentId());
        if(!depOut.isSuccess()){
            LOGGER.info("获取部门失败！" + depOut.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, "获取部门失败！");
        }
        depositOrder.setDepartmentName(depOut.getData().getName());
        depositOrder.setTypeCode(dto.getTypeCode());
        depositOrder.setTypeName(dto.getTypeName());
        depositOrder.setAssetsId(dto.getAssetsId());
        depositOrder.setAssetsName(dto.getAssetsName());
        depositOrder.setAssetsType(dto.getAssetsType());
        depositOrder.setCustomerId(dto.getCustomerId());
        depositOrder.setCustomerName(dto.getCustomerName());
        depositOrder.setCertificateNumber(dto.getCertificateNumber());
        depositOrder.setCustomerCellphone(dto.getCustomerCellphone());
        depositOrder.setDepartmentId(dto.getDepartmentId());
        depositOrder.setAmount(dto.getAmount());
        depositOrder.setNotes(dto.getNotes());
        depositOrder.setModifyTime(new Date());
        depositOrder.setVersion(dto.getVersion());

        return depositOrder;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput<DepositOrder> submitDepositOrder(Long id, Long amount, Long waitAmount) {
        DepositOrder de = this.get(id);
        if (null == de){
            LOG.info("提交失败，没有查询到保证金！id={}", id);
            return BaseOutput.failure("提交失败，没有查询到保证金单！");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(null == userTicket){
            return BaseOutput.failure("未登录");
        }
        //检查客户状态
        checkCustomerState(de.getCustomerId(),de.getMarketId());
//        if (CollectionUtils.isNotEmpty(detailList)){
//            detailList.forEach(o->{
//                //检查摊位状态
//                checkBoothState(o.getAssetsId());
//            });
//        }
        if (!de.getState().equals(DepositOrderStateEnum.CREATED.getCode())){
            return BaseOutput.failure("提交失败，状态已变更！");
        }
        de.setState(DepositOrderStateEnum.SUBMITTED.getCode());
        de.setSubmitterId(userTicket.getId());
        de.setSubmitter(userTicket.getRealName());
        de.setSubmitTime(new Date());
        if (this.updateSelective(de) == 0) {
            LOG.info("提交保证金【修改保证金单状态】失败 ,乐观锁生效！【保证金单ID:{}】", de.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        PaymentOrder pb = this.buildPaymentOrder(userTicket, de);
        paymentOrderService.insertSelective(pb);

        //提交到结算中心 --- 执行顺序不可调整！！因为异常只能回滚自己系统，无法回滚其它远程系统
        BaseOutput<SettleOrder> out= settlementRpc.submit(buildSettleOrderDto(userTicket, de, pb));
        if (!out.isSuccess()){
            LOGGER.info("提交到结算中心失败！" + out.getMessage() + out.getErrorData());
            throw new BusinessException(ResultCode.DATA_ERROR, out.getMessage());
        }
        return BaseOutput.success().setData(de);
    }

    //组装缴费单 PaymentOrder
    private PaymentOrder buildPaymentOrder(UserTicket userTicket, DepositOrder depositOrder){
        PaymentOrder pb = DTOUtils.newDTO(PaymentOrder.class);
        pb.setCode(userTicket.getFirmCode().toUpperCase() + this.getBizNumber(BizNumberTypeEnum.PAYMENT_ORDER.getCode()));
        pb.setAmount(depositOrder.getAmount());
        pb.setBusinessId(depositOrder.getId());
        pb.setBusinessCode(depositOrder.getCode());
        pb.setCreatorId(userTicket.getId());
        pb.setCreator(userTicket.getRealName());
        pb.setMarketId(userTicket.getFirmId());
        pb.setMarketCode(userTicket.getFirmCode());
        pb.setBizType(BizTypeEnum.DEPOSIT_ORDER.getCode());
        pb.setState(PayStateEnum.NOT_PAID.getCode());
        pb.setVersion(0);
        return pb;
    }
    //组装 -- 结算中心缴费单 SettleOrder
    private SettleOrderDto buildSettleOrderDto(UserTicket userTicket,DepositOrder depositOrder, PaymentOrder paymentOrder){
        SettleOrderDto settleOrder = new SettleOrderDto();
        //以下是提交到结算中心的必填字段
        settleOrder.setMarketId(depositOrder.getMarketId()); //市场ID
        settleOrder.setMarketCode(userTicket.getFirmCode());
        settleOrder.setOrderCode(paymentOrder.getCode());//订单号 唯一
        settleOrder.setBusinessCode(paymentOrder.getBusinessCode()); //缴费单业务单号
        settleOrder.setCustomerId(depositOrder.getCustomerId());//客户ID
        settleOrder.setCustomerName(depositOrder.getCustomerName());// "客户姓名
        settleOrder.setCustomerPhone(depositOrder.getCustomerCellphone());//"客户手机号
        settleOrder.setAmount(depositOrder.getAmount()); //金额
        settleOrder.setBusinessDepId(depositOrder.getDepartmentId()); //"业务部门ID
        settleOrder.setBusinessDepName(departmentRpc.get(depositOrder.getDepartmentId()).getData().getName());//"业务部门名称
        settleOrder.setSubmitterId(userTicket.getId());// "提交人ID
        settleOrder.setSubmitterName(userTicket.getRealName());// "提交人姓名
        if (userTicket.getDepartmentId() != null){
            settleOrder.setSubmitterDepId(userTicket.getDepartmentId()); //"提交人部门ID
            settleOrder.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        }
        settleOrder.setSubmitTime(LocalDateTime.now());
        settleOrder.setAppId(settlementAppId);//应用ID
        settleOrder.setBusinessType(BizTypeEnum.DEPOSIT_ORDER.getCode()); // 业务类型
        settleOrder.setType(SettleTypeEnum.PAY.getCode());// "结算类型  -- 付款
        settleOrder.setState(SettleStateEnum.WAIT_DEAL.getCode());
        settleOrder.setReturnUrl(settlerHandlerUrl); // 结算-- 缴费成功后回调路径

        return settleOrder;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput<DepositOrder> withdrawDepositOrder(Long depositOrderId) {
        //改状态，删除缴费单，通知撤回结算中心缴费单
        DepositOrder ea = this.get(depositOrderId);
        if (null == ea || !ea.getState().equals(DepositOrderStateEnum.SUBMITTED.getCode())){
            return BaseOutput.failure("撤回失败，状态已变更！");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (null == userTicket){
            return BaseOutput.failure("未登录！");
        }
        ea.setState(DepositOrderStateEnum.CREATED.getCode());
        ea.setWithdrawOperatorId(userTicket.getId());
        ea.setWithdrawOperator(userTicket.getRealName());
        if (this.updateSelective(ea) == 0) {
            LOG.info("撤回保证金【修改保证金单状态】失败,乐观锁生效。【保证金单ID：】" + depositOrderId);
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        PaymentOrder pb = this.findPaymentOrder(userTicket, ea.getId(), ea.getCode());
        if (paymentOrderService.delete(pb.getId()) == 0) {
            LOG.info("撤回保证金【删除缴费单】失败.");
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        BaseOutput<String>  setOut = settlementRpc.cancel(settlementAppId, pb.getCode());
        if (!setOut.isSuccess()){
            LOG.info("撤回，调用结算中心修改状态失败！" + setOut.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, "撤回，调用结算中心修改状态失败！" + setOut.getMessage());
        }
        return BaseOutput.success().setData(ea);
    }

    private PaymentOrder findPaymentOrder(UserTicket userTicket, Long businessId, String businessCode){
        PaymentOrder pb = DTOUtils.newDTO(PaymentOrder.class);
        pb.setBizType(BizTypeEnum.DEPOSIT_ORDER.getCode());
        pb.setBusinessId(businessId);
        pb.setBusinessCode(businessCode);
        pb.setMarketId(userTicket.getFirmId());
        pb.setState(PaymentOrderStateEnum.NOT_PAID.getCode());
        PaymentOrder order = paymentOrderService.listByExample(pb).stream().findFirst().orElse(null);
        if (null == order) {
            LOG.info("没有查询到付款单PaymentOrder【业务单businessId：{}】 【业务单businessCode:{}】", businessId, businessCode);
            throw new BusinessException(ResultCode.DATA_ERROR, "没有查询到付款单！");
        }
        return order;
    }

    @Override
    public BaseOutput<RefundOrder> addRefundOrder(RefundOrder refundOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (null == userTicket){
            return BaseOutput.failure("未登录！");
        }
        //检查客户状态
        checkCustomerState(refundOrder.getPayeeId(), userTicket.getFirmId());
        DepositOrder depositOrder = this.get(refundOrder.getBusinessId());
        if (null == depositOrder){
            LOG.info("保证金退款申请，保证金单【ID:{}】不存在！", refundOrder.getBusinessId());
            return BaseOutput.failure("保证金业务单不存在！");
        }
        Long totalRefundAmount = refundOrder.getPayeeAmount() + depositOrder.getRefundAmount();
        if (depositOrder.getAmount() < totalRefundAmount){
            return BaseOutput.failure("退款金额不能大于订单金额！");
        }
        refundOrder.setCode(userTicket.getFirmCode().toUpperCase() + this.getBizNumber(userTicket.getFirmCode() + "_" + BizNumberTypeEnum.DEPOSIT_REFUND_ORDER.getCode()));
        refundOrder.setBizType(BizTypeEnum.DEPOSIT_ORDER.getCode());
        refundOrder.setTotalRefundAmount(refundOrder.getPayeeAmount());
        return refundOrderService.doAddHandler(refundOrder);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput<DepositOrder> paySuccessHandler(SettleOrder settleOrder) {
        if (null == settleOrder){
            return BaseOutput.failure("回调参数为空！");
        }
        PaymentOrder condition = DTOUtils.newInstance(PaymentOrder.class);
        //结算单code唯一
        condition.setCode(settleOrder.getOrderCode());
        condition.setBizType(BizTypeEnum.DEPOSIT_ORDER.getCode());
        PaymentOrder paymentOrderPO = paymentOrderService.listByExample(condition).stream().findFirst().orElse(null);
        DepositOrder depositOrder = this.get(paymentOrderPO.getBusinessId());
        if (PaymentOrderStateEnum.PAID.getCode().equals(paymentOrderPO.getState())) { //如果已支付，直接返回
            return BaseOutput.success().setData(depositOrder);
        }
        if (!paymentOrderPO.getState().equals(PaymentOrderStateEnum.NOT_PAID.getCode())){
            LOG.info("缴费单状态已变更！状态为：" + PaymentOrderStateEnum.getPaymentOrderStateEnum(paymentOrderPO.getState()).getName() );
            return BaseOutput.failure("缴费单状态已变更！");
        }
        //缴费单数据更新
        paymentOrderPO.setState(PaymentOrderStateEnum.PAID.getCode());
        paymentOrderPO.setPayedTime(DateUtils.localDateTimeToUdate(settleOrder.getOperateTime()));
        paymentOrderPO.setSettlementCode(settleOrder.getCode());
        paymentOrderPO.setSettlementOperator(settleOrder.getOperatorName());
        paymentOrderPO.setSettlementWay(settleOrder.getWay());
        if (paymentOrderService.updateSelective(paymentOrderPO) == 0) {
            LOG.info("缴费单成功回调 -- 更新【缴费单】,乐观锁生效！【付款单paymentOrderID:{}】", paymentOrderPO.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        //修改订单状态
        depositOrder.setState(DepositOrderStateEnum.PAID.getCode());
        if (this.updateSelective(depositOrder) == 0) {
            LOG.info("缴费单成功回调 -- 更新【保证金单】状态,乐观锁生效！【保证金单EarnestOrderID:{}】", depositOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        return BaseOutput.success().setData(depositOrder);
    }

    @Override
    public BaseOutput<PrintDataDto> queryPrintData(String orderCode, Integer reprint) {
        PaymentOrder paymentOrderCondition = DTOUtils.newInstance(PaymentOrder.class);
        paymentOrderCondition.setCode(orderCode);
        paymentOrderCondition.setBizType(BizTypeEnum.DEPOSIT_ORDER.getCode());
        PaymentOrder paymentOrder = paymentOrderService.list(paymentOrderCondition).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            throw new RuntimeException("businessCode无效");
        }
        if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
            return BaseOutput.failure("此单未支付");
        }

        DepositOrder depositOrder = get(paymentOrder.getBusinessId());

        DepositOrderPrintDto depositOrderPrintDto = new DepositOrderPrintDto();
        depositOrderPrintDto.setPrintTime(new Date());
        depositOrderPrintDto.setReprint(reprint == 2 ? "(补打)" : "");
        depositOrderPrintDto.setCode(depositOrder.getCode());
        depositOrderPrintDto.setCustomerName(depositOrder.getCustomerName());
        depositOrderPrintDto.setCustomerCellphone(depositOrder.getCustomerCellphone());
        depositOrderPrintDto.setNotes(depositOrder.getNotes());
        depositOrderPrintDto.setAmount(MoneyUtils.centToYuan(depositOrder.getAmount()));
        depositOrderPrintDto.setSettlementWay(SettleWayEnum.getNameByCode(paymentOrder.getSettlementWay()));
        depositOrderPrintDto.setSettlementOperator(paymentOrder.getSettlementOperator());
        depositOrderPrintDto.setSubmitter(paymentOrder.getCreator());
        depositOrderPrintDto.setBusinessType(BizTypeEnum.DEPOSIT_ORDER.getName());

        PrintDataDto printDataDto = new PrintDataDto();
        printDataDto.setName(PrintTemplateEnum.DEPOSIT_ORDER.getCode());
        printDataDto.setItem(BeanMapUtil.beanToMap(depositOrderPrintDto));
        return BaseOutput.success().setData(printDataDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput refundSuccessHandler(RefundOrder refundOrder) {
        DepositOrder depositOrder = get(refundOrder.getBusinessId());
        if (DepositOrderStateEnum.REFUND.getCode().equals(depositOrder.getState())) {
            LOG.info("此单已退款【depositOrderId={}】", refundOrder.getBusinessId());
            return BaseOutput.success();
        }
        Long totalRefundAmount = refundOrder.getTotalRefundAmount() + depositOrder.getRefundAmount();
        if (depositOrder.getAmount() < totalRefundAmount){
            LOG.error("异常订单！！！---- 保证金单退款申请结算退款成功 但是退款单退款总金额大于订单可退金额【保证金单ID {}，退款单ID{}】", depositOrder.getId(), refundOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "异常订单！！！-- 退款金额不能大于保证金单可退金额！");
        }
        //@TODO fixbug多次退款，退款金额没有变化
        depositOrder.setRefundAmount(depositOrder.getRefundAmount() + refundOrder.getPayeeAmount());
        //@TODO 退款转入市场金额（罚款金额）
//            depositOrder.setChargebackAmount();
        depositOrder.setState(DepositOrderStateEnum.REFUND.getCode());
        if (updateSelective(depositOrder) == 0) {
            LOG.info("保证金单退款申请结算退款成功 更新保证金单乐观锁生效 【保证金单ID {}】", depositOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        return BaseOutput.success();
    }

    @Override
    public List<DepositOrder> selectBalanceList(DepositOrder depositOrder) {
        return this.getActualDao().selectBalanceList(depositOrder);
    }

    @Override
    public Integer countBalanceList(DepositOrder depositOrder) {
        return this.getActualDao().countBalanceList(depositOrder);
    }

    @Override
    public Long sumBalance(DepositOrder depositOrder) {
        return this.getActualDao().sumBalance(depositOrder);
    }
}