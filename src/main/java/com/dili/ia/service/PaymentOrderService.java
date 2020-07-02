package com.dili.ia.service;

import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.dto.PayInfoDto;
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
	 * @param payInfoDto
	 * @return
	 * @throws
	 */
	PaymentOrder buildPaymentOrder(UserTicket userTicket, PayInfoDto payInfoDto);
	
	/**
	 * 
	 * @Title getByCode
	 * @Description 通过id获取缴费单
	 * @param code
	 * @return
	 * @throws
	 */
	PaymentOrder getByCode(String code);
	
	/**
	 * 
	 * @Title updatePaymentOrder
	 * @Description 更新数据(版本号检查)
	 * @param domain
	 * @param version (code,version)
	 * @param code (code,version)
	 * @return
	 * @throws
	 */
	void updatePaymentOrder(PaymentOrder domain, Integer version, String code);
		
}
