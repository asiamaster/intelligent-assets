package com.dili.ia.domain.dto;

import com.dili.ia.domain.InvoiceRecord;
import com.dili.ss.domain.annotation.Operator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 开票记录
 * This file was generated on 2020-07-30 11:20:01.
 */
public class InvoiceRecordDto extends InvoiceRecord {

    /**
     * 开票开始日期
     */
    @Column(name = "`invoice_date`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate invoiceDateSt;
    /**
     * 开票结束日期
     */
    @Column(name = "`invoice_date`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate invoiceDateEnd;

    /**
     * 修改开始时间
     */
    @Column(name = "`modify_time`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTimeSt;
    /**
     * 修改结束时间
     */
    @Column(name = "`modify_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTimeEnd;

    public LocalDate getInvoiceDateSt() {
        return invoiceDateSt;
    }

    public void setInvoiceDateSt(LocalDate invoiceDateSt) {
        this.invoiceDateSt = invoiceDateSt;
    }

    public LocalDate getInvoiceDateEnd() {
        return invoiceDateEnd;
    }

    public void setInvoiceDateEnd(LocalDate invoiceDateEnd) {
        this.invoiceDateEnd = invoiceDateEnd;
    }

    public LocalDateTime getModifyTimeSt() {
        return modifyTimeSt;
    }

    public void setModifyTimeSt(LocalDateTime modifyTimeSt) {
        this.modifyTimeSt = modifyTimeSt;
    }

    public LocalDateTime getModifyTimeEnd() {
        return modifyTimeEnd;
    }

    public void setModifyTimeEnd(LocalDateTime modifyTimeEnd) {
        this.modifyTimeEnd = modifyTimeEnd;
    }
}