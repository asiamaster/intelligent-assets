package com.dili.ia.domain.dto;

import java.util.Date;
import java.util.List;

import com.dili.ia.domain.StockInDetail;

/**
 * 
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description 入库单数据传输
 * @author yangfan
 * @date 2020年6月12日
 */
public class StockInDto {

    private String code;

    /**
     * 入库时间
     */
    private Date stockInDate;

    /**
     * 客户id
     */
    private Long customerId;

    private String customerName;

    private String customerCellphone;

    /**
     * 操作员id
     */
    private Long operatorId;

    private String operatorName;

    /**
     * 总重量
     */
    private Long weight;

    /**
     * 总数量
     */
    private Long quantity;

    /**
     * 入库类型
     */
    private Integer type;

    /**
     * 品类id
     */
    private Long categoryId;

    private String categoryName;

    /**
     * 产地
     */
    private String origin;

    /**
     * 支付方式 1 现金，2 POS，3 刷卡
     */
    private Integer payType;

    /**
     * 部门
     */
    private Long departmentId;

    private String departmentName;

    /**
     * 计价单位
     */
    private Integer uom;

    /**
     * 市场id
     */
    private Long marketId;

    private String marketCode;
    
    /**
     * 金额
     */
    private Long amount;
    
    private Date expireDate;
    
    /**
     * 入库单详情(子单)
     */
    private List<StockInDetailDto> stockInDetailDtos;
    
    /**
     * 入库单详情(子单)
     */
    private List<StockInDetail> stockInDetails;
    
    /**
     * 入库单详情(子单)
     */
    private String jsonStockInDetailDtos;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getStockInDate() {
		return stockInDate;
	}

	public void setStockInDate(Date stockInDate) {
		this.stockInDate = stockInDate;
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

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
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

	public Integer getUom() {
		return uom;
	}

	public void setUom(Integer uom) {
		this.uom = uom;
	}

	public Long getMarketId() {
		return marketId;
	}

	public void setMarketId(Long marketId) {
		this.marketId = marketId;
	}

	public String getMarketCode() {
		return marketCode;
	}

	public void setMarketCode(String marketCode) {
		this.marketCode = marketCode;
	}

	public List<StockInDetailDto> getStockInDetailDtos() {
		return stockInDetailDtos;
	}

	public void setStockInDetailDtos(List<StockInDetailDto> stockInDetailDtos) {
		this.stockInDetailDtos = stockInDetailDtos;
	}

	public String getJsonStockInDetailDtos() {
		return jsonStockInDetailDtos;
	}

	public void setJsonStockInDetailDtos(String jsonStockInDetailDtos) {
		this.jsonStockInDetailDtos = jsonStockInDetailDtos;
	}

	public List<StockInDetail> getStockInDetails() {
		return stockInDetails;
	}

	public void setStockInDetails(List<StockInDetail> stockInDetails) {
		this.stockInDetails = stockInDetails;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	
	
    
}
