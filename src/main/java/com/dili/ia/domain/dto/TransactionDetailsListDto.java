package com.dili.ia.domain.dto;

import com.dili.ia.domain.TransactionDetails;
import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-19 14:26
 */
public interface TransactionDetailsListDto extends TransactionDetails {
    @Column(name = "`create_time`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    Date getCreatedStart();
    void setCreatedStart(Date createdStart);

    @Column(name = "`create_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    Date getCreatedEnd();
    void setCreatedEnd(Date createdEnd);

    /**
     * 昵称模糊查询
     * @return
     */
    @Column(name = "customer_name")
    @Like
    String getLikeCustomerName();
    void setLikeCustomerName(String likeCustomerName);

    @Operator(Operator.IN)
    @Column(name = "market_id")
    List<Long> getMarketIds();
    void setMarketIds(List<Long> marketIds);
}
