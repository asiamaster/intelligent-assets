package com.dili.ia.service;

import com.dili.ia.domain.LeaseOrder;
import com.dili.ia.domain.dto.LeaseOrderListDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-11 15:54:49.
 */
public interface LeaseOrderService extends BaseService<LeaseOrder, Long> {
    /**
     * 摊位租赁订单保存
     * @param dto
     * @return
     * @throws BusinessException
     */
    BaseOutput saveLeaseOrder(LeaseOrderListDto dto) throws BusinessException;
}