package com.dili.ia.mapper;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.ia.domain.RefundFeeItem;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RefundFeeItemMapper extends MyMapper<RefundFeeItem> {
    /**
     * 查询退款业务收费项（行转列）
     * @param refundOrderIds
     * @param chargeItemDtos
     * @return
     */
    List<Map<String, String>> queryRefundFeeItem( @Param("refundOrderIds") List<Long> refundOrderIds, @Param("chargeItemDtos") List<BusinessChargeItemDto> chargeItemDtos);
}