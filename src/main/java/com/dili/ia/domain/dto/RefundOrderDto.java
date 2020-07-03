package com.dili.ia.domain.dto;

import com.dili.ia.domain.RefundFeeItem;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.TransferDeductionItem;
import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 租赁订单列表查询dto
 * This file was generated on 2020-02-11 15:54:49.
 */
public interface RefundOrderDto extends RefundOrder {
    @Column(name = "`create_time`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    Date getCreatedStart();
    void setCreatedStart(Date createdStart);

    @Column(name = "`create_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    Date getCreatedEnd();
    void setCreatedEnd(Date createdEnd);

    @Operator(Operator.IN)
    @Column(name = "code")
    List<String> getCodes();
    void setCodes(List<String> codes);

    /**
     * 昵称模糊查询
     * @return
     */
    @Column(name = "customer_name")
    @Like
    String getLikeCustomerName();
    void setLikeCustomerName(String likeCustomerName);

    @Operator(Operator.IN)
    @Column(name = "id")
    List<Long> getIds();
    void setIds(List<Long> ids);

    //转抵扣
    List<TransferDeductionItem> getTransferDeductionItems();
    void setTransferDeductionItems(List<TransferDeductionItem> transferDeductionItems);

    //业务退款项
    List<RefundFeeItem> getRefundFeeItems();
    void setRefundFeeItems(List<RefundFeeItem> refundFeeItems);

    @Transient
    LocalDateTime getStopTime();

    void setStopTime(LocalDateTime stopTime);

}