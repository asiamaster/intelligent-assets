package com.dili.ia.service;

import com.dili.ia.domain.CustomerAccount;
import com.dili.ia.domain.EarnestTransferOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.EarnestTransferDto;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
public interface CustomerAccountService extends BaseService<CustomerAccount, Long> {
    /**
     * 检查当前市场客户账户是否已存在
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return Boolean true : 客户账户已存在； false 客户账户不存在
     * */
    Boolean checkCustomerAccountExist(Long customerId, Long marketId);
    /**
     * 获取市场客户账户信息
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
    CustomerAccount getCustomerAccountByCustomerId(Long customerId, Long marketId);

    /**
     * 客户账户定金转移
     * @param earnestTransferOrder
     * @return
     * */
    void earnestTransfer(EarnestTransferOrder earnestTransferOrder);
    /**
     * 客户账户定金退款单创建
     * @param order
     * @return
     * */
    void earnestRefund(RefundOrder order);
    /**
     * 根据用户信息，新增客户账户
     * @param customerId 客户ID
     * @param customerName 客户名字
     * @param customerCellphone 客户电话
     * @param certificateNumber 客户证件号码
     * @return CustomerAccount 客户账户信息
     * */
    CustomerAccount creatCustomerAccountByCustomerInfo(Long customerId, String customerName, String customerCellphone, String certificateNumber);

    /**
     * 根据用户信息，新增客户账户
     * @param efDto EarnestTransferDto
     * @return EarnestTransferOrder 转移单
     * */
    EarnestTransferOrder createEarnestTransferOrder(EarnestTransferDto efDto);

    /**
     * 客户账户 -- 冻结--【冻结定余额】增加
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
//    void addEarnestFrozenAmount(CustomerAccount customerAccount, Long marketId, Long amount);

    /**
     * 客户账户 -- 解冻-【冻结定金余额】扣减
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
//    void subtractEarnestFrozenAmount(CustomerAccount customerAccount, Long marketId, Long amount);

    /**
     * 客户账户 -- 【定金可用余额】 和 【定金余额】扣减
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @param availableAmount 【定金可用余额】扣减金额
     * @param balanceAmount 【定金余额】 扣减金额
     * @return
     * */
    void subtractEarnestAvailableAndBalance(Long customerId, Long marketId, Long availableAmount, Long balanceAmount);
    /**
     * 客户账户 -- 【定金可用余额】 和 【定金余额】增加
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @param availableAmount 【定金可用余额】增加金额
     * @param balanceAmount 【定金余额】 增加金额
     * @return
     * */
    void addEarnestAvailableAndBalance(Long customerId, Long marketId, Long availableAmount, Long balanceAmount);
    /**
     * 客户账户 -- 【定金余额】扣减
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
//    void subtractEarnestBalance(CustomerAccount customerAccount, Long marketId, Long amount);
    /**
     * 客户账户 -- 【定金余额】增加
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
//    void addEarnestBalance(CustomerAccount customerAccount, Long marketId, Long amount);

    /**
     * 客户账户 -- 冻结--【冻结转抵余额】增加
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
//    void addTransferFrozenAmount(CustomerAccount customerAccount, Long marketId, Long amount);

    /**
     * 客户账户 -- 解冻-【冻结转抵余额】扣减
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
//    void subtractTransferFrozenAmount(CustomerAccount customerAccount, Long marketId, Long amount);

    /**
     * 客户账户 -- 【转抵可用余额】扣减
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
//    void addTransferAvailableBalance(CustomerAccount customerAccount, Long marketId, Long amount);
    /**
     * 客户账户 -- 【转抵可用余额】增加
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
//    void subtractTransferAvailableBalance(CustomerAccount customerAccount, Long marketId, Long amount);
    /**
     * 客户账户 -- 【转抵余额】扣减
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
//    void addTransferBalance(CustomerAccount customerAccount, Long marketId, Long amount);
    /**
     * 客户账户 -- 【转抵余额】增加
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
//    void subtractTransferBalance(CustomerAccount customerAccount, Long marketId, Long amount);
}