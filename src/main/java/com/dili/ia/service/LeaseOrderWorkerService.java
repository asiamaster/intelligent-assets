package com.dili.ia.service;

import com.dili.ss.domain.BaseOutput;

/**
 * 租赁订单任务调度业务执行service
 */
public interface LeaseOrderWorkerService {
    /**
     * 扫描已生效但状态未变更的单子，更改其状态
     * @return
     */
    BaseOutput<Boolean> scanEffectiveLeaseOrder();

    /**
     * 扫描已到期但状态未变更的单子，更改其状态
     * @return
     */
    BaseOutput<Boolean> scanExpiredLeaseOrder();

    /**
     * 扫描等待停租的摊位
     * @return
     */
    BaseOutput<Boolean> scanWaitStopRentLeaseOrder();
}
