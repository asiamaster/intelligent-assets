package com.dili.ia.mapper;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.ia.domain.BusinessChargeItem;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BusinessChargeItemMapper extends MyMapper<BusinessChargeItem> {
    /**
     * 查询业务收费项（行转列）
     * @param bizType
     * @param businessIds
     * @param chargeItemDtos
     * @return
     */
    List<Map<String, String>> queryBusinessChargeItem(@Param("bizType") String bizType, @Param("businessIds") List<Long> businessIds, @Param("chargeItemDtos") List<BusinessChargeItemDto> chargeItemDtos);
}