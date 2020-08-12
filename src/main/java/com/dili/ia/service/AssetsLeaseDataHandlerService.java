package com.dili.ia.service;

import com.dili.ss.domain.BaseOutput;

import java.util.List;

/**
 * 租赁业务数据处理
 */
public interface AssetsLeaseDataHandlerService {
    /**
     * 租赁单数据处理
     * @return
     */
    BaseOutput leaseOrderDataHandler();

    /**
     * 迁移保证金数据到保证金模块
     * @return
     */
    BaseOutput moveDepositAmount();

    /**
     * 退款单数据处理
     * @return
     */
    BaseOutput refundOrderDataHandler();

    /**
     * 删除租赁单
     * @param leaseOrderIds
     * @return
     */
    BaseOutput deleteLeaseOrder(List<Long> leaseOrderIds);
}
