package com.dili.ia.domain.dto;

import com.dili.ia.domain.Customer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/5 14:14
 */
public class CustomerQuery extends Customer {

    /**
     * 创建时间区间查询-开始
     */
    private LocalDateTime createTimeStart;
    /**
     * 创建时间区间查询-结束
     */
    private LocalDateTime createTimeEnd;

    private String certificateNumberMatch;

    /**
     * 客户所属组织
     */
    private Long marketId;

    /**
     * 客户所属组织集
     */
    private List<Long> marketIdList;


    public LocalDateTime getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(LocalDateTime createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public LocalDateTime getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(LocalDateTime createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getCertificateNumberMatch() {
        return certificateNumberMatch;
    }

    public void setCertificateNumberMatch(String certificateNumberMatch) {
        this.certificateNumberMatch = certificateNumberMatch;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public List<Long> getMarketIdList() {
        return marketIdList;
    }

    public void setMarketIdList(List<Long> marketIdList) {
        this.marketIdList = marketIdList;
    }
}
