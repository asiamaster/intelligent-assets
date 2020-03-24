package com.dili.ia.controller;

import com.alibaba.fastjson.JSON;
import com.dili.ia.domain.*;
import com.dili.ia.domain.dto.EarnestOrderListDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.EarnestOrderStateEnum;
import com.dili.ia.glossary.LogBizTypeEnum;
import com.dili.ia.service.DataAuthService;
import com.dili.ia.service.EarnestOrderDetailService;
import com.dili.ia.service.EarnestOrderService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.crypto.Data;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
@Api("/earnestOrder")
@Controller
@RequestMapping("/earnestOrder")
public class EarnestOrderController {
    private final static Logger LOG = LoggerFactory.getLogger(EarnestOrderController.class);

    @Autowired
    EarnestOrderService earnestOrderService;
    @Autowired
    EarnestOrderDetailService earnestOrderDetailService;
    @Autowired
    BusinessLogRpc businessLogRpc;
    @Autowired
    DataAuthService dataAuthService;
    @Autowired
    PaymentOrderService paymentOrderService;

    /**
     * 跳转到定金管理页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        //默认显示最近3天，结束时间默认为当前日期的23:59:59，开始时间为当前日期-2的00:00:00，选择到年月日时分秒
        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        c.add(Calendar.DAY_OF_MONTH, -2);
        Date createdStart = c.getTime();

        Calendar ce = Calendar.getInstance();
        ce.set(ce.get(Calendar.YEAR), ce.get(Calendar.MONTH), ce.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date  createdEnd = ce.getTime();

        modelMap.put("createdStart", createdStart);
        modelMap.put("createdEnd", createdEnd);
        return "earnestOrder/index";
    }

    /**
     * 跳转到定金管理-新增页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "earnestOrder/add";
    }
    /**
     * 跳转到定金管理-查看页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap,Long id,String businessCode) {
        EarnestOrder earnestOrder = null;
        if(null != id) {
            earnestOrder = earnestOrderService.get(id);
        }else if(StringUtils.isNotBlank(businessCode)){
            PaymentOrder paymentOrder = DTOUtils.newInstance(PaymentOrder.class);
            paymentOrder.setCode(businessCode);
            paymentOrder.setBizType(BizTypeEnum.EARNEST.getCode());
            earnestOrder = earnestOrderService.get(paymentOrderService.listByExample(paymentOrder).stream().findFirst().orElse(null).getBusinessId());
        }
            EarnestOrderDetail condition = DTOUtils.newInstance(EarnestOrderDetail.class);
            condition.setEarnestOrderId(id);
            List<EarnestOrderDetail> earnestOrderDetails = earnestOrderDetailService.list(condition);
            modelMap.put("earnestOrder",earnestOrder);
            modelMap.put("earnestOrderDetails", earnestOrderDetails);
            try{
                //日志查询
                BusinessLogQueryInput businessLogQueryInput = new BusinessLogQueryInput();
                businessLogQueryInput.setBusinessId(id);
                businessLogQueryInput.setBusinessType(LogBizTypeEnum.EARNEST_ORDER.getCode());
                BaseOutput<List<BusinessLog>> businessLogOutput = businessLogRpc.list(businessLogQueryInput);
                if(businessLogOutput.isSuccess()){
                    modelMap.put("logs",businessLogOutput.getData());
                }
            }catch (Exception e){
                LOG.error("日志服务查询异常",e);
            }
        return "earnestOrder/view";
    }
    /**
     * 跳转到定金管理-修改页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap, Long id) {
        if(null != id){
            EarnestOrder earnestOrder = earnestOrderService.get(id);
            EarnestOrderDetail condition = DTOUtils.newInstance(EarnestOrderDetail.class);
            condition.setEarnestOrderId(id);
            List<EarnestOrderDetail> earnestOrderDetails = earnestOrderDetailService.list(condition);
            modelMap.put("earnestOrder",earnestOrder);
            modelMap.put("earnestOrderDetails", JSON.toJSONString(earnestOrderDetails));
        }
        return "earnestOrder/update";
    }


    /**
     * 分页查询EarnestOrder，返回easyui分页信息
     * @param earnestOrderListDto
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(EarnestOrderListDto earnestOrderListDto) throws Exception {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        List<Long> marketIdList = dataAuthService.getMarketDataAuth(userTicket);
        List<Long> departmentIdList = dataAuthService.getDepartmentDataAuth(userTicket);
        if (CollectionUtils.isEmpty(marketIdList) || CollectionUtils.isEmpty(departmentIdList)){
            return new EasyuiPageOutput(0, Collections.emptyList()).toString();
        }
        earnestOrderListDto.setMarketIds(marketIdList);
        earnestOrderListDto.setDepartmentIds(departmentIdList);
        return earnestOrderService.listEasyuiPageByExample(earnestOrderListDto, true).toString();
    }

    /**
     * 新增EarnestOrder
     * @param earnestOrder
     * @return BaseOutput
     */
    @BusinessLogger(businessType="earnest_order", content="", operationType="add", notes = "", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/doAdd.action", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput doAdd(EarnestOrderListDto earnestOrder) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(earnestOrder.getEndTime());
        calendar.add(Calendar.HOUR_OF_DAY,23);
        calendar.add(Calendar.MINUTE,59);
        calendar.add(Calendar.SECOND,59);
        earnestOrder.setEndTime(calendar.getTime());
        try{
            BaseOutput<EarnestOrder> output = earnestOrderService.addEarnestOrder(earnestOrder);

            if (output.isSuccess()){
                EarnestOrder order = output.getData();
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, order.getCode());
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, order.getId());
                if(userTicket != null) {
                    LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
                    LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
                    LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
                }
            }
            return output;
        }catch (BusinessException e){
            LOG.error("定金单保存异常！", e.getErrorMsg());
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("定金单保存异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 修改EarnestOrder
     * @param earnestOrder
     * @return BaseOutput
     */
    @RequestMapping(value="/doUpdate.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput doUpdate(EarnestOrderListDto earnestOrder) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(earnestOrder.getEndTime());
        calendar.add(Calendar.HOUR_OF_DAY,23);
        calendar.add(Calendar.MINUTE,59);
        calendar.add(Calendar.SECOND,59);
        earnestOrder.setEndTime(calendar.getTime());
        try{
            earnestOrderService.updateEarnestOrder(earnestOrder);
            return BaseOutput.success("修改成功");
        }catch (BusinessException e){
            LOG.error("定金单修改异常！", e.getErrorMsg());
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("定金单修改异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 定金管理--提交
     * @param id
     * @return BaseOutput
     */
    @BusinessLogger(businessType="earnest_order", content="", operationType="submit", notes = "", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput submit(Long id) {
        try {
            BaseOutput<EarnestOrder> output = earnestOrderService.submitEarnestOrder(id);
            if (output.isSuccess()){
                EarnestOrder order = output.getData();
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, order.getCode());
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, order.getId());
                if(userTicket != null) {
                    LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
                    LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
                    LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
                }
            }
            return output;
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            return BaseOutput.failure("提交出错！");
        }
    }

    /**
     * 定金管理--撤回
     * @param id
     * @return BaseOutput
     */
    @BusinessLogger(businessType="earnest_order", content="", operationType="withdraw", notes = "", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/withdraw.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput withdraw(Long id) {
        try {
            BaseOutput<EarnestOrder> output = earnestOrderService.withdrawEarnestOrder(id);
            if (output.isSuccess()){
                EarnestOrder order = output.getData();
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, order.getCode());
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, order.getId());
                if(userTicket != null) {
                    LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
                    LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
                    LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
                }
            }
            return BaseOutput.success("修改成功");
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            return BaseOutput.failure("撤回出错！");
        }
    }

    /**
     * 定金管理--取消
     * @param id
     * @return BaseOutput
     */
    @BusinessLogger(businessType="earnest_order", content="", operationType="cancel", notes = "", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput cancel(Long id) {
        EarnestOrder earnestOrder = earnestOrderService.get(id);
        if (!earnestOrder.getState().equals(EarnestOrderStateEnum.CREATED.getCode())){
            return BaseOutput.failure("取消失败，定金单状态已变更！");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null){
            return BaseOutput.failure("未登录！");
        }
        try {
            earnestOrder.setCancelerId(userTicket.getId());
            earnestOrder.setCanceler(userTicket.getRealName());
            earnestOrder.setState(EarnestOrderStateEnum.CANCELD.getCode());
            if (earnestOrderService.updateSelective(earnestOrder) != 1){
                LOG.info("取消失败，取消更新状态记录数不为 1，取消单定金单ID【{}】", id);
                return BaseOutput.failure("取消失败！");
            }

            //记录日志
            LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, earnestOrder.getCode());
            LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, earnestOrder.getId());
            if(userTicket != null) {
                LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
                LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
                LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
            }

            return BaseOutput.success("取消成功");
        } catch (Exception e) {
            return BaseOutput.failure("取消出错！");
        }
    }
}