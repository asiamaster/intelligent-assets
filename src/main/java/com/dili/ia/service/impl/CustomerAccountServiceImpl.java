package com.dili.ia.service.impl;

import com.dili.ia.domain.*;
import com.dili.ia.domain.dto.EarnestTransferDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.CustomerAccountMapper;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.EarnestTransferOrderService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.service.TransactionDetailsService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
@Service
public class CustomerAccountServiceImpl extends BaseServiceImpl<CustomerAccount, Long> implements CustomerAccountService {

    public CustomerAccountMapper getActualDao() {
        return (CustomerAccountMapper)getDao();
    }

    @Autowired
    EarnestTransferOrderService earnestTransferOrderService;
    @Autowired
    TransactionDetailsService transactionDetailsService;
    @Autowired
    RefundOrderService refundOrderService;

    @Override
    public Boolean checkCustomerAccountExist(Long customerId, Long marketId) {
        CustomerAccount customerAccount = DTOUtils.newDTO(CustomerAccount.class);
        customerAccount.setCustomerId(customerId);
        customerAccount.setMarketId(marketId);
        List<CustomerAccount> list = getActualDao().select(customerAccount);
        if (CollectionUtils.isEmpty(list)){
            return false;
        }
        return true;
    }

    @Override
    public CustomerAccount getCustomerAccountByCustomerId(Long customerId, Long marketId) {
        CustomerAccount customerAccount = DTOUtils.newDTO(CustomerAccount.class);
        customerAccount.setCustomerId(customerId);
        customerAccount.setMarketId(marketId);
        List<CustomerAccount> list = getActualDao().select(customerAccount);
        if (CollectionUtils.isEmpty(list) || list.size() > 1){
            throw new BusinessException("2","当前客户账户存在异常，不存在或者同一市场存在多个相同客户账户！");
        }
       return list.get(0);
    }

    @Override
    public void subtractEarnestAvailableAndBalance(Long customerId, Long marketId, Long availableAmount, Long balanceAmount) {
        CustomerAccount customerAccount = this.getCustomerAccountByCustomerId(customerId, marketId);
        customerAccount.setEarnestAvailableBalance(customerAccount.getEarnestAvailableBalance() - availableAmount);
        customerAccount.setEarnestBalance(customerAccount.getEarnestBalance() - balanceAmount);

        Integer count = this.getActualDao().updateEarnestAccountByVersion(customerAccount);
        if (count < 1){
            throw new BusinessException("2","当前数据正已被其他用户操作，更新失败！");
        }
    }

    @Override
    public void addEarnestAvailableAndBalance(Long customerId, Long marketId, Long availableAmount, Long balanceAmount) {
        CustomerAccount customerAccount = this.getCustomerAccountByCustomerId(customerId, marketId);
        customerAccount.setEarnestAvailableBalance(customerAccount.getEarnestAvailableBalance() + availableAmount);
        customerAccount.setEarnestBalance(customerAccount.getEarnestBalance() + balanceAmount);

        Integer count = this.getActualDao().updateEarnestAccountByVersion(customerAccount);
        if (count < 1){
            throw new BusinessException("2","当前数据正已被其他用户操作，更新失败！");
        }
    }

    @Override
    public CustomerAccount creatCustomerAccountByCustomerInfo(Long customerId, String customerName, String customerCellphone, String certificateNumber){
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        CustomerAccount customerAccount = DTOUtils.newDTO(CustomerAccount.class);
        customerAccount.setMarketId(userTicket.getFirmId());
        customerAccount.setCustomerId(customerId);
        customerAccount.setCustomerCellphone(customerCellphone);
        customerAccount.setCertificateNumber(certificateNumber);
        customerAccount.setCustomerName(customerName);
        customerAccount.setEarnestBalance(0L);
        customerAccount.setEarnestAvailableBalance(0L);
        customerAccount.setEarnestFrozenAmount(0L);
        customerAccount.setTransferAvailableBalance(0L);
        customerAccount.setTransferBalance(0L);
        customerAccount.setTransferFrozenAmount(0L);
        customerAccount.setVersion(0L);
        this.getActualDao().insertSelective(customerAccount);
        return customerAccount;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void earnestTransfer(EarnestTransferOrder order) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        //修改准入方转出方余额，
        CustomerAccount peyerCustomerAccount = this.get(order.getPayeeCustomerAccountId());
        peyerCustomerAccount.setEarnestBalance(peyerCustomerAccount.getEarnestBalance() - order.getAmount());
        peyerCustomerAccount.setEarnestAvailableBalance(peyerCustomerAccount.getEarnestAvailableBalance() - order.getAmount());
        this.update(peyerCustomerAccount);
        CustomerAccount peyeeCustomerAccount = this.get(order.getPayeeCustomerAccountId());
        peyeeCustomerAccount.setEarnestBalance(peyeeCustomerAccount.getEarnestBalance() + order.getAmount());
        peyeeCustomerAccount.setEarnestAvailableBalance(peyeeCustomerAccount.getEarnestAvailableBalance() + order.getAmount());
        this.update(peyerCustomerAccount);

        //记录定金转出转入流水
        TransactionDetails tdIn = buildTransactionDetails(order, TransactionSceneTypeEnum.EARNEST_IN.getCode());
        TransactionDetails tdOut = buildTransactionDetails(order, TransactionSceneTypeEnum.EARNEST_OUT.getCode());
        List<TransactionDetails> listDetails = new ArrayList<>();
        listDetails.add(tdIn);
        listDetails.add(tdOut);
        transactionDetailsService.batchInsert(listDetails );
        //回写转入转出流水号
        order.setState(EarnestTransferOrderStateEnum.TRANSFERED.getCode());
        order.setPayerTransactionDetailsCode(tdOut.getCode());
        order.setPayeeTransactionCode(tdIn.getCode());
        earnestTransferOrderService.updateSelective(order);
    }

    @Override
    public EarnestTransferOrder createEarnestTransferOrder(EarnestTransferDto efDto){
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw  new RuntimeException("未登陆");
        }
        CustomerAccount payeeCustomerAccount = this.getCustomerAccountByCustomerId(efDto.getCustomerId(), userTicket.getFirmId());
        //保存定金转移单
        efDto.setPayeeCustomerAccountId(payeeCustomerAccount.getId());
        efDto.setPayeeId(efDto.getCustomerId());
        efDto.setPayeeCellphone(efDto.getCustomerCellphone());
        efDto.setPayeeCertificateNumber(efDto.getCertificateNumber());
        efDto.setPayeeName(efDto.getCustomerName());
        efDto.setState(EarnestTransferOrderStateEnum.CREATED.getCode());

        efDto.setCreatorId(userTicket.getId());
        efDto.setCreator(userTicket.getRealName());
        efDto.setMarketId(userTicket.getFirmId());
        earnestTransferOrderService.insertSelective(efDto);
        return efDto;
    }

    private TransactionDetails buildTransactionDetails(EarnestTransferOrder ea, Integer sceneType){
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        TransactionDetails tds = DTOUtils.newDTO(TransactionDetails.class);
        if (sceneType.equals(TransactionSceneTypeEnum.EARNEST_IN.getCode())){ //定金转入
            tds.setAmount(ea.getAmount());
            tds.setCertificateNumber(ea.getPayeeCertificateNumber());
            tds.setCustomerCellphone(ea.getPayeeCellphone());
            tds.setCustomerId(ea.getPayeeId());
            tds.setCustomerName(ea.getPayeeName());
            tds.setCreator(userTicket.getRealName());
            tds.setCreatorId(userTicket.getId());
        }else if(sceneType.equals(TransactionSceneTypeEnum.EARNEST_OUT.getCode())){//定金转出
            tds.setAmount(0 - ea.getAmount());
            tds.setCertificateNumber(ea.getPayerCertificateNumber());
            tds.setCustomerCellphone(ea.getPayerCellphone());
            tds.setCustomerId(ea.getPayerId());
            tds.setCustomerName(ea.getPayerName());
            tds.setCreator(userTicket.getRealName());
            tds.setCreatorId(userTicket.getId());
        }
//        tds.setNotes(ea.ge());
//        tds.setOrderCode(ea.getCode());
        tds.setMarketId(ea.getMarketId());
        tds.setOrderId(ea.getId());
        tds.setCode("202002200001");
        tds.setBizType(BizTypeEnum.EARNEST.getCode());
        tds.setSceneType(sceneType);
        tds.setItemType(TransactionItemTypeEnum.EARNEST.getCode());
        tds.setMarketId(userTicket.getFirmId());
        tds.setCreateTime(new Date());
        return tds;
    }

    @Override
    public void earnestRefund(RefundOrder order) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登陆");
        }
        BaseOutput checkResult = checkParams(order);
        if (!checkResult.isSuccess()){
            throw new RuntimeException(checkResult.getMessage());
        }
        order.setCreator(userTicket.getRealName());
        order.setCreatorId(userTicket.getId());
        order.setMarketId(userTicket.getFirmId());
        //@TODO退款单编号
        order.setCode("DJTK202003020001");
        order.setBizType(BizTypeEnum.EARNEST.getCode());
        order.setState((long)RefundOrderStateEnum.CREATED.getCode());

        refundOrderService.insertSelective(order);
    }

    private BaseOutput checkParams(RefundOrder order){
        if (null == order.getOrderId()){//定金退款不是针对业务单，所以订单ID记录的是【客户账户ID】
            return BaseOutput.failure("退款单orderId不能为空！");
        }
        if (null == order.getCustomerId()){
            return BaseOutput.failure("客户ID不能为空！");
        }
        if (null == order.getCustomerName()){
            return BaseOutput.failure("客户名称不能为空！");
        }
        if (null == order.getCertificateNumber()){
            return BaseOutput.failure("客户证件号码不能为空！");
        }
        if (null == order.getCustomerCellphone()){
            return BaseOutput.failure("客户联系电话不能为空！");
        }
        if (null == order.getTotalRefundAmount()){
            return BaseOutput.failure("退款单金额不能为空！");
        }

        return BaseOutput.success();
    }
}