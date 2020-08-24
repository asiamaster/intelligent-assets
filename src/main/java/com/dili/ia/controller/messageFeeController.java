package com.dili.ia.controller;

import com.dili.ia.domain.messageFee;
import com.dili.ia.service.messageFeeService;
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
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-08-24 16:16:50.
 */
@Controller
@RequestMapping("/messageFee")
public class messageFeeController {
    @Autowired
    messageFeeService messageFeeService;

    /**
     * 跳转到messageFee页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "messageFee/index";
    }

    /**
     * 分页查询messageFee，返回easyui分页信息
     * @param messageFee
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute messageFee messageFee) throws Exception {
        return messageFeeService.listEasyuiPageByExample(messageFee, true).toString();
    }

    /**
     * 新增messageFee
     * @param messageFee
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute messageFee messageFee) {
        messageFeeService.insertSelective(messageFee);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改messageFee
     * @param messageFee
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute messageFee messageFee) {
        messageFeeService.updateSelective(messageFee);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除messageFee
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        messageFeeService.delete(id);
        return BaseOutput.success("删除成功");
    }
}