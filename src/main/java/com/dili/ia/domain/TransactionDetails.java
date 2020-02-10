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
 * ��ϸ��ˮ
 * This file was generated on 2020-02-10 17:43:44.
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
    @FieldDef(label="����ʱ��")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getCreateTime();

    void setCreateTime(Date createTime);

    @Column(name = "`modify_time`")
    @FieldDef(label="�޸�ʱ��")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getModifyTime();

    void setModifyTime(Date modifyTime);

    @Column(name = "`code`")
    @FieldDef(label="������ˮ��", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCode();

    void setCode(String code);

    @Column(name = "`scene_type`")
    @FieldDef(label="ת�룬ת�����ֿ����ѣ����ᣬ�ɷѣ��˿�")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getSceneType();

    void setSceneType(Integer sceneType);

    @Column(name = "`item_type`")
    @FieldDef(label="���?�֤��ת��")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getItemType();

    void setItemType(Integer itemType);

    @Column(name = "`biz_type`")
    @FieldDef(label="̯λ���ޣ��������ȵ�")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getBizType();

    void setBizType(Integer bizType);

    @Column(name = "`order_id`")
    @FieldDef(label="��������ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getOrderId();

    void setOrderId(Long orderId);

    @Column(name = "`order_code`")
    @FieldDef(label="��������ҵ�?��", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getOrderCode();

    void setOrderCode(String orderCode);

    @Column(name = "`customer_id`")
    @FieldDef(label="�������׵Ŀͻ�ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`customer_name`")
    @FieldDef(label="�������׵Ŀͻ�����", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerName();

    void setCustomerName(String customerName);

    @Column(name = "`certificate_number`")
    @FieldDef(label="�������׵Ŀͻ�֤����", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCertificateNumber();

    void setCertificateNumber(String certificateNumber);

    @Column(name = "`customer_cellphone`")
    @FieldDef(label="�������׵Ŀͻ��绰", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerCellphone();

    void setCustomerCellphone(String customerCellphone);

    @Column(name = "`amount`")
    @FieldDef(label="������Ϊ����֧��Ϊ��")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getAmount();

    void setAmount(Long amount);

    @Column(name = "`creator_id`")
    @FieldDef(label="������ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCreatorId();

    void setCreatorId(Long creatorId);

    @Column(name = "`creator`")
    @FieldDef(label="����������", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCreator();

    void setCreator(String creator);

    @Column(name = "`notes`")
    @FieldDef(label="�����¼��", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNotes();

    void setNotes(String notes);

    @Column(name = "`market_id`")
    @FieldDef(label="�г�ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getMarketId();

    void setMarketId(Long marketId);
}