package com.dili.ia.domain.dto;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-11-02 10:42
 */
public class CustomerAccountParam {

     // 业务类型，取自枚举【BizTypeEnum】 的 code
    private String bizType;

    // 用于记录客户账户流水 --- 场景，取自【TransactionSceneTypeEnum】 的 code
    private Integer sceneType;

    // 用于记录客户账户流水 --- 造成客户账户变化的 【业务单】订单ID
    private Long orderId;

    // 用于记录客户账户流水 --- 造成客户账户变化的 【业务单】订单编号
    private String orderCode;

     //customerId 客户ID
    private Long customerId;

     // 造成客户账户变化的  amount 金额
    private Long amount;

    // marketId 市场ID
    private Long marketId;

     // operaterId 操作员ID
    private Long operaterId;

     // operatorName 操作员名字
     private String operatorName;

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public Integer getSceneType() {
        return sceneType;
    }

    public void setSceneType(Integer sceneType) {
        this.sceneType = sceneType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public Long getOperaterId() {
        return operaterId;
    }

    public void setOperaterId(Long operaterId) {
        this.operaterId = operaterId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}
