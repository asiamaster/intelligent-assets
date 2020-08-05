package com.dili.ia.domain.dto;

import com.dili.ia.domain.InvoiceRecord;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *  由MyBatis Generator工具自动生成
 *  开票记录
 *  This file was generated on 2020-07-30 11:20:01.
 */
public interface InvoiceRecordDto extends InvoiceRecord {
  /**
   *  开票开始日期
   */
  @Column(
      name = "`invoice_date`"
  )
  @Operator(Operator.GREAT_EQUAL_THAN)
  LocalDate getInvoiceDateSt();

  void setInvoiceDateSt(LocalDate invoiceDateSt);

  /**
   *  开票结束日期
   */
  @Column(
      name = "`invoice_date`"
  )
  @Operator(Operator.LITTLE_EQUAL_THAN)
  LocalDate getInvoiceDateEnd();

  void setInvoiceDateEnd(LocalDate invoiceDateEnd);

  /**
   *  修改开始时间
   */
  @Column(
      name = "`modify_time`"
  )
  @Operator(Operator.GREAT_EQUAL_THAN)
  LocalDateTime getModifyTimeSt();

  void setModifyTimeSt(LocalDateTime modifyTimeSt);

  /**
   *  修改结束时间
   */
  @Column(
      name = "`modify_time`"
  )
  @Operator(Operator.LITTLE_EQUAL_THAN)
  LocalDateTime getModifyTimeEnd();

  void setModifyTimeEnd(LocalDateTime modifyTimeEnd);
}
