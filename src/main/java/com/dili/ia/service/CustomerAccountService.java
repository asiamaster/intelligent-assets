package com.dili.ia.service;

import com.dili.ia.domain.CustomerAccount;
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
     * 客户账户 -- 冻结--【冻结定余额】增加
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
    void addEarnestFrozenAmount(CustomerAccount customerAccount, Long marketId, Long amount);

    /**
     * 客户账户 -- 解冻-【冻结定金余额】扣减
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
    void subtractEarnestFrozenAmount(CustomerAccount customerAccount, Long marketId, Long amount);

    /**
     * 客户账户 -- 【定金可用余额】扣减
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
    void subtractEarnestAvailableBalance(CustomerAccount customerAccount, Long marketId, Long amount);
    /**
     * 客户账户 -- 【定金可用余额】增加
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
    void addEarnestAvailableBalance(CustomerAccount customerAccount, Long marketId, Long amount);
    /**
     * 客户账户 -- 【定金余额】扣减
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
    void subtractEarnestBalance(CustomerAccount customerAccount, Long marketId, Long amount);
    /**
     * 客户账户 -- 【定金余额】增加
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
    void addEarnestBalance(CustomerAccount customerAccount, Long marketId, Long amount);

    /**
     * 客户账户 -- 冻结--【冻结转抵余额】增加
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
    void addTransferFrozenAmount(CustomerAccount customerAccount, Long marketId, Long amount);

    /**
     * 客户账户 -- 解冻-【冻结转抵余额】扣减
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
    void subtractTransferFrozenAmount(CustomerAccount customerAccount, Long marketId, Long amount);

    /**
     * 客户账户 -- 【转抵可用余额】扣减
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
    void addTransferAvailableBalance(CustomerAccount customerAccount, Long marketId, Long amount);
    /**
     * 客户账户 -- 【转抵可用余额】增加
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
    void subtractTransferAvailableBalance(CustomerAccount customerAccount, Long marketId, Long amount);
    /**
     * 客户账户 -- 【转抵余额】扣减
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
    void addTransferBalance(CustomerAccount customerAccount, Long marketId, Long amount);
    /**
     * 客户账户 -- 【转抵余额】增加
     * @param customerAccount 客户
     * @param marketId 市场ID
     * @return CustomerAccount 客户账户信息
     * */
    void subtractTransferBalance(CustomerAccount customerAccount, Long marketId, Long amount);
}