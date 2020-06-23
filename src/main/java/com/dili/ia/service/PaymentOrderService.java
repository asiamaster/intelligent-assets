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
	 * @Title savePaymentOrder
	 * @Description 创建缴费单
	 * @param userTicket
	 * @param payInfoDto
	 * @return
	 * @throws
	 */
	PaymentOrder savePaymentOrder(UserTicket userTicket, PayInfoDto payInfoDto);
	
}
