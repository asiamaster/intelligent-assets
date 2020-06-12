package com.dili.ia.controller;

import com.dili.ia.domain.StockInDetail;
import com.dili.ia.service.StockInDetailService;
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
 * This file was generated on 2020-06-12 11:14:28.
 */
@Controller
@RequestMapping("/stockInDetail")
public class StockInDetailController {
    @Autowired
    StockInDetailService stockInDetailService;

    /**
     * 跳转到StockInDetail页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "stockInDetail/index";
    }

    /**
     * 分页查询StockInDetail，返回easyui分页信息
     * @param stockInDetail
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute StockInDetail stockInDetail) throws Exception {
        return stockInDetailService.listEasyuiPageByExample(stockInDetail, true).toString();
    }

    /**
     * 新增StockInDetail
     * @param stockInDetail
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute StockInDetail stockInDetail) {
        stockInDetailService.insertSelective(stockInDetail);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改StockInDetail
     * @param stockInDetail
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute StockInDetail stockInDetail) {
        stockInDetailService.updateSelective(stockInDetail);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除StockInDetail
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        stockInDetailService.delete(id);
        return BaseOutput.success("删除成功");
    }
}