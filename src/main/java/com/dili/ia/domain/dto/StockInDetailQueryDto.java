package com.dili.ia.domain.dto;

import java.util.Date;

import org.apache.ibatis.io.ResolverUtil.IsA;

import com.dili.ss.domain.BaseDomain;

/**
 * 
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description 入库详情查询
 * @author yangfan
 * @date 2020年6月12日
 */
public class StockInDetailQueryDto extends BaseDomain{

	/**
	 * 入库详情编号
	 */
	private String stockInDetailCode;

	private Integer state;
	/**
	 * 库位ID
	 */
	private Long assetsId;
	/**
	 * 区域id
	 */
	private Long districtId;
	/**
	 * 品类
	 */
	private Long categoryId;
	
	private String customerName;
	
	private String customerCellphone;
	
	private Integer type;
	
	private String stockInCode;

	private Date startDate;
	
	private Date endDate;
	
	private Date expireDate;
	
	private Long departmentId;

	/*private String orderByColumn;
	
	private String sort;*/
	

	public String getStockInDetailCode() {
		return stockInDetailCode;
	}

	public void setStockInDetailCode(String stockInDetailCode) {
		this.stockInDetailCode = stockInDetailCode;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getAssetsId() {
		return assetsId;
	}

	public void setAssetsId(Long assetsId) {
		this.assetsId = assetsId;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getStockInCode() {
		return stockInCode;
	}

	public void setStockInCode(String stockInCode) {
		this.stockInCode = stockInCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	/*public String getOrderByColumn() {
		return orderByColumn;
	}
	
	public void setOrderByColumn(String orderByColumn) {
		this.orderByColumn = orderByColumn;
	}
	
	public String getSort() {
		return sort;
	}
	
	public void setSort(String sort) {
		this.sort = sort;
	}
	*/
}
