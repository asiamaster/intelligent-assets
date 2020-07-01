package com.dili.ia.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.ia.domain.dto.StockInDetailQueryDto;
import com.dili.ia.service.StockInDetailService;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.github.pagehelper.Page;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Controller
@RequestMapping("/stock/stockInDetail")
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
        return "stock/stockInDetail/index";
    }
    
    /**
     * 跳转到StockInDetail详情页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap,String code) {
    	modelMap.putAll(stockInDetailService.viewStockInDetail(code));
        return "stock/stockInDetail/view";
    }
    
    /**
     * 分页查询StockInDetail，返回easyui分页信息
     * @param stockInDetail
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute StockInDetailQueryDto stockInDetailQueryDto) throws Exception {
    	Page<Map<String, String>> page = stockInDetailService.selectByContion(stockInDetailQueryDto);
    	Map<String, String> map = stockInDetailQueryDto.getMetadata();
    	List<Map> result = ValueProviderUtils.buildDataByProvider(map, page.getResult());
    	return new EasyuiPageOutput(Integer.parseInt(String.valueOf(page.getTotal())), result ).toString();
    }

   
}