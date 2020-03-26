package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BoothDTO;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.*;
import com.dili.ia.domain.dto.EarnestOrderListDto;
import com.dili.ia.domain.dto.EarnestOrderPrintDto;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.EarnestOrderMapper;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.rpc.UidFeignRpc;
import com.dili.ia.service.*;
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
import org.apache.commons.collections.CollectionUtils;
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
 * This file was generated on 2020-02-14 10:18:23.
 */
@Service
public class EarnestOrderServiceImpl extends BaseServiceImpl<EarnestOrder, Long> implements EarnestOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(EarnestOrderServiceImpl.class);

    public EarnestOrderMapper getActualDao() {
        return (EarnestOrderMapper)getDao();
    }

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
        earnestOrder.getEarnestOrderdetails().forEach(o->{
            //检查摊位状态
            checkBoothState(o.getAssetsId());
        });

        earnestOrder.setCreatorId(userTicket.getId());
        earnestOrder.setCreator(userTicket.getRealName());
        earnestOrder.setMarketId(userTicket.getFirmId());
        BaseOutput<Department> depOut = departmentRpc.get(userTicket.getDepartmentId());
        if(depOut.isSuccess()){
            earnestOrder.setDepartmentName(depOut.getData().getName());
        }
        earnestOrder.setState(EarnestOrderStateEnum.CREATED.getCode());
        earnestOrder.setAssetsType(AssetsTypeEnum.BOOTH.getCode());
        BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.EARNEST_ORDER.getCode());
        if(!bizNumberOutput.isSuccess()){
            throw new BusinessException(ResultCode.DATA_ERROR, "编号生成器微服务异常");
        }
        earnestOrder.setCode(userTicket.getFirmCode().toUpperCase() + bizNumberOutput.getData());
        earnestOrder.setVersion(0L);
        this.insertSelective(earnestOrder);
        insertEarnestOrderDetails(earnestOrder);

        if (!customerAccountService.checkCustomerAccountExist(earnestOrder.getCustomerId(), userTicket.getFirmId())){ //如果客户账户不存在，创建客户账户
           BaseOutput<CustomerAccount> addCusOut = customerAccountService.addCustomerAccountByCustomerInfo(earnestOrder.getCustomerId(), earnestOrder.getCustomerName(), earnestOrder.getCustomerCellphone(), earnestOrder.getCertificateNumber());
           if (!addCusOut.isSuccess()){
               return BaseOutput.failure(addCusOut.getMessage());
           }
        }
        return BaseOutput.success().setData(earnestOrder);
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
     * 批量插入租赁单项
     *
     * @param dto
     */
    private void insertEarnestOrderDetails(EarnestOrderListDto dto) {
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
        if (this.updateSelective(earnestOrder) != 1){
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        this.deleteEarnestOrderDetailByEarnestOrderId(earnestOrder.getId());
        //根据摊位ID插入到定金详情里面
        this.insertEarnestOrderDetails(earnestOrder);
        return BaseOutput.success("修改成功！").setData(earnestOrder);
    }

    private EarnestOrderDetail bulidEarnestOrderDetail(Long earnestOrderId, Long assetsId, String assetsName){
        EarnestOrderDetail eod = DTOUtils.newDTO(EarnestOrderDetail.class);
        eod.setEarnestOrderId(earnestOrderId);
        eod.setAssetsId(assetsId);
        eod.setAssetsName(assetsName);
        return eod;
    }

    private void deleteEarnestOrderDetailByEarnestOrderId(Long earnestOrderId){
        EarnestOrderDetail eod = DTOUtils.newDTO(EarnestOrderDetail.class);
        eod.setEarnestOrderId(earnestOrderId);
        List<EarnestOrderDetail> eodlist = earnestOrderDetailService.list(eod);
        eodlist.forEach(o -> {
            earnestOrderDetailService.delete(o.getId());
        });
        return;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput<EarnestOrder> submitEarnestOrder(Long earnestOrderId) {
        EarnestOrder ea = this.get(earnestOrderId);
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(null == userTicket){
            return BaseOutput.failure("未登录");
        }
        //检查客户状态
        checkCustomerState(ea.getCustomerId(),userTicket.getFirmId());
        EarnestOrderDetail query = DTOUtils.newInstance(EarnestOrderDetail.class);
        query.setEarnestOrderId(ea.getId());
        List<EarnestOrderDetail> detailList = earnestOrderDetailService.listByExample(query);
        if (CollectionUtils.isNotEmpty(detailList)){
            detailList.forEach(o->{
                //检查摊位状态
                checkBoothState(o.getAssetsId());
            });
        }
        if (null == ea || !ea.getState().equals(EarnestOrderStateEnum.CREATED.getCode())){
            return BaseOutput.failure("提交失败，状态已变更！");
        }
        ea.setState(EarnestOrderStateEnum.SUBMITTED.getCode());
        ea.setSubmitterId(userTicket.getId());
        ea.setSubmitter(userTicket.getRealName());
        ea.setSubDate(new Date());
        if (this.updateSelective(ea) != 1) {
            LOG.info("提交定金【修改定金单状态】失败 -- 记录数不为 1 ，多人操作，请重试！");
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        PaymentOrder pb = this.buildPaymentOrder(userTicket, ea);
        paymentOrderService.insertSelective(pb);

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
        settleOrder.setBusinessCode(paymentOrder.getCode()); //缴费单业务单号
        settleOrder.setCustomerId(ea.getCustomerId());//客户ID
        settleOrder.setCustomerName(ea.getCustomerName());// "客户姓名
        settleOrder.setCustomerPhone(ea.getCustomerCellphone());//"客户手机号
        settleOrder.setAmount(ea.getAmount()); //金额
        settleOrder.setBusinessDepId(ea.getDepartmentId()); //"业务部门ID
        settleOrder.setBusinessDepName(departmentRpc.get(ea.getDepartmentId()).getData().getName());//"业务部门名称
        settleOrder.setSubmitterId(userTicket.getId());// "提交人ID
        settleOrder.setSubmitterName(userTicket.getRealName());// "提交人姓名
        settleOrder.setSubmitterDepId(userTicket.getDepartmentId()); //"提交人部门ID
        settleOrder.setSubmitterDepName(departmentRpc.get(userTicket.getDepartmentId()).getData().getName());
        settleOrder.setSubmitTime(LocalDateTime.now());
        settleOrder.setAppId(settlementAppId);//应用ID
        settleOrder.setBusinessType(BizTypeEnum.EARNEST.getCode()); // 业务类型
        settleOrder.setType(SettleTypeEnum.PAY.getCode());// "结算类型  -- 付款
        settleOrder.setState(SettleStateEnum.WAIT_DEAL.getCode());
        settleOrder.setReturnUrl(settlerHandlerUrl); // 结算-- 缴费成功后回调路径

        return settleOrder;
    }

    //组装缴费单 PaymentOrder
    private PaymentOrder buildPaymentOrder(UserTicket userTicket, EarnestOrder earnestOrder){
        PaymentOrder pb = DTOUtils.newDTO(PaymentOrder.class);
        pb.setAmount(earnestOrder.getAmount());
        pb.setBusinessId(earnestOrder.getId());
        pb.setBusinessCode(earnestOrder.getCode());
        pb.setCreatorId(userTicket.getId());
        pb.setCreator(userTicket.getRealName());
        pb.setMarketId(userTicket.getFirmId());
        pb.setMarketCode(userTicket.getFirmCode());
        pb.setBizType(BizTypeEnum.EARNEST.getCode());
        pb.setState(PayStateEnum.NOT_PAID.getCode());
        pb.setVersion(0);
        BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.PAYMENT_ORDER.getCode());
        if(!bizNumberOutput.isSuccess()){
            throw new BusinessException(ResultCode.DATA_ERROR, "编号生成器微服务异常");
        }
        pb.setCode(userTicket.getFirmCode().toUpperCase() + bizNumberOutput.getData());
        return pb;
    }

    private PaymentOrder findPaymentOrder(UserTicket userTicket, Long businessId, String businessCode){
        PaymentOrder pb = DTOUtils.newDTO(PaymentOrder.class);
        pb.setBizType(BizTypeEnum.EARNEST.getCode());
        pb.setBusinessId(businessId);
        pb.setBusinessCode(businessCode);
        pb.setMarketId(userTicket.getFirmId());
        pb.setState(PaymentOrderStateEnum.NOT_PAID.getCode());
        PaymentOrder order = paymentOrderService.listByExample(pb).stream().findFirst().orElse(null);
        if (null == order) {
            throw new BusinessException(ResultCode.DATA_ERROR, "没有查询到付款单！");
        }
        return order;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput<EarnestOrder> withdrawEarnestOrder(Long earnestOrderId) {
        //改状态，删除缴费单，通知撤回结算中心缴费单
        EarnestOrder ea = this.getActualDao().selectByPrimaryKey(earnestOrderId);
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
        if (this.updateSelective(ea) != 1) {
            LOG.info("撤回定金【修改定金单状态】失败 -- 记录数不为 1 ，多人操作，请重试！");
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        PaymentOrder pb = this.findPaymentOrder(userTicket, ea.getId(), ea.getCode());
        if (paymentOrderService.delete(pb.getId()) != 1) {
            LOG.info("撤回定金【删除缴费单】失败 -- 记录数不为 1 ，多人操作，请重试！");
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
    @Override
    public BaseOutput<EarnestOrder> paySuccessHandler(SettleOrder settleOrder) {
        if (null == settleOrder){
            return BaseOutput.failure("回调参数为空！");
        }
        PaymentOrder condition = DTOUtils.newInstance(PaymentOrder.class);
        //结算单code唯一
        condition.setCode(settleOrder.getBusinessCode());
        PaymentOrder paymentOrderPO = paymentOrderService.listByExample(condition).stream().findFirst().orElse(null);
        if (PaymentOrderStateEnum.PAID.getCode().equals(paymentOrderPO.getState())) { //如果已支付，直接返回
            return BaseOutput.success();
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
        if (paymentOrderService.updateSelective(paymentOrderPO) != 1) {
            LOG.info("缴费单成功回调 -- 更新【缴费单】状态记录数不为 1 ，多人操作，请重试！");
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        //修改订单状态
        EarnestOrder ea = this.getActualDao().selectByPrimaryKey(paymentOrderPO.getBusinessId());
        ea.setState(EarnestOrderStateEnum.PAID.getCode());
        if (this.updateSelective(ea) != 1) {
            LOG.info("缴费单成功回调 -- 更新【租赁单】状态记录数不为 1 ，多人操作，请重试！");
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }

        //更新客户账户定金余额和可用余额
        customerAccountService.paySuccessEarnest(ea.getCustomerId(), ea.getMarketId(), ea.getAmount());
        //插入客户账户定金资金动账流水
        TransactionDetails details = transactionDetailsService.buildByConditions(TransactionSceneTypeEnum.PAYMENT.getCode(), BizTypeEnum.EARNEST.getCode(), TransactionItemTypeEnum.EARNEST.getCode(), ea.getAmount(), ea.getId(), ea.getCode(), ea.getCustomerId(), ea.getCode(), ea.getMarketId(), settleOrder.getOperatorId(), settleOrder.getOperatorName());
        transactionDetailsService.insertSelective(details);

        return BaseOutput.success().setData(ea);
    }

    @Override
    public BaseOutput<PrintDataDto> queryPrintData(String businessCode, Integer reprint) {
        PaymentOrder paymentOrderCondition = DTOUtils.newInstance(PaymentOrder.class);
        paymentOrderCondition.setCode(businessCode);
        PaymentOrder paymentOrder = paymentOrderService.list(paymentOrderCondition).stream().findFirst().orElse(null);
        if (null == paymentOrder) {
            throw new RuntimeException("businessCode无效");
        }
        if (!PaymentOrderStateEnum.PAID.getCode().equals(paymentOrder.getState())) {
            return BaseOutput.failure("此单未支付");
        }

        EarnestOrder earnestOrder = get(paymentOrder.getBusinessId());
        PrintDataDto printDataDto = new PrintDataDto();
        EarnestOrderPrintDto earnestOrderPrintDto = new EarnestOrderPrintDto();
        earnestOrderPrintDto.setPrintTime(new Date());
        earnestOrderPrintDto.setReprint(reprint == 2 ? "(补打)" : "");
        earnestOrderPrintDto.setCode(earnestOrder.getCode());
        printDataDto.setName(PrintTemplateEnum.EARNEST_ORDER.getCode());
        earnestOrderPrintDto.setCustomerName(earnestOrder.getCustomerName());
        earnestOrderPrintDto.setCustomerCellphone(earnestOrder.getCustomerCellphone());
        earnestOrderPrintDto.setStartTime(earnestOrder.getStartTime());
        earnestOrderPrintDto.setEndTime(earnestOrder.getEndTime());
        earnestOrderPrintDto.setNotes(earnestOrder.getNotes());
        earnestOrderPrintDto.setAmount(MoneyUtils.centToYuan(earnestOrder.getAmount()));
        earnestOrderPrintDto.setSettlementWay(SettleWayEnum.getNameByCode(paymentOrder.getSettlementWay()));
        earnestOrderPrintDto.setSettlementOperator(paymentOrder.getSettlementOperator());
        earnestOrderPrintDto.setSubmitter(paymentOrder.getCreator());
        earnestOrderPrintDto.setBusinessType(BizTypeEnum.EARNEST.getName());

        EarnestOrderDetail earnestOrderDetail = DTOUtils.newInstance(EarnestOrderDetail.class);
        earnestOrderDetail.setEarnestOrderId(earnestOrder.getId());
        StringBuffer assetsItems = new StringBuffer();
        earnestOrderDetailService.list(earnestOrderDetail).forEach(o -> {
            assetsItems.append(o.getAssetsName()).append(",");
        });
        if (assetsItems != null && assetsItems.length() > 1){
            assetsItems.substring(0, assetsItems.length() - 1);
        }
        earnestOrderPrintDto.setAssetsItems(assetsItems.toString());
        printDataDto.setItem(BeanMapUtil.beanToMap(earnestOrderPrintDto));
        return BaseOutput.success().setData(printDataDto);
    }
}