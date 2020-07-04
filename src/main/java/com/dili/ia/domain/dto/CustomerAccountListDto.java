package com.dili.ia.domain.dto;

import com.dili.ia.domain.CustomerAccount;
import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
import java.util.List;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-19 11:43
 */
public class CustomerAccountListDto extends CustomerAccount {
    /**
     * 昵称模糊查询
     * @return
     */
    @Column(name = "customer_name")
    @Like
    private String likeCustomerName;

    @Operator(Operator.IN)
    @Column(name = "market_id")
    private List<Long> marketIds;

    public String getLikeCustomerName() {
        return likeCustomerName;
    }

    public void setLikeCustomerName(String likeCustomerName) {
        this.likeCustomerName = likeCustomerName;
    }

    public List<Long> getMarketIds() {
        return marketIds;
    }

    public void setMarketIds(List<Long> marketIds) {
        this.marketIds = marketIds;
    }
}
