package com.dili.ia.domain.dto;

import com.dili.ia.domain.EarnestTransferOrder;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.Transient;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-02 11:32
 */
public interface EarnestTransferDto extends EarnestTransferOrder {
    @Transient
    @FieldDef(label="客户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Transient
    @FieldDef(label="客户名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerName();

    void setCustomerName(String customerName);

    @Transient
    @FieldDef(label="客户电话", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerCellphone();

    void setCustomerCellphone(String customerCellphone);

    @Transient
    @FieldDef(label="客户证件号码", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCertificateNumber();

    void setCertificateNumber(String certificateNumber);
}
