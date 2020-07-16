package com.dili.ia.api;

import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.bpmc.sdk.domain.TaskMapping;
import com.dili.bpmc.sdk.dto.Assignment;
import com.dili.ia.domain.ApproverAssignment;
import com.dili.ia.domain.dto.ApproverAssignmentDto;
import com.dili.ia.glossary.BpmBusinessType;
import com.dili.ia.glossary.BpmConstants;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ia.service.ApproverAssignmentService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 租赁审批流程任务分配接口
 */
@RestController
@RequestMapping("/api/rentalApprovalProcessTaskAssignmentApi")
public class RentalApprovalProcessTaskAssignmentApi {
    @Autowired
    private AssetsRpc assetsRpc;

    @Autowired
    private ApproverAssignmentService approverAssignmentService;

    /**
     * 流程异常时的审批人id，用于在流程异常时，作为兜底的处理人
     */
    @Value("${ia.bpm.exceptionHandlerId:1}")
    private String exceptionHandlerId;

    /**
     * 市场负责人审批
     * @param taskMapping 任务代理对象
     * @return
     */
    @PostMapping(value="/managerApprovalAssignment")
    public BaseOutput<Assignment> managerApprovalAssignment(@RequestBody TaskMapping taskMapping) {
        try {
            return BaseOutput.successData(buildApprovalAssignment(taskMapping, BpmBusinessType.RENTAL_APPROVAL.getCode(), BpmConstants.TK_MANAGER_APPROVAL));
        } catch (Exception e) {
            //流程异常时的审批人id，用于在流程异常时，作为兜底的处理人
            return BaseOutput.successData(getExceptionHandlerAssignment());
        }
    }

    /**
     * 副总经理审批
     * @param taskMapping 任务代理对象
     * @return
     */
    @PostMapping(value="/viceGeneralManagerApprovalAssignment")
    public BaseOutput<Assignment> viceGeneralManagerApprovalAssignment(@RequestBody TaskMapping taskMapping) {
        try {
            return BaseOutput.successData(buildApprovalAssignment(taskMapping, BpmBusinessType.RENTAL_APPROVAL.getCode(), BpmConstants.TK_VICE_GENERAL_MANAGER_APPROVAL));
        } catch (Exception e) {
            //流程异常时的审批人id，用于在流程异常时，作为兜底的处理人
            return BaseOutput.successData(getExceptionHandlerAssignment());
        }
    }

    /**
     * 总经理审批
     * @param taskMapping 任务代理对象
     * @return
     */
    @PostMapping(value="/generalManagerApprovalAssignment")
    public BaseOutput<Assignment> generalManagerApprovalAssignment(@RequestBody TaskMapping taskMapping) {
        try {
            return BaseOutput.successData(buildApprovalAssignment(taskMapping, BpmBusinessType.RENTAL_APPROVAL.getCode(), BpmConstants.TK_GENERAL_MANAGER_APPROVAL));
        } catch (Exception e) {
            //流程异常时的审批人id，用于在流程异常时，作为兜底的处理人
            return BaseOutput.successData(getExceptionHandlerAssignment());
        }
    }

    /**
     * 构建异常处理人分配
     * @return
     */
    private Assignment getExceptionHandlerAssignment(){
        Assignment assignment = DTOUtils.newInstance(Assignment.class);
        assignment.setAssignee(exceptionHandlerId);
        return assignment;
    }

    /**
     * 构建异处理人分配
     * @return
     */
    private Assignment getHandlerAssignment(String handlerId){
        Assignment assignment = DTOUtils.newInstance(Assignment.class);
        assignment.setAssignee(handlerId);
        return assignment;
    }

    /**
     * 构建审批分配
     * @param taskMapping
     * @param businessType
     * @param taskDefinitionKey
     * @return
     */
    private Assignment buildApprovalAssignment(TaskMapping taskMapping, Integer businessType, String taskDefinitionKey){
        Map<String, Object> processVariables = taskMapping.getProcessVariables();
        //获取流程参数中的区域id，以确认审批人
        Long districtId = (Long)processVariables.get("districtId");
        //根据区域ID查询本身和子节点
        BaseOutput<List<DistrictDTO>> listBaseOutput = assetsRpc.listDistrictChild(districtId);
        if(!listBaseOutput.isSuccess()){
            //流程异常时的审批人id，用于在流程异常时，作为兜底的处理人
            return getExceptionHandlerAssignment();
        }
        //区域id列表
        List<Long> districtIds = listBaseOutput.getData().stream().map(t -> t.getId()).collect(Collectors.toList());
        ApproverAssignmentDto approverAssignment = new ApproverAssignmentDto();
        approverAssignment.setBusinessType(businessType);
        approverAssignment.setTaskDefinitionKey(taskDefinitionKey);
        approverAssignment.setIds(districtIds);
        //查询审批人分配，只要业务类型(可理解为流程定义)，任务定义和区域id满足，则认为第一条数据的用户是任务执行人
        List<ApproverAssignment> approverAssignments = approverAssignmentService.listByExample(approverAssignment);
        if(CollectionUtils.isEmpty(approverAssignments)){
            //流程异常时的审批人id，用于在流程异常时，作为兜底的处理人
            return getExceptionHandlerAssignment();
        }
        return getHandlerAssignment(approverAssignments.get(0).getUserId().toString());
    }

}
