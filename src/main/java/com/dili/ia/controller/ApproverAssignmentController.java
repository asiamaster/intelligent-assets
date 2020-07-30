package com.dili.ia.controller;

import com.dili.ia.domain.ApproverAssignment;
import com.dili.ia.domain.dto.ApproverAssignmentDto;
import com.dili.ia.service.ApproverAssignmentService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 审批人任务分配控制器
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-22 11:16:04.
 */
@Controller
@RequestMapping("/approverAssignment")
public class ApproverAssignmentController {
    @Autowired
    ApproverAssignmentService approverAssignmentService;

    /**
     * 跳转到ApproverAssignment页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "approverAssignment/index";
    }


    /**
     * 分页查询ApproverAssignment，返回easyui分页信息
     * @param approverAssignment
     * @return String
     * @throws Exception
     */
    @PostMapping(value="/listPage.action")
    public @ResponseBody String listPage(ApproverAssignment approverAssignment) throws Exception {
//        approverAssignment.setWhereSuffixSql("group by process_definition_key, task_definition_key, assignee");
//        HashSet<String> columns = Sets.newHashSet();
//        columns.add("group_concat(district_id) district");
//        columns.add("assignee");
//        columns.add("task_definition_key");
//        columns.add("process_definition_key");
//        columns.add("max(modify_time) modify_time");
//        columns.add("max(create_time) create_time");
//        approverAssignment.setSelectColumns(columns);
        return approverAssignmentService.listEasyuiPageByExample(approverAssignment, true).toString();
    }

    /**
     * 根据流程定义， 任务定义和办理人查询区域列表
     * @param approverAssignment
     * @return
     * @throws Exception
     */
    @PostMapping(value="/listDistricts.action")
    public @ResponseBody List<Map> listDistricts(ApproverAssignment approverAssignment) throws Exception {
        approverAssignment.setSelectColumns(Sets.newHashSet("district_id"));
        EasyuiPageOutput easyuiPageOutput = approverAssignmentService.listEasyuiPageByExample(approverAssignment, true);
        return easyuiPageOutput.getRows();

    }

    /**
     * 新增ApproverAssignment
     * @param approverAssignmentDto
     * @return BaseOutput
     */
    @PostMapping(value="/insert.action")
    public @ResponseBody BaseOutput insert(ApproverAssignmentDto approverAssignmentDto) {
        ApproverAssignment approverAssignment = DTOUtils.newInstance(ApproverAssignment.class);
        approverAssignment.setProcessDefinitionKey(approverAssignmentDto.getProcessDefinitionKey());
        approverAssignment.setTaskDefinitionKey(approverAssignmentDto.getTaskDefinitionKey());
        approverAssignment.setAssignee(approverAssignmentDto.getAssignee());
        approverAssignment.setDistrictId(approverAssignmentDto.getDistrictId());
        approverAssignment.setRows(1);
        if(!approverAssignmentService.list(approverAssignment).isEmpty()){
            return BaseOutput.failure("当前数据已存在");
        }
        approverAssignmentService.insertSelective(approverAssignmentDto);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改ApproverAssignment
     * @param approverAssignmentDto
     * @return BaseOutput
     */
    @PostMapping(value="/update.action")
    public @ResponseBody BaseOutput update(ApproverAssignmentDto approverAssignmentDto) {
        try {
            approverAssignmentService.updateEx(approverAssignmentDto);
            return BaseOutput.success("修改成功");
        }catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 删除ApproverAssignment
     * @param id
     * @return BaseOutput
     */
    @PostMapping(value="/delete.action")
    public @ResponseBody BaseOutput delete(Long id) {
        approverAssignmentService.delete(id);
        return BaseOutput.success("删除成功");
    }

    /**
     * 删除ApproverAssignment
     * @param ids
     * @return BaseOutput
     */
    @PostMapping(value="/batchDelete.action")
    public @ResponseBody BaseOutput batchDelete(@RequestBody List<Long> ids) {
        approverAssignmentService.delete(ids);
        return BaseOutput.success("删除成功");
    }

}