package com.dili.ia.domain.dto;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年7月14日
 */
public class StockDto {

	
	private Long id;
    /**
     * 区域id
     */
    private Long districtId;

    /**
     * 区域
     */
    private String districtName;

    /**
     * 资产 冷库id
     */
    private Long assetsId;

    /**
     * 资产 冷库名称
     */
    private String assetsName;

    private Long categoryId;

    private String categoryName;

    private Long customerId;

    private String customerName;
    
    private String customerCellphone;
    
    private Long departmentId;

    private String departmentName;

    private Long quantity;

    private Long weight;

    private String marketCode;
    
    private Long marketId;
    
    private LocalDate stockInDay;
    
    //数据统计
    private Long inQuantity;
    private Long inWeight;
    private Long outQuantity;
    private Long outWeight;
    private Long cancelQuantity;
    private Long cancelWeight;
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public Long getAssetsId() {
		return assetsId;
	}
	public void setAssetsId(Long assetsId) {
		this.assetsId = assetsId;
	}
	public String getAssetsName() {
		return assetsName;
	}
	public void setAssetsName(String assetsName) {
		this.assetsName = assetsName;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public Long getWeight() {
		return weight;
	}
	public void setWeight(Long weight) {
		this.weight = weight;
	}
	public String getMarketCode() {
		return marketCode;
	}
	public void setMarketCode(String marketCode) {
		this.marketCode = marketCode;
	}
	public Long getMarketId() {
		return marketId;
	}
	public void setMarketId(Long marketId) {
		this.marketId = marketId;
	}
	public Long getInQuantity() {
		return inQuantity;
	}
	public void setInQuantity(Long inQuantity) {
		this.inQuantity = inQuantity;
	}
	public Long getInWeight() {
		return inWeight;
	}
	public void setInWeight(Long inWeight) {
		this.inWeight = inWeight;
	}
	public Long getOutQuantity() {
		return outQuantity;
	}
	public void setOutQuantity(Long outQuantity) {
		this.outQuantity = outQuantity;
	}
	public Long getOutWeight() {
		return outWeight;
	}
	public void setOutWeight(Long outWeight) {
		this.outWeight = outWeight;
	}
	public Long getCancelQuantity() {
		return cancelQuantity;
	}
	public void setCancelQuantity(Long cancelQuantity) {
		this.cancelQuantity = cancelQuantity;
	}
	public Long getCancelWeight() {
		return cancelWeight;
	}
	public void setCancelWeight(Long cancelWeight) {
		this.cancelWeight = cancelWeight;
	}
	public LocalDate getStockInDay() {
		return stockInDay;
	}
	public void setStockInDay(LocalDate stockInDay) {
		this.stockInDay = stockInDay;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
    
}
