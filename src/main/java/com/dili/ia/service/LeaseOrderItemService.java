package com.dili.ia.service;

import com.dili.ia.domain.LeaseOrder;
import com.dili.ia.domain.LeaseOrderItem;
import com.dili.ia.mapper.LeaseOrderItemMapper;
import com.dili.ia.mapper.LeaseOrderMapper;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

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
     * 扫描等待停租的摊位
     * @return
     */
    BaseOutput<Boolean> scanWaitStopRentLeaseOrder();
}