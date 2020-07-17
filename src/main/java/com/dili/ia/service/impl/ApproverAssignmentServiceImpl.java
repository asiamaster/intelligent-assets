package com.dili.ia.service.impl;

import com.dili.ia.domain.ApproverAssignment;
import com.dili.ia.mapper.ApproverAssignmentMapper;
import com.dili.ia.service.ApproverAssignmentService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-13 14:42:58.
 */
@Service
public class ApproverAssignmentServiceImpl extends BaseServiceImpl<ApproverAssignment, Long> implements ApproverAssignmentService {

    public ApproverAssignmentMapper getActualDao() {
        return (ApproverAssignmentMapper)getDao();
    }
}