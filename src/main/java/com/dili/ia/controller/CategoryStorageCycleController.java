package com.dili.ia.controller;

import com.dili.ia.domain.CategoryStorageCycle;
import com.dili.ia.service.CategoryStorageCycleService;
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
 * This file was generated on 2020-07-16 15:35:44.
 */
@Controller
@RequestMapping("/categoryStorageCycle")
public class CategoryStorageCycleController {
    @Autowired
    CategoryStorageCycleService categoryStorageCycleService;

    /**
     * 跳转到CategoryStorageCycle页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "categoryStorageCycle/index";
    }

    /**
     * 分页查询CategoryStorageCycle，返回easyui分页信息
     * @param categoryStorageCycle
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute CategoryStorageCycle categoryStorageCycle) throws Exception {
        return categoryStorageCycleService.listEasyuiPageByExample(categoryStorageCycle, true).toString();
    }

    /**
     * 新增CategoryStorageCycle
     * @param categoryStorageCycle
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute CategoryStorageCycle categoryStorageCycle) {
        categoryStorageCycleService.insertSelective(categoryStorageCycle);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改CategoryStorageCycle
     * @param categoryStorageCycle
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute CategoryStorageCycle categoryStorageCycle) {
        categoryStorageCycleService.updateSelective(categoryStorageCycle);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除CategoryStorageCycle
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        categoryStorageCycleService.delete(id);
        return BaseOutput.success("删除成功");
    }
}