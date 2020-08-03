package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 开票记录
 * This file was generated on 2020-07-30 11:20:01.
 */
@Table(name = "`invoice_record`")
public class InvoiceRecord extends BaseDomain {
    /**
     * id
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 业务号
     */
    @Column(name = "`business_key`")
    private String businessKey;

    /**
     * 开票类型, 1:普票， 2: 专票
     */
    @Column(name = "`type`")
    private Byte type;

    /**
     * 开票主体
     */
    @Column(name = "`target`")
    private String target;

    /**
     * 开票金额
     */
    @Column(name = "`amount`")
    private String amount;

    /**
     * 开票日期
     */
    @Column(name = "`invoice_date`")
    private Date invoiceDate;

    /**
     * 开票人
     */
    @Column(name = "`creator_id`")
    private Long creatorId;

    /**
     * 开票人名称
     */
    @Column(name = "`creator`")
    private String creator;

    /**
     * 备注
     */
    @Column(name = "`notes`")
    private String notes;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    private Date modifyTime;

    /**
     * 市场id
     */
    @Column(name = "`firm_id`")
    private Long firmId;

    /**
     * 获取id
     *
     * @return id - id
     */
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取业务号
     *
     * @return business_key - 业务号
     */
    @FieldDef(label="业务号", maxLength = 64)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getBusinessKey() {
        return businessKey;
    }

    /**
     * 设置业务号
     *
     * @param businessKey 业务号
     */
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    /**
     * 获取开票类型, 1:普票， 2: 专票
     *
     * @return type - 开票类型, 1:普票， 2: 专票
     */
    @FieldDef(label="开票类型, 1:普票， 2: 专票")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Byte getType() {
        return type;
    }

    /**
     * 设置开票类型, 1:普票， 2: 专票
     *
     * @param type 开票类型, 1:普票， 2: 专票
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 获取开票主体
     *
     * @return target - 开票主体
     */
    @FieldDef(label="开票主体", maxLength = 64)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getTarget() {
        return target;
    }

    /**
     * 设置开票主体
     *
     * @param target 开票主体
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * 获取开票金额
     *
     * @return amount - 开票金额
     */
    @FieldDef(label="开票金额", maxLength = 64)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getAmount() {
        return amount;
    }

    /**
     * 设置开票金额
     *
     * @param amount 开票金额
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * 获取开票日期
     *
     * @return invoice_date - 开票日期
     */
    @FieldDef(label="开票日期")
    @EditMode(editor = FieldEditor.Date, required = false)
    public Date getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * 设置开票日期
     *
     * @param invoiceDate 开票日期
     */
    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    /**
     * 获取开票人
     *
     * @return creator_id - 开票人
     */
    @FieldDef(label="开票人")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * 设置开票人
     *
     * @param creatorId 开票人
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * 获取开票人名称
     *
     * @return creator - 开票人名称
     */
    @FieldDef(label="开票人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCreator() {
        return creator;
    }

    /**
     * 设置开票人名称
     *
     * @param creator 开票人名称
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取备注
     *
     * @return notes - 备注
     */
    @FieldDef(label="备注", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getNotes() {
        return notes;
    }

    /**
     * 设置备注
     *
     * @param notes 备注
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取市场id
     *
     * @return firm_id - 市场id
     */
    @FieldDef(label="市场id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getFirmId() {
        return firmId;
    }

    /**
     * 设置市场id
     *
     * @param firmId 市场id
     */
    public void setFirmId(Long firmId) {
        this.firmId = firmId;
    }
}