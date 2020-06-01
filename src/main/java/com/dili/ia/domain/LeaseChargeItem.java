package com.dili.ia.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 租赁收费项目
 * This file was generated on 2020-05-29 16:13:04.
 */
@Table(name = "`lease_charge_item`")
public class LeaseChargeItem extends BaseDomain {
    /**
     * id
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 业务ID
     */
    @Column(name = "`business_id`")
    private Long businessId;

    /**
     * 业务code
     */
    @Column(name = "`business_code`")
    private String businessCode;

    /**
     * 收费项ID
     */
    @Column(name = "`charge_id`")
    private Long chargeId;

    /**
     * 收费项名称
     */
    @Column(name = "`charge_name`")
    private String chargeName;

    /**
     * 金额
     */
    @Column(name = "`amount`")
    private Long amount;


    //创建时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`create_time`")
    private LocalDateTime createTime;

    //修改时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`modify_time`")
    private LocalDateTime modifyTime;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public Long getChargeId() {
        return chargeId;
    }

    public void setChargeId(Long chargeId) {
        this.chargeId = chargeId;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}