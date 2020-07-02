package com.dili.ia.domain.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description 支付单创建,可增加需要字段
 * @author yangfan
 * @date 2020年6月23日
 */
public class PayInfoDto {
	
	@NotNull(message = "code is null")
	private String businessCode;
	
	private Long amount;
		
	private String password;
	
	private Integer payType;
	
	private Long payCustomerId;

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Long getPayCustomerId() {
		return payCustomerId;
	}

	public void setPayCustomerId(Long payCustomerId) {
		this.payCustomerId = payCustomerId;
	}

	
	
}
