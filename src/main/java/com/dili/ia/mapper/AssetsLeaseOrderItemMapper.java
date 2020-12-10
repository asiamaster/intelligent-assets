package com.dili.ia.mapper;

import com.dili.ia.domain.AssetsLeaseOrderItem;
import com.dili.ia.domain.dto.AssetsLeaseOrderItemListDto;
import com.dili.ss.base.MyMapper;

public interface AssetsLeaseOrderItemMapper extends MyMapper<AssetsLeaseOrderItem> {
    /**
     * 根据表地址查询是否处于租期状态和相应的用户
     *
     * @param  assetsLeaseOrderItem
     * @return AssetsLeaseOrderItemListDto
     * @date   2020/12/10
     */
    AssetsLeaseOrderItemListDto getCustomerByAssetsIdAndAssetsType(AssetsLeaseOrderItem assetsLeaseOrderItem);
}