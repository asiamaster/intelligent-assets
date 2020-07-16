package com.dili.ia.service.impl;

import com.dili.ia.domain.CategoryStorageCycle;
import com.dili.ia.mapper.CategoryStorageCycleMapper;
import com.dili.ia.service.CategoryStorageCycleService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-16 15:35:44.
 */
@Service
public class CategoryStorageCycleServiceImpl extends BaseServiceImpl<CategoryStorageCycle, Long> implements CategoryStorageCycleService {

    public CategoryStorageCycleMapper getActualDao() {
        return (CategoryStorageCycleMapper)getDao();
    }
}