package com.dili.ia.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.apache.ibatis.io.ResolverUtil.IsA;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.annotation.Operator;

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
	 * 根据库存,查看入库详情,需要查看状态为4,5,6,7的数据
	 */
	private Boolean isStockInDetails;
	
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
	
	private Long parentDistrictId;
	/**
	 * 品类
	 */
	private Long categoryId;
	private Long customerId;
	
	private String customerName;
	
	private String customerCellphone;
	
	private Integer type;
	
	private String stockInCode;

	private LocalDateTime startDate;
	
	private LocalDateTime endDate;
	
	private LocalDate expireDate;
	
	private Integer expireDay;
	
	private Long departmentId;
	
	private Long marketId;
	
	private Long firstDistrictId;

	/*private String orderByColumn;
	
	private String sort;*/
	
	@Column(name = "`department_id`")
	@Operator(Operator.IN)
	private List<Long> depIds;

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

	public LocalDate getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(LocalDate expireDate) {
		this.expireDate = expireDate;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getParentDistrictId() {
		return parentDistrictId;
	}

	public void setParentDistrictId(Long parentDistrictId) {
		this.parentDistrictId = parentDistrictId;
	}

	public Integer getExpireDay() {
		return expireDay;
	}

	public void setExpireDay(Integer expireDay) {
		this.expireDay = expireDay;
	}

	public Long getMarketId() {
		return marketId;
	}

	public void setMarketId(Long marketId) {
		this.marketId = marketId;
	}

	public List<Long> getDepIds() {
		return depIds;
	}

	public void setDepIds(List<Long> depIds) {
		this.depIds = depIds;
	}

	public Boolean getIsStockInDetails() {
		return isStockInDetails;
	}

	public void setIsStockInDetails(Boolean isStockInDetails) {
		this.isStockInDetails = isStockInDetails;
	}

	public Long getFirstDistrictId() {
		return firstDistrictId;
	}

	public void setFirstDistrictId(Long firstDistrictId) {
		this.firstDistrictId = firstDistrictId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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
