package com.dili.ia.domain.dto.printDto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.ia.domain.dto.LeaseOrderItemPrintDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class StockInPrintDto {
	// 打印时间
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime printTime;
	// 补打标记
	private String reprint;
	// 订单编号
	private String stockInCode;
	// 业务类型
	private String businessType;
	// 客户名称
	private String customerName;
	// 客户电话
	private String customerCellphone;
	// 卡号
	private String cardNo;
	// 品类
	private String categoryName;
	// 汽车编号
	private String carPlate;
	// 汽车类型
	private String carTypePublicCode;
	// 部门
	private String departmentName;
	// 区域
	private String districtName;
	// 总金额
	private String totalAmount;
	// 提交人
	private String submitter;
	// 结算员
	private String settlementOperator;
	// 复核人
	private String reviewer;
	// 订单项
	private List<StockInPrintItemDto> stockInItems;

	public LocalDateTime getPrintTime() {
		return printTime;
	}

	public void setPrintTime(LocalDateTime printTime) {
		this.printTime = printTime;
	}

	public String getReprint() {
		return reprint;
	}

	public void setReprint(String reprint) {
		this.reprint = reprint;
	}

	public String getStockInCode() {
		return stockInCode;
	}

	public void setStockInCode(String stockInCode) {
		this.stockInCode = stockInCode;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCarPlate() {
		return carPlate;
	}

	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}

	public String getCarTypePublicCode() {
		return carTypePublicCode;
	}

	public void setCarTypePublicCode(String carTypePublicCode) {
		this.carTypePublicCode = carTypePublicCode;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public String getSettlementOperator() {
		return settlementOperator;
	}

	public void setSettlementOperator(String settlementOperator) {
		this.settlementOperator = settlementOperator;
	}

	public String getReviewer() {
		return reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public List<StockInPrintItemDto> getStockInItems() {
		return stockInItems;
	}

	public void setStockInItems(List<StockInPrintItemDto> stockInItems) {
		this.stockInItems = stockInItems;
	}

	

}
