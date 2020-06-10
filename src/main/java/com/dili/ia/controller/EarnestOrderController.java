package com.dili.ia.controller;

import com.alibaba.fastjson.JSON;
import com.dili.ia.domain.EarnestOrder;
import com.dili.ia.domain.EarnestOrderDetail;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.dto.EarnestOrderListDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.EarnestOrderStateEnum;
import com.dili.ia.service.DataAuthService;
import com.dili.ia.service.EarnestOrderDetailService;
import com.dili.ia.service.EarnestOrderService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
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

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
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
    @RequestMapping(value="/view.action", method = RequestMethod.GET)
    public String view(ModelMap modelMap,Long id,String orderCode) {
        EarnestOrder earnestOrder = null;
        if(null != id) {
            earnestOrder = earnestOrderService.get(id);
        }else if(StringUtils.isNotBlank(orderCode)){
            PaymentOrder paymentOrder = DTOUtils.newInstance(PaymentOrder.class);
            paymentOrder.setCode(orderCode);
            paymentOrder.setBizType(BizTypeEnum.EARNEST.getCode());
            earnestOrder = earnestOrderService.get(paymentOrderService.listByExample(paymentOrder).stream().findFirst().orElse(null).getBusinessId());
            id = earnestOrder.getId();
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
                businessLogQueryInput.setBusinessType(LogBizTypeConst.EARNEST_ORDER);
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
        List<Long> departmentIdList = dataAuthService.getDepartmentDataAuth(userTicket);
        if (CollectionUtils.isEmpty(departmentIdList)){
            return new EasyuiPageOutput(0, Collections.emptyList()).toString();
        }
        earnestOrderListDto.setMarketId(userTicket.getFirmId());
        earnestOrderListDto.setDepartmentIds(departmentIdList);
        return earnestOrderService.listEasyuiPageByExample(earnestOrderListDto, true).toString();
    }

    /**
     * 新增EarnestOrder
     * @param earnestOrder
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.EARNEST_ORDER, content="${businessCode!}", operationType="add", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/doAdd.action", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput doAdd(EarnestOrderListDto earnestOrder) {
        if (null != earnestOrder.getEndTime()){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(earnestOrder.getEndTime());
            calendar.add(Calendar.HOUR_OF_DAY,23);
            calendar.add(Calendar.MINUTE,59);
            calendar.add(Calendar.SECOND,59);
            earnestOrder.setEndTime(calendar.getTime());
        }
        try{
            BaseOutput<EarnestOrder> output = earnestOrderService.addEarnestOrder(earnestOrder);
            //写业务日志
            if (output.isSuccess()){
                EarnestOrder order = output.getData();
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(order.getId(), order.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }
            return output;
        }catch (BusinessException e){
            LOG.error("定金单保存异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("定金单保存异常！", e);
            return BaseOutput.failure("定金单保存异常！");
        }
    }

    /**
     * 修改EarnestOrder
     * @param earnestOrder
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.EARNEST_ORDER, content="${logContent!}", operationType="edit", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/doUpdate.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput doUpdate(EarnestOrderListDto earnestOrder) {
        if (null != earnestOrder.getEndTime()){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(earnestOrder.getEndTime());
            calendar.add(Calendar.HOUR_OF_DAY,23);
            calendar.add(Calendar.MINUTE,59);
            calendar.add(Calendar.SECOND,59);
            earnestOrder.setEndTime(calendar.getTime());
        }
        try{
           BaseOutput<EarnestOrder> output = earnestOrderService.updateEarnestOrder(earnestOrder);
            //写业务日志
            if (output.isSuccess()){
                EarnestOrder order = earnestOrderService.get(earnestOrder.getId());
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(order.getId(), order.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), order.getNotes());
            }
            return output;
        }catch (BusinessException e){
            LOG.error("定金单修改异常！", e);
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
    @BusinessLogger(businessType = LogBizTypeConst.EARNEST_ORDER, content="${businessCode!}", operationType="submit", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput submit(Long id) {
        try {
            BaseOutput<EarnestOrder> output = earnestOrderService.submitEarnestOrder(id);
            //写业务日志
            if (output.isSuccess()){
                EarnestOrder order = output.getData();
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(order.getId(), order.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }
            return output;
        } catch (BusinessException e) {
            LOG.error("定金单提交失败！", e);
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOG.error("submit 定金单提交出错!" ,e);
            return BaseOutput.failure("提交出错！");
        }
    }

    /**
     * 定金管理--撤回
     * @param id
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.EARNEST_ORDER, content="${businessCode!}", operationType="withdraw", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/withdraw.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput withdraw(Long id) {
        try {
            BaseOutput<EarnestOrder> output = earnestOrderService.withdrawEarnestOrder(id);
            if (output.isSuccess()){
                EarnestOrder order = output.getData();
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(order.getId(), order.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }
            return BaseOutput.success("修改成功");
        } catch (BusinessException e) {
            LOG.error("定金单撤回出错！", e);
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOG.error("withdraw 定金单撤回出错!" ,e);
            return BaseOutput.failure("撤回出错！");
        }
    }

    /**
     * 定金管理--取消
     * @param id
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.EARNEST_ORDER, content="${businessCode!}", operationType="cancel", systemCode = "INTELLIGENT_ASSETS")
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
            if (earnestOrderService.updateSelective(earnestOrder) == 0){
                LOG.error("定金单取消失败，取消更新状态记录数为 0，取消单定金单ID【{}】", id);
                return BaseOutput.failure("取消失败！");
            }

            //记录业务日志
            LoggerUtil.buildLoggerContext(earnestOrder.getId(), earnestOrder.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);

            return BaseOutput.success("取消成功");
        } catch (Exception e) {
            LOG.error("cancel 定金单取消出错!" ,e);
            return BaseOutput.failure("取消出错！");
        }
    }
}