package com.dili.ia.service.impl;

import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.glossary.PaymentOrderStateEnum;
import com.dili.ia.mapper.PaymentOrderMapper;
import com.dili.ia.rpc.UidRpcResolver;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.UserTicket;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-28 21:11:27.
 */
@Service
public class PaymentOrderServiceImpl extends BaseServiceImpl<PaymentOrder, Long> implements PaymentOrderService {

	@Autowired
	private UidRpcResolver uidRpcResolver;
	
    public PaymentOrderMapper getActualDao() {
        return (PaymentOrderMapper)getDao();
    }

	@Override
	public PaymentOrder buildPaymentOrder(UserTicket userTicket) {
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setCode(uidRpcResolver.bizNumber(BizNumberTypeEnum.PAYMENT_ORDER.getCode()));
		paymentOrder.setCreateTime(LocalDateTime.now());
		paymentOrder.setCreator(userTicket.getUserName());
		paymentOrder.setCreatorId(userTicket.getId());
		paymentOrder.setMarketCode(userTicket.getFirmCode());
		paymentOrder.setMarketId(userTicket.getFirmId());
		// paymentOrder.setBizType(bizType);
		paymentOrder.setState(PaymentOrderStateEnum.NOT_PAID.getCode());
		return paymentOrder;
	}

	@Override
	public PaymentOrder getByCode(String code) {
		PaymentOrder condtion = new PaymentOrder();
		condtion.setCode(code);
		List<PaymentOrder> paymentOrders = this.listByExample(condtion);
		if (CollectionUtils.isEmpty(paymentOrders) || paymentOrders.size() != 1) {
			return null;
		}
		return paymentOrders.get(0);
	}
}