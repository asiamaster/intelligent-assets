package com.dili.ia.service.impl;

import com.dili.ia.domain.DepartmentChargeItem;
import com.dili.ia.mapper.DepartmentChargeItemMapper;
import com.dili.ia.service.DepartmentChargeItemService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-21 18:08:01.
 */
@Service
public class DepartmentChargeItemServiceImpl extends BaseServiceImpl<DepartmentChargeItem, Long> implements DepartmentChargeItemService {

    public DepartmentChargeItemMapper getActualDao() {
        return (DepartmentChargeItemMapper)getDao();
    }
}