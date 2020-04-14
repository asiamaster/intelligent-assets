package com.dili.ia.controller;

import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.Customer;
import com.dili.ia.domain.CustomerAccount;
import com.dili.ia.domain.EarnestTransferOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.CustomerAccountListDto;
import com.dili.ia.domain.dto.EarnestTransferDto;
import com.dili.ia.glossary.TransactionItemTypeEnum;
import com.dili.ia.rpc.CustomerRpc;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.DataAuthService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
@Api("/customerAccount")
@Controller
@RequestMapping("/customerAccount")
public class CustomerAccountController {
    private final static Logger LOG = LoggerFactory.getLogger(EarnestOrderController.class);
    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    DataAuthService dataAuthService;
    @Autowired
    CustomerRpc customerRpc;
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
     * @param id 客户账户Id
     * @return String
     */
    @ApiOperation("跳转到CustomerAccount页面---定金退款")
    @RequestMapping(value="/earnestRefund.html", method = RequestMethod.GET)
    public String earnestRefund(ModelMap modelMap, Long id) {
        if(null != id){
            CustomerAccount customerAccount = customerAccountService.get(id);
            modelMap.put("customerAccount",customerAccount);
        }
        return "customerAccount/earnestRefund";
    }
    /**
     * 跳转到CustomerAccount页面--定金转移
     * @param modelMap
     * @param id 客户账户Id
     * @return String
     */
    @ApiOperation("跳转到CustomerAccount页面--定金转移")
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
    @RequestMapping(value="/doAddEarnestRefund.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput doEarnestRefund(RefundOrder order) {
        try {
            BaseOutput<RefundOrder> out = customerAccountService.addEarnestRefund(order);
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
     * CustomerAccount --- 定金转移
     * @param efDto
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.CUSTOMER_ACCOUNT, content="${businessCode}客户【${payerName}】转移给客户【${customerName}】${amountYuan}元", operationType="transfer", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/doEarnestTransfer.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput doEarnestTransfer(EarnestTransferDto efDto) {
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            if (null == userTicket){
                return BaseOutput.failure("未登录！");
            }
            if (efDto.getPayerId().equals(efDto.getCustomerId())){
                return BaseOutput.failure("转移失败，不能转移给自己！");
            }
            //检查收款人客户状态
            BaseOutput checkResult  = checkCustomerState(efDto.getCustomerId(), userTicket.getFirmId());
            //检查付款款人客户状态
            BaseOutput checkResultPayer  = checkCustomerState(efDto.getPayerId(), userTicket.getFirmId());
            if (!checkResult.isSuccess()){
                return checkResult;
            }
            if (!checkResultPayer.isSuccess()){
                return checkResultPayer;
            }
            //判断转入方客户账户是否存在,不存在先创建客户账户
            if (!customerAccountService.checkCustomerAccountExist(efDto.getCustomerId(), userTicket.getFirmId())){
                BaseOutput<CustomerAccount> cusOut = customerAccountService.addCustomerAccountByCustomerInfo(efDto.getCustomerId(), efDto.getCustomerName(), efDto.getCustomerCellphone(), efDto.getCertificateNumber());
                if (!cusOut.isSuccess()){
                    return cusOut;
                }
            }
            BaseOutput<EarnestTransferOrder> output = customerAccountService.addEarnestTransferOrder(efDto);
            if (!output.isSuccess()){
                return output;
            }
            BaseOutput<EarnestTransferOrder> transOutput = customerAccountService.earnestTransfer(output.getData(), efDto.getPayerAccountVersion());
            if (!transOutput.isSuccess()){
                return transOutput;
            }
            if (transOutput.isSuccess()){
                LoggerContext.put("amountYuan", MoneyUtils.centToYuan(efDto.getAmount()));
                //记录业务日志
                LoggerUtil.buildLoggerContext(transOutput.getData().getId(), transOutput.getData().getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), efDto.getTransferReason());

            }

            return BaseOutput.success("转移成功！");
        } catch (BusinessException e) {
            LOG.error("定金转移失败!", e);
            return BaseOutput.failure(e.getErrorMsg());
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
            return BaseOutput.failure("客户不存在，请核实和修改后再保存");
        }else if(EnabledStateEnum.DISABLED.getCode().equals(customer.getState())){
            return BaseOutput.failure("客户已禁用，请核实和修改后再保存");
        }else if(YesOrNoEnum.YES.getCode().equals(customer.getIsDelete())){
            return BaseOutput.failure("客户已删除，请核实和修改后再保存");
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