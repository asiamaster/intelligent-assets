package com.dili.ia.service;

import com.dili.ia.domain.PaymentOrder;
import com.dili.ss.base.BaseService;
import com.dili.uap.sdk.domain.UserTicket;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-28 21:11:27.
 */
public interface PaymentOrderService extends BaseService<PaymentOrder, Long> {
	
	/**
	 * 
	 * @Title buildPaymentOrder
	 * @Description 创建缴费单
	 * @param userTicket
	 * @return
	 * @throws
	 */
	PaymentOrder buildPaymentOrder(UserTicket userTicket);
	
	/**
	 * 
	 * @Title getByCode
	 * @Description 通过code获取缴费单
	 * @param code
	 * @return
	 * @throws
	 */
	PaymentOrder getByCode(String code);
		
}
