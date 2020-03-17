package com.dili.ia.service.impl;

import com.dili.ia.domain.CustomerAccount;
import com.dili.ia.domain.EarnestTransferOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.TransactionDetails;
import com.dili.ia.domain.dto.EarnestTransferDto;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.CustomerAccountMapper;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.rpc.UidFeignRpc;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.EarnestTransferOrderService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.service.TransactionDetailsService;
import com.dili.ia.util.ResultCodeConst;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
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
    @Autowired
    UidFeignRpc uidFeignRpc;

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
        if (CollectionUtils.isEmpty(list) || list.size() != 1){
            LOGGER.info("当前【客户账户】不存在，或者一个客户同一市场存在多个客户账户！customerId={}，marketId={}", customerId, marketId);
            return null;
        }
       return list.get(0);
    }

    @Override
    public void frozenEarnest(Long customerId, Long marketId, Long amount) {
        CustomerAccount customerAccount = this.getCustomerAccountByCustomerId(customerId, marketId);
        customerAccount.setEarnestAvailableBalance(customerAccount.getEarnestAvailableBalance() - amount);
        customerAccount.setEarnestFrozenAmount(customerAccount.getEarnestFrozenAmount() + amount);

        Integer count = this.getActualDao().updateAmountByAccountIdAndVersion(customerAccount);
        if (count < 1){
            throw new BusinessException(ResultCode.DATA_ERROR,"当前数据正已被其他用户操作，更新失败！");
        }
    }

    @Override
    public void unfrozenEarnest(Long customerId, Long marketId, Long amount) {
        CustomerAccount customerAccount = this.getCustomerAccountByCustomerId(customerId, marketId);
        customerAccount.setEarnestAvailableBalance(customerAccount.getEarnestAvailableBalance() + amount);
        customerAccount.setEarnestFrozenAmount(customerAccount.getEarnestFrozenAmount() - amount);

        Integer count = this.getActualDao().updateAmountByAccountIdAndVersion(customerAccount);
        if (count < 1){
            throw new BusinessException(ResultCode.DATA_ERROR,"当前数据正已被其他用户操作，更新失败！");
        }
    }

    @Override
    public void paySuccessEarnest(Long customerId, Long marketId, Long amount) {
        CustomerAccount customerAccount = this.getCustomerAccountByCustomerId(customerId, marketId);
        customerAccount.setEarnestAvailableBalance(customerAccount.getEarnestAvailableBalance() + amount);
        customerAccount.setEarnestBalance(customerAccount.getEarnestBalance() + amount);

        Integer count = this.getActualDao().updateAmountByAccountIdAndVersion(customerAccount);
        if (count < 1){
            throw new BusinessException(ResultCode.DATA_ERROR, "当前数据正已被其他用户操作，更新失败！");
        }
    }
    @Override
    public void refundSuccessEarnest(Long customerId, Long marketId, Long amount) {
        CustomerAccount customerAccount = this.getCustomerAccountByCustomerId(customerId, marketId);
        customerAccount.setEarnestBalance(customerAccount.getEarnestBalance() - amount);
        customerAccount.setEarnestFrozenAmount(customerAccount.getEarnestFrozenAmount() - amount);

        Integer count = this.getActualDao().updateAmountByAccountIdAndVersion(customerAccount);
        if (count < 1){
            throw new BusinessException(ResultCode.DATA_ERROR, "当前数据正已被其他用户操作，更新失败！");
        }
    }

    @Override
    public CustomerAccount addCustomerAccountByCustomerInfo(Long customerId, String customerName, String customerCellphone, String certificateNumber){
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new BusinessException(ResultCode.NOT_AUTH_ERROR, "未登陆");
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
        //修改准入方转出方余额，
        CustomerAccount payerCustomerAccount = this.get(order.getPayeeCustomerAccountId());
        payerCustomerAccount.setEarnestBalance(payerCustomerAccount.getEarnestBalance() - order.getAmount());
        payerCustomerAccount.setEarnestAvailableBalance(payerCustomerAccount.getEarnestAvailableBalance() - order.getAmount());
        int countPayer = this.getActualDao().updateAmountByAccountIdAndVersion(payerCustomerAccount);
        if (countPayer != 1){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户账户正在被多人操作，稍后再试");
        }
        CustomerAccount payeeCustomerAccount = this.get(order.getPayeeCustomerAccountId());
        payeeCustomerAccount.setEarnestBalance(payeeCustomerAccount.getEarnestBalance() + order.getAmount());
        payeeCustomerAccount.setEarnestAvailableBalance(payeeCustomerAccount.getEarnestAvailableBalance() + order.getAmount());
        int countPayee = this.getActualDao().updateAmountByAccountIdAndVersion(payeeCustomerAccount);
        if (countPayee != 1){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户账户正在被多人操作，稍后再试");
        }

        //记录定金转出转入流水
        TransactionDetails tdIn = transactionDetailsService.buildByConditions(TransactionSceneTypeEnum.EARNEST_IN.getCode(), BizTypeEnum.EARNEST.getCode(), TransactionItemTypeEnum.EARNEST.getCode(), order.getAmount(), order.getId(), "code", order.getPayeeId(), order.getTransferReason(), order.getMarketId());
        TransactionDetails tdOut = transactionDetailsService.buildByConditions(TransactionSceneTypeEnum.EARNEST_OUT.getCode(), BizTypeEnum.EARNEST.getCode(), TransactionItemTypeEnum.EARNEST.getCode(), 0 - order.getAmount(), order.getId(), "code", order.getPayeeId(), order.getTransferReason(), order.getMarketId());
        List<TransactionDetails> listDetails = new ArrayList<>();
        listDetails.add(tdIn);
        listDetails.add(tdOut);
        transactionDetailsService.batchInsert(listDetails );
        //回写转入转出流水号
        order.setState(EarnestTransferOrderStateEnum.TRANSFERED.getCode());
        order.setPayerTransactionDetailsCode(tdOut.getCode());
        order.setPayeeTransactionCode(tdIn.getCode());
        order.setTransferTime(new Date());
        int count = earnestTransferOrderService.updateSelective(order);
        if (count != 1){
            throw new BusinessException(ResultCode.DATA_ERROR, "转移单被多人操作，稍后再试");
        }
    }

    @Override
    public EarnestTransferOrder addEarnestTransferOrder(EarnestTransferDto efDto){
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new BusinessException(ResultCode.NOT_AUTH_ERROR, "未登陆");
        }
        CustomerAccount payeeCustomerAccount = this.getCustomerAccountByCustomerId(efDto.getCustomerId(), userTicket.getFirmId());
        //保存定金转移单
        efDto.setPayeeCustomerAccountId(payeeCustomerAccount.getId());
        efDto.setPayeeId(efDto.getCustomerId());
        efDto.setPayeeCellphone(efDto.getCustomerCellphone());
        efDto.setPayeeCertificateNumber(efDto.getCertificateNumber());
        efDto.setPayeeName(efDto.getCustomerName());
        efDto.setState(EarnestTransferOrderStateEnum.CREATED.getCode());
        BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.EARNEST_TRANSFER_ORDER.getCode());
        if(!bizNumberOutput.isSuccess()){
            throw new BusinessException(ResultCode.DATA_ERROR, "编号生成器微服务异常");
        }
        efDto.setCode(bizNumberOutput.getData());

        efDto.setCreatorId(userTicket.getId());
        efDto.setCreator(userTicket.getRealName());
        efDto.setMarketId(userTicket.getFirmId());
        efDto.setVersion(0L);
        earnestTransferOrderService.insertSelective(efDto);
        return efDto;
    }

    @Override
    public void addEarnestRefund(RefundOrder order) {
        BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.EARNEST_REFUND_ORDER.getCode());
        if(!bizNumberOutput.isSuccess()){
            throw new BusinessException(ResultCode.DATA_ERROR, "编号生成器微服务异常");
        }
        order.setCode(bizNumberOutput.getData());
        order.setBizType(BizTypeEnum.EARNEST.getCode());
        refundOrderService.doAddHandler(order);
    }


    @Override
    public BaseOutput submitLeaseOrderCustomerAmountFrozen(Long orderId, String orderCode, Long customerId, Long earnestDeduction, Long transferDeduction, Long depositDeduction, Long marketId){
        try {
            Integer sceneType = TransactionSceneTypeEnum.FROZEN.getCode();
            BaseOutput checkOut = this.checkParams(orderId, orderCode, customerId, marketId);
            if(!checkOut.isSuccess()){
                return checkOut;
            }
            CustomerAccount ca = this.getCustomerAccountByCustomerId(customerId, marketId);
            BaseOutput caCheckOut = this.checkCustomerAccount(sceneType, ca, earnestDeduction, transferDeduction);
            if (!caCheckOut.isSuccess()){
                return caCheckOut;
            }
            this.submitChangeCustomerAmountAndDetails(sceneType, orderId, orderCode, ca, earnestDeduction, transferDeduction, depositDeduction);
            return BaseOutput.success();
        } catch (BusinessException e) {
            return BaseOutput.failure().setCode(e.getErrorCode()).setMessage(e.getErrorMsg());
        } catch (Exception e) {
            return BaseOutput.failure();
        }
    }

    @Override
    public BaseOutput withdrawLeaseOrderCustomerAmountUnFrozen(Long orderId, String orderCode, Long customerId, Long earnestDeduction, Long transferDeduction, Long depositDeduction, Long marketId) {
        try {
            Integer sceneType = TransactionSceneTypeEnum.UNFROZEN.getCode();

            BaseOutput checkOut = this.checkParams(orderId, orderCode, customerId, marketId);
            if(!checkOut.isSuccess()){
                return checkOut;
            }
            CustomerAccount ca = this.getCustomerAccountByCustomerId(customerId, marketId);
            BaseOutput caCheckOut = this.checkCustomerAccount(sceneType, ca, earnestDeduction, transferDeduction);
            if (!caCheckOut.isSuccess()){
                return caCheckOut;
            }

            this.withdrawChangeCustomerAmountAndDetails(sceneType, orderId, orderCode, ca, earnestDeduction, transferDeduction, depositDeduction);
            return BaseOutput.success("处理成功！");
        } catch (BusinessException e) {
            return BaseOutput.failure().setCode(e.getErrorCode()).setMessage(e.getErrorMsg());
        } catch (Exception e) {
            return BaseOutput.failure("处理出错！");
        }
    }

    @Override
    public BaseOutput paySuccessLeaseOrderCustomerAmountConsume(Long orderId, String orderCode, Long customerId, Long earnestDeduction, Long transferDeduction, Long depositDeduction, Long marketId) {
        try {
            Integer sceneType = TransactionSceneTypeEnum.DEDUCT_USE.getCode();
            BaseOutput checkOut = this.checkParams(orderId, orderCode, customerId, marketId);
            if(!checkOut.isSuccess()){
                return checkOut;
            }
            CustomerAccount ca = this.getCustomerAccountByCustomerId(customerId, marketId);
            BaseOutput caCheckOut = this.checkCustomerAccount(sceneType, ca, earnestDeduction, transferDeduction);
            if (!caCheckOut.isSuccess()){
                return caCheckOut;
            }
            this.payChangeCustomerAmountAndDetails(orderId, orderCode, ca, earnestDeduction, transferDeduction, depositDeduction);
            return BaseOutput.success("处理成功！");
        } catch (BusinessException e) {
            return BaseOutput.failure().setCode(e.getErrorCode()).setMessage(e.getErrorMsg());
        } catch (Exception e) {
            return BaseOutput.failure("处理出错！");
        }
    }

    private BaseOutput checkCustomerAccount(Integer sceneType, CustomerAccount customerAccount, Long earnestDeduction, Long transferDeduction){
        if (customerAccount == null){
            return BaseOutput.failure().setCode(ResultCodeConst.CUSTOMER_ACCOUNT_ERROR).setMessage("客户在该市场不存在客户余额！");
        }
        if (sceneType.equals(TransactionSceneTypeEnum.UNFROZEN.getCode())){
            if (customerAccount.getEarnestFrozenAmount() < earnestDeduction){
                return BaseOutput.failure().setCode(ResultCode.DATA_ERROR).setMessage("客户定金冻结总金额小于解冻金额！");
            }
            if (customerAccount.getTransferFrozenAmount() < transferDeduction){
                return BaseOutput.failure().setCode(ResultCode.DATA_ERROR).setMessage("客户转抵冻结总金额小于解冻金额！");
            }
        }else if (sceneType.equals(TransactionSceneTypeEnum.FROZEN.getCode())){
            if (customerAccount.getEarnestAvailableBalance() < earnestDeduction){
                return BaseOutput.failure().setCode(ResultCodeConst.EARNEST_ERROR).setMessage( "客户定金可用余额不足！");
            }
            if (customerAccount.getTransferAvailableBalance() < transferDeduction){
                return BaseOutput.failure().setCode(ResultCodeConst.TRANSFER_ERROR).setMessage("客户转抵可用余额不足！");
            }
        }else if (sceneType.equals(TransactionSceneTypeEnum.DEDUCT_USE.getCode())){
            if (customerAccount.getEarnestFrozenAmount() < earnestDeduction){
                return BaseOutput.failure().setCode(ResultCode.DATA_ERROR).setMessage("客户定金冻结总金额小于解冻金额！");
            }
            if (customerAccount.getTransferFrozenAmount() < transferDeduction){
                return BaseOutput.failure().setCode(ResultCode.DATA_ERROR).setMessage("客户转抵冻结总金额小于解冻金额！");
            }
            if (customerAccount.getEarnestBalance() < earnestDeduction){
                return BaseOutput.failure().setCode(ResultCodeConst.EARNEST_ERROR).setMessage( "客户定金余额不足！");
            }
            if (customerAccount.getTransferBalance() < transferDeduction){
                return BaseOutput.failure().setCode(ResultCodeConst.TRANSFER_ERROR).setMessage("客户转抵余额不足！");
            }
        }

        return BaseOutput.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public void submitChangeCustomerAmountAndDetails(Integer sceneType, Long orderId, String orderCode, CustomerAccount ca, Long earnestDeduction, Long transferDeduction, Long depositDeduction) {
        //写入 定金，转抵，保证金的【冻结】流水
        this.addTransactionDetails(sceneType,orderId, orderCode, ca, earnestDeduction, transferDeduction, depositDeduction);
        //客户账户金额修改
        if (earnestDeduction != null && !earnestDeduction.equals(0L)){
            ca.setEarnestAvailableBalance(ca.getEarnestAvailableBalance() - earnestDeduction);
            ca.setEarnestFrozenAmount(ca.getEarnestFrozenAmount() + earnestDeduction);
        }
        if (transferDeduction != null && !transferDeduction.equals(0L)){
            ca.setTransferAvailableBalance(ca.getTransferAvailableBalance() - transferDeduction);
            ca.setTransferFrozenAmount(ca.getTransferFrozenAmount() + transferDeduction);
        }
        //只有定金或者转抵有金额变动，才执行更新客户账户
        if ((earnestDeduction != null && !earnestDeduction.equals(0L)) || (transferDeduction != null && !transferDeduction.equals(0L))){
            this.getActualDao().updateAmountByAccountIdAndVersion(ca);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void withdrawChangeCustomerAmountAndDetails(Integer sceneType, Long orderId, String orderCode, CustomerAccount ca, Long earnestDeduction, Long transferDeduction, Long depositDeduction) {
        //写入 定金，转抵，保证金的【冻结】流水
        this.addTransactionDetails(sceneType,orderId, orderCode, ca, earnestDeduction, transferDeduction, depositDeduction);
        //客户账户金额修改
        if (earnestDeduction != null && !earnestDeduction.equals(0L)){
            ca.setEarnestAvailableBalance(ca.getEarnestAvailableBalance() + earnestDeduction);
            ca.setEarnestFrozenAmount(ca.getEarnestFrozenAmount() - earnestDeduction);
        }
        if (transferDeduction != null && !transferDeduction.equals(0L)){
            ca.setTransferAvailableBalance(ca.getTransferAvailableBalance() + transferDeduction);
            ca.setTransferFrozenAmount(ca.getTransferFrozenAmount() - transferDeduction);
        }
        //只有定金或者转抵有金额变动，才执行更新客户账户
        if ((earnestDeduction != null && !earnestDeduction.equals(0L)) || (transferDeduction != null && !transferDeduction.equals(0L))){
            int count = this.getActualDao().updateAmountByAccountIdAndVersion(ca);
            if (count != 1){
                throw new BusinessException(ResultCode.DATA_ERROR, "更新客户账户失败，客户账户多人操作！");
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void payChangeCustomerAmountAndDetails(Long orderId, String orderCode, CustomerAccount ca, Long earnestDeduction, Long transferDeduction, Long depositDeduction) {
        Integer unfrozen = TransactionSceneTypeEnum.UNFROZEN.getCode();
        Integer deductUse = TransactionSceneTypeEnum.DEDUCT_USE.getCode();

        //写入 定金，转抵，保证金的【解冻】【抵扣消费】流水
        this.addTransactionDetails(unfrozen, orderId, orderCode, ca, earnestDeduction, transferDeduction, depositDeduction);
        this.addTransactionDetails(deductUse, orderId, orderCode, ca, earnestDeduction, transferDeduction, depositDeduction);
        //客户账户余额变动
        if (earnestDeduction != null && !earnestDeduction.equals(0L)){ // 解冻 + 抵扣消费 = 冻结金额减少 + 余额减少
            ca.setEarnestBalance(ca.getEarnestBalance() - earnestDeduction);
            ca.setEarnestFrozenAmount(ca.getEarnestFrozenAmount() - earnestDeduction);
        }
        if (transferDeduction != null && !transferDeduction.equals(0L)){  // 解冻 + 抵扣消费 = 冻结金额减少 + 余额减少
            ca.setTransferBalance(ca.getTransferBalance() - transferDeduction);
            ca.setTransferFrozenAmount(ca.getTransferFrozenAmount() - transferDeduction);
        }
        //只有定金或者转抵有金额变动，才执行更新客户账户
        if ((earnestDeduction != null && !earnestDeduction.equals(0L)) || (transferDeduction != null && !transferDeduction.equals(0L))){
            this.getActualDao().updateAmountByAccountIdAndVersion(ca);
        }
    }


    private BaseOutput addTransactionDetails(Integer sceneType,Long orderId, String orderCode, CustomerAccount ca, Long earnestDeduction, Long transferDeduction, Long depositDeduction){
        Integer bizType = BizTypeEnum.BOOTH_LEASE.getCode();
        List<TransactionDetails> listDetails = new ArrayList<>();
        //写入 定金，转抵，保证金的【冻结】流水
        if (earnestDeduction != null && !earnestDeduction.equals(0L)){ // 解冻 + 抵扣消费 = 冻结金额减少 + 余额减少
            Integer itemType = TransactionItemTypeEnum.EARNEST.getCode();
            TransactionDetails earnestUnfrozenDetail = transactionDetailsService.buildByConditions(sceneType, bizType, itemType, earnestDeduction, orderId, orderCode, ca.getCustomerId(), orderCode, ca.getMarketId());
            listDetails.add(earnestUnfrozenDetail);
        }
        if (transferDeduction != null && !transferDeduction.equals(0L)){  // 解冻 + 抵扣消费 = 冻结金额减少 + 余额减少
            Integer itemType = TransactionItemTypeEnum.TRANSFER.getCode();
            TransactionDetails transferUnfrozenDetail = transactionDetailsService.buildByConditions(sceneType, bizType, itemType, transferDeduction, orderId, orderCode, ca.getCustomerId(), orderCode, ca.getMarketId());
            listDetails.add(transferUnfrozenDetail);
        }
        if (depositDeduction != null && !depositDeduction.equals(0L)){
            Integer itemType = TransactionItemTypeEnum.DEPOSIT.getCode();
            TransactionDetails depositUnfrozenDetail = transactionDetailsService.buildByConditions(sceneType, bizType, itemType, depositDeduction, orderId, orderCode, ca.getCustomerId(), orderCode, ca.getMarketId());
            listDetails.add(depositUnfrozenDetail);
        }
        if (!CollectionUtils.isEmpty(listDetails)){
            transactionDetailsService.batchInsert(listDetails );
        }
        return BaseOutput.success();
    }

    private BaseOutput checkParams(Long orderId, String orderCode, Long customerId, Long marketId){
        if (orderId == null){
            return BaseOutput.failure().setCode(ResultCode.PARAMS_ERROR).setMessage("订单ID不能为空");
        }
        if (orderCode == null){
            return BaseOutput.failure().setCode(ResultCode.PARAMS_ERROR).setMessage("订单CODE不能为空");
        }
        if (customerId == null){
            return BaseOutput.failure().setCode(ResultCode.PARAMS_ERROR).setMessage("客户ID不能为空");
        }
        if (marketId == null){
            return BaseOutput.failure().setCode(ResultCode.PARAMS_ERROR).setMessage("市场ID不能为空");
        }
        return BaseOutput.success();
    }

}

