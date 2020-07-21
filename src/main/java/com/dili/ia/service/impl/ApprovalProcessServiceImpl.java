package com.dili.ia.service.impl;

import com.dili.ia.domain.ApprovalProcess;
import com.dili.ia.mapper.ApprovalProcessMapper;
import com.dili.ia.service.ApprovalProcessService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-15 16:59:23.
 */
@Service
public class ApprovalProcessServiceImpl extends BaseServiceImpl<ApprovalProcess, Long> implements ApprovalProcessService {

    public ApprovalProcessMapper getActualDao() {
        return (ApprovalProcessMapper)getDao();
    }
}