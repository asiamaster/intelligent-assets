package com.dili.ia.domain.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.dili.ia.domain.BusinessChargeItem;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年7月27日
 */
public class LaborDto {

    private String code;

    /**
     * 运营车型
     */
    private String models;

    /**
     * 发票编号
     */
    private String invoiceNumber;

    /**
     * 马甲号
     */
    private String workCard;

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 办理周期
     */
    @NotNull(message = "办理周期不能为空")
    private Long interval;

    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空")
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    @NotNull(message = "结算日期不能为空")
    private LocalDateTime endDate;

    /**
     * 备注
     */
    private String notes;

    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    private Long amount;

    /**
     * 加收类型
     */
    private Long extraChargeType;

    /**
     * 加收天数
     */
    private Long extraChargeDays;

    /**
     * 加收费用
     */
    private Long extraCharge;

    /**
     * 客户ID
     */
    @NotNull(message = "客户id不能为空")
    private Long customerId;

    /**
     * 客户姓名
     */
    @NotNull(message = "客户姓名不能为空")
    private String customerName;

    /**
     * 客户手机号
     */
    @NotNull(message = "客户手机号不能为空")
    private String customerCellphone;
    
    /**
     * 证件号
     */
    @NotNull(message = "客户证件号不能为空")
    private String certificateNumber;

    /**
     * 客户性别
     */
    private Integer customerSex;

    /**
     * 上下半年
     */
    private Long halfYear;

    /**
     * 劳务类型(1:马甲证,2:自用证)
     */
    private String laborType;

    /**
     * 更名费用
     */
    private Long renameFee;

    /**
     * 更名主单id
     */
    private String renameCode;

    /**
     * 更型费用
     */
    private Long remodelFee;

    /**
     * 更型主单id
     */
    private String remodelCode;

    /**
     * 续费父单ID
     */
    private String renewCode;

    private Long departmentId;

    private String departmentName;

    private Integer state;
    
    /**
     * rename remodel renew
     */
    private String actionType;
    
    /**
     * 收费项
     */
    private List<BusinessChargeItem> businessChargeItems;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getModels() {
		return models;
	}

	public void setModels(String models) {
		this.models = models;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getWorkCard() {
		return workCard;
	}

	public void setWorkCard(String workCard) {
		this.workCard = workCard;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Long getInterval() {
		return interval;
	}

	public void setInterval(Long interval) {
		this.interval = interval;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getExtraChargeType() {
		return extraChargeType;
	}

	public void setExtraChargeType(Long extraChargeType) {
		this.extraChargeType = extraChargeType;
	}

	public Long getExtraChargeDays() {
		return extraChargeDays;
	}

	public void setExtraChargeDays(Long extraChargeDays) {
		this.extraChargeDays = extraChargeDays;
	}

	public Long getExtraCharge() {
		return extraCharge;
	}

	public void setExtraCharge(Long extraCharge) {
		this.extraCharge = extraCharge;
	}

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

	public Integer getCustomerSex() {
		return customerSex;
	}

	public void setCustomerSex(Integer customerSex) {
		this.customerSex = customerSex;
	}

	public Long getHalfYear() {
		return halfYear;
	}

	public void setHalfYear(Long halfYear) {
		this.halfYear = halfYear;
	}

	public String getLaborType() {
		return laborType;
	}

	public void setLaborType(String laborType) {
		this.laborType = laborType;
	}

	public Long getRenameFee() {
		return renameFee;
	}

	public void setRenameFee(Long renameFee) {
		this.renameFee = renameFee;
	}

	public Long getRemodelFee() {
		return remodelFee;
	}

	public void setRemodelFee(Long remodelFee) {
		this.remodelFee = remodelFee;
	}

	

	public String getRenameCode() {
		return renameCode;
	}

	public void setRenameCode(String renameCode) {
		this.renameCode = renameCode;
	}

	public String getRemodelCode() {
		return remodelCode;
	}

	public void setRemodelCode(String remodelCode) {
		this.remodelCode = remodelCode;
	}

	public String getRenewCode() {
		return renewCode;
	}

	public void setRenewCode(String renewCode) {
		this.renewCode = renewCode;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public List<BusinessChargeItem> getBusinessChargeItems() {
		return businessChargeItems;
	}

	public void setBusinessChargeItems(List<BusinessChargeItem> businessChargeItems) {
		this.businessChargeItems = businessChargeItems;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
}
