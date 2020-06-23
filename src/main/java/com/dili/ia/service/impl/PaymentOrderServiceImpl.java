package com.dili.ia.service.impl;

import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.dto.PayInfoDto;
import com.dili.ia.glossary.PayStateEnum;
import com.dili.ia.mapper.PaymentOrderMapper;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.UserTicket;

import java.util.Date;

import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-28 21:11:27.
 */
@Service
public class PaymentOrderServiceImpl extends BaseServiceImpl<PaymentOrder, Long> implements PaymentOrderService {

    public PaymentOrderMapper getActualDao() {
        return (PaymentOrderMapper)getDao();
    }

	@Override
	public PaymentOrder savePaymentOrder(UserTicket userTicket, PayInfoDto payInfoDto) {
		PaymentOrder paymentOrder = DTOUtils.newInstance(PaymentOrder.class);
		paymentOrder.setBusinessCode(payInfoDto.getBusinessCode());
		paymentOrder.setAmount(payInfoDto.getPayMoney());
		paymentOrder.setCreateTime(new Date());
		paymentOrder.setCreator(userTicket.getUserName());
		paymentOrder.setCreatorId(userTicket.getId());
		paymentOrder.setMarketCode(userTicket.getFirmCode());
		paymentOrder.setMarketId(userTicket.getFirmId());
		// paymentOrder.setBizType(bizType);
		paymentOrder.setState(PayStateEnum.NOT_PAID.getCode());
		this.insertSelective(paymentOrder);
		return null;
	}
}