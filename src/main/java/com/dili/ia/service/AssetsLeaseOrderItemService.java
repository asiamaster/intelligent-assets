package com.dili.ia.service;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.ia.domain.AssetsLeaseOrderItem;
import com.dili.ia.domain.dto.AssetsLeaseOrderItemListDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 14:40:05.
 */
public interface AssetsLeaseOrderItemService extends BaseService<AssetsLeaseOrderItem, Long> {
    /**
     * 停租
     * @param leaseOrderItem
     * @return
     */
    BaseOutput stopRent(AssetsLeaseOrderItemListDto leaseOrderItem);
    /**
     * 停租摊位租赁
     * @param assetsLeaseOrderItem
     * @param startTime
     * @param stopTime
     */
    void stopAssetsRent(AssetsLeaseOrderItem assetsLeaseOrderItem, LocalDateTime startTime, LocalDateTime stopTime);

    /**
     * 摊位订单项停租处理
     * @param o
     */
    void stopRentLeaseOrderItemFromTimer(AssetsLeaseOrderItem o);

    /**
     * PO lIST转DTO LIST
     * @param assetsLeaseOrderItems
     * @param bizType
     * @param chargeItemDtos
     * @return
     */
    List<AssetsLeaseOrderItemListDto> leaseOrderItemListToDto(List<AssetsLeaseOrderItem> assetsLeaseOrderItems, String bizType, List<BusinessChargeItemDto> chargeItemDtos);

    /**
     * 根据表地址查询是否处于租期状态和相应的用户
     *
     * @param  assetsLeaseOrderItem
     * @return AssetsLeaseOrderItemListDto
     * @date   2020/12/10
     */
    AssetsLeaseOrderItemListDto getCustomerByAssetsIdAndAssetsType(AssetsLeaseOrderItem assetsLeaseOrderItem);
}