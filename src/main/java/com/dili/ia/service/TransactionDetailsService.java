package com.dili.ia.service;

import com.dili.ia.domain.TransactionDetails;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
public interface TransactionDetailsService extends BaseService<TransactionDetails, Long> {

    /**构建业务流水对象
     * @param sceneType 场景类型
     * @param bizType 业务类型
     * @param itemType 业务场景
     * @param amount 交易金额
     * @param orderId 订单ID
     * @param orderCode 订单code
     * @param customerId 客户ID
     * @param notes 详情
     * @param marketId 市场ID
     * @return TransactionDetails
     *
     */
    TransactionDetails buildByConditions(Integer sceneType, Integer bizType, Integer itemType, Long amount, Long orderId, String orderCode, Long customerId, String notes, Long marketId);

}