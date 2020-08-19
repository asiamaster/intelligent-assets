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
public class EarnestTransferDto extends EarnestTransferOrder {
    @Transient
    private Long customerId;

    @Transient
    private String customerName;


    @Transient
    private String customerCellphone;

    @Transient
    private String certificateNumber;

    @Transient
    private Long payerAccountVersion;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCellphone() {
        return customerCellphone;
    }

    public void setCustomerCellphone(String customerCellphone) {
        this.customerCellphone = customerCellphone;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public Long getPayerAccountVersion() {
        return payerAccountVersion;
    }

    public void setPayerAccountVersion(Long payerAccountVersion) {
        this.payerAccountVersion = payerAccountVersion;
    }
}
