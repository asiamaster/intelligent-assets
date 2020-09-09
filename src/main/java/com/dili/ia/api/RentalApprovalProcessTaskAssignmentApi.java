package com.dili.ia.api;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.bpmc.sdk.domain.TaskMapping;
import com.dili.bpmc.sdk.dto.Assignment;
import com.dili.ia.domain.ApproverAssignment;
import com.dili.ia.domain.dto.ApproverAssignmentDto;
import com.dili.ia.service.ApproverAssignmentService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 租赁审批流程任务分配接口
 * BPMC的动态任务分配调用
 */
@RestController
@RefreshScope
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
//    @NacosValue(value = "${ia.bpm.exceptionHandlerId:1}", autoRefreshed = true)
    private String exceptionHandlerId;

    /**
     * 摊位租赁审批, 摊位退款审批 -> 市场负责人审批
     * @param taskMapping 任务代理对象
     * @return
     */
    @PostMapping(value="/managerApprovalAssignment")
    public BaseOutput<Assignment> managerApprovalAssignment(TaskMapping taskMapping) {
        try {
            return BaseOutput.successData(buildApprovalAssignment(taskMapping));
        } catch (Exception e) {
            //流程异常时的审批人id，用于在流程异常时，作为兜底的处理人
            return BaseOutput.successData(getExceptionHandlerAssignment());
        }
    }

    /**
     * 摊位退款审批 -> 复核人审批
     * @param taskMapping 任务代理对象
     * @return
     */
    @PostMapping(value="/checkerApprovalAssignment")
    public BaseOutput<Assignment> checkerApprovalAssignment(TaskMapping taskMapping) {
        try {
            return BaseOutput.successData(buildApprovalAssignment(taskMapping));
        } catch (Exception e) {
            //流程异常时的审批人id，用于在流程异常时，作为兜底的处理人
            return BaseOutput.successData(getExceptionHandlerAssignment());
        }
    }


    /**
     * 摊位租赁审批, 摊位退款审批 -> 副总经理审批
     * @param taskMapping 任务代理对象
     * @return
     */
    @PostMapping(value="/viceGeneralManagerApprovalAssignment")
    public BaseOutput<Assignment> viceGeneralManagerApprovalAssignment(TaskMapping taskMapping) {
        try {
            return BaseOutput.successData(buildApprovalAssignment(taskMapping));
        } catch (Exception e) {
            //流程异常时的审批人id，用于在流程异常时，作为兜底的处理人
            return BaseOutput.successData(getExceptionHandlerAssignment());
        }
    }

    /**
     * 摊位退款审批 -> 工程部审批
     * @param taskMapping 任务代理对象
     * @return
     */
    @PostMapping(value="/engineeringDepartmentApprovalAssignment")
    public BaseOutput<Assignment> engineeringDepartmentApprovalAssignment(TaskMapping taskMapping) {
        try {
            return BaseOutput.successData(buildApprovalAssignment(taskMapping));
        } catch (Exception e) {
            //流程异常时的审批人id，用于在流程异常时，作为兜底的处理人
            return BaseOutput.successData(getExceptionHandlerAssignment());
        }
    }

    /**
     * 摊位租赁审批, 摊位退款审批 -> 总经理审批
     * @param taskMapping 任务代理对象
     * @return
     */
    @PostMapping(value="/generalManagerApprovalAssignment")
    public BaseOutput<Assignment> generalManagerApprovalAssignment(TaskMapping taskMapping) {
        try {
            return BaseOutput.successData(buildApprovalAssignment(taskMapping));
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
     * 构建处理人分配
     * @return
     */
    private Assignment getHandlerAssignment(String handlerId){
        Assignment assignment = DTOUtils.newInstance(Assignment.class);
        assignment.setAssignee(handlerId);
        return assignment;
    }

    /**
     * 构建审批分配, 根据流程参数中的districtId， 流程定义Key和任务定义Key从ApproverAssignment中获取唯一的受托人
     * @param taskMapping
     * @return
     */
    private Assignment buildApprovalAssignment(TaskMapping taskMapping){
//        Map<String, Object> processVariables = taskMapping.getProcessVariables();
        //获取流程参数中的区域id，以确认审批人
//        Long districtId = Long.parseLong((String)processVariables.get("districtId"));
//        BaseOutput<DistrictDTO> districtById = assetsRpc.getDistrictById(districtId);
//        //查询失败，则返回默认处理人
//        if(!districtById.isSuccess()){
//            //流程异常时的审批人id，用于在流程异常时，作为兜底的处理人
//            return getExceptionHandlerAssignment();
//        }
        //审批人分配(现在不需要根据区域获取审批人，这里注释掉)
//        List<ApproverAssignment> approverAssignments = listApproverAssignmentByDistrict(districtById.getData(), taskMapping);

        //根据流程和任务查询审批人分配
        ApproverAssignmentDto approverAssignment = DTOUtils.newInstance(ApproverAssignmentDto.class);
        approverAssignment.setProcessDefinitionKey(taskMapping.getProcessDefinitionKey());
        approverAssignment.setTaskDefinitionKey(taskMapping.getTaskDefinitionKey());
        Object firmId = taskMapping.getProcessVariables().get("firmId");
        if(firmId != null) {
            approverAssignment.setFirmId(Long.parseLong(firmId.toString()));
        }
        List<ApproverAssignment> approverAssignments = approverAssignmentService.listByExample(approverAssignment);
        //如果未找到审批人
        if(CollectionUtils.isEmpty(approverAssignments)){
            //流程异常时的审批人id，用于在流程异常时，作为兜底的处理人
            return getExceptionHandlerAssignment();
        }
        return getHandlerAssignment(approverAssignments.get(0).getAssignee().toString());
    }

    /**
     * 根据区域构建审批人分配
     * @param districtDTO
     * @param taskMapping
     * @return
     */
    private List<ApproverAssignment> listApproverAssignmentByDistrict( DistrictDTO districtDTO, TaskMapping taskMapping){
        //审批人分配
        List<ApproverAssignment> approverAssignments = null;
        //审批人分配查询条件
        ApproverAssignmentDto approverAssignment = DTOUtils.newInstance(ApproverAssignmentDto.class);
        approverAssignment.setProcessDefinitionKey(taskMapping.getProcessDefinitionKey());
        approverAssignment.setTaskDefinitionKey(taskMapping.getTaskDefinitionKey());
        //如果当前区域是一级区域，则从其本身找审批人
        if(districtDTO.getParentId().equals(0L)){
            //根据区域ID查询本身和子节点
            BaseOutput<List<DistrictDTO>> listBaseOutput = assetsRpc.listDistrictChild(districtDTO.getId());
            if(!listBaseOutput.isSuccess()){
                return null;
            }
//            //区域id列表
//            List<Long> districtIds = listBaseOutput.getData().stream().map(t -> t.getId()).collect(Collectors.toList());
            approverAssignment.setDistrictId(districtDTO.getId());
            //查询审批人分配，只要业务类型(可理解为流程定义)，任务定义和区域id满足，则认为第一条数据的用户是任务执行人
            approverAssignments = approverAssignmentService.listByExample(approverAssignment);
        }else{//如果是二级区域，则从当前区域和其上级区域找审批人
            approverAssignment.setDistrictIds(Lists.newArrayList(districtDTO.getId(), districtDTO.getParentId()));
            approverAssignments = approverAssignmentService.listByExample(approverAssignment);
        }
        return approverAssignments;
    }

}
