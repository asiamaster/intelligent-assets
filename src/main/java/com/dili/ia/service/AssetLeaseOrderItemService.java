package com.dili.ia.service;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.ia.domain.AssetLeaseOrderItem;
import com.dili.ia.domain.dto.AssetLeaseOrderItemListDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

import java.time.LocalDateTime;
import java.util.List;

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
     * 停租摊位租赁
     * @param assetLeaseOrderItem
     * @param startTime
     * @param stopTime
     */
    void stopBoothRent(AssetLeaseOrderItem assetLeaseOrderItem, LocalDateTime startTime, LocalDateTime stopTime);

    /**
     * 摊位订单项停租处理
     * @param o
     */
    void stopRentLeaseOrderItemFromTimer(AssetLeaseOrderItem o);

    /**
     * PO lIST转DTO LIST
     * @param assetLeaseOrderItems
     * @param bizType
     * @param chargeItemDtos
     * @return
     */
    List<AssetLeaseOrderItemListDto> leaseOrderItemListToDto(List<AssetLeaseOrderItem> assetLeaseOrderItems, String bizType, List<BusinessChargeItemDto> chargeItemDtos);
}