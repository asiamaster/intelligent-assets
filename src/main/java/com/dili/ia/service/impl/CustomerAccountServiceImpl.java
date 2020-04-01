package com.dili.ia.service.impl;

import com.dili.ia.domain.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
@Service
public class CustomerAccountServiceImpl extends BaseServiceImpl<CustomerAccount, Long> implements CustomerAccountService {
    private final static Logger LOG = LoggerFactory.getLogger(CustomerAccountServiceImpl.class);

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
        List<CustomerAccount> list = this.listByExample(customerAccount);
        if (CollectionUtils.isEmpty(list) || list.size() != 1){
            LOG.info("当前【客户账户】不存在，或者一个客户同一市场存在多个【客户账户】！customerId={}，marketId={}", customerId, marketId);
            return null;
        }
       return list.get(0);
    }

    @Override
    public void frozenEarnest(Long customerId, Long marketId, Long amount) {
        CustomerAccount customerAccount = this.getCustomerAccountByCustomerId(customerId, marketId);
        if (null == customerAccount){
            LOG.info("客户账户退款申请，客户账户【{}】在市场【{}】不存在！", customerId, marketId);
            throw new BusinessException(ResultCode.DATA_ERROR,"客户账户不存在！");
        }
        if (customerAccount.getEarnestAvailableBalance() < amount){
            throw new BusinessException(ResultCode.DATA_ERROR,"客户可用余额不足！");
        }
        customerAccount.setEarnestAvailableBalance(customerAccount.getEarnestAvailableBalance() - amount);
        customerAccount.setEarnestFrozenAmount(customerAccount.getEarnestFrozenAmount() + amount);

        if (this.updateSelective(customerAccount) == 0){
            LOG.info("定金冻结失败,乐观锁生效【客户名称：{}】 【客户账户ID:{}】", customerAccount.getCustomerName(), customerAccount.getId());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作客户账户，请重试！");
        }
    }

    @Override
    public void unfrozenEarnest(Long customerId, Long marketId, Long amount) {
        CustomerAccount customerAccount = this.getCustomerAccountByCustomerId(customerId, marketId);
        if (null == customerAccount){
            LOG.info("客户账户退款申请，客户账户【{}】在市场【{}】不存在！", customerId, marketId);
            throw new BusinessException(ResultCode.DATA_ERROR,"客户账户不存在！");
        }
        if (customerAccount.getEarnestFrozenAmount() < amount){
            throw new BusinessException(ResultCode.DATA_ERROR,"客户冻结金额小于解冻金额！");
        }
        customerAccount.setEarnestAvailableBalance(customerAccount.getEarnestAvailableBalance() + amount);
        customerAccount.setEarnestFrozenAmount(customerAccount.getEarnestFrozenAmount() - amount);

        if (this.updateSelective(customerAccount) == 0){
            LOG.info("定金解冻失败,乐观锁生效【客户名称：{}】 【客户账户ID:{}】", customerAccount.getCustomerName(), customerAccount.getId());
            throw new BusinessException(ResultCode.DATA_ERROR,"多人操作客户账户，请重试！");
        }
    }

    @Override
    public void paySuccessEarnest(Long customerId, Long marketId, Long amount) {
        CustomerAccount customerAccount = this.getCustomerAccountByCustomerId(customerId, marketId);
        if (null == customerAccount){
            LOG.info("客户账户退款申请，客户账户【{}】在市场【{}】不存在！", customerId, marketId);
            throw new BusinessException(ResultCode.DATA_ERROR,"客户账户不存在！");
        }
        customerAccount.setEarnestAvailableBalance(customerAccount.getEarnestAvailableBalance() + amount);
        customerAccount.setEarnestBalance(customerAccount.getEarnestBalance() + amount);

        if (this.updateSelective(customerAccount) == 0){
            LOG.info("定金付款成功回调修改客户账户失败,乐观锁生效【客户名称：{}】 【客户账户ID:{}】", customerAccount.getCustomerName(), customerAccount.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
    }
    @Override
    public void refundSuccessEarnest(Long customerId, Long marketId, Long amount) {
        CustomerAccount customerAccount = this.getCustomerAccountByCustomerId(customerId, marketId);
        if (null == customerAccount){
            LOG.info("客户账户退款申请，客户账户【{}】在市场【{}】不存在！", customerId, marketId);
            throw new BusinessException(ResultCode.DATA_ERROR,"客户账户不存在！");
        }
        if (customerAccount.getEarnestBalance() < amount){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户余额不足！");
        }
        if (customerAccount.getEarnestFrozenAmount() < amount){
            throw new BusinessException(ResultCode.DATA_ERROR, "退款成功回调出错，客户冻结金额小于解冻金额！");
        }
        customerAccount.setEarnestBalance(customerAccount.getEarnestBalance() - amount);
        customerAccount.setEarnestFrozenAmount(customerAccount.getEarnestFrozenAmount() - amount);

        if (this.updateSelective(customerAccount) == 0){
            LOG.info("定金退款成功回调修改客户账户失败,乐观锁生效【客户名称：{}】 【客户账户ID:{}】", customerAccount.getCustomerName(), customerAccount.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作客户账户，请重试！");
        }
    }

    @Override
    public BaseOutput<CustomerAccount> addCustomerAccountByCustomerInfo(Long customerId, String customerName, String customerCellphone, String certificateNumber){
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        BaseOutput<Customer> out= customerRpc.get(customerId, userTicket.getFirmId());
        if(!out.isSuccess()){
            LOG.info("客户微服务调用返回失败！【customerId={}; marketId={}】,{}", customerId, userTicket.getFirmId(), out.getMessage());
            return BaseOutput.failure(out.getMessage());
        }
        Customer customer = out.getData();
        if (null == customer){
            LOG.info("客户不存在！【customerId={}; marketId={}】", customerId,userTicket.getFirmId());
            return BaseOutput.failure("客户不存在！");
        }

        CustomerAccount customerAccount = DTOUtils.newDTO(CustomerAccount.class);
        customerAccount.setMarketId(userTicket.getFirmId());
        customerAccount.setCustomerId(customerId);
        customerAccount.setCustomerCellphone(customer.getContactsPhone());
        customerAccount.setCertificateNumber(customer.getCertificateNumber());
        customerAccount.setCustomerName(customer.getName());
        customerAccount.setEarnestBalance(0L);
        customerAccount.setEarnestAvailableBalance(0L);
        customerAccount.setEarnestFrozenAmount(0L);
        customerAccount.setTransferAvailableBalance(0L);
        customerAccount.setTransferBalance(0L);
        customerAccount.setTransferFrozenAmount(0L);
        customerAccount.setVersion(0L);
        this.insertSelective(customerAccount);
        return BaseOutput.success().setData(customerAccount);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput<EarnestTransferOrder> earnestTransfer(EarnestTransferOrder order, Long payerAccountVersion) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (null == userTicket){
            throw new BusinessException(ResultCode.NOT_AUTH_ERROR, "未登录");
        }
        //转出方
        CustomerAccount payerCustomerAccount = this.get(order.getPayerCustomerAccountId());
        if (payerCustomerAccount.getEarnestBalance() < order.getAmount()){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户余额不足！");
        }
        if (payerCustomerAccount.getEarnestAvailableBalance() < order.getAmount()){
            throw new BusinessException(ResultCode.DATA_ERROR, "客户可用余额不足！");
        }
        payerCustomerAccount.setEarnestBalance(payerCustomerAccount.getEarnestBalance() - order.getAmount());
        payerCustomerAccount.setEarnestAvailableBalance(payerCustomerAccount.getEarnestAvailableBalance() - order.getAmount());
        //乐观锁控制，防止重复点击转移按钮发生多次转移
        payerCustomerAccount.setVersion(payerAccountVersion);
        if (this.updateSelective(payerCustomerAccount) == 0){
            LOG.info("定金转移修改付款人客户账户失败,乐观锁生效【客户名称：{}】 【客户账户ID:{}】", payerCustomerAccount.getCustomerName(), payerCustomerAccount.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "客户账户正在被多人操作，稍后再试");
        }
        //转入方（收款人）
        CustomerAccount payeeCustomerAccount = this.get(order.getPayeeCustomerAccountId());
        payeeCustomerAccount.setEarnestBalance(payeeCustomerAccount.getEarnestBalance() + order.getAmount());
        payeeCustomerAccount.setEarnestAvailableBalance(payeeCustomerAccount.getEarnestAvailableBalance() + order.getAmount());
        if (this.updateSelective(payeeCustomerAccount) == 0){
            LOG.info("定金转移修改收款人客户账户失败,乐观锁生效【客户名称：{}】 【客户账户ID:{}】", payeeCustomerAccount.getCustomerName(), payeeCustomerAccount.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "客户账户正在被多人操作，稍后再试");
        }
        String transferReason = order.getTransferReason()==null ? "": "；转移原因：" + order.getTransferReason();
        String notesPayer = "转出到：" + order.getPayeeName() + transferReason;
        String notesPayee = "来源：" + order.getPayerName() + transferReason;
        //记录定金转出转入流水
        TransactionDetails tdIn = transactionDetailsService.buildByConditions(TransactionSceneTypeEnum.EARNEST_IN.getCode(), BizTypeEnum.EARNEST.getCode(), TransactionItemTypeEnum.EARNEST.getCode(), order.getAmount(), order.getId(), order.getCode(), order.getPayeeId(), notesPayee, order.getMarketId(), userTicket.getId(), userTicket.getRealName());
        TransactionDetails tdOut = transactionDetailsService.buildByConditions(TransactionSceneTypeEnum.EARNEST_OUT.getCode(), BizTypeEnum.EARNEST.getCode(), TransactionItemTypeEnum.EARNEST.getCode(), order.getAmount(), order.getId(), order.getCode(), order.getPayerId(), notesPayer, order.getMarketId(), userTicket.getId(), userTicket.getRealName());
        transactionDetailsService.insertSelective(tdIn);
        transactionDetailsService.insertSelective(tdOut);
        //回写转入转出流水号
        order.setState(EarnestTransferOrderStateEnum.TRANSFERED.getCode());
        order.setPayerTransactionDetailsCode(tdOut.getCode());
        order.setPayeeTransactionCode(tdIn.getCode());
        order.setTransferTime(new Date());
        if (earnestTransferOrderService.updateSelective(order) == 0){
            LOG.info("定金转移回写转移单转入转出流水失败,乐观锁生效【转移单ID：{}】", order.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "转移单被多人操作，稍后再试");
        }

        return BaseOutput.success().setData(order);
    }

    @Override
    public BaseOutput<EarnestTransferOrder> addEarnestTransferOrder(EarnestTransferDto efDto){
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        CustomerAccount customerAccount = this.get(efDto.getPayerCustomerAccountId());
        if (customerAccount.getEarnestAvailableBalance() < efDto.getAmount()){
            return BaseOutput.failure("转移金额不能大于可用余额！");
        }

        CustomerAccount payeeCustomerAccount = this.getCustomerAccountByCustomerId(efDto.getCustomerId(), userTicket.getFirmId());
        if (null == customerAccount){
            LOG.info("客户账户退款申请，客户账户【{}】在市场【{}】不存在！", efDto.getCustomerId(), userTicket.getFirmId());
            return BaseOutput.failure("客户账户不存在！");
        }
        //保存定金转移单
        efDto.setPayeeCustomerAccountId(payeeCustomerAccount.getId());
        efDto.setPayeeId(efDto.getCustomerId());
        efDto.setPayeeCellphone(efDto.getCustomerCellphone());
        efDto.setPayeeCertificateNumber(efDto.getCertificateNumber());
        efDto.setPayeeName(efDto.getCustomerName());
        efDto.setState(EarnestTransferOrderStateEnum.CREATED.getCode());
        BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.EARNEST_TRANSFER_ORDER.getCode());
        if(!bizNumberOutput.isSuccess()){
            LOG.info("编号生成器返回失败，{}", bizNumberOutput.getMessage());
            return BaseOutput.failure("编号生成器微服务异常");
        }
        efDto.setCode(userTicket.getFirmCode().toUpperCase() + bizNumberOutput.getData());

        efDto.setCreatorId(userTicket.getId());
        efDto.setCreator(userTicket.getRealName());
        efDto.setMarketId(userTicket.getFirmId());
        efDto.setVersion(0L);
        earnestTransferOrderService.insertSelective(efDto);
        return BaseOutput.success().setData(efDto);
    }

    @Override
    public BaseOutput addEarnestRefund(RefundOrder order) {
        BaseOutput<String> bizNumberOutput = uidFeignRpc.bizNumber(BizNumberTypeEnum.EARNEST_REFUND_ORDER.getCode());
        if(!bizNumberOutput.isSuccess()){
            LOG.info("编号生成器返回失败，{}", bizNumberOutput.getMessage());
           return BaseOutput.failure("编号生成器微服务异常");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (null == userTicket){
            return BaseOutput.failure("未登录！");
        }
        CustomerAccount customerAccount = this.getCustomerAccountByCustomerId(order.getCustomerId(), userTicket.getFirmId());
        if (null == customerAccount){
            LOG.info("客户账户退款申请，客户账户【{}】在市场【{}:{}】不存在！", order.getCustomerId(), userTicket.getFirmId(), userTicket.getFirmCode());
            return BaseOutput.failure("客户账户不存在！");
        }
        if (customerAccount.getEarnestAvailableBalance() < order.getPayeeAmount()){
            return BaseOutput.failure("退款金额不能大于可用余额！");
        }
        order.setCode(userTicket.getFirmCode().toUpperCase() + bizNumberOutput.getData());
        order.setBizType(BizTypeEnum.EARNEST.getCode());
        order.setTotalRefundAmount(order.getPayeeAmount());
        //定金退款给本人，收款人为本人
        order.setPayeeId(order.getCustomerId());
        order.setPayee(order.getCustomerName());
        return refundOrderService.doAddHandler(order);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput submitLeaseOrderCustomerAmountFrozen(Long orderId, String orderCode, Long customerId, Long earnestDeduction, Long transferDeduction, Long depositDeduction, Long marketId, Long operaterId, String operatorName){
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
            this.submitChangeCustomerAmountAndDetails(sceneType, orderId, orderCode, ca, customerId, earnestDeduction, transferDeduction, depositDeduction, marketId, operaterId, operatorName);
            return BaseOutput.success();
        } catch (BusinessException e) {
            return BaseOutput.failure().setCode(e.getErrorCode()).setMessage(e.getErrorMsg());
        } catch (Exception e) {
            return BaseOutput.failure();
        }
    }

    @Override
    public BaseOutput withdrawLeaseOrderCustomerAmountUnFrozen(Long orderId, String orderCode, Long customerId, Long earnestDeduction, Long transferDeduction, Long depositDeduction, Long marketId, Long operaterId, String operatorName) {
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

            this.withdrawChangeCustomerAmountAndDetails(sceneType, orderId, orderCode, ca,customerId, earnestDeduction, transferDeduction, depositDeduction, marketId, operaterId, operatorName);
            return BaseOutput.success("处理成功！");
        } catch (BusinessException e) {
            return BaseOutput.failure().setCode(e.getErrorCode()).setMessage(e.getErrorMsg());
        } catch (Exception e) {
            return BaseOutput.failure("处理出错！");
        }
    }

    @Override
    public BaseOutput paySuccessLeaseOrderCustomerAmountConsume(Long orderId, String orderCode, Long customerId, Long earnestDeduction, Long transferDeduction, Long depositDeduction, Long marketId, Long operaterId, String operatorName) {
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
            this.payChangeCustomerAmountAndDetails(orderId, orderCode, ca, customerId, earnestDeduction, transferDeduction, depositDeduction, marketId, operaterId, operatorName);
            return BaseOutput.success("处理成功！");
        } catch (BusinessException e) {
            return BaseOutput.failure().setCode(e.getErrorCode()).setMessage(e.getErrorMsg());
        } catch (Exception e) {
            return BaseOutput.failure("处理出错！");
        }
    }

    private BaseOutput checkCustomerAccount(Integer sceneType, CustomerAccount customerAccount, Long earnestDeduction, Long transferDeduction){
        //如果客户账户不存在，并且 定金 和 转抵的抵扣都为0，即未使用，则验证返回成功。
        if (null == customerAccount && (earnestDeduction.equals(0L) || null == earnestDeduction) && (transferDeduction.equals(0L) || null == transferDeduction)){
            return BaseOutput.success();
        }
        if (customerAccount == null){
            return BaseOutput.failure().setCode(ResultCodeConst.CUSTOMER_ACCOUNT_ERROR).setMessage("客户在该市场不存在客户余额！");
        }
        if (sceneType.equals(TransactionSceneTypeEnum.UNFROZEN.getCode())){
            if (null != earnestDeduction && customerAccount.getEarnestFrozenAmount() < earnestDeduction){
                return BaseOutput.failure().setCode(ResultCode.DATA_ERROR).setMessage("客户定金冻结总金额小于解冻金额！");
            }
            if (null != transferDeduction && customerAccount.getTransferFrozenAmount() < transferDeduction){
                return BaseOutput.failure().setCode(ResultCode.DATA_ERROR).setMessage("客户转抵冻结总金额小于解冻金额！");
            }
        }else if (sceneType.equals(TransactionSceneTypeEnum.FROZEN.getCode())){
            if (null != earnestDeduction && customerAccount.getEarnestAvailableBalance() < earnestDeduction){
                return BaseOutput.failure().setCode(ResultCodeConst.EARNEST_ERROR).setMessage( "客户定金可用余额不足！");
            }
            if (null != transferDeduction && customerAccount.getTransferAvailableBalance() < transferDeduction){
                return BaseOutput.failure().setCode(ResultCodeConst.TRANSFER_ERROR).setMessage("客户转抵可用余额不足！");
            }
        }else if (sceneType.equals(TransactionSceneTypeEnum.DEDUCT_USE.getCode())){
            if (null != earnestDeduction && customerAccount.getEarnestFrozenAmount() < earnestDeduction){
                return BaseOutput.failure().setCode(ResultCode.DATA_ERROR).setMessage("客户定金冻结总金额小于解冻金额！");
            }
            if (null != transferDeduction && customerAccount.getTransferFrozenAmount() < transferDeduction){
                return BaseOutput.failure().setCode(ResultCode.DATA_ERROR).setMessage("客户转抵冻结总金额小于解冻金额！");
            }
            if (null != earnestDeduction && customerAccount.getEarnestBalance() < earnestDeduction){
                return BaseOutput.failure().setCode(ResultCodeConst.EARNEST_ERROR).setMessage( "客户定金余额不足！");
            }
            if (null != transferDeduction && customerAccount.getTransferBalance() < transferDeduction){
                return BaseOutput.failure().setCode(ResultCodeConst.TRANSFER_ERROR).setMessage("客户转抵余额不足！");
            }
        }

        return BaseOutput.success();
    }

    @Transactional(rollbackFor = Exception.class)
    //租赁单提交调用接口，另起事务使其不影响原有事务
    public void submitChangeCustomerAmountAndDetails(Integer sceneType, Long orderId, String orderCode, CustomerAccount ca,Long customerId, Long earnestDeduction, Long transferDeduction, Long depositDeduction, Long marketId,Long operaterId,String operatorName) {
        //写入 定金，转抵，保证金的【冻结】流水
        this.addTransactionDetails(sceneType,orderId, orderCode, customerId, earnestDeduction, transferDeduction, depositDeduction, marketId, operaterId, operatorName );
        if (null == ca){ //如果【客户账户】不存在，就不用修改客户账户金额信息
            return;
        }
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
            if(this.updateSelective(ca) == 0){
                LOG.info("摊位租赁提交调用接口异常，冻结修改客户账户金额失败,乐观锁生效【客户名称：{}】 【客户账户ID:{}】", ca.getCustomerName(), ca.getId());
                throw new BusinessException(ResultCode.DATA_ERROR, "多人操作客户账户，请重试");
            }

        }
    }

    //租赁单撤回调用接口，另起事务使其不影响原有事务
    @Transactional(rollbackFor = Exception.class)
    public void withdrawChangeCustomerAmountAndDetails(Integer sceneType, Long orderId, String orderCode, CustomerAccount ca,Long customerId, Long earnestDeduction, Long transferDeduction, Long depositDeduction, Long marketId,Long operaterId,String operatorName) {
        //写入 定金，转抵，保证金的【冻结】流水
        this.addTransactionDetails(sceneType,orderId, orderCode, customerId, earnestDeduction, transferDeduction, depositDeduction, marketId, operaterId, operatorName );
        if (null == ca){ //如果【客户账户】不存在，就不用修改客户账户金额信息
            return;
        }
        //客户账户金额修改 -- 客户检查已判断金额合法性
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
            if (this.updateSelective(ca) == 0){
                LOG.info("摊位租赁撤回调用接口异常，解冻修改客户账户金额失败,乐观锁生效【客户名称：{}】 【客户账户ID:{}】", ca.getCustomerName(), ca.getId());
                throw new BusinessException(ResultCode.DATA_ERROR, "更新客户账户失败，客户账户多人操作！");
            }
        }
    }

    //租赁单提交成功调用接口
    @Transactional(rollbackFor = Exception.class)
    public void payChangeCustomerAmountAndDetails(Long orderId, String orderCode, CustomerAccount ca, Long customerId, Long earnestDeduction, Long transferDeduction, Long depositDeduction, Long marketId,Long operaterId,String operatorName) {
        Integer unfrozen = TransactionSceneTypeEnum.UNFROZEN.getCode();
        Integer deductUse = TransactionSceneTypeEnum.DEDUCT_USE.getCode();

        //写入 定金，转抵，保证金的【解冻】【抵扣消费】流水
        this.addTransactionDetails(unfrozen, orderId, orderCode, customerId, earnestDeduction, transferDeduction, depositDeduction, marketId,operaterId, operatorName);
        this.addTransactionDetails(deductUse, orderId, orderCode, customerId, earnestDeduction, transferDeduction, depositDeduction, marketId,operaterId, operatorName);
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
            if(this.updateSelective(ca) == 0){
                LOG.info("摊位租赁付款成功回调调用接口异常，解冻，消费修改客户账户金额失败,乐观锁生效【客户名称：{}】 【客户账户ID:{}】", ca.getCustomerName(), ca.getId());
                throw new BusinessException(ResultCode.DATA_ERROR, "更新客户账户失败，客户账户多人操作！");
            }
        }
    }

    //租赁单退款调用接口--充值转抵金，另起事务使其不影响原有事务
    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput leaseOrderRechargTransfer(Long orderId, String orderCode, Long customerId, Long amount, Long marketId, Long operaterId, String operatorName){
        if (null == amount || amount < 0){
            return BaseOutput.failure("转抵充值金额不合法！amount=" + amount).setCode(ResultCode.DATA_ERROR);
        }
        if (amount.equals(0L)){
            return BaseOutput.success();
        }
        BaseOutput check = this.checkParams(orderId, orderCode, customerId, marketId);
        if (!check.isSuccess()){
            return check;
        }
        try {
            this.rechargeTransfer(customerId, amount, marketId);
            Integer sceneType = TransactionSceneTypeEnum.TRANSFER_IN.getCode();
            Integer bizType = BizTypeEnum.BOOTH_LEASE.getCode();
            Integer itemType = TransactionItemTypeEnum.TRANSFER.getCode();
            TransactionDetails detail = transactionDetailsService.buildByConditions(sceneType, bizType, itemType, amount, orderId, orderCode, customerId, orderCode, marketId, operaterId, operatorName);
            transactionDetailsService.insertSelective(detail);
            return BaseOutput.success("处理成功！");
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg()).setCode(e.getErrorCode());
        } catch (Exception e) {
            return BaseOutput.failure("处理出错！");
        }

    }

    public void rechargeTransfer(Long customerId, Long amount, Long marketId){
        CustomerAccount ca = this.getCustomerAccountByCustomerId(customerId, marketId);
        //判断转入方客户账户是否存在,不存在先创建客户账户
        if (null == ca){
            BaseOutput<Customer> out= customerRpc.get(customerId, marketId);
            if(!out.isSuccess()){
                LOG.info("客户微服务异常！【customerId={}; marketId={}】{}", customerId, marketId, out.getMessage());
                throw new BusinessException(ResultCode.DATA_ERROR, out.getMessage());
            }
            Customer customer = out.getData();
            if (null == customer){
                LOG.info("客户不存在！【customerId={}; marketId={}】", customerId, marketId);
                throw new BusinessException(ResultCode.DATA_ERROR, "客户不存在！");
            }

            CustomerAccount customerAccount = DTOUtils.newInstance(CustomerAccount.class);
            customerAccount.setMarketId(marketId);
            customerAccount.setCustomerId(customerId);
            customerAccount.setCustomerCellphone(customer.getContactsPhone());
            customerAccount.setCertificateNumber(customer.getCertificateNumber());
            customerAccount.setCustomerName(customer.getName());
            customerAccount.setEarnestBalance(0L);
            customerAccount.setEarnestAvailableBalance(0L);
            customerAccount.setEarnestFrozenAmount(0L);
            customerAccount.setTransferAvailableBalance(null == amount?0L:amount);
            customerAccount.setTransferBalance(null == amount?0L:amount);
            customerAccount.setTransferFrozenAmount(0L);
            customerAccount.setVersion(0L);
            this.insertSelective(customerAccount);
        }else {
            ca.setTransferAvailableBalance(ca.getTransferAvailableBalance() + amount);
            ca.setTransferBalance(ca.getTransferBalance() + amount);
            if(this.updateSelective(ca) == 0){
                LOG.info("摊位租赁转抵充值调用接口异常，转抵金额充值修改客户账户金额失败,乐观锁生效【客户名称：{}】 【客户账户ID:{}】", ca.getCustomerName(), ca.getId());
                throw new BusinessException(ResultCode.DATA_ERROR, "客户不存在！");
            }
        }
    }

    @Transactional
    public void addTransactionDetails(Integer sceneType,Long orderId, String orderCode, Long customerId, Long earnestDeduction, Long transferDeduction, Long depositDeduction, Long marketId, Long operaterId, String operatorName){
        Integer bizType = BizTypeEnum.BOOTH_LEASE.getCode();
        //写入 定金，转抵，保证金对应 sceneType 的流水 --- 抵扣项为 null 或者 0 元 不写入流水记录
        if (earnestDeduction != null && !earnestDeduction.equals(0L)){ //定金流水
            Integer itemType = TransactionItemTypeEnum.EARNEST.getCode();
            TransactionDetails earnestDetail = transactionDetailsService.buildByConditions(sceneType, bizType, itemType, earnestDeduction, orderId, orderCode, customerId, orderCode, marketId, operaterId, operatorName);
            transactionDetailsService.insertSelective(earnestDetail);
        }
        if (transferDeduction != null && !transferDeduction.equals(0L)){//转抵流水
            Integer itemType = TransactionItemTypeEnum.TRANSFER.getCode();
            TransactionDetails transferDetail = transactionDetailsService.buildByConditions(sceneType, bizType, itemType, transferDeduction, orderId, orderCode, customerId, orderCode, marketId, operaterId, operatorName);
            transactionDetailsService.insertSelective(transferDetail);
        }
        if (depositDeduction != null && !depositDeduction.equals(0L)){//保证金流水
            Integer itemType = TransactionItemTypeEnum.DEPOSIT.getCode();
            TransactionDetails depositDetail = transactionDetailsService.buildByConditions(sceneType, bizType, itemType, depositDeduction, orderId, orderCode, customerId, orderCode, marketId, operaterId, operatorName);
            transactionDetailsService.insertSelective(depositDetail);
        }
        return;
    }

    private BaseOutput checkParams(Long orderId, String orderCode, Long customerId, Long marketId){
        if (orderId == null){
            return BaseOutput.failure("订单ID不能为空").setCode(ResultCode.PARAMS_ERROR);
        }
        if (orderCode == null){
            return BaseOutput.failure("订单CODE不能为空").setCode(ResultCode.PARAMS_ERROR);
        }
        if (customerId == null){
            return BaseOutput.failure("客户ID不能为空").setCode(ResultCode.PARAMS_ERROR);
        }
        if (marketId == null){
            return BaseOutput.failure("市场ID不能为空").setCode(ResultCode.PARAMS_ERROR);
        }
        return BaseOutput.success();
    }

}

