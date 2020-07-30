package com.dili.ia.service.impl;

import com.dili.ia.domain.ApproverAssignment;
import com.dili.ia.domain.dto.ApproverAssignmentDto;
import com.dili.ia.mapper.ApproverAssignmentMapper;
import com.dili.ia.service.ApproverAssignmentService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-22 11:16:04.
 */
@Service
public class ApproverAssignmentServiceImpl extends BaseServiceImpl<ApproverAssignment, Long> implements ApproverAssignmentService {

    public ApproverAssignmentMapper getActualDao() {
        return (ApproverAssignmentMapper)getDao();
    }

    @Override
    @Transactional
    public void updateEx(ApproverAssignmentDto approverAssignmentDto) {
        ApproverAssignment approverAssignment = DTOUtils.newInstance(ApproverAssignment.class);
        approverAssignment.setProcessDefinitionKey(approverAssignmentDto.getProcessDefinitionKey());
        approverAssignment.setTaskDefinitionKey(approverAssignmentDto.getTaskDefinitionKey());
        approverAssignment.setAssignee(approverAssignmentDto.getAssignee());
        approverAssignment.setRows(1);
        //查询原始的分配数据，用于判断是否修改流程、任务和办理人
        ApproverAssignment originalApproverAssignment = get(approverAssignmentDto.getId());
        //如果修改了流程、任务和办理人
        //并且
        //修改后的流程、任务和办理人，在数据库中存在其它相同的流程、任务和办理人
        if(!isEqual(originalApproverAssignment, approverAssignmentDto) && !list(approverAssignment).isEmpty()){
            throw new BusinessException(ResultCode.DATA_ERROR, "已存在相同的流程、任务和办理人");
        }
        if (approverAssignmentDto.getDistrictIds() != null){
            ApproverAssignment deleteApproverAssignment = DTOUtils.newInstance(ApproverAssignment.class);
            deleteApproverAssignment.setAssignee(originalApproverAssignment.getAssignee());
            deleteApproverAssignment.setTaskDefinitionKey(originalApproverAssignment.getTaskDefinitionKey());
            deleteApproverAssignment.setProcessDefinitionKey(originalApproverAssignment.getProcessDefinitionKey());
            //先按分配人、流程定义和任务定义删除
            deleteByExample(deleteApproverAssignment);
            List<ApproverAssignment> approverAssignments = Lists.newArrayList();
            for (Long districtId : approverAssignmentDto.getDistrictIds()) {
                ApproverAssignment newApproverAssignment = DTOUtils.proxyInstance(approverAssignmentDto.aget(), ApproverAssignment.class);
                newApproverAssignment.setDistrictId(districtId);
                newApproverAssignment.setModifyTime(new Date());
                approverAssignments.add(newApproverAssignment);
            }
            //再重新进行批量插入
            batchInsert(approverAssignments);
        }else {
            updateSelective(approverAssignmentDto);
        }
    }

    /**
     * 判断是否相同的流程、任务和办理人
     * @param originalApproverAssignment
     * @param approverAssignmentDto
     * @return
     */
    private boolean isEqual(ApproverAssignment originalApproverAssignment, ApproverAssignmentDto approverAssignmentDto){
        return originalApproverAssignment.getProcessDefinitionKey().equals(approverAssignmentDto.getProcessDefinitionKey())
                && originalApproverAssignment.getTaskDefinitionKey().equals(approverAssignmentDto.getTaskDefinitionKey())
                && originalApproverAssignment.getAssignee().equals(approverAssignmentDto.getAssignee());
    }
}