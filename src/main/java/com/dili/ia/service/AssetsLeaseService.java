package com.dili.ia.service;

import com.dili.ia.domain.AssetsLeaseOrder;
import com.dili.ia.domain.AssetsLeaseOrderItem;

import java.util.List;

/**
 * 资产租赁Service
 */
public interface AssetsLeaseService {
    /**
     * 资产类型
     * @return
     */
    Integer getAssetsType();

    /**
     * 检查资产
     * @param assetsIds
     * @param mchId
     * @param batchId
     * @return 最新商户
     */
    Long checkAssets(List<Long> assetsIds, Long mchId , String batchId);

    /**
     * 冻结资产
     * @param leaseOrder
     * @param leaseOrderItems
     */
    void frozenAsset(AssetsLeaseOrder leaseOrder, List<AssetsLeaseOrderItem> leaseOrderItems);

    /**
     * 释放订单所有资产
     * @param leaseOrderId
     */
    void unFrozenAllAsset(Long leaseOrderId);

    /**
     * 解冻消费摊位
     * @param leaseOrder
     */
    void leaseAsset(AssetsLeaseOrder leaseOrder);
}
