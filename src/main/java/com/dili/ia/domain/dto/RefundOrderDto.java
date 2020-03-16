package com.dili.ia.domain.dto;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.TransferDeductionItem;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
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

    @Operator(Operator.IN)
    @Column(name = "id")
    List<Long> getIds();
    void setIds(List<Long> ids);

    //转抵扣
    List<TransferDeductionItem> getTransferDeductionItems();
    void setTransferDeductionItems(List<TransferDeductionItem> transferDeductionItems);

}