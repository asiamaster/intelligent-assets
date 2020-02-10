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
 * ����ת������ҵ��
 * This file was generated on 2020-02-10 17:43:44.
 */
@Table(name = "`earnest_transfer_order`")
public interface EarnestTransferOrder extends IBaseDomain {
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

    @Column(name = "`state`")
    @FieldDef(label="״̬")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getState();

    void setState(Integer state);

    @Column(name = "`amount`")
    @FieldDef(label="ת�ƽ��")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getAmount();

    void setAmount(Long amount);

    @Column(name = "`payer_id`")
    @FieldDef(label="ת�����ͻ�ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getPayerId();

    void setPayerId(Long payerId);

    @Column(name = "`payer_certificate_number`")
    @FieldDef(label="ת�����ͻ�֤������", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPayerCertificateNumber();

    void setPayerCertificateNumber(String payerCertificateNumber);

    @Column(name = "`payer_name`")
    @FieldDef(label="ת�����ͻ�����", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPayerName();

    void setPayerName(String payerName);

    @Column(name = "`payer_cellphone`")
    @FieldDef(label="ת�����ͻ��绰", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPayerCellphone();

    void setPayerCellphone(String payerCellphone);

    @Column(name = "`creator_id`")
    @FieldDef(label="��������ԱID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCreatorId();

    void setCreatorId(Long creatorId);

    @Column(name = "`creator`")
    @FieldDef(label="��������Ա����", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCreator();

    void setCreator(String creator);

    @Column(name = "`transfer_reason`")
    @FieldDef(label="����ת��ԭ��", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getTransferReason();

    void setTransferReason(String transferReason);

    @Column(name = "`payee_id`")
    @FieldDef(label="ת�뷽�ͻ�ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getPayeeId();

    void setPayeeId(Long payeeId);

    @Column(name = "`payee_name`")
    @FieldDef(label="ת�뷽�ͻ�����", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPayeeName();

    void setPayeeName(String payeeName);

    @Column(name = "`payee_certificate_number`")
    @FieldDef(label="ת�뷽�ͻ�֤������", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPayeeCertificateNumber();

    void setPayeeCertificateNumber(String payeeCertificateNumber);

    @Column(name = "`payee_cellphone`")
    @FieldDef(label="ת�뷽�ͻ��绰", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPayeeCellphone();

    void setPayeeCellphone(String payeeCellphone);

    @Column(name = "`payer_transaction_details_code`")
    @FieldDef(label="ת�����Ķ���ת����ˮ��", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPayerTransactionDetailsCode();

    void setPayerTransactionDetailsCode(String payerTransactionDetailsCode);

    @Column(name = "`payee_transaction_code`")
    @FieldDef(label="ת�뷽�Ķ���ת����ˮ��", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPayeeTransactionCode();

    void setPayeeTransactionCode(String payeeTransactionCode);
}