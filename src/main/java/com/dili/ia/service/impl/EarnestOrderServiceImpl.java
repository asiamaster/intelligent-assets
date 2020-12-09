package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.AssetsDTO;
import com.dili.assets.sdk.rpc.AreaMarketRpc;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.*;
import com.dili.ia.domain.dto.CustomerAccountParam;
import com.dili.ia.domain.dto.EarnestOrderListDto;
import com.dili.ia.domain.dto.printDto.EarnestOrderPrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.EarnestOrderMapper;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.rpc.UidFeignRpc;
import com.dili.ia.service.*;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleWayDetail;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.DateUtils;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.Department;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
@Service
public class EarnestOrderServiceImpl extends BaseServiceImpl<EarnestOrder, Long> implements EarnestOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(EarnestOrderServiceImpl.class);

    public EarnestOrderMapper getActualDao() {
        return (EarnestOrderMapper)getDao();
    }

    @SuppressWarnings("all")
    @Autowired
    DepartmentRpc departmentRpc;
    @Autowired
    CustomerRpc customerRpc;
    @Autowired
    AssetsRpc assetsRpc;
    @Autowired
    SettlementRpc settlementRpc;
    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    PaymentOrderService paymentOrderService;
    @Autowired
    EarnestOrderDetailService earnestOrderDetailService;
    @Autowired
    TransactionDetailsService transactionDetailsService;
    @Autowired
    UidFeignRpc uidFeignRpc;
    @Autowired
    AreaMarketRpc areaMarketRpc;

    @Value("${settlement.app-id}")
    private Long settlementAppId;
    @Value("${earnestOrder.settlement.handler.url}")
    private String settlerHandlerUrl;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput<EarnestOrder> addEarnestOrder(EarnestOrderListDto earnestOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(null == userTicket){
            return BaseOutput.failure("未登录");
        }
        //检查客户状态
        checkCustomerState(earnestOrder.getCustomerId(),userTicket.getFirmId());
        if(CollectionUtils.isNotEmpty(earnestOrder.getEarnestOrderdetails())){
            earnestOrder.getEarnestOrderdetails().forEach(o->{
                //检查摊位状态
                checkBoothState(o.getAssetsId());
            });
        }
        BaseOutput<Department> depOut = departmentRpc.get(earnestOrder.getDepartmentId());
        if(!depOut.isSuccess()){
            LOGGER.info("获取部门失败！" + depOut.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, "获取部门失败！");
        }
        earnestOrder.setCode(this.getBizNumber(userTicket.getFirmCode() + "_" + BizNumberTypeEnum.EARNEST_ORDER.getCode()));
        earnestOrder.setCreatorId(userTicket.getId());
        earnestOrder.setCreator(userTicket.getRealName());
        earnestOrder.setMarketId(userTicket.getFirmId());
        earnestOrder.setDepartmentName(depOut.getData().getName());
        earnestOrder.setState(EarnestOrderStateEnum.CREATED.getCode());
        earnestOrder.setAssetsType(AssetsTypeEnum.BOOTH.getCode());
        earnestOrder.setVersion(0L);
        if (earnestOrder.getFirstDistrictId() !=  null || earnestOrder.getSecondDistrictId() != null){
            earnestOrder.setMchId(getMchIdByDistrictId(earnestOrder.getSecondDistrictId() == null?earnestOrder.getFirstDistrictId():earnestOrder.getSecondDistrictId()));
        }else {
            earnestOrder.setMchId(userTicket.getFirmId());
        }
        this.insertSelective(earnestOrder);
        this.insertEarnestOrderDetails(earnestOrder);
        return BaseOutput.success().setData(earnestOrder);
    }

    private Long getMchIdByDistrictId(Long districtId){
        if (districtId == null){
            throw new BusinessException(ResultCode.PARAMS_ERROR, "查询商户，区域ID不能为空!");
        }
        Long mchId = null;
        //@TODO 为空抛异常，现在基础数据有问题，暂时注释掉代码，后期打开
//        try {
//            BaseOutput<Long> mchOutput = areaMarketRpc.getMarketByArea(districtId);
//            if (!mchOutput.isSuccess()){
//                LOG.error("根据区域ID查询商户，返回失败：{}", mchOutput.getMessage());
//                throw new BusinessException(ResultCode.APP_ERROR, "根据区域ID查询商户，返回失败!");
//            }
//            mchId = mchOutput.getData();
//        }catch (Exception e){
//            LOG.error("根据区域ID查询商户，接口调用异常："+e.getMessage(),e);
//            throw new BusinessException(ResultCode.APP_ERROR, "根据区域ID查询商户，接口调用异常！");
//        }
//        if (mchId == null){
//            LOG.error("根据区域ID查询商户，返回为空，districtId:{}", districtId);
//            throw new BusinessException(ResultCode.APP_ERROR, "根据区域ID查询商户，返回为空！");
//        }
        return mchId;
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
            throw new BusinessException(ResultCode.DATA_ERROR, "客户不存在，请核实！");
        }else if(EnabledStateEnum.DISABLED.getCode().equals(customer.getState())){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户已禁用，请核实！");
        }else if(YesOrNoEnum.YES.getCode().equals(customer.getIsDelete())){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户已删除，请核实！");
        }
    }
    /**
     * 检查摊位状态
     * @param boothId
     */
    private void checkBoothState(Long boothId){
        BaseOutput<AssetsDTO> output = assetsRpc.getAssetsById(boothId);
        if(!output.isSuccess()){
            throw new BusinessException(ResultCode.DATA_ERROR, "摊位接口调用异常 "+output.getMessage());
        }
        AssetsDTO booth = output.getData();
        if(null == booth){
            throw new BusinessException(ResultCode.DATA_ERROR, "摊位不存在，请核实和修改后再保存");
        }else if(EnabledStateEnum.DISABLED.getCode().equals(booth.getState())){
            throw new BusinessException(ResultCode.DATA_ERROR, "摊位已禁用，请核实和修改后再保存");
        }else if(YesOrNoEnum.YES.getCode().equals(booth.getIsDelete())){
            throw new BusinessException(ResultCode.DATA_ERROR, "摊位已删除，请核实和修改后再保存");
        }
    }
    /**
     * 批量插入租赁单项
     *
     * @param dto
     */
    private void insertEarnestOrderDetails(EarnestOrderListDto dto) {
        if (CollectionUtils.isEmpty(dto.getEarnestOrderdetails())){
            return;
        }
        dto.getEarnestOrderdetails().forEach(o -> {
            o.setEarnestOrderId(dto.getId());
            earnestOrderDetailService.insertSelective(o);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput<EarnestOrder> updateEarnestOrder(EarnestOrderListDto earnestOrder) {
        if (earnestOrder.getId() == null){
            return BaseOutput.failure("Id不能为空！");
        }
        EarnestOrder oldDTO = this.get(earnestOrder.getId());
        if (null == oldDTO || !oldDTO.getState().equals(EarnestOrderStateEnum.CREATED.getCode())){
            return BaseOutput.failure("修改失败，定金单状态已变更！");
        }
        //修改有清空修改，所以使用update
        if (this.update(this.buildUpdateDto(oldDTO, earnestOrder)) == 0){
            LOG.info("修改定金单失败,乐观锁生效【客户名称：{}】 【定金单ID:{}】", earnestOrder.getCustomerName(), earnestOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        this.deleteEarnestOrderDetailByEarnestOrderId(earnestOrder.getId());
        //根据摊位ID插入到定金详情里面
        this.insertEarnestOrderDetails(earnestOrder);
        return BaseOutput.success("修改成功！").setData(earnestOrder);
    }

    private void deleteEarnestOrderDetailByEarnestOrderId(Long earnestOrderId){
        EarnestOrderDetail eod = new EarnestOrderDetail();
        eod.setEarnestOrderId(earnestOrderId);
        List<EarnestOrderDetail> eodlist = earnestOrderDetailService.list(eod);
        if (CollectionUtils.isNotEmpty(eodlist)){
            eodlist.forEach(o -> {
                earnestOrderDetailService.delete(o.getId());
            });
        }
        return;
    }

    private EarnestOrder buildUpdateDto(EarnestOrder oldDTO, EarnestOrder dto){
        BaseOutput<Department> depOut = departmentRpc.get(dto.getDepartmentId());
        if(!depOut.isSuccess()){
            LOGGER.info("获取部门失败！" + depOut.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, "获取部门失败！");
        }
        oldDTO.setDepartmentName(depOut.getData().getName());
        oldDTO.setCustomerId(dto.getCustomerId());
        oldDTO.setCustomerName(dto.getCustomerName());
        oldDTO.setCertificateNumber(dto.getCertificateNumber());
        oldDTO.setCustomerCellphone(dto.getCustomerCellphone());
        oldDTO.setStartTime(dto.getStartTime());
        oldDTO.setEndTime(dto.getEndTime());
        oldDTO.setDepartmentId(dto.getDepartmentId());
        oldDTO.setAmount(dto.getAmount());
        oldDTO.setNotes(dto.getNotes());
        oldDTO.setModifyTime(LocalDateTime.now());
        oldDTO.setVersion(dto.getVersion());
        if (dto.getFirstDistrictId() !=  null || dto.getSecondDistrictId() != null){
            oldDTO.setMchId(this.getMchIdByDistrictId(dto.getSecondDistrictId() == null?dto.getFirstDistrictId():dto.getSecondDistrictId()));
        }
        return oldDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    @Override
    public BaseOutput<EarnestOrder> submitEarnestOrder(Long earnestOrderId) {
        EarnestOrder ea = this.get(earnestOrderId);
        if (null == ea){
            LOG.info("提交失败，没有查询到定金单！id={}", earnestOrderId);
            return BaseOutput.failure("提交失败，没有查询到定金单！");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(null == userTicket){
            return BaseOutput.failure("未登录");
        }
        //检查客户状态
        checkCustomerState(ea.getCustomerId(),userTicket.getFirmId());
        EarnestOrderDetail query = new EarnestOrderDetail();
        query.setEarnestOrderId(ea.getId());
        List<EarnestOrderDetail> detailList = earnestOrderDetailService.listByExample(query);
        if (CollectionUtils.isNotEmpty(detailList)){
            detailList.forEach(o->{
                //检查摊位状态
                checkBoothState(o.getAssetsId());
            });
        }
        if (!ea.getState().equals(EarnestOrderStateEnum.CREATED.getCode())){
            return BaseOutput.failure("提交失败，状态已变更！");
        }
        ea.setState(EarnestOrderStateEnum.SUBMITTED.getCode());
        ea.setSubmitterId(userTicket.getId());
        ea.setSubmitter(userTicket.getRealName());
        ea.setSubDate(LocalDateTime.now());
        //获取商户
        if (ea.getFirstDistrictId() !=  null || ea.getSecondDistrictId() != null){
            ea.setMchId(this.getMchIdByDistrictId(ea.getSecondDistrictId() == null?ea.getFirstDistrictId():ea.getSecondDistrictId()));
        }
        if (this.updateSelective(ea) == 0) {
            LOG.info("提交定金【修改定金单状态】失败 ,乐观锁生效！【定金单ID:{}】", ea.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        PaymentOrder pb = this.buildPaymentOrder(userTicket, ea);
        paymentOrderService.insertSelective(pb);
        // @TODO 客户余额迁移，这里需要删除
        //如果客户账户不存在，创建客户账户
        if (!customerAccountService.checkCustomerAccountExist(ea.getCustomerId(), userTicket.getFirmId())){
            customerAccountService.addCustomerAccountByCustomerInfo(ea.getCustomerId(), ea.getCustomerName(), ea.getCustomerCellphone(), ea.getCertificateNumber(), ea.getMarketId());
        }
        //提交到结算中心 --- 执行顺序不可调整！！因为异常只能回滚自己系统，无法回滚其它远程系统
        BaseOutput<SettleOrder> out= settlementRpc.submit(buildSettleOrderDto(userTicket, ea, pb));
        if (!out.isSuccess()){
            LOGGER.info("提交到结算中心失败！" + out.getMessage() + out.getErrorData());
            throw new BusinessException(ResultCode.DATA_ERROR, out.getMessage());
        }
        return BaseOutput.success().setData(ea);
    }

    //组装 -- 结算中心缴费单 SettleOrder
    private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, EarnestOrder ea, PaymentOrder paymentOrder){
        SettleOrderDto settleOrder = new SettleOrderDto();
        //以下是提交到结算中心的必填字段
        settleOrder.setMarketId(ea.getMarketId()); //市场ID
        settleOrder.setMarketCode(userTicket.getFirmCode());
        settleOrder.setOrderCode(paymentOrder.getCode());//订单号 唯一
        settleOrder.setBusinessCode(paymentOrder.getBusinessCode()); //缴费单业务单号
        settleOrder.setCustomerId(ea.getCustomerId());//客户ID
        settleOrder.setCustomerName(ea.getCustomerName());// "客户姓名
        settleOrder.setCustomerPhone(ea.getCustomerCellphone());//"客户手机号
        settleOrder.setAmount(ea.getAmount()); //金额
        settleOrder.setBusinessDepId(ea.getDepartmentId()); //"业务部门ID
        settleOrder.setBusinessDepName(departmentRpc.get(ea.getDepartmentId()).getData().getName());//"业务部门名称
        settleOrder.setSubmitterId(userTicket.getId());// "提交人ID
        settleOrder.setSubmitterName(userTicket.getRealName());// "提交人姓名
        if (userTicket.getDepartmentId() != null){
            settleOrder.setSubmitterDepId(userTicket.getDepartmentId()); //"提交人部门ID
            settleOrder.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        }
        settleOrder.setSubmitTime(LocalDateTime.now());
        settleOrder.setAppId(settlementAppId);//应用ID
        //结算单业务类型 为 Integer
        settleOrder.setBusinessType(Integer.valueOf(BizTypeEnum.EARNEST.getCode())); // 业务类型
        settleOrder.setType(SettleTypeEnum.PAY.getCode());// "结算类型  -- 付款
        settleOrder.setState(SettleStateEnum.WAIT_DEAL.getCode());
        settleOrder.setReturnUrl(settlerHandlerUrl); // 结算-- 缴费成功后回调路径
        //@TODO 提交到结算必须要穿的商户ID
        return settleOrder;
    }

    //组装缴费单 PaymentOrder
    private PaymentOrder buildPaymentOrder(UserTicket userTicket, EarnestOrder earnestOrder){
        PaymentOrder pb = new PaymentOrder();
        pb.setCode(this.getBizNumber(userTicket.getFirmCode() + "_" + BizTypeEnum.EARNEST.getEnName() + "_" +  BizNumberTypeEnum.PAYMENT_ORDER.getCode()));
        pb.setAmount(earnestOrder.getAmount());
        pb.setBusinessId(earnestOrder.getId());
        pb.setBusinessCode(earnestOrder.getCode());
        pb.setCreatorId(userTicket.getId());
        pb.setCreator(userTicket.getRealName());
        pb.setMarketId(userTicket.getFirmId());
        pb.setMarketCode(userTicket.getFirmCode());
        pb.setBizType(BizTypeEnum.EARNEST.getCode());
        pb.setState(PayStateEnum.NOT_PAID.getCode());
        pb.setCustomerId(earnestOrder.getCustomerId());
        pb.setCustomerName(earnestOrder.getCustomerName());
        pb.setVersion(0);
        pb.setMchId(earnestOrder.getMchId());
        pb.setDistrictId(earnestOrder.getSecondDistrictId() == null? earnestOrder.getFirstDistrictId():earnestOrder.getSecondDistrictId());
        return pb;
    }

    private PaymentOrder findPaymentOrder(Long marketId, Long businessId, String businessCode, Integer paymentOrderState){
        PaymentOrder pb = new PaymentOrder();
        pb.setBizType(BizTypeEnum.EARNEST.getCode());
        pb.setBusinessId(businessId);
        pb.setBusinessCode(businessCode);
        pb.setMarketId(marketId);
        pb.setState(paymentOrderState);
        PaymentOrder order = paymentOrderService.listByExample(pb).stream().findFirst().orElse(null);
        if (null == order) {
            LOG.info("没有查询到付款单PaymentOrder【业务单businessId：{}】 【业务单businessCode:{}】", businessId, businessCode);
            throw new BusinessException(ResultCode.DATA_ERROR, "没有查询到付款单！");
        }
        return order;
    }

    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    @Override
    public BaseOutput<EarnestOrder> withdrawEarnestOrder(Long earnestOrderId) {
        //改状态，删除缴费单，通知撤回结算中心缴费单
        EarnestOrder ea = this.get(earnestOrderId);
        if (null == ea || !ea.getState().equals(EarnestOrderStateEnum.SUBMITTED.getCode())){
            return BaseOutput.failure("撤回失败，状态已变更！");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (null == userTicket){
            return BaseOutput.failure("未登录！");
        }
        ea.setState(EarnestOrderStateEnum.CREATED.getCode());
        ea.setWithdrawOperatorId(userTicket.getId());
        ea.setWithdrawOperator(userTicket.getRealName());
        if (this.updateSelective(ea) == 0) {
            LOG.info("撤回定金【修改定金单状态】失败,乐观锁生效。【定金单ID：】" + earnestOrderId);
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        //查询【未支付】的缴费单
        PaymentOrder pb = this.findPaymentOrder(userTicket.getFirmId(), ea.getId(), ea.getCode(), PaymentOrderStateEnum.NOT_PAID.getCode());
        if (paymentOrderService.delete(pb.getId()) == 0) {
            LOG.info("撤回定金【删除缴费单】失败.");
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        BaseOutput<String>  setOut = settlementRpc.cancel(settlementAppId, pb.getCode());
        if (!setOut.isSuccess()){
            LOG.info("撤回，调用结算中心修改状态失败！" + setOut.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, "撤回，调用结算中心修改状态失败！" + setOut.getMessage());
        }
        return BaseOutput.success().setData(ea);
    }

    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    @Override
    public BaseOutput<EarnestOrder> invalidEarnestOrder(Long earnestOrderId, String invalidReason) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (null == userTicket){
            throw new BusinessException(ResultCode.NOT_AUTH_ERROR, "未登录！");
        }
        //修改业务单改状态 为【已作废】
        EarnestOrder ea = this.get(earnestOrderId);
        if (null == ea || !ea.getState().equals(EarnestOrderStateEnum.PAID.getCode())){
            throw new BusinessException(ResultCode.DATA_ERROR, "作废失败，状态已变更！");
        }
        ea.setState(EarnestOrderStateEnum.INVALID.getCode());
        ea.setInvalidOperatorId(userTicket.getId());
        ea.setInvalidOperator(userTicket.getRealName());
        ea.setInvalidReason(invalidReason);
        ea.setInvalidTime(LocalDateTime.now());
        if (this.updateSelective(ea) == 0) {
            LOG.info("作废定金【修改定金单状态】失败,乐观锁生效。【定金单ID：】" + earnestOrderId);
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        //warn : 定金不支持多次缴费的，所以这里只用查询订单是否有已交费的缴费单即可
        PaymentOrder pb = this.findPaymentOrder(userTicket.getFirmId(), ea.getId(), ea.getCode(), PaymentOrderStateEnum.PAID.getCode());
        if (null == pb){
            throw new BusinessException(ResultCode.DATA_ERROR, "没有查询到该业务单已交费的缴费单");
        }
        //构建缴费红冲单
        List<PaymentOrder> rerverpaymentOrders = this.buildRerverpaymentOrder(userTicket, pb);
        if (!rerverpaymentOrders.isEmpty() && paymentOrderService.batchInsert(rerverpaymentOrders) != rerverpaymentOrders.size()) {
            throw new BusinessException(ResultCode.DATA_ERROR, "缴费单红冲写入失败！");
        }
        //@TODO 客户余额迁移 此处需要 调用结算进行定金余额扣减
        //客户定金余额扣减
        BaseOutput result = customerAccountService.deductEarnestBalance(this.buildCustomerAccountParam(ea,userTicket.getId(), userTicket.getRealName() ,ea.getAmount(), TransactionSceneTypeEnum.INVALID_OUT.getCode()));
        if (!result.isSuccess()){
            LOG.info("作废定金，调用扣减定金余额失败！" + result.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, "作废定金，调用扣减定金余额失败！" + result.getMessage());
        }
        //调用结算生成结算红冲单
        BaseOutput<String> setOut = settlementRpc.invalid(this.buildInvalidRequestDto(userTicket, pb));
        if (!setOut.isSuccess()){
            LOG.info("作废，调用结算中心生成红冲单失败！" + setOut.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, "作废，调用结算中心生成红冲单失败！" + setOut.getMessage());
        }
        return BaseOutput.success().setData(ea);
    }

    private List<PaymentOrder> buildRerverpaymentOrder(UserTicket userTicket, PaymentOrder paymentOrder){
        List<PaymentOrder> rerverpaymentOrders = new ArrayList<>();
        if (PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
            PaymentOrder newPaymentOrder = paymentOrder.clone();
            newPaymentOrder.setCreateTime(LocalDateTime.now());
            newPaymentOrder.setModifyTime(LocalDateTime.now());
            newPaymentOrder.setCreatorId(userTicket.getId());
            newPaymentOrder.setCreator(userTicket.getRealName());
            newPaymentOrder.setIsReverse(YesOrNoEnum.YES.getCode());
            newPaymentOrder.setParentId(paymentOrder.getId());
            newPaymentOrder.setId(null);
            newPaymentOrder.setCode(this.getBizNumber(userTicket.getFirmCode() + "_" + BizTypeEnum.EARNEST.getEnName() + "_" +  BizNumberTypeEnum.PAYMENT_ORDER.getCode()));
            rerverpaymentOrders.add(newPaymentOrder);
        } else if (PaymentOrderStateEnum.NOT_PAID.getCode().equals(paymentOrder.getState())) {
            this.withdrawPaymentOrder(paymentOrder.getId());
        }
        return rerverpaymentOrders;
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
            BaseOutput output = settlementRpc.cancel(settlementAppId, paymentCode);
            if (!output.isSuccess()) {
                LOG.info("结算单撤回异常 【缴费单CODE {}】", paymentCode);
                throw new BusinessException(ResultCode.DATA_ERROR, output.getMessage());
            }

            payingOrder.setState(PaymentOrderStateEnum.CANCEL.getCode());
            if (paymentOrderService.updateSelective(payingOrder) == 0) {
                LOG.info("撤回缴费单异常，乐观锁生效，【缴费单编号:{}】", payingOrder.getCode());
                throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
            }
        }
    }

    /**
     * 构建客户定金修改 的 方法参数
     * @param ea 发生客户定金变动的业务单
     * @param operaterId 操作员ID
     * @param operatorName 操作员姓名
     * @param amount 发生变动的金额
     * @param sceneType 发生金额变动的场景，用于记录流水
     * @return CustomerAccountParam
     * */
    private CustomerAccountParam buildCustomerAccountParam(EarnestOrder ea, Long operaterId, String operatorName, Long amount, Integer sceneType){
        CustomerAccountParam caParam = new CustomerAccountParam();
        caParam.setBizType(BizTypeEnum.EARNEST.getCode());
        caParam.setAmount(amount);
        caParam.setCustomerId(ea.getCustomerId());
        caParam.setOrderId(ea.getId());
        caParam.setOrderCode(ea.getCode());
        caParam.setOperaterId(operaterId);
        caParam.setOperatorName(operatorName);
        caParam.setSceneType(sceneType);
        caParam.setMarketId(ea.getMarketId());

        return caParam;
    }
    private InvalidRequestDto buildInvalidRequestDto(UserTicket userTicket, PaymentOrder paymentOrder){
        InvalidRequestDto param = new InvalidRequestDto();
        param.setAppId(this.settlementAppId);
        param.setMarketCode(userTicket.getFirmCode());
        param.setMarketId(userTicket.getFirmId());
        param.setOperatorId(userTicket.getId());
        param.setOperatorName(userTicket.getRealName());
        List<String> orderCodeList = new ArrayList<>();
        orderCodeList.add(paymentOrder.getCode());
        param.setOrderCodeList(orderCodeList);
        return param;
    }
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    @Override
    public BaseOutput<EarnestOrder> paySuccessHandler(SettleOrder settleOrder) {
        if (null == settleOrder){
            return BaseOutput.failure("回调参数为空！");
        }
        PaymentOrder condition = new PaymentOrder();
        //结算单code唯一
        condition.setCode(settleOrder.getOrderCode());
        condition.setBizType(BizTypeEnum.EARNEST.getCode());
        PaymentOrder paymentOrderPO = paymentOrderService.listByExample(condition).stream().findFirst().orElse(null);
        EarnestOrder ea = this.get(paymentOrderPO.getBusinessId());
        if (PaymentOrderStateEnum.PAID.getCode().equals(paymentOrderPO.getState())) { //如果已支付，直接返回
            return BaseOutput.success().setData(ea);
        }
        if (!paymentOrderPO.getState().equals(PaymentOrderStateEnum.NOT_PAID.getCode())){
            LOG.info("缴费单状态已变更！状态为：" + PaymentOrderStateEnum.getPaymentOrderStateEnum(paymentOrderPO.getState()).getName() );
            return BaseOutput.failure("缴费单状态已变更！");
        }
        //缴费单数据更新
        paymentOrderPO.setState(PaymentOrderStateEnum.PAID.getCode());
        paymentOrderPO.setPayedTime(settleOrder.getOperateTime());
        paymentOrderPO.setSettlementCode(settleOrder.getCode());
        paymentOrderPO.setSettlementOperator(settleOrder.getOperatorName());
        paymentOrderPO.setSettlementWay(settleOrder.getWay());
        if (paymentOrderService.updateSelective(paymentOrderPO) == 0) {
            LOG.info("缴费单成功回调 -- 更新【缴费单】,乐观锁生效！【付款单paymentOrderID:{}】", paymentOrderPO.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        //修改订单状态
        ea.setState(EarnestOrderStateEnum.PAID.getCode());
        if (this.updateSelective(ea) == 0) {
            LOG.info("缴费单成功回调 -- 更新【定金单】状态,乐观锁生效！【定金单EarnestOrderID:{}】", ea.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        //TODO 付款成功 结算自己处理余额变动及流水记录 ，此处删除
        //更新客户账户定金余额和可用余额
        BaseOutput output = customerAccountService.rechargeEarnestBalance(this.buildCustomerAccountParam(ea,settleOrder.getOperatorId(), settleOrder.getOperatorName(), paymentOrderPO.getAmount() ,TransactionSceneTypeEnum.PAYMENT.getCode()));
        if (!output.isSuccess()){
            LOG.info("【定金单支付成功回调接口】充值定金接口返回失败！EarnestOrderId={}；原因：{}", ea.getId(),output.getMessage());
            throw new BusinessException(ResultCode.DATA_ERROR, "充值定金接口返回失败！" + output.getMessage());
        }
        return BaseOutput.success().setData(ea);
    }

    @Override
    public BaseOutput<PrintDataDto> queryPrintData(String orderCode, Integer reprint) {
        PaymentOrder paymentOrderCondition = new PaymentOrder();
        paymentOrderCondition.setCode(orderCode);
        paymentOrderCondition.setBizType(BizTypeEnum.EARNEST.getCode());
        PaymentOrder paymentOrder = paymentOrderService.list(paymentOrderCondition).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            throw new RuntimeException("businessCode无效");
        }
        if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
            return BaseOutput.failure("此单未支付");
        }

        EarnestOrder earnestOrder = get(paymentOrder.getBusinessId());
        EarnestOrderPrintDto earnestOrderPrintDto = new EarnestOrderPrintDto();
        earnestOrderPrintDto.setPrintTime(DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss"));
        earnestOrderPrintDto.setReprint(reprint == 2 ? "(补打)" : "");
        earnestOrderPrintDto.setCode(earnestOrder.getCode());
        earnestOrderPrintDto.setCustomerName(earnestOrder.getCustomerName());
        earnestOrderPrintDto.setCustomerCellphone(earnestOrder.getCustomerCellphone());
        earnestOrderPrintDto.setStartTime(earnestOrder.getStartTime() != null ? DateUtils.format(earnestOrder.getStartTime(), "yyyy-MM-dd"): "");
        earnestOrderPrintDto.setEndTime(earnestOrder.getEndTime() != null ? DateUtils.format(earnestOrder.getEndTime(), "yyyy-MM-dd"): "");
        earnestOrderPrintDto.setNotes(earnestOrder.getNotes());
        earnestOrderPrintDto.setAmount(MoneyUtils.centToYuan(earnestOrder.getAmount()));
        earnestOrderPrintDto.setSettlementWay(SettleWayEnum.getNameByCode(paymentOrder.getSettlementWay()));
        earnestOrderPrintDto.setSettlementOperator(paymentOrder.getSettlementOperator());
        earnestOrderPrintDto.setSubmitter(paymentOrder.getCreator());
        earnestOrderPrintDto.setBusinessType(BizTypeEnum.EARNEST.getName());

        EarnestOrderDetail earnestOrderDetail = new EarnestOrderDetail();
        earnestOrderDetail.setEarnestOrderId(earnestOrder.getId());
        StringBuffer assetsItems = new StringBuffer();
        earnestOrderDetailService.list(earnestOrderDetail).forEach(o -> {
            assetsItems.append(o.getAssetsName()).append(",");
        });
        if (assetsItems != null && assetsItems.length() > 1){
            //去掉最后一个， 符
            assetsItems.replace(assetsItems.length()-1, assetsItems.length(), " ");
        }
        earnestOrderPrintDto.setAssetsItems(assetsItems.toString());
        earnestOrderPrintDto.setSettleWayDetails(this.buildSettleWayDetails(paymentOrder.getSettlementWay(), paymentOrder.getSettlementCode()));
        PrintDataDto<EarnestOrderPrintDto> printDataDto = new PrintDataDto<>();
        printDataDto.setName(PrintTemplateEnum.EARNEST_ORDER.getCode());
        printDataDto.setItem(earnestOrderPrintDto);
        return BaseOutput.success().setData(printDataDto);
    }

    /**
     * 票据获取结算详情
     * 组合支付，结算详情格式 : 【微信 150.00 2020-08-19 4237458467568870 备注：微信付款150元;银行卡 150.00 2020-08-19 4237458467568870 备注：微信付款150元】
     * 园区卡支付，结算详情格式：【卡号：428838247888（李四）】
     * 除了园区卡 和 组合支付 ，结算详情格式：【2020-08-19 4237458467568870 备注：微信付款150元】
     * @param settlementWay 结算方式
     * @param settlementCode 结算详情
     * @return
     * */
    private String buildSettleWayDetails(Integer settlementWay, String settlementCode){
        //组合支付需要显示结算详情
        StringBuffer settleWayDetails = new StringBuffer();
        settleWayDetails.append("【");
        if (settlementWay.equals(SettleWayEnum.MIXED_PAY.getCode())){
            //摊位租赁单据的交款时间，也就是结算时填写的时间，显示到结算详情中，显示内容为：支付方式（组合支付的，只显示该类型下的具体支付方式）、金额、收款日期、流水号、结算备注，每个字段间隔一个空格；如没填写的则不显示；
            // 多个支付方式的，均在一行显示，当前行满之后换行，支付方式之间用;隔开；
            BaseOutput<List<SettleWayDetail>> output = settlementRpc.listSettleWayDetailsByCode(settlementCode);
            List<SettleWayDetail> swdList = output.getData();
            if (output.isSuccess() && CollectionUtils.isNotEmpty(swdList)){
                for(SettleWayDetail swd : swdList){
                    //此循环字符串拼接顺序不可修改，组合支付，结算详情格式 : 【微信 150.00 2020-08-19 4237458467568870 备注：微信付款150元;银行卡 150.00 2020-08-19 4237458467568870 备注：微信付款150元】
                    settleWayDetails.append(SettleWayEnum.getNameByCode(swd.getWay())).append(" ").append(MoneyUtils.centToYuan(swd.getAmount()));
                    if (null != swd.getChargeDate()){
                        settleWayDetails.append(" ").append(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(swd.getChargeDate()));
                    }
                    if (StringUtils.isNotEmpty(swd.getSerialNumber())){
                        settleWayDetails.append(" ").append(swd.getSerialNumber());
                    }
                    if (StringUtils.isNotEmpty(swd.getNotes())){
                        settleWayDetails.append(" ").append("备注：").append(swd.getNotes());
                    }
                    settleWayDetails.append("；");
                }
                //去掉最后一个; 符
                settleWayDetails.replace(settleWayDetails.length()-1, settleWayDetails.length(), " ");
            }else {
                LOGGER.info("查询结算微服务组合支付，支付详情失败；原因：{}",output.getMessage());
            }
        } else if (settlementWay.equals(SettleWayEnum.CARD.getCode())){
            // 园区卡支付，结算详情格式：【卡号：428838247888（李四）】
            BaseOutput<SettleOrder> output = settlementRpc.getByCode(settlementCode);
            if(output.isSuccess()){
                SettleOrder settleOrder = output.getData();
                if (null != settleOrder.getTradeCardNo()){
                    settleWayDetails.append("卡号:" + settleOrder.getTradeCardNo());
                }
                if(StringUtils.isNotBlank(settleOrder.getTradeCustomerName())){
                    settleWayDetails.append("（").append(settleOrder.getTradeCustomerName()).append("）");
                }
            }else {
                LOGGER.info("查询结算微服务非组合支付，支付详情失败；原因：{}",output.getMessage());
            }
        }else{
            // 除了园区卡 和 组合支付 ，结算详情格式：【2020-08-19 4237458467568870 备注：微信付款150元】
            BaseOutput<SettleOrder> output = settlementRpc.getByCode(settlementCode);
            if(output.isSuccess()){
                SettleOrder settleOrder = output.getData();
                if (null != settleOrder.getChargeDate()){
                    settleWayDetails.append(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(settleOrder.getChargeDate()));
                }
                if(StringUtils.isNotBlank(settleOrder.getSerialNumber())){
                    settleWayDetails.append(" ").append(settleOrder.getSerialNumber());
                }
                if (StringUtils.isNotBlank(settleOrder.getNotes())){
                    settleWayDetails.append(" ").append("备注：").append(settleOrder.getNotes());
                }
            }else {
                LOGGER.info("查询结算微服务非组合支付，支付详情失败；原因：{}",output.getMessage());
            }
        }
        settleWayDetails.append("】");
        if (StringUtils.isNotEmpty(settleWayDetails) && settleWayDetails.length() > 2){ // 长度大于2 是因为，避免内容为空，显示成 【】
            return settleWayDetails.toString();
        }
        return "";
    }
}