package com.dili.ia.domain.dto;

import com.dili.ia.domain.IdTextPair;
import com.dili.ia.domain.LeaseOrder;
import com.dili.ia.domain.LeaseOrderItem;
import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 租赁订单列表查询dto
 * This file was generated on 2020-02-11 15:54:49.
 */
public interface LeaseOrderListDto extends LeaseOrder{
    @Column(name = "`create_time`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    Date getCreatedStart();
    void setCreatedStart(Date createdStart);

    @Column(name = "`create_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    Date getCreatedEnd();
    void setCreatedEnd(Date createdEnd);

    @Column(name = "`start_time`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    Date getStartTimeStart();
    void setStartTimeStart(Date startTimeStart);

    @Column(name = "`start_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    Date getStartTimeEnd();
    void setStartTimeEnd(Date startTimeEnd);

    @Operator(Operator.IN)
    @Column(name = "code")
    List<String> getCodes();
    void setCodes(List<String> codes);

    @Operator(Operator.IN)
    @Column(name = "id")
    List<Long> getIds();
    void setIds(List<Long> ids);

    /**
     * 昵称模糊查询
     * @return
     */
    @Column(name = "customer_name")
    @Like(value = "RIGHT")
    String getLikeCustomerName();
    void setLikeCustomerName(String likeCustomerName);

    /**
     * 创建人模糊查询
     * @return
     */
    @Column(name = "creator")
    @Like(value = "RIGHT")
    String getLikeCreator();
    void setLikeCreator(String likeCreator);

    //摊位名称
    @Transient
    String getBoothName();
    void setBoothName(String boothName);

    @Transient
    List<LeaseOrderItem> getLeaseOrderItems();
    void setLeaseOrderItems(List<LeaseOrderItem> leaseOrderItems);

    @Column(name = "`start_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    Date getStartTimeLT();
    void setStartTimeLT(Date startTimeLT);

    @Column(name = "`end_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    Date getEndTimeLT();
    void setEndTimeLT(Date endTimeLT);

    @Operator(Operator.IN)
    @Column(name = "market_id")
    List<Long> getMarketIds();
    void setMarketIds(List<Long> marketIds);

    @Operator(Operator.IN)
    @Column(name = "department_id")
    List<Long> getDepartmentIds();
    void setDepartmentIds(List<Long> departmentIds);

    @Transient
    List<IdTextPair> getCategorys();
    void setCategorys(List<IdTextPair> categorys);
}