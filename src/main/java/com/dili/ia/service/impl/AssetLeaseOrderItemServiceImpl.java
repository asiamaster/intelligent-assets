package com.dili.ia.service.impl;

import com.dili.ia.domain.AssetLeaseOrderItem;
import com.dili.ia.mapper.AssetLeaseOrderItemMapper;
import com.dili.ia.service.AssetLeaseOrderItemService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 14:40:05.
 */
@Service
public class AssetLeaseOrderItemServiceImpl extends BaseServiceImpl<AssetLeaseOrderItem, Long> implements AssetLeaseOrderItemService {

    public AssetLeaseOrderItemMapper getActualDao() {
        return (AssetLeaseOrderItemMapper)getDao();
    }
}