package com.dili.ia.service;

import com.dili.assets.sdk.dto.ChargeItemDto;
import com.dili.ia.domain.BusinessChargeItem;
import com.dili.ss.base.BaseService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 16:13:04.
 */
public interface BusinessChargeItemService extends BaseService<BusinessChargeItem, Long> {
    /**
     * 查询业务收费项（行转列）
     * @param bizType
     * @param businessIds
     * @param chargeItemDtos
     * @return
     */
    List<Map<String, String>> queryBusinessChargeItem(Integer bizType, List<Long> businessIds, List<ChargeItemDto> chargeItemDtos);
}