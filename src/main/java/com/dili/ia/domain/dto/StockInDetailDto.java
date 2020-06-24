package com.dili.ia.domain.dto;

import org.apache.ibatis.io.ResolverUtil.IsA;

/**
 * 
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description 入库详情传输
 * @author yangfan
 * @date 2020年6月12日
 */
public class StockInDetailDto {

	/**
	 * 入库详情编号
	 */
	private String code;

	private Integer state;

	/**
	 * 单件重量
	 */
	private Long unitWeight;

	/**
	 * 查件管理员
	 */
	private Long checkOperatorId;

	private String checkOperator;

	/**
	 * 接车单号
	 */
	private String pickupNumber;

	/**
	 * 司磅记录
	 */
	private Long weightmanId;

	/**
	 * 库位ID
	 */
	private Long assetsId;

	private String assetsCode;

	/**
	 * 备注
	 */
	private String notes;

	/**
	 * 区域id
	 */
	private Long districtId;

	private String districtName;

	/**
	 * 应收款
	 */
	private Long receivable;

	/**
	 * 实收款
	 */
	private Long cope;

	/**
	 * 入库数量
	 */
	private Long quantity;

	/**
	 * 入库总量
	 */
	private Long weight;
	
	/**
	 * 入库金额
	 */
	private Long amount;

	/**
	 * 市场id
	 */
	private Long marketId;

	private String marketCode;

	/**
	 * 车牌号
	 */
	private String carPlate;

	/**
	 * 汽车编号,类型
	 */
	private String carTypePublicCode;
	
	/**
	 * 入库子单删除
	 */
	private Boolean delete = false;
	
	/**
     * 司磅记录
     */
    private StockWeighmanRecordDto stockWeighmanRecordDto;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getUnitWeight() {
		return unitWeight;
	}

	public void setUnitWeight(Long unitWeight) {
		this.unitWeight = unitWeight;
	}

	public Long getCheckOperatorId() {
		return checkOperatorId;
	}

	public void setCheckOperatorId(Long checkOperatorId) {
		this.checkOperatorId = checkOperatorId;
	}

	public String getCheckOperator() {
		return checkOperator;
	}

	public void setCheckOperator(String checkOperator) {
		this.checkOperator = checkOperator;
	}

	public String getPickupNumber() {
		return pickupNumber;
	}

	public void setPickupNumber(String pickupNumber) {
		this.pickupNumber = pickupNumber;
	}

	public Long getWeightmanId() {
		return weightmanId;
	}

	public void setWeightmanId(Long weightmanId) {
		this.weightmanId = weightmanId;
	}

	public Long getAssetsId() {
		return assetsId;
	}

	public void setAssetsId(Long assetsId) {
		this.assetsId = assetsId;
	}

	public String getAssetsCode() {
		return assetsCode;
	}

	public void setAssetsCode(String assetsCode) {
		this.assetsCode = assetsCode;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

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

	public Long getReceivable() {
		return receivable;
	}

	public void setReceivable(Long receivable) {
		this.receivable = receivable;
	}

	public Long getCope() {
		return cope;
	}

	public void setCope(Long cope) {
		this.cope = cope;
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

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
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
	
	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public StockWeighmanRecordDto getStockWeighmanRecordDto() {
		return stockWeighmanRecordDto;
	}

	public void setStockWeighmanRecordDto(StockWeighmanRecordDto stockWeighmanRecordDto) {
		this.stockWeighmanRecordDto = stockWeighmanRecordDto;
	}

	
}
