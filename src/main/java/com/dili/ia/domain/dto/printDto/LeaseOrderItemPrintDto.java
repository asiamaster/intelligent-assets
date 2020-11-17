package com.dili.ia.domain.dto.printDto;

import java.util.Map;

public class LeaseOrderItemPrintDto {
    //资产名称
    private String assetsName;
    //区域名称
    private String districtName;
    //数量
    private String number;
    //单位
    private String unitName;
    //单价
    private String unitPrice;
    //是否转角
    private String isCorner;
    //应交月数
    private String paymentMonth;
    //优惠金额
    private String discountAmount;
    //租金
    private String rentAmount;
    //物管费
    private String manageAmount;
    //保证金
    private String depositAmount;
    //租金退款额
    private String rentRefundAmount;
    //物管费退款额
    private String manageRefundAmount;
    //保证金退款额
    private String depositRefundAmount;
    //保证金已交金额
    private String depositBalance;
    //保证金补交金额
    private String depositMakeUpAmount;

    //业务收费项
    private Map<String,String> businessChargeItem;

    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getIsCorner() {
        return isCorner;
    }

    public void setIsCorner(String isCorner) {
        this.isCorner = isCorner;
    }

    public String getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(String paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(String rentAmount) {
        this.rentAmount = rentAmount;
    }

    public String getManageAmount() {
        return manageAmount;
    }

    public void setManageAmount(String manageAmount) {
        this.manageAmount = manageAmount;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getRentRefundAmount() {
        return rentRefundAmount;
    }

    public void setRentRefundAmount(String rentRefundAmount) {
        this.rentRefundAmount = rentRefundAmount;
    }

    public String getManageRefundAmount() {
        return manageRefundAmount;
    }

    public void setManageRefundAmount(String manageRefundAmount) {
        this.manageRefundAmount = manageRefundAmount;
    }

    public String getDepositRefundAmount() {
        return depositRefundAmount;
    }

    public void setDepositRefundAmount(String depositRefundAmount) {
        this.depositRefundAmount = depositRefundAmount;
    }

    public String getDepositBalance() {
        return depositBalance;
    }

    public void setDepositBalance(String depositBalance) {
        this.depositBalance = depositBalance;
    }

    public String getDepositMakeUpAmount() {
        return depositMakeUpAmount;
    }

    public void setDepositMakeUpAmount(String depositMakeUpAmount) {
        this.depositMakeUpAmount = depositMakeUpAmount;
    }

    public Map<String, String> getBusinessChargeItem() {
        return businessChargeItem;
    }

    public void setBusinessChargeItem(Map<String, String> businessChargeItem) {
        this.businessChargeItem = businessChargeItem;
    }
}
