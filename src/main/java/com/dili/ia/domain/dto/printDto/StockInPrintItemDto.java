package com.dili.ia.domain.dto.printDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年7月6日
 */
public class StockInPrintItemDto {
	// 冷库编号
	private String assetsCode;
	// 入库方式
	private String stockInType;
	// 单价
	private String unitPrice;
	// 件数
	private String quantity;
	// 净重
	private String weight;
	// 付款方式
	private String payWay;
	// 代缴人
	private String proxyPayer;
	// 汽车编号
	private String carPlate;
	// 汽车类型
	private String carTypePublicCode;
	// 入库时间
	@JSONField(format = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime stockInDate;

	@JSONField(format = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime expireDate;

	public String getAssetsCode() {
		return assetsCode;
	}

	public void setAssetsCode(String assetsCode) {
		this.assetsCode = assetsCode;
	}

	public String getStockInType() {
		return stockInType;
	}

	public void setStockInType(String stockInType) {
		this.stockInType = stockInType;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getProxyPayer() {
		return proxyPayer;
	}

	public void setProxyPayer(String proxyPayer) {
		this.proxyPayer = proxyPayer;
	}

	public LocalDateTime getStockInDate() {
		return stockInDate;
	}

	public void setStockInDate(LocalDateTime stockInDate) {
		this.stockInDate = stockInDate;
	}

	public LocalDateTime getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(LocalDateTime expireDate) {
		this.expireDate = expireDate;
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

}
