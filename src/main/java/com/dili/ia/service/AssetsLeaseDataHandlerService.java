package com.dili.ia.service;

import com.dili.ss.domain.BaseOutput;

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
     * 租赁单收费项数据处理
     * @return
     */
    BaseOutput leaseOrderChargeItemDataHandler();
    /**
     * 退款单单收费项数据处理
     * @return
     */
    BaseOutput refundOrderChargeItemDataHandler();
}
