package com.dili.ia.service;

import com.dili.ia.domain.EarnestOrder;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-10 17:43:43.
 */
public interface EarnestOrderService extends BaseService<EarnestOrder, Long> {
    /**
    * 检查用户账户是否已经存在
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return Boolean true 可以账户在市场已经存在， false 客户账户在市场不存在
    * */
    Boolean checkCustomerExist(Long customerId, Long marketId);
}