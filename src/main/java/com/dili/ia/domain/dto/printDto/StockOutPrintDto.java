package com.dili.ia.domain.dto.printDto;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年7月6日
 */
public class StockOutPrintDto {
	
	private String stockOutCode;
	
	private LocalDateTime stockOutDate;
	
	private String departmentName;
	
	private String districtName;
	
	private String categoryName;
	
	private String quantity;
	
	private String notes;
	
	// 提交人
	private String submitter;

	public String getStockOutCode() {
		return stockOutCode;
	}

	public void setStockOutCode(String stockOutCode) {
		this.stockOutCode = stockOutCode;
	}

	public LocalDateTime getStockOutDate() {
		return stockOutDate;
	}

	public void setStockOutDate(LocalDateTime stockOutDate) {
		this.stockOutDate = stockOutDate;
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}
	
}
