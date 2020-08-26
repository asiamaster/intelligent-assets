package com.dili.ia.controller;

import com.dili.ia.domain.OtherFee;
import com.dili.ia.domain.Passport;
import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.domain.dto.OtherFeeDto;
import com.dili.ia.domain.dto.OtherFeeRefundOrderDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.service.DataAuthService;
import com.dili.ia.service.DepartmentChargeItemService;
import com.dili.ia.service.OtherFeeService;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
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
import io.seata.common.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author: xiaosa
 * @date: 2020/8/18
 * @version: 农批业务系统重构
 * @description: 其他收费
 */
@Controller
@RequestMapping("/otherFee")
public class OtherFeeController {
    private final static Logger LOG = LoggerFactory.getLogger(OtherFeeController.class);

    @Autowired
    OtherFeeService otherFeeService;

    @Autowired
    DepartmentChargeItemService departmentChargeItemService;

    @Autowired
    PaymentOrderService paymentOrderService;

    @Autowired
    BusinessLogRpc businessLogRpc;

    @Autowired
    DataAuthService dataAuthService;

    /**
     * 跳转到导航页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        //默认显示最近3天，结束时间默认为当前日期的23:59:59，开始时间为当前日期-2的00:00:00，选择到年月日时分秒
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime createdStart = LocalDateTime.of(nowTime.getYear(), nowTime.getMonth(), nowTime.getDayOfMonth() - 2, 0, 0, 0);
        LocalDateTime createdEnd = LocalDateTime.of(nowTime.getYear(), nowTime.getMonth(), nowTime.getDayOfMonth(), 23, 59, 59);
        modelMap.put("createdStart", createdStart);
        modelMap.put("createdEnd", createdEnd);

        modelMap.put("createdStart", createdStart);
        modelMap.put("createdEnd", createdEnd);
        return "otherFee/index";
    }

    /**
     * 跳转到其它收费管理-查看页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/view.action", method = RequestMethod.GET)
    public String view(ModelMap modelMap, Long id, String orderCode) {
        OtherFee otherFee = null;
        if (null != id) {
            otherFee = otherFeeService.get(id);
        } else if (StringUtils.isNotBlank(orderCode)) {
            PaymentOrder paymentOrder = new PaymentOrder();
            paymentOrder.setCode(orderCode);
            paymentOrder.setBizType(BizTypeEnum.OTHER_FEE.getCode());
            otherFee = otherFeeService.get(paymentOrderService.listByExample(paymentOrder).stream().findFirst().orElse(null).getBusinessId());
            id = otherFee.getId();
        }
        modelMap.put("otherFee", otherFee);
        try {
            //日志查询
            BusinessLogQueryInput businessLogQueryInput = new BusinessLogQueryInput();
            businessLogQueryInput.setBusinessId(id);
            businessLogQueryInput.setBusinessType(LogBizTypeConst.OTHER_FEE);
            BaseOutput<List<BusinessLog>> businessLogOutput = businessLogRpc.list(businessLogQueryInput);
            if (businessLogOutput.isSuccess()) {
                modelMap.put("logs", businessLogOutput.getData());
            }
        } catch (Exception e) {
            LOG.error("日志服务查询异常", e);
        }
        return "otherFee/view";
    }

    /**
     * 跳转到其它收费管理 - 新增页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "otherFee/add";
    }

    /**
     * 跳转到其它收费管理 - 修改页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap, Long id) {
        if (null != id) {
            OtherFee otherFee = otherFeeService.get(id);
            modelMap.put("otherFee", otherFee);
        }
        return "otherFee/update";
    }

    /**
     * 跳转到其它收费管理 - 退款申请页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/refundApply.html", method = RequestMethod.GET)
    public String refundApply(ModelMap modelMap, Long id) {
        if (null != id) {
            OtherFee otherFee = otherFeeService.get(id);
            modelMap.put("otherFee", otherFee);
        }
        return "otherFee/refundApply";
    }

    /**
     * 分页查询otherFee，返回easyui分页信息
     *
     * @param otherFeeDto
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(OtherFeeDto otherFeeDto) throws Exception {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        List<Long> departmentIdList = dataAuthService.getDepartmentDataAuth(userTicket);
        if (CollectionUtils.isEmpty(departmentIdList)) {
            return new EasyuiPageOutput(0, Collections.emptyList()).toString();
        }
        otherFeeDto.setMarketId(userTicket.getFirmId());
        otherFeeDto.setDepartmentIds(departmentIdList);
        return otherFeeService.listEasyuiPageByExample(otherFeeDto, true).toString();
    }

    /**
     * 新增 其他收费
     *
     * @param otherFeeDto
     * @return BaseOutput
     * @date 2020/8/18
     */
    @BusinessLogger(businessType = LogBizTypeConst.OTHER_FEE, content = "${businessCode!}", operationType = "add", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value = "/doAdd.action", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput doAdd(@RequestBody OtherFeeDto otherFeeDto) {
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            BaseOutput<OtherFee> output = otherFeeService.addOtherFee(otherFeeDto, userTicket);

            // 写业务日志
            if (output.isSuccess()) {
                OtherFee otherFee = output.getData();
                LoggerUtil.buildLoggerContext(otherFee.getId(), otherFee.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }

            return output;
        } catch (BusinessException e) {
            LOG.error("其他收费保存异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("其他收费保存异常！", e);
            return BaseOutput.failure("其他收费保存异常！");
        }
    }

    /**
     * 修改 其他收费
     *
     * @param otherFeeDto
     * @return BaseOutput
     * @date 2020/8/19
     */
    @BusinessLogger(businessType = LogBizTypeConst.OTHER_FEE, content = "${logContent!}", operationType = "edit", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value = "/doUpdate.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput doUpdate(OtherFeeDto otherFeeDto) {
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            BaseOutput<OtherFee> output = otherFeeService.updateOtherFee(otherFeeDto, userTicket);

            // 写业务日志
            if (output.isSuccess()) {
                OtherFee otherFee = output.getData();
                LoggerUtil.buildLoggerContext(otherFee.getId(), otherFee.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }

            return output;
        } catch (BusinessException e) {
            LOG.error("其它收费单修改异常！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("其它收费单修改异常！", e);
            return BaseOutput.failure("其它收费单修改异常");
        }
    }

    /**
     * 提交 其它收费
     *
     * @param id
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.OTHER_FEE, content = "${businessCode!}", operationType = "submit", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value = "/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput submit(Long id) {
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            BaseOutput<OtherFee> output = otherFeeService.submit(id, userTicket);

            // 写业务日志，只是退款申请
            if (output.isSuccess()) {
                OtherFee otherFee = output.getData();
                LoggerUtil.buildLoggerContext(otherFee.getId(), otherFee.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }

            return output;
        } catch (BusinessException e) {
            LOG.error("其它收费单提交失败！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("submit 其它收费单提交出错!", e);
            return BaseOutput.failure("提交出错！");
        }
    }

    /**
     * 取消 其他收费
     *
     * @param
     * @return
     * @date 2020/7/27
     */
    @BusinessLogger(businessType = LogBizTypeConst.OTHER_FEE, content = "${businessCode!}", operationType = "cancel", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value = "/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput cancel(Long id) throws Exception {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        BaseOutput<OtherFee> baseOutput = otherFeeService.cancel(id, userTicket);

        // 写业务日志
        if (baseOutput.isSuccess()) {
            OtherFee otherFee = baseOutput.getData();
            LoggerUtil.buildLoggerContext(otherFee.getId(), otherFee.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }

    /**
     * 撤回 其他消费
     *
     * @param id
     * @return BaseOutput
     * @date 2020/8/19
     */
    @BusinessLogger(businessType = LogBizTypeConst.OTHER_FEE, content = "${businessCode!}", operationType = "withdraw", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value = "/withdraw.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput withdraw(Long id) {
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            BaseOutput<OtherFee> baseOutput = otherFeeService.withdraw(id, userTicket);

            // 写业务日志
            if (baseOutput.isSuccess()) {
                OtherFee otherFee = baseOutput.getData();
                LoggerUtil.buildLoggerContext(otherFee.getId(), otherFee.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }

            return baseOutput;
        } catch (BusinessException e) {
            LOG.info("其他收费撤回异常！");
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return BaseOutput.failure(ResultCode.APP_ERROR, "撤回出错");
        }
    }

    /**
     * 其他收费 退款
     *
     * @param refundOrderDto
     * @return BaseOutput
     * @date 2020/8/19
     */
    @RequestMapping(value = "/refund.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput addRefundOrder(OtherFeeRefundOrderDto refundOrderDto) {
        try {
            return otherFeeService.refund(refundOrderDto);
        } catch (BusinessException e) {
            LOG.error("其他收费创建退款失败！", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("其他收费创建退款出错！", e);
            return BaseOutput.failure("创建退款出错！");
        }
    }

    /**
     * 新增其他收费时根据费用项查询部门集合
     *
     * @param
     * @return
     * @date 2020/8/19
     */
}