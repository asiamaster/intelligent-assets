package com.dili.ia.controller;

import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.Customer;
import com.dili.ia.domain.CustomerAccount;
import com.dili.ia.domain.EarnestTransferOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.CustomerAccountListDto;
import com.dili.ia.domain.dto.EarnestRefundOrderDto;
import com.dili.ia.domain.dto.EarnestTransferDto;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.DataAuthService;
import com.dili.ia.service.RefundOrderService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import io.seata.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
@Controller
@RequestMapping("/customerAccount")
public class CustomerAccountController {
    private final static Logger LOG = LoggerFactory.getLogger(CustomerAccountController.class);
    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    DataAuthService dataAuthService;
    @Autowired
    CustomerRpc customerRpc;
    @Autowired
    RefundOrderService refundOrderService;
    /**
     * 跳转到CustomerAccount页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "customerAccount/index";
    }

    /**
     * 分页查询CustomerAccount，返回easyui分页信息
     * @param customerAccount
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(CustomerAccountListDto customerAccount) throws Exception {
        customerAccount.setMarketId(SessionContext.getSessionContext().getUserTicket().getFirmId());
        return customerAccountService.listEasyuiPageByExample(customerAccount, true).toString();
    }

    /**
     * 跳转到CustomerAccount页面---定金退款
     * @param modelMap
     * @param customerAccountId 客户账户Id
     * @param refundOrderId 退款单ID
     * @return String
     */
    @RequestMapping(value="/earnestRefund.html", method = RequestMethod.GET)
    public String earnestRefund(ModelMap modelMap, Long customerAccountId, Long refundOrderId) {
        if(null != customerAccountId){
            CustomerAccount customerAccount = customerAccountService.get(customerAccountId);
            modelMap.put("customerAccount",customerAccount);
        }
        if (null != refundOrderId) {
            modelMap.put("refundOrder", refundOrderService.get(refundOrderId));
        }
        return "customerAccount/earnestRefund";
    }
    /**
     * 跳转到CustomerAccount页面--定金转移
     * @param modelMap
     * @param id 客户账户Id
     * @return String
     */
    @RequestMapping(value="/earnestTransfer.html", method = RequestMethod.GET)
    public String earnestTransfer(ModelMap modelMap, Long id) {
        if(null != id){
            CustomerAccount customerAccount = customerAccountService.get(id);
            modelMap.put("customerAccount",customerAccount);
        }
        return "customerAccount/earnestTransfer";
    }

    /**
     * CustomerAccount--- 定金退款
     * @param order
     * @return BaseOutput
     */
    @BusinessLogger(content = "${content}", systemCode = "IA")
    @RequestMapping(value="/saveOrUpdateRefundOrder.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput saveOrUpdateRefundOrder(@RequestBody EarnestRefundOrderDto order) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (null == userTicket){
            return BaseOutput.failure("未登录！");
        }
        try {
            BaseOutput<RefundOrder> out = customerAccountService.saveOrUpdateRefundOrder(order);
            if (out.isSuccess()) {
                if(order.getId() != null){
                    LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE, LogBizTypeConst.REFUND_ORDER);
                    LoggerContext.put("content", order.getLogContent());
                    LoggerContext.put(LoggerConstant.LOG_OPERATION_TYPE_KEY, "edit");
                    LoggerUtil.buildLoggerContext(order.getId(), order.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), order.getRefundReason());
                }else{
                    LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE, LogBizTypeConst.CUSTOMER_ACCOUNT);
                    LoggerContext.put("content", MoneyUtils.centToYuan(order.getTotalRefundAmount()));
                    LoggerContext.put(LoggerConstant.LOG_OPERATION_TYPE_KEY, "refundApply");
                    LoggerUtil.buildLoggerContext(order.getBusinessId(), order.getBusinessCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), order.getRefundReason());
                }
            }
            return out;
        } catch (BusinessException e) {
            LOG.error("定金创建退款失败！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("定金创建退款出错！", e);
            return BaseOutput.failure("创建退款出错！");
        }
    }

    /**
     * CustomerAccount --- 定金转移
     * @param etDto 前端参数
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.CUSTOMER_ACCOUNT, content="${businessCode}客户【${payerName}】转移给客户【${customerName}】${amountYuan}元", operationType="transfer", systemCode = "IA")
    @RequestMapping(value="/doEarnestTransfer.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput doEarnestTransfer(EarnestTransferDto etDto) {
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            if (null == userTicket){
                return BaseOutput.failure("未登录！");
            }
            if (etDto.getPayerId().equals(etDto.getCustomerId())){
                return BaseOutput.failure("转移失败，不能转移给自己！");
            }
            //检查收款人客户状态
            BaseOutput checkResult  = checkCustomerState(etDto.getCustomerId(), userTicket.getFirmId());
            if (!checkResult.isSuccess()){
                return checkResult;
            }
            //检查付款款人客户状态
            BaseOutput checkResultPayer  = checkCustomerState(etDto.getPayerId(), userTicket.getFirmId());
            if (!checkResultPayer.isSuccess()){
                return checkResultPayer;
            }
            BaseOutput<EarnestTransferOrder> transOutput = customerAccountService.earnestTransfer(etDto);
            if (transOutput.isSuccess()){
                LoggerContext.put("amountYuan", MoneyUtils.centToYuan(etDto.getAmount()));
                //记录业务日志
                LoggerUtil.buildLoggerContext(transOutput.getData().getId(), transOutput.getData().getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), etDto.getTransferReason());

            }
            return BaseOutput.success("转移成功！");
        } catch (BusinessException e) {
            LOG.error("定金转移失败!", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("定金转移出错!", e);
            return BaseOutput.failure("转移出错！");
        }
    }

    /**
     * 检查客户状态
     * @param customerId
     * @param marketId
     */
    private BaseOutput checkCustomerState(Long customerId,Long marketId){
        BaseOutput<Customer> output = customerRpc.get(customerId,marketId);
        if(!output.isSuccess()){
            return BaseOutput.failure("客户接口调用异常 "+output.getMessage());
        }
        Customer customer = output.getData();
        if(null == customer){
            return BaseOutput.failure("客户不存在，请核实！");
        }else if(EnabledStateEnum.DISABLED.getCode().equals(customer.getState())){
            return BaseOutput.failure("客户已禁用，请核实！");
        }else if(YesOrNoEnum.YES.getCode().equals(customer.getIsDelete())){
            return BaseOutput.failure("客户已删除，请核实！");
        }
        return BaseOutput.success();
    }
    /**
     * 账户余额查询
     * @param customerId
     * @return
     */
    @RequestMapping(value="/getCustomerAccountByCustomerId.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getCustomerAccountByCustomerId(Long customerId) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return BaseOutput.failure("未登录");
        }
        return BaseOutput.success().setData(customerAccountService.getCustomerAccountByCustomerId(customerId,userTicket.getFirmId()));
    }


}