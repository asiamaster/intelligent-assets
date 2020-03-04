package com.dili.ia.service.impl;

import com.dili.ia.domain.PrintTemplate;
import com.dili.ia.mapper.PrintTemplateMapper;
import com.dili.ia.service.PrintTemplateService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-03 14:30:11.
 */
@Service
public class PrintTemplateServiceImpl extends BaseServiceImpl<PrintTemplate, Long> implements PrintTemplateService {

    public PrintTemplateMapper getActualDao() {
        return (PrintTemplateMapper)getDao();
    }
}