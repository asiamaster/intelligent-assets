package com.dili.ia.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.mvc.annotation.Cent2Yuan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *  由MyBatis Generator工具自动生成
 *  开票记录
 *  This file was generated on 2020-07-30 11:20:01.
 */
@Table(
        name = "`invoice_record`"
)
public interface InvoiceRecord extends IBaseDomain {
  /**
   *  id
   */
  @Column(
          name = "`id`"
  )
  @Id
  @GeneratedValue(
          strategy = GenerationType.IDENTITY
  )
  Long getId();

  void setId(Long id);

  /**
   *  业务号
   */
  @Column(
          name = "`business_key`"
  )
  String getBusinessKey();

  void setBusinessKey(String businessKey);

  /**
   *  开票类型, 1:普票， 2: 专票
   */
  @Column(
          name = "`type`"
  )
  Byte getType();

  void setType(Byte type);

  /**
   *  开票主体
   */
  @Column(
          name = "`target`"
  )
  String getTarget();

  void setTarget(String target);

  /**
   *  开票主体id
   */
  @Column(
          name = "`target_id`"
  )
  String getTargetId();

  void setTargetId(String targetId);

  /**
   *  开票金额
   */
  @Column(
          name = "`amount`"
  )
  @Cent2Yuan
  Long getAmount();

  void setAmount(Long amount);

  /**
   * 订单总金额，用于校验开票金额不大于订单总金额
   * @return
   */
  @Cent2Yuan
  Long getTotalAmount();
  void setTotalAmount(Long totalAmount);

  /**
   *  开票日期
   */
  @Column(
          name = "`invoice_date`"
  )
  LocalDate getInvoiceDate();

  void setInvoiceDate(LocalDate invoiceDate);

  /**
   *  开票人
   */
  @Column(
          name = "`creator_id`"
  )
  Long getCreatorId();

  void setCreatorId(Long creatorId);

  /**
   *  开票人名称
   */
  @Column(
          name = "`creator`"
  )
  String getCreator();

  void setCreator(String creator);

  /**
   *  备注
   */
  @Column(
          name = "`notes`"
  )
  String getNotes();

  void setNotes(String notes);

  /**
   *  创建时间
   */
  @Column(
          name = "`create_time`"
  )
  LocalDateTime getCreateTime();

  void setCreateTime(LocalDateTime createTime);

  /**
   *  修改时间
   */
  @Column(
          name = "`modify_time`"
  )
  LocalDateTime getModifyTime();

  void setModifyTime(LocalDateTime modifyTime);

  /**
   *  市场id
   */
  @Column(
          name = "`firm_id`"
  )
  Long getFirmId();

  void setFirmId(Long firmId);
}
