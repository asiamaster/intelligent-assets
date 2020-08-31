package com.dili.ia.controller;


import com.dili.ia.domain.Passport;
import com.dili.ia.domain.dto.PassportDto;
import com.dili.ia.domain.dto.PassportRefundOrderDto;
import com.dili.ia.service.PassportService;
import com.dili.ia.util.AssertUtils;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * @author: xiaosa
 * @date: 2020/7/27
 * @version: 农批业务系统重构
 * @description: 通行证
 */
@Controller
@RequestMapping("/passport")
public class PassportController {

    private final static Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private PassportService passportService;

    @Autowired
    BusinessLogRpc businessLogRpc;

    /**
     * 跳转到 index 页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "passport/index";
    }

    /**
     * 跳转到 查看 页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/view.action", method = RequestMethod.GET)
    public String view(ModelMap modelMap, long id) throws Exception {

        Passport passport = passportService.get(id);
        modelMap.put("passport", passport);

        try {
            //日志查询
            BusinessLogQueryInput businessLogQueryInput = new BusinessLogQueryInput();
            businessLogQueryInput.setBusinessId(id);
            businessLogQueryInput.setBusinessType(LogBizTypeConst.PASSPORT);
            BaseOutput<List<BusinessLog>> businessLogOutput = businessLogRpc.list(businessLogQueryInput);
            if (businessLogOutput.isSuccess()) {
                modelMap.put("logs", businessLogOutput.getData());
            }
        } catch (Exception e) {
            logger.error("日志服务查询异常", e);
        }

        return "passport/view";
    }

    /**
     * 跳转到 查看 页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/print.html", method = RequestMethod.GET)
    public String print(ModelMap modelMap, long id) {

        Passport passport = passportService.get(id);
        modelMap.put("passport", passport);

        return "passport/print";
    }

    /**
     * 跳转到 新增 页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {

        return "passport/add";
    }

    /**
     * 跳转到 修改 页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap, long id) {

        Passport passport = passportService.get(id);
        modelMap.put("passport", passport);

        return "passport/update";
    }

    /**
     * 跳转到 申请退款 页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/refundApply.html", method = RequestMethod.GET)
    public String refund(ModelMap modelMap, long id) {

        Passport passport = passportService.get(id);
        modelMap.put("passport", passport);

        return "passport/refundApply";
    }

    /**
     * 分页查询
     *
     * @param passportDto
     * @return String
     * @date 2020/7/27
     */
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(@ModelAttribute PassportDto passportDto) throws Exception {
        return passportService.listPassports(passportDto, true).toString();
    }

    /**
     * 新增通行证
     *
     * @param
     * @return
     * @date 2020/7/27
     */
    @BusinessLogger(businessType = LogBizTypeConst.PASSPORT, content = "${businessCode!}", operationType = "add", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value = "/add.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput add(@RequestBody PassportDto passportDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            this.ParamValidate(passportDto);

            // 新增通行证
            BaseOutput<Passport> baseOutput = passportService.addPassport(passportDto, userTicket);

            // 写业务日志
            if (baseOutput.isSuccess()) {
                Passport passport = baseOutput.getData();
                LoggerUtil.buildLoggerContext(passport.getId(), passport.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }

            return baseOutput;
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.info("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 修改通行证
     *
     * @param
     * @return
     * @date 2020/7/27
     */
    @BusinessLogger(businessType = LogBizTypeConst.PASSPORT, content = "${businessCode!}", operationType = "edit", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@RequestBody PassportDto passportDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(passportDto.getId(), "主键不能为空");
            this.ParamValidate(passportDto);

            BaseOutput<Passport> baseOutput = passportService.updatePassport(passportDto, userTicket);

            // 写业务日志
            if (baseOutput.isSuccess()) {
                Passport passport = baseOutput.getData();
                LoggerUtil.buildLoggerContext(passport.getId(), passport.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }

            return baseOutput;
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.info("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 提交通行证缴费
     *
     * @param id
     * @return BaseOutput
     * @date 2020/7/27
     */
    @BusinessLogger(businessType = LogBizTypeConst.PASSPORT, content = "${businessCode!}", operationType = "submit", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value = "/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput submit(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(id, "主键不能为空");

            // 提交操作
            BaseOutput<Passport> baseOutput = passportService.submit(id, userTicket);

            // 写业务日志
            if (baseOutput.isSuccess()) {
                Passport passport = baseOutput.getData();
                LoggerUtil.buildLoggerContext(passport.getId(), passport.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }

            return baseOutput;
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.info("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 取消通行证
     *
     * @param
     * @return
     * @date 2020/7/27
     */
    @BusinessLogger(businessType = LogBizTypeConst.PASSPORT, content = "${businessCode!}", operationType = "cancel", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value = "/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput cancel(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(id, "主键不能为空");

            // 撤销操作
            BaseOutput<Passport> baseOutput = passportService.cancel(id, userTicket);

            // 写业务日志
            if (baseOutput.isSuccess()) {
                Passport passport = baseOutput.getData();
                LoggerUtil.buildLoggerContext(passport.getId(), passport.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }

            return baseOutput;
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.info("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 撤回通行证缴费
     *
     * @param id
     * @return BaseOutput
     * @date 2020/7/27
     */
    @BusinessLogger(businessType = LogBizTypeConst.PASSPORT, content = "${businessCode!}", operationType = "withdraw", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value = "/withdraw.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput withdraw(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(id, "主键不能为空");

            // 撤回操作
            BaseOutput<Passport> baseOutput = passportService.withdraw(id, userTicket);

            // 写业务日志
            if (baseOutput.isSuccess()) {
                Passport passport = baseOutput.getData();
                LoggerUtil.buildLoggerContext(passport.getId(), passport.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }

            return baseOutput;
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.info("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 退款申请
     *
     * @param passportRefundOrderDto
     * @return BaseOutput
     * @date 2020/7/27
     */
    @RequestMapping(value = "/refund.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput refund(@RequestBody PassportRefundOrderDto passportRefundOrderDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(passportRefundOrderDto.getBusinessId(), "业务编号不能为空");

            // 退款
            BaseOutput<Passport> baseOutput = passportService.refund(passportRefundOrderDto, userTicket);

            // 写业务日志，只是退款申请
            if (baseOutput.isSuccess()) {
                Passport passport = baseOutput.getData();
                LoggerUtil.buildLoggerContext(passport.getId(), passport.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }

            return baseOutput;
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.info("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 参数校验
     *
     * @param passportDto
     */
    private void ParamValidate(PassportDto passportDto) {
        AssertUtils.notEmpty(passportDto.getCustomerName(), "客户名称不能为空");
        AssertUtils.notEmpty(passportDto.getCertificateNumber(), "证件号码不能为空");
        AssertUtils.notEmpty(passportDto.getCustomerCellphone(), "联系电话不能为空");
        AssertUtils.notNull(passportDto.getCarType(), "车型不能为空");
        AssertUtils.notNull(passportDto.getEndTime(), "结束日期不能为空");
        AssertUtils.notEmpty(passportDto.getCarNumber(), "车牌号不能为空");
        AssertUtils.notNull(passportDto.getStartTime(), "开始日期不能为空");
        AssertUtils.notNull(passportDto.getValidPeriod(), "有效期不能为空");
        AssertUtils.notEmpty(passportDto.getLicenseCode(), "证件类型不能为空");
        AssertUtils.notNull(passportDto.getDepartmentId(), "业务所属部门不能为空");
        AssertUtils.notNull(passportDto.getTollAmount(), "通行费不能为空");
    }
}