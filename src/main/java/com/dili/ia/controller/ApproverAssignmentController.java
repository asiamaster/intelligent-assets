package com.dili.ia.controller;

import com.dili.ia.domain.ApproverAssignment;
import com.dili.ia.service.ApproverAssignmentService;
import com.dili.ss.domain.BaseOutput;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute ApproverAssignment approverAssignment) throws Exception {
        return approverAssignmentService.listEasyuiPageByExample(approverAssignment, true).toString();
    }

    /**
     * 新增ApproverAssignment
     * @param approverAssignment
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute ApproverAssignment approverAssignment) {
        approverAssignmentService.insertSelective(approverAssignment);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改ApproverAssignment
     * @param approverAssignment
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute ApproverAssignment approverAssignment) {
        approverAssignmentService.updateSelective(approverAssignment);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除ApproverAssignment
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        approverAssignmentService.delete(id);
        return BaseOutput.success("删除成功");
    }
}