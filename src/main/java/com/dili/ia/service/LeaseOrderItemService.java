package com.dili.ia.service;

import com.dili.ia.domain.LeaseOrder;
import com.dili.ia.domain.LeaseOrderItem;
import com.dili.ia.domain.dto.LeaseOrderItemListDto;
import com.dili.ia.mapper.LeaseOrderItemMapper;
import com.dili.ia.mapper.LeaseOrderMapper;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-11 15:54:49.
 */
public interface LeaseOrderItemService extends BaseService<LeaseOrderItem, Long> {

    /**
     * 停租
     * @param leaseOrderItem
     * @return
     */
    BaseOutput stopRent(LeaseOrderItem leaseOrderItem);

    /**
     * 查询保证金可用抵扣的订单项
     * @param leaseOrderItem
     * @return
     */
    Map<Long,List<LeaseOrderItem>> queryDepositAmountAvailableItem(LeaseOrderItemListDto leaseOrderItem);

    /**
     * 摊位订单项停租处理
     * @param o
     */
    void stopRentLeaseOrderItemFromTimer(LeaseOrderItem o);
}