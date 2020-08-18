package com.dili.ia.controller;

import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.CategoryDTO;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.ia.domain.dto.CategoryStorageCycleDto;
import com.dili.ia.service.CategoryStorageCycleService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-16 15:35:44.
 */
@Controller
@RequestMapping("/stock/categoryCycle")
public class CategoryStorageCycleController {
	
	private final static Logger LOG = LoggerFactory.getLogger(CategoryStorageCycleController.class);

	
	@Autowired
    private AssetsRpc assetsRpc;

	@Autowired
	private CategoryStorageCycleService categoryStorageCycleService;
	
	/**
     * 跳转到品类列表页
     */
    @RequestMapping("/list.html")
    public String list() {
        return "stock/categoryCycle/list";
    }
    
    /**
     * 获取品类树
     */
    @RequestMapping(value = "/getTree.action")
    @ResponseBody
    public BaseOutput<List<CategoryDTO>> getTree(CategoryDTO input) {
        try {
    		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
    		input.setMarketId(userTicket.getFirmId());
            return assetsRpc.list(input);
        } catch (Exception e) {
            return BaseOutput.failure(e.getMessage());
        }
    }
    
    /**
     * 品类列表页面
     *
     * @param input
     * @return
     */
    @RequestMapping("/table.html")
    public ModelAndView list(@ModelAttribute CategoryDTO input) {
    	Map<String, Object> result = new HashMap<>();
    	result.put("obj", categoryStorageCycleService.list(input));
        return new ModelAndView("stock/categoryCycle/table", result);
    }
    
    /**
     * 品类列表页面
     *
     * @param input
     * @return
     */
    @RequestMapping("/table.action")
    public @ResponseBody String listPage(@ModelAttribute CategoryDTO input) {
        Page<JSONObject> page = categoryStorageCycleService.list(input);
    	Map<String, String> map = input.getMetadata();
    	List<Map> result = null;
		try {
			result = ValueProviderUtils.buildDataByProvider(map, page.getResult());
		} catch (Exception e) {
        	LOG.error("品类加载失败",e.getMessage());
			e.printStackTrace();
		}
    	return new EasyuiPageOutput(Integer.parseInt(String.valueOf(page.getTotal())), result).toString();
    }

    /**
     * 保存品类存储周期
     */
    @RequestMapping(value = "/saveCycle.action")
    public @ResponseBody BaseOutput<List<CategoryDTO>> saveCycle(@RequestBody CategoryStorageCycleDto dto) {
        try {
        	categoryStorageCycleService.saveCycle(dto);
            return BaseOutput.success("保存成功!");
        } catch (Exception e) {
        	LOG.error("品类{}修改失败",dto.getCode(),e.getMessage());
            return BaseOutput.failure(e.getMessage());
        }
    }
    
    /**
     * 品类搜索获取存储周期
     */
    @RequestMapping(value = "/search.action")
    public @ResponseBody BaseOutput<JSONObject> searchCategory(String keyword) {
        try {
            return BaseOutput.success().setData(categoryStorageCycleService.searchCategory(keyword));
        } catch (Exception e) {
        	LOG.error("获取品类周期失败",e.getMessage());
            return BaseOutput.failure(e.getMessage());
        }
    }
    
    /**
     * 品类搜索
     */
    @RequestMapping(value = "/searchV2.action")
    public @ResponseBody BaseOutput<JSONObject> searchV2(String keyword) {
        try {
        	CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setMarketId(SessionContext.getSessionContext().getUserTicket().getFirmId());
            categoryDTO.setState(EnabledStateEnum.ENABLED.getCode());
            categoryDTO.setKeyword(keyword);
            //根据关键词查询品类
            return BaseOutput.success().setData(assetsRpc.list(categoryDTO).getData());
        } catch (Exception e) {
        	LOG.error("获取品类周期失败",e.getMessage());
            return BaseOutput.failure(e.getMessage());
        }
    }
     
}