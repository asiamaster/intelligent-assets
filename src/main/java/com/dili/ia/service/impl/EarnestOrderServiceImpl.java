package com.dili.ia.service.impl;

import com.dili.ia.domain.EarnestOrder;
import com.dili.ia.domain.EarnestOrderDetail;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.TransactionDetails;
import com.dili.ia.domain.dto.EarnestOrderListDto;
import com.dili.ia.domain.dto.EarnestOrderPrintDto;
import com.dili.ia.domain.dto.LeaseOrderListDto;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.EarnestOrderMapper;
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
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
@Service
public class EarnestOrderServiceImpl extends BaseServiceImpl<EarnestOrder, Long> implements EarnestOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EarnestOrderServiceImpl.class);

    public EarnestOrderMapper getActualDao() {
        return (EarnestOrderMapper)getDao();
    }

    @Autowired
    DepartmentRpc departmentRpc;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addEarnestOrder(EarnestOrderListDto earnestOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
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
            throw new RuntimeException("编号生成器微服务异常");
        }
        //@TODO业务单号, 多摊位存储  验证客户状态，摊位状态是否正常
        earnestOrder.setCode(bizNumberOutput.getData());
        earnestOrder.setVersion(0L);
        this.getActualDao().insertSelective(earnestOrder);
        insertEarnestOrderDetails(earnestOrder);
        if (!customerAccountService.checkCustomerAccountExist(earnestOrder.getCustomerId(), userTicket.getFirmId())){ //如果客户账户不存在，创建客户账户
           customerAccountService.addCustomerAccountByCustomerInfo(earnestOrder.getCustomerId(), earnestOrder.getCustomerName(), earnestOrder.getCustomerCellphone(), earnestOrder.getCertificateNumber());
        }
        return 0;
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

    @Override
    public BaseOutput updateEarnestOrder(EarnestOrderListDto earnestOrder) {
        if (earnestOrder.getId() == null){
            return BaseOutput.failure();
        }
        this.getActualDao().updateByPrimaryKey(earnestOrder);
        this.deleteEarnestOrderDetailByEarnestOrderId(earnestOrder.getId());
        //根据摊位ID插入到定金详情里面
        insertEarnestOrderDetails(earnestOrder);
        return BaseOutput.success();
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
        if (CollectionUtils.isEmpty(eodlist) && eodlist.size() > 0){
            for (EarnestOrderDetail ed : eodlist){
                earnestOrderDetailService.delete(ed.getId());
            }
        }
        return;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput submitEarnestOrder(Long earnestOrderId) {
        //@TODO改状态，创建缴费单，提交到结算中心
        EarnestOrder ea = this.getActualDao().selectByPrimaryKey(earnestOrderId);
        if (null == ea || !ea.getState().equals(EarnestOrderStateEnum.CREATED.getCode())){
            return BaseOutput.failure("提交失败，状态已变更！");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new BusinessException(ResultCode.NOT_AUTH_ERROR, "未登陆");
        }
        ea.setState(EarnestOrderStateEnum.SUBMITTED.getCode());
        ea.setSubmitterId(userTicket.getId());
        ea.setSubmitter(userTicket.getRealName());
        ea.setSubDate(new Date());
        this.getActualDao().updateByPrimaryKey(ea);

        PaymentOrder pb = this.buildPaymentOrder(userTicket, ea);
        paymentOrderService.insert(pb);

        //提交到结算中心 --- 执行顺序不可调整！！因为异常只能回滚自己系统，无法回滚其它远程系统
        BaseOutput<SettleOrder> out= settlementRpc.submit(buildSettleOrderDto(userTicket, ea));
        if (!out.isSuccess()){
            LOGGER.info("提交到结算中心失败！" + out.getMessage() + out.getErrorData());
            throw new RuntimeException("提交到结算中心失败！" + out.getMessage());
        }


        return BaseOutput.success();
    }

    //组装 -- 结算中心缴费单 SettleOrder
    private SettleOrderDto buildSettleOrderDto(UserTicket userTicket, EarnestOrder ea){
        SettleOrderDto settleOrder = new SettleOrderDto();
        //以下是提交到结算中心的必填字段
        settleOrder.setMarketId(ea.getMarketId()); //市场ID
        settleOrder.setMarketCode(userTicket.getFirmCode());
        settleOrder.setBusinessCode(ea.getCode()); //业务单号
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
        String returnUrl = "http://ia.diligrp.com/earnestOrder/paySuccess";
        settleOrder.setReturnUrl(returnUrl); // 结算-- 缴费成功后回调路径

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
        pb.setBizType(BizTypeEnum.EARNEST.getCode());
        pb.setState(PayStateEnum.NOT_PAID.getCode());
        BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.PAYMENT_ORDER.getCode());
        if(!bizNumberOutput.isSuccess()){
            throw new RuntimeException("编号生成器微服务异常");
        }
        pb.setCode(bizNumberOutput.getData());
        return pb;
    }

    private PaymentOrder findPaymentOrder(UserTicket userTicket, Long businessId, String businessCode){
        PaymentOrder pb = DTOUtils.newDTO(PaymentOrder.class);
        pb.setBizType(BizTypeEnum.EARNEST.getCode());
        pb.setBusinessId(businessId);
        pb.setBusinessCode(businessCode);
        pb.setMarketId(userTicket.getFirmId());
        pb.setState(PaymentOrderStateEnum.NOT_PAID.getCode());
        List<PaymentOrder> list = paymentOrderService.selectByExample(pb);
        if (CollectionUtils.isEmpty(list) || list.size() > 1) {
            throw new RuntimeException("没有查询到有效付款单！");
        }
        return list.get(0);
    }

    @Override
    public BaseOutput withdrawEarnestOrder(Long earnestOrderId) {
        //@TODO改状态，删除缴费单，通知撤销结算中心缴费单
        EarnestOrder ea = this.getActualDao().selectByPrimaryKey(earnestOrderId);
        if (null == ea || !ea.getState().equals(EarnestOrderStateEnum.SUBMITTED.getCode())){
            return BaseOutput.failure("撤回失败，状态已变更！");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        ea.setState(EarnestOrderStateEnum.CREATED.getCode());
        ea.setWithdrawOperatorId(userTicket.getId());
        ea.setWithdrawOperator(userTicket.getRealName());
        this.getActualDao().updateByPrimaryKey(ea);

        PaymentOrder pb = this.findPaymentOrder(userTicket, ea.getId(), ea.getCode());
        paymentOrderService.deleteByExample(pb);

        //@TODO 更改提交到结算中心的数据
        return BaseOutput.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput paySuccessEarnestOrder(Long earnestOrderId) {
        //修改订单状态
        EarnestOrder ea = this.getActualDao().selectByPrimaryKey(earnestOrderId);
        ea.setState(EarnestOrderStateEnum.PAID.getCode());
        this.getActualDao().updateByPrimaryKey(ea);
        //@TODO缴费单数据更新，结算编号，缴费时间等
        //更新客户账户定金余额和可用余额
        customerAccountService.paySuccessEarnest(ea.getCustomerId(), ea.getMarketId(), ea.getAmount());
        //插入客户账户定金资金动账流水
        transactionDetailsService.insert(buildTransactionDetails(ea));
        return BaseOutput.success();
    }

    private TransactionDetails buildTransactionDetails(EarnestOrder ea){
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        TransactionDetails tds = DTOUtils.newDTO(TransactionDetails.class);
        tds.setAmount(ea.getAmount());
        tds.setCertificateNumber(ea.getCertificateNumber());
        tds.setCreator(userTicket.getRealName());
        tds.setCreatorId(userTicket.getId());
        tds.setCustomerCellphone(ea.getCustomerCellphone());
        tds.setCustomerId(ea.getCustomerId());
        tds.setCustomerName(ea.getCustomerName());
        tds.setMarketId(ea.getMarketId());
        tds.setNotes(ea.getCode());
        tds.setOrderId(ea.getId());
        tds.setOrderCode(ea.getCode());
        BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.TRANSACTION_CODE.getCode());
        if(!bizNumberOutput.isSuccess()){
            throw new RuntimeException("编号生成器微服务异常");
        }
        tds.setCode(bizNumberOutput.getData());
        tds.setBizType(BizTypeEnum.EARNEST.getCode());
        tds.setSceneType(TransactionSceneTypeEnum.PAYMENT.getCode());
        tds.setItemType(TransactionItemTypeEnum.EARNEST.getCode());
        return tds;
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
        if (assetsItems != null){
            assetsItems.substring(0, assetsItems.length() - 1);
        }
        earnestOrderPrintDto.setAssetsItems(assetsItems.toString());
        printDataDto.setItem(BeanMapUtil.beanToMap(earnestOrderPrintDto));
        return BaseOutput.success().setData(printDataDto);
    }
}