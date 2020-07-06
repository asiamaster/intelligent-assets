package com.dili.ia.domain.dto.printDto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.ia.domain.dto.LeaseOrderItemPrintDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public class StockInPrintDto {
	// 打印时间
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDate printTime;
	// 补打标记
	private String reprint;
	// 订单编号
	private String leaseOrderCode;
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
	private List<StockInPrintItemDto> leaseOrderItems;

	public LocalDate getPrintTime() {
		return printTime;
	}

	public void setPrintTime(LocalDate printTime) {
		this.printTime = printTime;
	}

	public String getReprint() {
		return reprint;
	}

	public void setReprint(String reprint) {
		this.reprint = reprint;
	}

	public String getLeaseOrderCode() {
		return leaseOrderCode;
	}

	public void setLeaseOrderCode(String leaseOrderCode) {
		this.leaseOrderCode = leaseOrderCode;
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

	public List<StockInPrintItemDto> getLeaseOrderItems() {
		return leaseOrderItems;
	}

	public void setLeaseOrderItems(List<StockInPrintItemDto> leaseOrderItems) {
		this.leaseOrderItems = leaseOrderItems;
	}

	public StockInPrintItemDto getItemDto() {
		return new StockInPrintItemDto();
	}

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
		private LocalDate stockInDate;

		@JSONField(format = "yyyy-MM-dd")
		@JsonFormat(pattern = "yyyy-MM-dd")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate expireDate;

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

		public LocalDate getStockInDate() {
			return stockInDate;
		}

		public void setStockInDate(LocalDate stockInDate) {
			this.stockInDate = stockInDate;
		}

		public LocalDate getExpireDate() {
			return expireDate;
		}

		public void setExpireDate(LocalDate expireDate) {
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
}
