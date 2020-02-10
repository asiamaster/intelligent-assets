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
 * �����ҵ��
 * This file was generated on 2020-02-10 17:43:43.
 */
@Table(name = "`earnest_order`")
public interface EarnestOrder extends IBaseDomain {
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

    @Column(name = "`customer_id`")
    @FieldDef(label="�ͻ�ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`customer_name`")
    @FieldDef(label="�ͻ�����", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerName();

    void setCustomerName(String customerName);

    @Column(name = "`customer_cellphone`")
    @FieldDef(label="�ͻ��绰", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerCellphone();

    void setCustomerCellphone(String customerCellphone);

    @Column(name = "`customer_certificate_number`")
    @FieldDef(label="customerCertificateNumber", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerCertificateNumber();

    void setCustomerCertificateNumber(String customerCertificateNumber);

    @Column(name = "`start_time`")
    @FieldDef(label="��ʼʱ��")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getStartTime();

    void setStartTime(Date startTime);

    @Column(name = "`end_time`")
    @FieldDef(label="��ֹʱ��")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getEndTime();

    void setEndTime(Date endTime);

    @Column(name = "`department_id`")
    @FieldDef(label="departmentId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDepartmentId();

    void setDepartmentId(Long departmentId);

    @Column(name = "`department_name`")
    @FieldDef(label="departmentName", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getDepartmentName();

    void setDepartmentName(String departmentName);

    @Column(name = "`state`")
    @FieldDef(label="״̬")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getState();

    void setState(Integer state);

    @Column(name = "`assets_type`")
    @FieldDef(label="�ʲ����ͣ�����̯λ����⣬��Ԣ��")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getAssetsType();

    void setAssetsType(Long assetsType);

    @Column(name = "`amount`")
    @FieldDef(label="���")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getAmount();

    void setAmount(Long amount);

    @Column(name = "`code`")
    @FieldDef(label="����ҵ�?��", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCode();

    void setCode(String code);

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

    @Column(name = "`submitter_id`")
    @FieldDef(label="submitterId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getSubmitterId();

    void setSubmitterId(Long submitterId);

    @Column(name = "`submitter`")
    @FieldDef(label="submitter", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSubmitter();

    void setSubmitter(String submitter);

    @Column(name = "`sub_date`")
    @FieldDef(label="�ύʱ��")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getSubDate();

    void setSubDate(Date subDate);

    @Column(name = "`notes`")
    @FieldDef(label="��ע", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNotes();

    void setNotes(String notes);

    @Column(name = "`canceler_id`")
    @FieldDef(label="cancelerId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCancelerId();

    void setCancelerId(Long cancelerId);

    @Column(name = "`canceler`")
    @FieldDef(label="canceler", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCanceler();

    void setCanceler(String canceler);

    @Column(name = "`delete_id`")
    @FieldDef(label="deleteId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDeleteId();

    void setDeleteId(Long deleteId);

    @Column(name = "`market_id`")
    @FieldDef(label="marketId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getMarketId();

    void setMarketId(Long marketId);

    @Column(name = "`delete_name`")
    @FieldDef(label="deleteName")
    @EditMode(editor = FieldEditor.Text, required = false)
    byte[] getDeleteName();

    void setDeleteName(byte[] deleteName);
}