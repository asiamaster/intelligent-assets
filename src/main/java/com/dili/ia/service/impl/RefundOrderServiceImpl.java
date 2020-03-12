package com.dili.ia.service.impl;

import com.dili.ia.domain.EarnestOrder;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.TransactionDetails;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.RefundOrderMapper;
import com.dili.ia.service.RefundOrderService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-09 19:34:40.
 */
@Service
public class RefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderService {

    public RefundOrderMapper getActualDao() {
        return (RefundOrderMapper)getDao();
    }

    @Override
    public BaseOutput submitRefundOrder(Long refundOrderId) {
        return null;
    }

    @Override
    public BaseOutput withdrawRefundOrder(Long refundOrderId) {
        return null;
    }

    @Override
    public BaseOutput refundSuccessOrder(Long refundOrderId) {
        return null;
    }
}