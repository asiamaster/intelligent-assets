package com.dili.ia.domain.dto.printDto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年8月5日
 */
@JSONType(serialzeFeatures = SerializerFeature.WriteNullStringAsEmpty)
public class LaborPayPrintDto {
	// 打印时间
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime printTime;
	// 补打标记
	private String reprint;
	// 订单编号
	private String code;
	// 业务类型
	private String businessType;
	// 客户名称
	private String customerName;
	// 客户电话
	private String customerCellphone;
	// 马甲号
	private String workCard;
	// 运营车型
	private String models;
	// 总金额
	private String totalAmount;
	// 提交人
	private String submitter;
	// 结算员
	private String settlementOperator;
	// 有效期
	private String effectiveDate;
	// 付款方式
	private String payWay;
	// 流水号
	private String serialNumber;
	// 卡号
	private String cardNo;
	// 备注信息
	private String notes;
	
	private String settleWayDetails;

	public String getSettleWayDetails() {
		return settleWayDetails;
	}
	public void setSettleWayDetails(String settleWayDetails) {
		this.settleWayDetails = settleWayDetails;
	}
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getWorkCard() {
		return workCard;
	}
	public void setWorkCard(String workCard) {
		this.workCard = workCard;
	}
	public String getModels() {
		return models;
	}
	public void setModels(String models) {
		this.models = models;
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
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

	

}
