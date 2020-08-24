package com.dili.ia.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.dto.LaborDto;
import com.dili.ia.domain.dto.LaborQueryDto;
import com.dili.ia.domain.dto.RefundInfoDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.LaborActionEnum;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.LaborService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.valid.LaborGetCost;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Controller
@RequestMapping("/labor/vest")
public class LaborController {
	
	private final static Logger LOG = LoggerFactory.getLogger(LaborController.class);
	
    @Autowired
    private LaborService laborService;
    
    @Autowired
    private BusinessChargeItemService businessChargeItemService;
    
    @Autowired
    private BusinessLogRpc businessLogRpc;
    
    
    /**
     * 跳转到新增页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap,String type) {
    	UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
    	List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.
				queryBusinessChargeItemConfig(userTicket.getFirmId(), BizTypeEnum.LABOR_VEST.getCode(), YesOrNoEnum.YES.getCode());
        modelMap.put("chargeItems", chargeItemDtos);
        modelMap.put("type", "add");
    	modelMap.put("businessChargeType", BizTypeEnum.LABOR_VEST.getCode());
        return "labor/add";
    }
    
    /**
     * 跳转到labor页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "labor/index";
    }
    
    /**
     * 查看
     * @param labor
     * @return BaseOutput
     */
    @RequestMapping(value="/view.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String view(ModelMap modelMap,String code) {
    	modelMap.put("labor", laborService.getLabor(code));
    	try{
            //日志查询
            BusinessLogQueryInput businessLogQueryInput = new BusinessLogQueryInput();
            businessLogQueryInput.setBusinessCode(code);
            businessLogQueryInput.setBusinessType(LogBizTypeConst.STOCK);
            BaseOutput<BusinessLog> businessLogOutput = businessLogRpc.list(businessLogQueryInput);
            if(businessLogOutput.isSuccess()){
                modelMap.put("logs",businessLogOutput.getData());
            }
        }catch (Exception e){
            LOG.error("日志服务查询异常",e);
        }
        return "labor/view";
	}

    /**
     * 修改labor
     * @param labor
     * @return BaseOutput
     */
    @RequestMapping(value="/update.html", method = {RequestMethod.GET, RequestMethod.POST})
	public String update(ModelMap modelMap, String code, String type) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		// 判断业务类型
		String businessChargeType = null;
		switch (LaborActionEnum.getLaborActionEnum(type)) {
		case RENEW: {
			businessChargeType = BizTypeEnum.LABOR_VEST.getCode();
			break;
		}
		case REMODEL: {
			businessChargeType = BizTypeEnum.LABOR_VEST_REMODEL.getCode();
			break;
		}
		case RENAME: {
			businessChargeType = BizTypeEnum.LABOR_VEST_RENAME.getCode();
			break;
		}
		default: {
			businessChargeType = BizTypeEnum.LABOR_VEST.getCode();
		}
		}
		;

		if (StringUtils.isNotEmpty(businessChargeType)
				&& LaborActionEnum.UPDATE != LaborActionEnum.getLaborActionEnum(type)) {
			List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.queryBusinessChargeItemConfig(
					userTicket.getFirmId(), businessChargeType, YesOrNoEnum.YES.getCode());
			modelMap.put("chargeItems", chargeItemDtos);
		}
		modelMap.put("labor", laborService.getLabor(code));
		modelMap.put("type", type);
		modelMap.put("businessChargeType", businessChargeType);
		return "labor/add";
	}
    
    /**
     * 分页查询labor，返回easyui分页信息
     * @param labor
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute LaborQueryDto labor) throws Exception {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		labor.setMarketId(userTicket.getFirmId());
		return laborService.listEasyuiPageByExample(labor, true).toString();
    	
    }

    /**
     * 新增labor
     * @param labor
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.LABOR_VEST, content = "", operationType = "add", systemCode = "INTELLIGENT_ASSETS")
	public @ResponseBody BaseOutput insert(@RequestBody @Validated LaborDto laborDto) {
		try {
			switch (LaborActionEnum.getLaborActionEnum(laborDto.getActionType())) {
			case RENAME: {
				laborService.rename(laborDto);
				break;
			}
			case REMODEL: {
				laborService.remodel(laborDto);
				break;
			}
			case RENEW: {
				laborService.renew(laborDto);
				break;
			}

			default: {
				laborService.create(laborDto);
				break;
			}

			}
		} catch (BusinessException e) {
			LOG.error("劳务马甲单保存异常！", e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		} catch (Exception e) {
			LOG.error("劳务马甲单保存异常！", e);
			return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
		// LoggerUtil.buildLoggerContext(id, String.valueOf(value), userTicket.getId(),
		// userTicket.getRealName(), userTicket.getFirmId(), null);
		return BaseOutput.success("新增成功");
	}
    
    /**
     * 修改labor
     * @param labor
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.LABOR_VEST, content = "${code}", operationType = "edit", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput update(@RequestBody @Validated LaborDto laborDto) {
    	try {
    		laborService.update(laborDto);
    	}catch (BusinessException e) {
			LOG.error("劳务马甲单{}修改异常！",laborDto.getCode(), e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("劳务马甲单{}修改异常！",laborDto.getCode(), e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
        return BaseOutput.success("修改成功");
    }
    
    /**
     * 取消(逻辑删除)labor
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.LABOR_VEST, content = "${code}", operationType = "cancel", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput cancel(String code) {
        try {
            laborService.cancel(code);
    	}catch (BusinessException e) {
			LOG.error("劳务马甲单{}取消异常！",code, e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("劳务马甲单{}取消异常！",code, e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
        return BaseOutput.success("取消成功");
    }
    
    /**
     * 提交
     * @param labor
     * @return BaseOutput
     */
    @RequestMapping(value="/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.LABOR_VEST, content = "${code}", operationType = "submit", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput submit(String code) {
        try {
            laborService.submit(code);
    	}catch (BusinessException e) {
			LOG.error("劳务马甲单{}提交异常！",code, e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("劳务马甲单{}提交异常！",code, e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
        //LoggerUtil.buildLoggerContext(id, String.valueOf(value), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        return BaseOutput.success("提交成功");
    }
    
    /**
     * 撤回
     * @param labor
     * @return BaseOutput
     */
    @RequestMapping(value="/withdraw.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.LABOR_VEST, content = "${code}", operationType = "withdraw", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput withdraw(String code) {
        try {
        	laborService.withdraw(code);
    	}catch (BusinessException e) {
			LOG.error("劳务马甲单{}撤回异常！",code, e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("劳务马甲单{}撤回异常！",code, e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}//LoggerUtil.buildLoggerContext(id, String.valueOf(value), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        return BaseOutput.success("撤回成功");
    }
    
    /**
     * 退款申请
     * @param labor
     * @return BaseOutput
     */
    @RequestMapping(value="/refundApply.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String refundApply(ModelMap modelMap,String code) {	        
    	modelMap.put("labor", laborService.getLabor(code));
    	return "labor/refundApply";
    }
    
    /**
     * 退款申请
     * @param labor
     * @return BaseOutput
     */
    @RequestMapping(value="/refund.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.LABOR_VEST, content = "", operationType = "refund", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput refund(@RequestBody @Validated RefundInfoDto refundInfoDto) {	        //throw new BusinessException("2000", "errorCode");
    	try {
    		laborService.refund(refundInfoDto);
    	}catch (BusinessException e) {
			LOG.error("劳务马甲单{}退款申请异常！",refundInfoDto.getCode(), e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("劳务马甲单{}退款申请异常！",refundInfoDto.getCode(), e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
    	return BaseOutput.success("退款成功");
    }
    
    /**
     * 通过计费规则算取费用
     * @param labor
     * @return BaseOutput
     */
    @RequestMapping(value="/getCost.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getCost(@RequestBody @Validated(LaborGetCost.class) LaborDto laborDto) {	        //throw new BusinessException("2000", "errorCode");
    	BaseOutput baseOutput = BaseOutput.success();
    	try {
    		baseOutput.setData(laborService.getCost(laborDto));
    	}catch (BusinessException e) {
			LOG.error("费用{}计算异常！",JSON.toJSON(laborDto), e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("费用{}计算异常！",JSON.toJSON(laborDto), e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
    	return baseOutput;
    }
    
    
}