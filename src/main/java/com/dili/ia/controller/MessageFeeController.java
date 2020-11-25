package com.dili.ia.controller;

import com.alibaba.fastjson.JSON;
import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.MessageFee;
import com.dili.ia.domain.dto.MessageFeeDto;
import com.dili.ia.domain.dto.MessageFeeQuery;
import com.dili.ia.domain.dto.RefundInfoDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.DataAuthService;
import com.dili.ia.service.MessageFeeService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.valid.LaborGetCost;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
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

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-08-24 16:16:50.
 */
@Controller
@RequestMapping("/fee/message")
public class MessageFeeController {
	
	private final static Logger LOG = LoggerFactory.getLogger(MessageFeeController.class);

	
    @Autowired
    private MessageFeeService messageFeeService;
    
    @Autowired
    private  BusinessChargeItemService businessChargeItemService;
    
    @Autowired
    private BusinessLogRpc businessLogRpc;
    
    @Autowired
    private DataAuthService dataAuthService;

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
     * 跳转到messageFee页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
    	UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
    	List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.
				queryBusinessChargeItemConfig(1L, BizTypeEnum.MESSAGEFEE.getCode(), YesOrNoEnum.YES.getCode());
        modelMap.put("chargeItems", chargeItemDtos);
        return "messageFee/add";
    }
    
    /**
     * 跳转到messageFee页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap,String code) {
    	modelMap.put("messageFee", messageFeeService.view(code));
        return "messageFee/add";
    }

    /**
     * 跳转到messageFee页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap,String code) {
    	modelMap.put("messageFee", messageFeeService.view(code));
    	try{
            //日志查询
            BusinessLogQueryInput businessLogQueryInput = new BusinessLogQueryInput();
            businessLogQueryInput.setBusinessCode(code);
            businessLogQueryInput.setBusinessType(LogBizTypeConst.STOCK);
            businessLogQueryInput.setSystemCode("IA");
            BaseOutput<BusinessLog> businessLogOutput = businessLogRpc.list(businessLogQueryInput);
            if(businessLogOutput.isSuccess()){
                modelMap.put("logs",businessLogOutput.getData());
            }
        }catch (Exception e){
            LOG.error("日志服务查询异常",e);
        }
        return "messageFee/view";
    }
    
    /**
     * 分页查询messageFee，返回easyui分页信息
     * @param messageFee
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute MessageFeeQuery messageFee) throws Exception {
    	UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
    	messageFee.setMarketId(userTicket.getFirmId());
		// 获取部门数据权限
		List<Long> departmentIdList = dataAuthService.getDepartmentDataAuth(userTicket);
		if (CollectionUtils.isEmpty(departmentIdList)) {
			return new EasyuiPageOutput(0L, Collections.emptyList()).toString();
		}
		messageFee.setDepIds(departmentIdList);
    	return messageFeeService.listEasyuiPageByExample(messageFee, true).toString();
    }

    /**
     * 新增messageFee
     * @param messageFee
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.MESSAGE_FEE, content = "", operationType = "add", systemCode = "IA")
    public @ResponseBody BaseOutput insert(@RequestBody @Validated MessageFeeDto messageFee) {
        messageFeeService.create(messageFee);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改messageFee
     * @param messageFee
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.MESSAGE_FEE, content = "${code}", operationType = "edit", systemCode = "IA")
    public @ResponseBody BaseOutput update(@RequestBody @Validated MessageFeeDto messageFee) {
        messageFeeService.update(messageFee);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除messageFee
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.MESSAGE_FEE, content = "${code}", operationType = "cancle", systemCode = "IA")
    public @ResponseBody BaseOutput delete(String code) {
        messageFeeService.cancel(code);
        return BaseOutput.success("取消成功");
    }
    
    /**
     * 提交
     * @param labor
     * @return BaseOutput
     */
    @RequestMapping(value="/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.MESSAGE_FEE, content = "${code}", operationType = "submit", systemCode = "IA")
    public @ResponseBody BaseOutput submit(String code) {
        try {
            messageFeeService.submit(code);
    	}catch (BusinessException e) {
			LOG.error("信息费单{}提交异常！",code, e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("信息费单{}提交异常！",code, e);
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
    @BusinessLogger(businessType = LogBizTypeConst.MESSAGE_FEE, content = "${code}", operationType = "withdraw", systemCode = "IA")
    public @ResponseBody BaseOutput withdraw(String code) {
        try {
        	messageFeeService.withdraw(code);
    	}catch (BusinessException e) {
			LOG.error("信息费单{}撤回异常！",code, e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("信息费单{}撤回异常！",code, e);
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
    	modelMap.put("messageFee", messageFeeService.view(code));
    	return "messageFee/refundApply";
    }
    
    /**
     * 退款申请
     * @param labor
     * @return BaseOutput
     */
    @RequestMapping(value="/refund.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.MESSAGE_FEE, content = "", operationType = "refund", systemCode = "IA")
    public @ResponseBody BaseOutput refund(@RequestBody @Validated RefundInfoDto refundInfoDto) {	        //throw new BusinessException("2000", "errorCode");
    	try {
    		messageFeeService.refund(refundInfoDto);
    	}catch (BusinessException e) {
			LOG.error("信息费单{}退款申请异常！",refundInfoDto.getCode(), e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("信息费单{}退款申请异常！",refundInfoDto.getCode(), e);
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
    public @ResponseBody BaseOutput getCost(@RequestBody @Validated(LaborGetCost.class) MessageFeeDto messageFeeDto) {	        //throw new BusinessException("2000", "errorCode");
    	BaseOutput baseOutput = BaseOutput.success();
    	try {
    		baseOutput.setData(messageFeeService.getCost(messageFeeDto));
    	}catch (BusinessException e) {
			LOG.error("费用{}计算异常！",JSON.toJSON(messageFeeDto), e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("费用{}计算异常！",JSON.toJSON(messageFeeDto), e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}
    	return baseOutput;
    }
    
    /**
     * 同步消息系统
     * @param labor
     * @return BaseOutput
     */
    @RequestMapping(value="/syncState.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.MESSAGE_FEE, content = "${code}", operationType = "update", systemCode = "IA")
    public @ResponseBody BaseOutput syncState(String code,Integer syncState) {
        try {
        	messageFeeService.syncState(code, syncState);
    	}catch (BusinessException e) {
			LOG.error("信息费单{}同步异常！",code, e);
			return BaseOutput.failure(e.getCode(), e.getMessage());
		}catch (Exception e) {
			LOG.error("信息费单{}同步异常！",code, e);
    		return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
		}//LoggerUtil.buildLoggerContext(id, String.valueOf(value), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        return BaseOutput.success("撤回成功");
    }
    
    
}