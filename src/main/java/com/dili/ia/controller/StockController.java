package com.dili.ia.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.ia.domain.dto.StockDto;
import com.dili.ia.domain.dto.StockQueryDto;
import com.dili.ia.service.StockService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.metadata.ValueProviderUtils;
import com.github.pagehelper.Page;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Controller
@RequestMapping("/stock/stock")
public class StockController {
	
	private final static Logger LOG = LoggerFactory.getLogger(StockInController.class);
	
    @Autowired
    StockService stockService;

    /**
     * 跳转到出库页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/out.html", method = RequestMethod.GET)
    public String out(ModelMap modelMap) {
        return "stock/stock/out";
    }
    
    /**
     * 客户库存
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "stock/stock/index";
    }
    
    /**
     * 客户库存 入库列表
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/inList.html", method = RequestMethod.GET)
    public String inList(ModelMap modelMap,Long assetsId,Long categoryId,Long customerId) {
    	modelMap.put("assetsId", assetsId);
    	modelMap.put("categoryId", categoryId);
    	modelMap.put("customerId", customerId);
        return "stock/stock/inList";
    }
    
    /**
     * 客户库存 出库列表
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/outList.html", method = RequestMethod.GET)
    public String outList(ModelMap modelMap,Long assetsId,Long categoryId,Long customerId) {
    	modelMap.put("assetsId", assetsId);
    	modelMap.put("categoryId", categoryId);
    	modelMap.put("customerId", customerId);
        return "stock/stock/outList";
    }

    /**
     * 分页查询出库Stock，返回easyui分页信息
     * @param stock
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/outListPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String outListPage(@ModelAttribute StockQueryDto stock) throws Exception {
    	return stockService.listEasyuiPageByExample(stock, true).toString();
    }
    
    /**
     * 分页查询客户Stock，返回easyui分页信息
     * @param stock
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute StockQueryDto stockQueryDto) throws Exception {
    	Page<StockDto> page = stockService.countCustomerStock(stockQueryDto);
    	Map<String, String> map = stockQueryDto.getMetadata();
    	List<Map> result = ValueProviderUtils.buildDataByProvider(map, page.getResult());
    	return new EasyuiPageOutput(page.getTotal(), result ).toString();
    }
    
    /**
     * 提交出库单
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/stockOut.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "", operationType = "out", systemCode = "IA")
    public @ResponseBody BaseOutput stockOut(Long stockId,Long weight,Long quantity,String notes) {
    	try {
    		return BaseOutput.successData(stockService.stockOut(stockId, weight, quantity,notes));
    	}catch (BusinessException e) {
			LOG.error("出库{}异常！",stockId, e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("出库{}异常！",stockId, e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
        //LoggerUtil.buildLoggerContext(id, String.valueOf(value), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
    }
    

}