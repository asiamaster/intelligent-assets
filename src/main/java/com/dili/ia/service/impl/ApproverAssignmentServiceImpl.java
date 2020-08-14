package com.dili.ia.service.impl;

import com.dili.ia.domain.ApproverAssignment;
import com.dili.ia.domain.dto.ApproverAssignmentDto;
import com.dili.ia.mapper.ApproverAssignmentMapper;
import com.dili.ia.service.ApproverAssignmentService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
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
        //查询原始的分配数据，用于判断是否修改流程、任务和办理人
        ApproverAssignment originalApproverAssignment = get(approverAssignmentDto.getId());
        if (approverAssignmentDto.getDistrictIds() != null){
            //如果修改了流程、任务和办理人，则抛异常
            if(!isProcessTaskAssigneeEqual(originalApproverAssignment, approverAssignmentDto)){
                throw new BusinessException(ResultCode.DATA_ERROR, "已存在相同的流程、任务和办理人");
            }
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
            //如果修改了流程、任务和区域，则判断是否和已有的数据重复
            if(!isProcessTaskDistrictEqual(originalApproverAssignment, approverAssignmentDto)){
                ApproverAssignment approverAssignmentCondition = DTOUtils.newInstance(ApproverAssignment.class);
//                approverAssignmentCondition.setAssignee(approverAssignmentDto.getAssignee());
                approverAssignmentCondition.setTaskDefinitionKey(approverAssignmentDto.getTaskDefinitionKey());
                approverAssignmentCondition.setProcessDefinitionKey(approverAssignmentDto.getProcessDefinitionKey());
                approverAssignmentCondition.setDistrictId(approverAssignmentDto.getDistrictId());
                List<ApproverAssignment> select = getActualDao().select(approverAssignmentCondition);
                //在相同的区域、流程和任务定义，不允许有不同的审批人(以后支持多实例/会签功能后，会调整)
                if(!select.isEmpty()){
                    throw new BusinessException(ResultCode.DATA_ERROR, "已存在相同的审批人分配");
                }
            }
            updateSelective(approverAssignmentDto);
        }
    }

    /**
     * 判断是否相同的流程、任务和办理人
     * @param originalApproverAssignment
     * @param approverAssignmentDto
     * @return
     */
    private boolean isProcessTaskAssigneeEqual(ApproverAssignment originalApproverAssignment, ApproverAssignmentDto approverAssignmentDto){
        return originalApproverAssignment.getProcessDefinitionKey().equals(approverAssignmentDto.getProcessDefinitionKey())
                && originalApproverAssignment.getTaskDefinitionKey().equals(approverAssignmentDto.getTaskDefinitionKey())
                && originalApproverAssignment.getAssignee().equals(approverAssignmentDto.getAssignee());
    }

    /**
     * 判断是否相同的流程、任务和区域
     * @param originalApproverAssignment
     * @param approverAssignmentDto
     * @return
     */
    private boolean isProcessTaskDistrictEqual(ApproverAssignment originalApproverAssignment, ApproverAssignmentDto approverAssignmentDto){
        return originalApproverAssignment.getProcessDefinitionKey().equals(approverAssignmentDto.getProcessDefinitionKey())
                && originalApproverAssignment.getTaskDefinitionKey().equals(approverAssignmentDto.getTaskDefinitionKey())
                && originalApproverAssignment.getDistrictId().equals(approverAssignmentDto.getDistrictId());
    }
}