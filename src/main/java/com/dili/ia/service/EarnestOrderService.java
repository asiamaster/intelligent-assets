package com.dili.ia.service;

import com.dili.ia.domain.EarnestOrder;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
public interface EarnestOrderService extends BaseService<EarnestOrder, Long> {
    /**
     * 新增定金单，需要先检查客户账户是否存在
     * */
    int addEarnestOrder(EarnestOrder earnestOrder);
}