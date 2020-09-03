package com.dili.ia.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.assets.sdk.dto.CarTypeForBusinessDTO;
import com.dili.assets.sdk.dto.DistrictDTO;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.dto.AssetsDto;
import com.dili.ia.domain.dto.RefundInfoDto;
import com.dili.ia.domain.dto.StockInDto;
import com.dili.ia.domain.dto.StockInQueryDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.StockInService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Controller
@RequestMapping("/stock/stockIn")
public class StockInController {
	
	private final static Logger LOG = LoggerFactory.getLogger(StockInController.class);
	
    @Autowired
    private StockInService stockInService;
    
    @Autowired
    private BusinessChargeItemService businessChargeItemService;
    
    @Autowired
    private BusinessLogRpc businessLogRpc;
   
    @Autowired
    private AssetsRpc assetsRpc;
    
    /**
     * 跳转到StockIn页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap,Integer type) {
    	UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
    	modelMap.put("type", type == null ? 1:type);
    	//动态收费项
		List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.
				queryBusinessChargeItemConfig(userTicket.getFirmId(), BizTypeEnum.STOCKIN.getCode(), YesOrNoEnum.YES.getCode());
        modelMap.put("chargeItems", chargeItemDtos);
        return "stock/add";
    }
    
    /**
     * 跳转到StockIn页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "stock/index";
    }
    
    /**
     * 查看
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/view.html", method = {RequestMethod.GET, RequestMethod.POST})
    //@BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "", operationType = "add", systemCode = "IA")
    public String view(ModelMap modelMap,String code) {
    	StockInDto stockInDto = stockInService.view(code);
    	modelMap.put("stockIn",stockInDto);
    	try{
            //日志查询
            BusinessLogQueryInput businessLogQueryInput = new BusinessLogQueryInput();
            businessLogQueryInput.setBusinessCode(code);
            businessLogQueryInput.setBusinessType(LogBizTypeConst.STOCK);
            BaseOutput<List<BusinessLog>> businessLogOutput = businessLogRpc.list(businessLogQueryInput);
            if(businessLogOutput.isSuccess()){
                modelMap.put("logs",businessLogOutput.getData());
            }
        }catch (Exception e){
            LOG.error("日志服务查询异常",e);
        }
        return "stock/view";
    }

    /**
     * 修改StockIn
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/update.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String update(ModelMap modelMap,String code) {
    	UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
    	modelMap.put("stockIn",stockInService.view(code));
    	List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.
				queryBusinessChargeItemConfig(userTicket.getFirmId(), BizTypeEnum.STOCKIN.getCode(), YesOrNoEnum.YES.getCode());
    	modelMap.put("chargeItems", chargeItemDtos);
        return "stock/update";
    }
    
    /**
     * 分页查询StockIn，返回easyui分页信息
     * @param stockIn
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute StockInQueryDto stockIn) throws Exception {
    	
        return stockInService.listPageAction(stockIn);
    }

    /**
     * 新增StockIn
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "", operationType = "add", systemCode = "IA")
    public @ResponseBody BaseOutput insert(@RequestBody @Validated StockInDto stockInDto) {
    	try {
    		stockInService.createStockIn(stockInDto);
		}catch (BusinessException e) {
			LOG.error("入库单保存异常！", e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("入库单保存异常！", e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
        //LoggerUtil.buildLoggerContext(id, String.valueOf(value), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        return BaseOutput.success("新增成功");
    }
    
    /**
     * 修改StockIn
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "${code}", operationType = "edit", systemCode = "IA")
    public @ResponseBody BaseOutput update(@RequestBody @Validated StockInDto stockIn) {
    	try {
    		stockInService.updateStockIn(stockIn);
    	}catch (BusinessException e) {
			LOG.error("入库单{}修改异常！",stockIn.getCode(), e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("入库单{}修改异常！",stockIn.getCode(), e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
        return BaseOutput.success("修改成功");
    }
    
    /**
     * 取消(逻辑删除)StockIn
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "${code}", operationType = "cancel", systemCode = "IA")
    public @ResponseBody BaseOutput cancel(String code) {
        try {
            stockInService.cancel(code);
    	}catch (BusinessException e) {
			LOG.error("入库单{}取消异常！",code, e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("入库单{}取消异常！",code, e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
        return BaseOutput.success("取消成功");
    }
    
    /**
     * 提交入库单
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "${code}", operationType = "submit", systemCode = "IA")
    public @ResponseBody BaseOutput submit(String code) {
        try {
            stockInService.submit(code);
    	}catch (BusinessException e) {
			LOG.error("入库单{}提交异常！",code, e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("入库单{}提交异常！",code, e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
        //LoggerUtil.buildLoggerContext(id, String.valueOf(value), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        return BaseOutput.success("提交成功");
    }
    
    /**
     * 撤回入库单
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/remove.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "${code}", operationType = "withdraw", systemCode = "IA")
    public @ResponseBody BaseOutput remove(String code) {
        try {
        	stockInService.withdraw(code);
    	}catch (BusinessException e) {
			LOG.error("入库单{}撤回异常！",code, e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("入库单{}撤回异常！",code, e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}//LoggerUtil.buildLoggerContext(id, String.valueOf(value), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        return BaseOutput.success("撤回成功");
    }
    
    /**
     * 结算入库单
     * @param stockIn
     * @return BaseOutput
     */
	/*@RequestMapping(value="/pay.action", method = {RequestMethod.GET, RequestMethod.POST})
	@BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "", operationType = "add", systemCode = "IA")
	public @ResponseBody BaseOutput pay(@Validated PayInfoDto payInfoDto) {
	    stockInService.pay(payInfoDto);
	    return BaseOutput.success("支付成功");
	}*/
    
    /**
     * 退款申请
     * @param labor
     * @return BaseOutput
     */
    @RequestMapping(value="/refundApply.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String refundApply(ModelMap modelMap,String code) {	        
    	modelMap.put("stockIn", stockInService.view(code));
    	return "stock/refundApply";
    }

    /**
     * 退款申请
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/refund.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.STOCK, content = "", operationType = "refund", systemCode = "IA")
    public @ResponseBody BaseOutput refund(@RequestBody @Validated RefundInfoDto refundInfoDto) {	        //throw new BusinessException("2000", "errorCode");
    	try {
    		stockInService.refund(refundInfoDto);
    	}catch (BusinessException e) {
			LOG.error("入库单{}退款申请异常！",refundInfoDto.getCode(), e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("入库单{}退款申请异常！",refundInfoDto.getCode(), e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
    	return BaseOutput.success("退款申请成功");
    }
    
    /**
     * 通过计费规则算取费用
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/getCost.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getCost(@RequestBody StockInDto stockInDto) {	        //throw new BusinessException("2000", "errorCode");
    	BaseOutput baseOutput = BaseOutput.success();
    	try {
    		baseOutput.setData(stockInService.getCost(stockInDto));
    	}catch (BusinessException e) {
			LOG.error("费用{}计算异常！",stockInDto.getCode(), e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("费用{}计算异常！",stockInDto.getCode(), e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
    	return baseOutput;
    }
    
    /**
     * 获取冷库
     * @param stockIn
     * @return BaseOutput
     */
    @RequestMapping(value="/getColdStorage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getColdStorage(AssetsDto assetsDto) {	        
    	BaseOutput baseOutput = BaseOutput.success();
    	try {
    		baseOutput.setData(assetsRpc.searchBooth((JSONObject)JSON.toJSON(assetsDto)).getData());
    	}catch (Exception e) {
			LOG.error("获取异常！", e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
    	return baseOutput;
    }
   
    /**
     * 获取司磅入库车型
     * @Title searchCarType
     * @param name
     * @param code
     */
    @RequestMapping(value="/searchCarType.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput searchCarType(CarTypeForBusinessDTO dto) {	        
    	BaseOutput baseOutput = BaseOutput.success();
    	try {
    		dto.setBusinessCode("lkrk");
    		baseOutput.setData(assetsRpc.listCarTypePublicByBusiness(dto).getData());
    	}catch (Exception e) {
			LOG.error("获取异常！", e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
    	return baseOutput;
    }
    
    /**
     * 获取司磅入库车型
     * @Title searchCarType
     * @param name
     * @param code
     */
    @RequestMapping(value="/searchDistrict.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput searchDistrict(DistrictDTO input) {	        
    	BaseOutput baseOutput = BaseOutput.success();
    	try {
    		 input.setMarketId(SessionContext.getSessionContext().getUserTicket().getFirmId());
    		 baseOutput.setData(assetsRpc.searchDistrict(input).getData());
    	}catch (Exception e) {
			LOG.error("获取异常！", e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
    	return baseOutput;
    }
}