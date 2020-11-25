package com.dili.ia.domain.dto;

import javax.persistence.Column;
/***
 * 查询保证金余额的维度
 *
 * */
public class DepositBalanceParam{

    /**
     * 客户ID
     */
    @Column(name = "`customer_id`")
    private Long customerId;

    /**
     * 保证金类型，来源数据字典
     */
    @Column(name = "`type_code`")
    private String typeCode;

    /**
     * 资产类型
     */
    @Column(name = "`assets_type`")
    private Integer assetsType;

    /**
     * 资产ID
     */
    @Column(name = "`assets_id`")
    private Long assetsId;

    /**
     * 资产名称
     */
    @Column(name = "`assets_name`")
    private String assetsName;

    /**
     * 市场Id
     */
    @Column(name = "`market_id`")
    private Long marketId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Integer getAssetsType() {
        return assetsType;
    }

    public void setAssetsType(Integer assetsType) {
        this.assetsType = assetsType;
    }

    public Long getAssetsId() {
        return assetsId;
    }

    public void setAssetsId(Long assetsId) {
        this.assetsId = assetsId;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }
}
