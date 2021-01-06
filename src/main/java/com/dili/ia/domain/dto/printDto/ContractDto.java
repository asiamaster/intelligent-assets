package com.dili.ia.domain.dto.printDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description 合同打印dto
 * @author yangfan
 * @date 2020年12月23日
 */
public class ContractDto {
	
	/**
	 * 线下合同号
	 */
	private String contractNo;
	
	/**
	 * 甲方
	 */
	private String partya;
	
	/**
	 * 乙方
	 */
	private String partyb;
	
	/**
	 * 地址
	 */
	private String address;
	
	/**
	 * 证件
	 */
	private String certificateNo;

	/**
	 * 电话
	 */
	private String phone;
	
	/**
	 * 资产类型
	 */
	private String assetsType;

	/**
	 * 面积
	 */
	private String area;
	
	/**
	 * 资产项目列表(租赁位置)
	 */
	private List<Map<String, String>>  items;
	
	/**
	 * 开始时间
	 */
	private LocalDateTime sTime;
	
	/**
	 * 结束时间
	 */
	private LocalDateTime eTime;
	
	/**
	 * 合同天数
	 */
	private String days;

	/**
	 * 资产收费列表(收费标准)
	 */
	private List<Map<String, String>>  feeItems;
	
	/**
	 * 合同金额
	 */
	private String amount;
	private String amountCn;

	public String getPartya() {
		return partya;
	}

	public void setPartya(String partya) {
		this.partya = partya;
	}

	public String getPartyb() {
		return partyb;
	}

	public void setPartyb(String partyb) {
		this.partyb = partyb;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAssetsType() {
		return assetsType;
	}

	public void setAssetsType(String assetsType) {
		this.assetsType = assetsType;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	

	public LocalDateTime getsTime() {
		return sTime;
	}

	public void setsTime(LocalDateTime sTime) {
		this.sTime = sTime;
	}

	public LocalDateTime geteTime() {
		return eTime;
	}

	public void seteTime(LocalDateTime eTime) {
		this.eTime = eTime;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}


	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getAmountCn() {
		return amountCn;
	}

	public void setAmountCn(String amountCn) {
		this.amountCn = amountCn;
	}

	public List<Map<String, String>> getItems() {
		return items;
	}

	public void setItems(List<Map<String, String>> items) {
		this.items = items;
	}

	public List<Map<String, String>> getFeeItems() {
		return feeItems;
	}

	public void setFeeItems(List<Map<String, String>> feeItems) {
		this.feeItems = feeItems;
	}

	
	
}
