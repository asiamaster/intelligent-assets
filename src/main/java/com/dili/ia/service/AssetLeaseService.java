package com.dili.ia.service;

import com.dili.ia.domain.AssetLeaseOrder;
import com.dili.ia.domain.AssetLeaseOrderItem;

import java.util.List;

/**
 * 资产租赁Service
 */
public interface AssetLeaseService {
    /**
     * 资产类型
     * @return
     */
    Integer getAssetsType();
    /**
     * 检查资产状态
     * @param assetId
     */
    void checkAssetState(Long assetId);

    /**
     * 冻结资产
     * @param leaseOrder
     * @param leaseOrderItems
     */
    void frozenAsset(AssetLeaseOrder leaseOrder, List<AssetLeaseOrderItem> leaseOrderItems);

    /**
     * 解冻租赁订单所有资产
     * @param leaseOrderId
     */
    void unFrozenAllAsset(Long leaseOrderId);

    /**
     * 解冻消费摊位
     * @param leaseOrder
     */
    void leaseAsset(AssetLeaseOrder leaseOrder);
}
