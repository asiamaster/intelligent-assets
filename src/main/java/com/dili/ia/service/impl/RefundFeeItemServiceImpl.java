package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.ia.domain.RefundFeeItem;
import com.dili.ia.mapper.RefundFeeItemMapper;
import com.dili.ia.service.RefundFeeItemService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 16:13:04.
 */
@Service
public class RefundFeeItemServiceImpl extends BaseServiceImpl<RefundFeeItem, Long> implements RefundFeeItemService {

    public RefundFeeItemMapper getActualDao() {
        return (RefundFeeItemMapper) getDao();
    }

    @Override
    public List<Map<String, String>> queryRefundFeeItem(List<Long> refundOrderIds, List<BusinessChargeItemDto> chargeItemDtos) {
        return getActualDao().queryRefundFeeItem(refundOrderIds, chargeItemDtos);
    }
}