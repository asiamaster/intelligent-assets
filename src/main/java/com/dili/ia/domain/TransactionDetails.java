package com.dili.ia.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 明细流水
 * This file was generated on 2020-02-28 18:25:12.
 */
@Table(name = "`transaction_details`")
public interface TransactionDetails extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`create_time`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getCreateTime();

    void setCreateTime(Date createTime);

    @Column(name = "`modify_time`")
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getModifyTime();

    void setModifyTime(Date modifyTime);

    @Column(name = "`code`")
    @FieldDef(label="交易流水号", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCode();

    void setCode(String code);

    @Column(name = "`scene_type`")
    @FieldDef(label="场景：1-交费,2-抵扣消费,3-定金转入,4-定金转出,5-退款,6-冻结，7-解冻,8-转抵转入")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getSceneType();

    void setSceneType(Integer sceneType);

    @Column(name = "`item_type`")
    @FieldDef(label="1-定金，2-转抵，3-保证金")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getItemType();

    void setItemType(Integer itemType);

    @Column(name = "`biz_type`")
    @FieldDef(label="摊位租赁，定金管理等等")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getBizType();

    void setBizType(Integer bizType);

    @Column(name = "`order_id`")
    @FieldDef(label="发生订单ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getOrderId();

    void setOrderId(Long orderId);

    @Column(name = "`order_code`")
    @FieldDef(label="发生订单业务单编号", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getOrderCode();

    void setOrderCode(String orderCode);

    @Column(name = "`customer_id`")
    @FieldDef(label="发生交易的客户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`customer_name`")
    @FieldDef(label="发生交易的客户名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerName();

    void setCustomerName(String customerName);

    @Column(name = "`certificate_number`")
    @FieldDef(label="发生交易的客户证件号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCertificateNumber();

    void setCertificateNumber(String certificateNumber);

    @Column(name = "`customer_cellphone`")
    @FieldDef(label="发生交易的客户电话", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerCellphone();

    void setCustomerCellphone(String customerCellphone);

    @Column(name = "`amount`")
    @FieldDef(label="金额，收入为正，支付为负")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getAmount();

    void setAmount(Long amount);

    @Column(name = "`creator_id`")
    @FieldDef(label="创建人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCreatorId();

    void setCreatorId(Long creatorId);

    @Column(name = "`creator`")
    @FieldDef(label="创建人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCreator();

    void setCreator(String creator);

    @Column(name = "`notes`")
    @FieldDef(label="详情记录，", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNotes();

    void setNotes(String notes);

    @Column(name = "`market_id`")
    @FieldDef(label="市场ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getMarketId();

    void setMarketId(Long marketId);
}