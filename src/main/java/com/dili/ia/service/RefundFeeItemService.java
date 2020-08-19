package com.dili.ia.service;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.ia.domain.RefundFeeItem;
import com.dili.ss.base.BaseService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 16:13:04.
 */
public interface RefundFeeItemService extends BaseService<RefundFeeItem, Long> {
    /**
     * 查询退款业务收费项（行转列）
     *
     * @param refundOrderIds
     * @param chargeItemDtos
     * @return
     */
    List<Map<String, String>> queryRefundFeeItem(List<Long> refundOrderIds, List<BusinessChargeItemDto> chargeItemDtos);
}