package com.dili.ia.service.impl;

import com.dili.ia.domain.AssetsLeaseOrder;
import com.dili.ia.domain.InvoiceRecord;
import com.dili.ia.mapper.InvoiceRecordMapper;
import com.dili.ia.service.AssetsLeaseOrderService;
import com.dili.ia.service.InvoiceRecordService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.exception.NotLoginException;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-30 11:20:01.
 */
@Service
public class InvoiceRecordServiceImpl extends BaseServiceImpl<InvoiceRecord, Long> implements InvoiceRecordService {

    @Autowired
    private AssetsLeaseOrderService assetsLeaseOrderService;

    public InvoiceRecordMapper getActualDao() {
        return (InvoiceRecordMapper)getDao();
    }

    @Override
    @Transactional
    public void insertInvoiceRecord(InvoiceRecord invoiceRecord) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            throw new NotLoginException();
        }
        invoiceRecord.setAmount(invoiceRecord.getAmount());
        invoiceRecord.setFirmId(userTicket.getFirmId());
        invoiceRecord.setCreatorId(userTicket.getId());
        invoiceRecord.setCreator(userTicket.getRealName());
        insertSelective(invoiceRecord);
        AssetsLeaseOrder assetsLeaseOrder = new AssetsLeaseOrder();
        assetsLeaseOrder.setIsInvoice(1);
        AssetsLeaseOrder condition = new AssetsLeaseOrder();
        condition.setCode(invoiceRecord.getBusinessKey());
        assetsLeaseOrderService.updateSelectiveByExample(assetsLeaseOrder, condition);
    }
}