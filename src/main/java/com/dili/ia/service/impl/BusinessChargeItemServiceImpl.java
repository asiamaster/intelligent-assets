package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.ChargeItemDto;
import com.dili.ia.domain.BusinessChargeItem;
import com.dili.ia.mapper.BusinessChargeItemMapper;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 16:13:04.
 */
@Service
public class BusinessChargeItemServiceImpl extends BaseServiceImpl<BusinessChargeItem, Long> implements BusinessChargeItemService {

    public BusinessChargeItemMapper getActualDao() {
        return (BusinessChargeItemMapper)getDao();
    }

    @Override
    public List<Map<String, String>> queryBusinessChargeItem(Integer bizType, List<Long> businessIds, List<ChargeItemDto> chargeItemDtos) {
        return getActualDao().queryBusinessChargeItem(bizType,businessIds,chargeItemDtos);
    }
}