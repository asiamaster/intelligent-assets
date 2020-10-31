package com.dili.ia.service;

import com.dili.ia.domain.CustomerAccount;
import com.dili.ia.domain.EarnestTransferOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.EarnestTransferDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

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
     * 客户账户 -- 定金转移
     * @param etDto 前端传入参数
     * @return
     * */
    BaseOutput<EarnestTransferOrder> earnestTransfer(EarnestTransferDto etDto);
    /**
     * 客户账户定金退款单创建 或者 修改
     * @param order
     * @return
     * */
    BaseOutput saveOrUpdateRefundOrder(RefundOrder order);
    /**
     * 根据用户信息，新增客户账户
     * @param customerId 客户ID
     * @param customerName 客户名字
     * @param customerCellphone 客户电话
     * @param certificateNumber 客户证件号码
     * @param marketId 市场ID
     * @return BaseOutput<CustomerAccount> 客户账户信息
     * */
    BaseOutput<CustomerAccount> addCustomerAccountByCustomerInfo(Long customerId, String customerName, String customerCellphone, String certificateNumber, Long marketId);

    /**
     * 摊位租赁【提交】-- 客户账户金额[冻结]及流水变动记录
     * @param orderId 订单ID
     * @param orderCode 订单编号
     * @param customerId 客户ID
     * @param earnestDeduction 定金抵扣金额
     * @param transferDeduction 转抵抵扣金额
     * @param marketId 市场ID
     * @param operaterId 操作员ID
     * @param operatorName 操作员名字
     * @return BaseOutput
     * */
    BaseOutput submitLeaseOrderCustomerAmountFrozen(Long orderId, String orderCode, Long customerId, Long earnestDeduction, Long transferDeduction, Long marketId , Long operaterId, String operatorName);

    /**
     * 摊位租赁【撤回】-- 客户账户金额[解冻]及流水变动记录
     * @param orderId 订单ID
     * @param orderCode 订单编号
     * @param customerId 客户ID
     * @param earnestDeduction 定金抵扣金额
     * @param transferDeduction 转抵抵扣金额
     * @param marketId 市场ID
     * @param operaterId 操作员ID
     * @param operatorName 操作员名字
     * @return BaseOutput
     * */
    BaseOutput withdrawLeaseOrderCustomerAmountUnFrozen(Long orderId, String orderCode, Long customerId, Long earnestDeduction, Long transferDeduction, Long marketId, Long operaterId, String operatorName);

    /**
     * 摊位租赁【缴费成功】-- 客户账户金额[解冻] [消费抵扣]及相应流水变动记录
     * @param orderId 订单ID
     * @param orderCode 订单编号
     * @param customerId 客户ID
     * @param earnestDeduction 定金抵扣金额
     * @param transferDeduction 转抵抵扣金额
     * @param marketId 市场ID
     * @param operaterId 操作员ID
     * @param operatorName 操作员名字
     * @return BaseOutput
     * */
    BaseOutput paySuccessLeaseOrderCustomerAmountConsume(Long orderId, String orderCode, Long customerId, Long earnestDeduction, Long transferDeduction, Long marketId, Long operaterId, String operatorName);
    /**
     * 客户账户 -- 冻结定金， 定金【冻结金额】加，【可用余额】减，【余额】不变
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @param amount 冻结金额
     * @return
     * */
    void frozenEarnest(Long customerId, Long marketId, Long amount);
    /**
     * 客户账户 -- 解冻定金， 定金【冻结金额】见，【可用余额】加，【余额】不变
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @param amount 冻结金额
     * @return
     * */
    void unfrozenEarnest(Long customerId, Long marketId, Long amount);
    /**
     * 客户账户 定金单付款成功-- 【定金可用余额】 和 【定金余额】增加
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @param amount 付款成功金额
     * @return
     * */
    void paySuccessEarnest(Long customerId, Long marketId, Long amount);
    /**
     * 客户账户 定金单退款成功-- 实际是解冻 和 退款扣除。定金【定金可用余额】不变 ， 【定金余额】减少，【冻结金额】减少
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @param amount 退款成功金额
     * @return
     * */
    void refundSuccessEarnest(Long customerId, Long marketId, Long amount);
    /**
     * 摊位租赁【退款转抵成功】-- 客户账户转抵余额加， 及相应流水变动记录
     * @param bizType 业务类型，取自枚举【BizTypeEnum】 的 code
     * @param sceneType 场景，取自【TransactionSceneTypeEnum】 的 code
     * @param orderId 产生转抵金额的【业务单单】订单ID
     * @param orderCode 产生转抵金额的【业务单单】订单编号
     * @param customerId 客户ID
     * @param amount 转抵金额
     * @param marketId 市场ID
     * @param operaterId 操作员ID
     * @param operatorName 操作员名字
     * @return BaseOutput
     * */
    BaseOutput rechargeTransferBalance(String bizType, Integer sceneType, Long orderId, String orderCode, Long customerId, Long amount, Long marketId, Long operaterId, String operatorName);
    /**
     * 客户账户 定金【定金可用余额】 和 【定金余额】增加, 及相应流水变动记录
     * @param bizType 业务类型，取自枚举【BizTypeEnum】 的 code
     * @param sceneType 场景，取自【TransactionSceneTypeEnum】 的 code
     * @param orderId 产生转抵金额的【业务单单】订单ID
     * @param orderCode 产生转抵金额的【业务单单】订单编号
     * @param customerId 客户ID
     * @param amount 转抵金额
     * @param marketId 市场ID
     * @param operaterId 操作员ID
     * @param operatorName 操作员名字
     * @return BaseOutput
     * @return
     * */
    BaseOutput rechargeEarnestBalance(String bizType, Integer sceneType, Long orderId, String orderCode, Long customerId, Long amount, Long marketId, Long operaterId, String operatorName);

    /**
     * 客户账户 定金【定金可用余额】 和 【定金余额】扣减, 及相应流水变动记录
     * @param bizType 业务类型，取自枚举【BizTypeEnum】 的 code
     * @param sceneType 场景，取自【TransactionSceneTypeEnum】 的 code
     * @param orderId 产生转抵金额的【业务单】订单ID
     * @param orderCode 产生转抵金额的【业务单单】订单编号
     * @param customerId 客户ID
     * @param amount 转抵金额
     * @param marketId 市场ID
     * @param operaterId 操作员ID
     * @param operatorName 操作员名字
     * @return BaseOutput
     * @return
     * */
    BaseOutput deductEarnestBalance(String bizType, Integer sceneType, Long orderId, String orderCode, Long customerId, Long amount, Long marketId, Long operaterId, String operatorName);

}