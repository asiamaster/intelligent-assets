package com.dili.ia.service.impl;

import com.dili.ia.domain.*;
import com.dili.ia.domain.dto.CustomerQuery;
import com.dili.ia.domain.dto.EarnestTransferDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.CustomerAccountMapper;
import com.dili.ia.rpc.CustomerRpc;
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
import io.swagger.models.auth.In;
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
    @Autowired
    CustomerRpc customerRpc;

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

        Integer count = this.getActualDao().updateAmountByAccountIdAndVersion(customerAccount);
        if (count < 1){
            throw new BusinessException("2","当前数据正已被其他用户操作，更新失败！");
        }
    }

    @Override
    public void addEarnestAvailableAndBalance(Long customerId, Long marketId, Long availableAmount, Long balanceAmount) {
        CustomerAccount customerAccount = this.getCustomerAccountByCustomerId(customerId, marketId);
        customerAccount.setEarnestAvailableBalance(customerAccount.getEarnestAvailableBalance() + availableAmount);
        customerAccount.setEarnestBalance(customerAccount.getEarnestBalance() + balanceAmount);

        Integer count = this.getActualDao().updateAmountByAccountIdAndVersion(customerAccount);
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
        TransactionDetails tdIn = buildTransactionDetails(TransactionSceneTypeEnum.EARNEST_IN.getCode(), BizTypeEnum.EARNEST.getCode(), TransactionItemTypeEnum.EARNEST.getCode(), order.getAmount(), order.getId(), "code", order.getPayeeId(), order.getTransferReason(), order.getMarketId());
        TransactionDetails tdOut = buildTransactionDetails(TransactionSceneTypeEnum.EARNEST_OUT.getCode(), BizTypeEnum.EARNEST.getCode(), TransactionItemTypeEnum.EARNEST.getCode(), 0 - order.getAmount(), order.getId(), "code", order.getPayeeId(), order.getTransferReason(), order.getMarketId());
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

    @Override
    public BaseOutput submitLeaseOrderCustomerAmountFrozen(Long orderId, String orderCode, Long customerId, Long earnestDeduction, Long transferDeduction, Long depositDeduction, Long marketId){
        try {
            Integer sceneType = TransactionSceneTypeEnum.FROZEN.getCode();
            this.changeCustomerAmountAndDetails(sceneType, orderId, orderCode, customerId, earnestDeduction, transferDeduction, depositDeduction, marketId);
            return BaseOutput.success("处理成功！");
        } catch (RuntimeException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            return BaseOutput.failure("处理出错！");
        }
    }

    @Override
    public BaseOutput withdrawLeaseOrderCustomerAmountUnFrozen(Long orderId, String orderCode, Long customerId, Long earnestDeduction, Long transferDeduction, Long depositDeduction, Long marketId) {
        try {
            Integer sceneType = TransactionSceneTypeEnum.UNFROZEN.getCode();
            this.changeCustomerAmountAndDetails(sceneType, orderId, orderCode, customerId, 0 - earnestDeduction, 0 - transferDeduction, 0 - depositDeduction, marketId);
            return BaseOutput.success("处理成功！");
        } catch (RuntimeException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            return BaseOutput.failure("处理出错！");
        }
    }

    @Override
    public BaseOutput paySuccessLeaseOrderCustomerAmountConsume(Long orderId, String orderCode, Long customerId, Long earnestDeduction, Long transferDeduction, Long depositDeduction, Long marketId) {
        try {
            this.payChangeCustomerAmountAndDetails(orderId, orderCode, customerId, 0 - earnestDeduction, 0 - transferDeduction, 0 - depositDeduction, marketId);
            return BaseOutput.success("处理成功！");
        } catch (RuntimeException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            return BaseOutput.failure("处理出错！");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void payChangeCustomerAmountAndDetails(Long orderId, String orderCode, Long customerId, Long earnestDeduction, Long transferDeduction, Long depositDeduction, Long marketId) {
        BaseOutput checkOut = this.checkParams(orderId, orderCode, customerId, marketId);
        if(!checkOut.isSuccess()){
            throw new RuntimeException(checkOut.getMessage());
        }
        Integer bizType = BizTypeEnum.BOOTH_LEASE.getCode();
        Integer itemType = TransactionItemTypeEnum.EARNEST.getCode();
        Integer unfrozen = TransactionSceneTypeEnum.UNFROZEN.getCode();
        Integer deductUse = TransactionSceneTypeEnum.DEDUCT_USE.getCode();

        CustomerAccount ca = this.getCustomerAccountByCustomerId(customerId, marketId);
        List<TransactionDetails> listDetails = new ArrayList<>();
        //写入 定金，转抵，保证金的【冻结】流水
        if (earnestDeduction != null && earnestDeduction != 0){
            ca.setEarnestBalance(ca.getEarnestBalance() - earnestDeduction);
            ca.setEarnestFrozenAmount(ca.getEarnestFrozenAmount() - earnestDeduction);

            TransactionDetails earnestUnfrozenDetail = buildTransactionDetails(unfrozen, bizType, itemType, earnestDeduction, orderId, orderCode, customerId, orderCode, marketId);
            TransactionDetails earnestDeductUseDetail = buildTransactionDetails(deductUse, bizType, itemType, 0 - earnestDeduction, orderId, orderCode, customerId, orderCode, marketId);
            listDetails.add(earnestUnfrozenDetail);
            listDetails.add(earnestDeductUseDetail);
        }
        if (transferDeduction != null && transferDeduction != 0){
            ca.setTransferBalance(ca.getTransferBalance() - transferDeduction);
            ca.setTransferFrozenAmount(ca.getTransferFrozenAmount() + transferDeduction);

            TransactionDetails transferUnfrozenDetail = buildTransactionDetails(unfrozen, bizType, itemType, transferDeduction, orderId, orderCode, customerId, orderCode, marketId);
            TransactionDetails transferDeductUseDetail = buildTransactionDetails(deductUse, bizType, itemType, 0 - transferDeduction, orderId, orderCode, customerId, orderCode, marketId);
            listDetails.add(transferUnfrozenDetail);
            listDetails.add(transferDeductUseDetail);
        }
        if (depositDeduction != null && depositDeduction != 0){
            TransactionDetails depositUnfrozenDetail = buildTransactionDetails(unfrozen, bizType, itemType, depositDeduction, orderId, orderCode, customerId, orderCode, marketId);
            TransactionDetails depositDeductUseDetail = buildTransactionDetails(deductUse, bizType, itemType, 0 - depositDeduction, orderId, orderCode, customerId, orderCode, marketId);
            listDetails.add(depositUnfrozenDetail);
            listDetails.add(depositDeductUseDetail);
        }
        //只有定金或者转抵有金额变动，才执行更新客户账户
        if ((earnestDeduction != null && earnestDeduction != 0) || (transferDeduction != null && transferDeduction != 0)){
            this.getActualDao().updateAmountByAccountIdAndVersion(ca);
        }

        if (!CollectionUtils.isEmpty(listDetails)){
            transactionDetailsService.batchInsert(listDetails );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void changeCustomerAmountAndDetails(Integer sceneType, Long orderId, String orderCode, Long customerId, Long earnestDeduction, Long transferDeduction, Long depositDeduction, Long marketId) {
        BaseOutput checkOut = this.checkParams(orderId, orderCode, customerId, marketId);
        if(!checkOut.isSuccess()){
            throw new RuntimeException(checkOut.getMessage());
        }
        Integer bizType = BizTypeEnum.BOOTH_LEASE.getCode();
        Integer itemType = TransactionItemTypeEnum.EARNEST.getCode();
        CustomerAccount ca = this.getCustomerAccountByCustomerId(customerId, marketId);
        List<TransactionDetails> listDetails = new ArrayList<>();
        //写入 定金，转抵，保证金的【冻结】流水
        if (earnestDeduction != null && earnestDeduction != 0){
            ca.setEarnestAvailableBalance(ca.getEarnestAvailableBalance() - earnestDeduction);
            ca.setEarnestFrozenAmount(ca.getEarnestFrozenAmount() + earnestDeduction);

            TransactionDetails earnestDetail = buildTransactionDetails(sceneType, bizType, itemType, 0 - earnestDeduction, orderId, orderCode, customerId, orderCode, marketId);
            listDetails.add(earnestDetail);
        }
        if (transferDeduction != null && transferDeduction != 0){
            ca.setTransferAvailableBalance(ca.getTransferAvailableBalance() - transferDeduction);
            ca.setTransferFrozenAmount(ca.getTransferFrozenAmount() + transferDeduction);

            TransactionDetails transferDetail = buildTransactionDetails(sceneType, bizType, itemType, 0 - transferDeduction, orderId, orderCode, customerId, orderCode, marketId);
            listDetails.add(transferDetail);
        }
        if (depositDeduction != null && depositDeduction != 0){
            TransactionDetails depositDetail = buildTransactionDetails(sceneType, bizType, itemType, 0 - depositDeduction, orderId, orderCode, customerId, orderCode, marketId);
            listDetails.add(depositDetail);
        }
        //只有定金或者转抵有金额变动，才执行更新客户账户
        if ((earnestDeduction != null && earnestDeduction != 0) || (transferDeduction != null && transferDeduction != 0)){
            this.getActualDao().updateAmountByAccountIdAndVersion(ca);
        }

        if (!CollectionUtils.isEmpty(listDetails)){
            transactionDetailsService.batchInsert(listDetails );
        }
    }

    private BaseOutput checkParams(Long orderId, String orderCode, Long customerId, Long marketId){
        if (orderId == null){
            return BaseOutput.failure("订单ID不能为空");
        }
        if (orderCode == null){
            return BaseOutput.failure("订单CODE不能为空");
        }
        if (customerId == null){
            return BaseOutput.failure("客户ID不能为空");
        }
        if (marketId == null){
            return BaseOutput.failure("市场ID不能为空");
        }
        return BaseOutput.success();
    }

    private TransactionDetails buildTransactionDetails(Integer sceneType, Integer bizType, Integer itemType, Long amount, Long orderId, String orderCode, Long customerId, String notes, Long marketId){
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        TransactionDetails tds = DTOUtils.newDTO(TransactionDetails.class);
        Customer customer = customerRpc.get(customerId, marketId).getData();
        if (null == customer){
            LOGGER.info("客户不存在！【customerId=" + customerId + "; marketId=" + marketId + "】" );
            throw new RuntimeException("客户不存在！");
        }
        tds.setCustomerId(customer.getId());
        tds.setCustomerName(customer.getName());
        tds.setCertificateNumber(customer.getCertificateNumber());
        tds.setCustomerCellphone(customer.getContactsPhone());
        //参数传入
        tds.setAmount(amount);
        tds.setNotes(notes);
        tds.setOrderCode(orderCode);
        tds.setOrderId(orderId);
        tds.setBizType(bizType);
        tds.setSceneType(sceneType);
        tds.setItemType(itemType);
        tds.setMarketId(marketId);
        //固定构建参数
        tds.setCreator(userTicket.getRealName());
        tds.setCreatorId(userTicket.getId());
        tds.setCode("202002200001");
        tds.setCreateTime(new Date());
        return tds;
    }
}

