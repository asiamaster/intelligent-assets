package com.dili.ia.controller;

import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.DepositOrder;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.DepositOrderQuery;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.DepositOrderStateEnum;
import com.dili.ia.service.DataAuthService;
import com.dili.ia.service.DepositOrderService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import io.seata.common.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-20 17:29:10.
 */
@Controller
@RequestMapping("/depositOrder")
public class DepositOrderController {
    private final static Logger LOG = LoggerFactory.getLogger(DepositOrderController.class);

    @Autowired
    DepositOrderService depositOrderService;
    @Autowired
    PaymentOrderService paymentOrderService;
    @Autowired
    BusinessLogRpc businessLogRpc;
    @Autowired
    DataAuthService dataAuthService;

    /**
     * 跳转到DepositOrder页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        //默认显示最近3天，结束时间默认为当前日期的23:59:59，开始时间为当前日期-2的00:00:00，选择到年月日时分秒
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime createdStart = LocalDateTime.of(nowTime.getYear(), nowTime.getMonth(), nowTime.getDayOfMonth() -2 , 0, 0 ,0);
        LocalDateTime createdEnd = LocalDateTime.of(nowTime.getYear(), nowTime.getMonth(), nowTime.getDayOfMonth() , 23, 59 ,59);
        modelMap.put("createdStart", createdStart);
        modelMap.put("createdEnd", createdEnd);

        modelMap.put("createdStart", createdStart);
        modelMap.put("createdEnd", createdEnd);
        return "depositOrder/index";
    }

    /**
     * 分页查询DepositOrder，返回easyui分页信息
     * @param depositOrderQuery
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(DepositOrderQuery depositOrderQuery) throws Exception {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        List<Long> departmentIdList = dataAuthService.getDepartmentDataAuth(userTicket);
        if (CollectionUtils.isEmpty(departmentIdList)){
            return new EasyuiPageOutput(0, Collections.emptyList()).toString();
        }
        depositOrderQuery.setMarketId(userTicket.getFirmId());
        depositOrderQuery.setDepartmentIds(departmentIdList);
        return depositOrderService.listEasyuiPageByExample(depositOrderQuery, true).toString();
    }

    /**
     * 跳转到保证金管理-新增页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/refundApply.html", method = RequestMethod.GET)
    public String refundApply(ModelMap modelMap, Long id) {
        if(null != id){
            DepositOrder depositOrder = depositOrderService.get(id);
            Long maxRefundAmount = depositOrder.getPaidAmount() - depositOrder.getRefundAmount();
            modelMap.put("depositOrder",depositOrder);
            modelMap.put("maxRefundAmount", maxRefundAmount);
        }
        return "depositOrder/refundApply";
    }
    /**
     * CustomerAccount--- 保证金退款
     * @param order
     * @return BaseOutput
     */
    @RequestMapping(value="/addRefundOrder.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput doEarnestRefund(RefundOrder order) {
        try {
            BaseOutput<RefundOrder> out = depositOrderService.addRefundOrder(order);
            return out;
        } catch (BusinessException e) {
            LOG.error("定金创建退款失败！", e);
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOG.error("定金创建退款出错！", e);
            return BaseOutput.failure("创建退款出错！");
        }
    }

    /**
     * 跳转到保证金管理-新增页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "depositOrder/add";
    }

    /**
     * 新增DepositOrder
//     * @param depositOrderListDto
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.DEPOSIT_ORDER, content="${businessCode!}", operationType="add", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/doAdd.action", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput doAdd(DepositOrder depositOrder) {

        try{
            BaseOutput<DepositOrder> output = depositOrderService.addDepositOrder(depositOrder);
            //写业务日志
            if (output.isSuccess()){
                DepositOrder order = output.getData();
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(order.getId(), order.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }
            return output;
        }catch (BusinessException e){
            LOG.error("保证金单保存异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("保证金单保存异常！", e);
            return BaseOutput.failure("保证金单保存异常！");
        }
    }


    /**
     * 跳转到保证金管理-修改页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap, Long id) {
        if(null != id){
            DepositOrder depositOrder = depositOrderService.get(id);
            modelMap.put("depositOrder",depositOrder);
        }
        return "depositOrder/update";
    }

    /**
     * 修改DepositOrder
     * @param depositOrder
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.DEPOSIT_ORDER, content="${logContent!}", operationType="edit", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/doUpdate.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput doUpdate(DepositOrder depositOrder) {
        try{
            DepositOrder old = depositOrderService.get(depositOrder.getId());
            if (null != old && old.getIsRelated().equals(YesOrNoEnum.YES.getCode())){
                return BaseOutput.failure("关联订单不能修改!");
            }
            BaseOutput<DepositOrder> output = depositOrderService.updateDepositOrder(depositOrder);
            //写业务日志
            if (output.isSuccess()){
                DepositOrder order = depositOrderService.get(depositOrder.getId());
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(order.getId(), order.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), order.getNotes());
            }
            return output;
        }catch (BusinessException e){
            LOG.error("保证金单修改异常！", e);
            return BaseOutput.failure(e.getErrorMsg());
        }catch (Exception e){
            LOG.error("保证金单修改异常！", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 跳转到保证金管理-查看页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/view.action", method = RequestMethod.GET)
    public String view(ModelMap modelMap,Long id,String orderCode) {
        DepositOrder depositOrder = null;
        if(null != id) {
            depositOrder = depositOrderService.get(id);
        }else if(StringUtils.isNotBlank(orderCode)){
            PaymentOrder paymentOrder = new PaymentOrder();
            paymentOrder.setCode(orderCode);
            paymentOrder.setBizType(BizTypeEnum.DEPOSIT_ORDER.getCode());
            depositOrder = depositOrderService.get(paymentOrderService.listByExample(paymentOrder).stream().findFirst().orElse(null).getBusinessId());
            id = depositOrder.getId();
        }
        modelMap.put("depositOrder",depositOrder);
        try{
            //日志查询
            BusinessLogQueryInput businessLogQueryInput = new BusinessLogQueryInput();
            businessLogQueryInput.setBusinessId(id);
            businessLogQueryInput.setBusinessType(LogBizTypeConst.DEPOSIT_ORDER);
            BaseOutput<List<BusinessLog>> businessLogOutput = businessLogRpc.list(businessLogQueryInput);
            if(businessLogOutput.isSuccess()){
                modelMap.put("logs",businessLogOutput.getData());
            }
        }catch (Exception e){
            LOG.error("日志服务查询异常",e);
        }
        return "depositOrder/view";
    }

    /**
     * 保证金管理--提交
     * @param id
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.DEPOSIT_ORDER, content="${businessCode!}", operationType="submit", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput submit(@RequestParam Long id, @RequestParam Long amount, @RequestParam Long waitAmount) {
        try {
            DepositOrder old = depositOrderService.get(id);
            if (null != old && old.getIsRelated().equals(YesOrNoEnum.YES.getCode())){
                return BaseOutput.failure("关联订单不能提交!");
            }
            BaseOutput<DepositOrder> output = depositOrderService.submitDepositOrder(id, amount, waitAmount);
            //写业务日志
            if (output.isSuccess()){
                DepositOrder order = output.getData();
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(order.getId(), order.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }
            return output;
        } catch (BusinessException e) {
            LOG.error("保证金单提交失败！", e);
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOG.error("submit 保证金单提交出错!" ,e);
            return BaseOutput.failure("提交出错！");
        }
    }

    /**
     * 保证金管理--撤回
     * @param id
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.DEPOSIT_ORDER, content="${businessCode!}", operationType="withdraw", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/withdraw.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput withdraw(Long id) {
        try {
            DepositOrder old = depositOrderService.get(id);
            if (null != old && old.getIsRelated().equals(YesOrNoEnum.YES.getCode())){
                return BaseOutput.failure("关联订单不能撤回!");
            }
            BaseOutput<DepositOrder> output = depositOrderService.withdrawDepositOrder(id);
            if (output.isSuccess()){
                DepositOrder order = output.getData();
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(order.getId(), order.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }
            return BaseOutput.success("撤回成功");
        } catch (BusinessException e) {
            LOG.error("保证金单撤回出错！", e);
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOG.error("withdraw 保证金单撤回出错!" ,e);
            return BaseOutput.failure("撤回出错！");
        }
    }

    /**
     * 保证管理--取消
     * @param id
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.DEPOSIT_ORDER, content="${businessCode!}", operationType="cancel", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput cancel(Long id) {
        DepositOrder depositOrder = depositOrderService.get(id);
        if (null != depositOrder && depositOrder.getIsRelated().equals(YesOrNoEnum.YES.getCode())){
            return BaseOutput.failure("关联订单不能取消!");
        }
        if (!depositOrder.getState().equals(DepositOrderStateEnum.CREATED.getCode())){
            return BaseOutput.failure("取消失败，保证金单状态已变更！");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null){
            return BaseOutput.failure("未登录！");
        }
        try {
            depositOrder.setCancelerId(userTicket.getId());
            depositOrder.setCanceler(userTicket.getRealName());
            depositOrder.setState(DepositOrderStateEnum.CANCELD.getCode());
            if (depositOrderService.updateSelective(depositOrder) == 0){
                LOG.error("保证金取消失败，取消更新状态记录数为 0，取消保证金ID【{}】", id);
                return BaseOutput.failure("取消失败！");
            }

            //记录业务日志
            LoggerUtil.buildLoggerContext(depositOrder.getId(), depositOrder.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);

            return BaseOutput.success("取消成功");
        } catch (Exception e) {
            LOG.error("cancel 保证金单取消出错!" ,e);
            return BaseOutput.failure("取消出错！");
        }
    }
}