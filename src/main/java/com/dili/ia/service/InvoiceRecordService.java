package com.dili.ia.service;

import com.dili.ia.domain.InvoiceRecord;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-30 11:20:01.
 */
public interface InvoiceRecordService extends BaseService<InvoiceRecord, Long> {

    /**
     * 插入开票记录，同时更新租赁业务单为已开票
     * @param invoiceRecord
     */
    void insertInvoiceRecord(InvoiceRecord invoiceRecord);
}