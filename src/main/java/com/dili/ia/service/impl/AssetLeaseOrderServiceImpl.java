package com.dili.ia.service.impl;

import com.dili.ia.domain.AssetLeaseOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.AssetLeaseOrderListDto;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.RefundOrderDto;
import com.dili.ia.mapper.AssetLeaseOrderMapper;
import com.dili.ia.service.AssetLeaseOrderService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 14:40:05.
 */
@Service
public class AssetLeaseOrderServiceImpl extends BaseServiceImpl<AssetLeaseOrder, Long> implements AssetLeaseOrderService {

    public AssetLeaseOrderMapper getActualDao() {
        return (AssetLeaseOrderMapper)getDao();
    }

    @Override
    public BaseOutput saveLeaseOrder(AssetLeaseOrderListDto dto) {
        return null;
    }

    @Override
    public void checkCustomerState(Long customerId, Long marketId) {

    }

    @Override
    public BaseOutput submitPayment(Long id, Long amount, Long waitAmount) {
        return null;
    }

    @Override
    public BaseOutput cancelOrder(Long id) {
        return null;
    }

    @Override
    public BaseOutput withdrawOrder(Long id) {
        return null;
    }

    @Override
    public BaseOutput<Boolean> updateLeaseOrderBySettleInfo(SettleOrder settleOrder) {
        return null;
    }

    @Override
    public void leaseOrderEffectiveHandler(AssetLeaseOrder o) {

    }

    @Override
    public void leaseOrderExpiredHandler(AssetLeaseOrder o) {

    }

    @Override
    public BaseOutput<PrintDataDto> queryPrintData(String orderCode, Integer reprint) {
        return null;
    }

    @Override
    public BaseOutput createRefundOrder(RefundOrderDto refundOrderDto) {
        return null;
    }

    @Override
    public BaseOutput cancelRefundOrderHandler(Long leaseOrderId, Long leaseOrderItemId) {
        return null;
    }

    @Override
    public BaseOutput settleSuccessRefundOrderHandler(RefundOrder refundOrder) {
        return null;
    }

    @Override
    public BaseOutput supplement(AssetLeaseOrder leaseOrder) {
        return null;
    }
}