package com.dili.ia.service;

import com.dili.ia.domain.AssetLeaseOrderItem;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 14:40:05.
 */
public interface AssetLeaseOrderItemService extends BaseService<AssetLeaseOrderItem, Long> {
    /**
     * 停租
     * @param leaseOrderItem
     * @return
     */
    BaseOutput stopRent(AssetLeaseOrderItem leaseOrderItem);

    /**
     * 摊位订单项停租处理
     * @param o
     */
    void stopRentLeaseOrderItemFromTimer(AssetLeaseOrderItem o);
}