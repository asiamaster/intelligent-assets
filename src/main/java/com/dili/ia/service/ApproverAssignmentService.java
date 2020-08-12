package com.dili.ia.service;

import com.dili.ia.domain.ApproverAssignment;
import com.dili.ia.domain.dto.ApproverAssignmentDto;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-22 11:16:04.
 */
public interface ApproverAssignmentService extends BaseService<ApproverAssignment, Long> {

    /**
     * 根据districtId或者districtIds修改或批量修改审批人分配
     * @param approverAssignmentDto
     * @author wangmi
     * @Date 15:45 2020/7/29
    **/
    void updateEx(ApproverAssignmentDto approverAssignmentDto);
}