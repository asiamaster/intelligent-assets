package com.dili.ia.controller;

import com.dili.ia.domain.StockWeighmanImg;
import com.dili.ia.service.StockWeighmanImgService;
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
@RequestMapping("/stockWeighmanImg")
public class StockWeighmanImgController {
    @Autowired
    StockWeighmanImgService stockWeighmanImgService;

    /**
     * 跳转到StockWeighmanImg页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "stockWeighmanImg/index";
    }

    /**
     * 分页查询StockWeighmanImg，返回easyui分页信息
     * @param stockWeighmanImg
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute StockWeighmanImg stockWeighmanImg) throws Exception {
        return stockWeighmanImgService.listEasyuiPageByExample(stockWeighmanImg, true).toString();
    }

    /**
     * 新增StockWeighmanImg
     * @param stockWeighmanImg
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute StockWeighmanImg stockWeighmanImg) {
        stockWeighmanImgService.insertSelective(stockWeighmanImg);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改StockWeighmanImg
     * @param stockWeighmanImg
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute StockWeighmanImg stockWeighmanImg) {
        stockWeighmanImgService.updateSelective(stockWeighmanImg);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除StockWeighmanImg
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        stockWeighmanImgService.delete(id);
        return BaseOutput.success("删除成功");
    }
}