package com.dili.ia.domain.dto;

import com.dili.ia.domain.LeaseOrder;
import com.dili.ia.domain.LeaseOrderItem;
import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import io.swagger.models.auth.In;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 租赁订单列表查询dto
 * This file was generated on 2020-02-11 15:54:49.
 */
public interface LeaseOrderItemListDto extends LeaseOrderItem {
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

    @Operator(Operator.IN)
    @Column(name = "`booth_id`")
    List<Long> getBoothIds();
    void setBoothIds(List<Long> boothIds);

    @Operator(Operator.NOT_IN)
    @Column(name = "`state`")
    List<Integer> getStateNotEquals();
    void setStateNotEquals(List<Integer> stateNotEquals);

    @Operator(Operator.IN)
    @Column(name = "`state`")
    List<Integer> getStates();
    void setStates(List<Integer> states);

}