package com.dili.ia.domain.account;

import java.io.Serializable;
import java.util.List;

public class CardQuery implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long firmId;
    private List<Long> customerIds;
    private String customerName;
    private String customerCertificateNumber;


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCertificateNumber() {
        return customerCertificateNumber;
    }

    public void setCustomerCertificateNumber(String customerCertificateNumber) {
        this.customerCertificateNumber = customerCertificateNumber;
    }

    public CardQuery(){

    }

    public List<Long> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<Long> customerIds) {
        this.customerIds = customerIds;
    }

	public Long getFirmId() {
		return firmId;
	}

	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}
}
