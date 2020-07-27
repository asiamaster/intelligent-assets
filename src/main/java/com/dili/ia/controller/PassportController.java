package com.dili.ia.controller;


import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.Passport;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ia.domain.dto.PassportDto;
import com.dili.ia.service.PassportService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * @author:      xiaosa
 * @date:        2020/7/27
 * @version:     农批业务系统重构
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
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "passport/index";
    }

    /**
     * 跳转到 查看 页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap, long id) {

        Passport passport = passportService.get(id);
        modelMap.put("passport",passport);

        try{
            //日志查询
            BusinessLogQueryInput businessLogQueryInput = new BusinessLogQueryInput();
            businessLogQueryInput.setBusinessId(id);
            businessLogQueryInput.setBusinessType(LogBizTypeConst.PASSPORT);
            BaseOutput<List<BusinessLog>> businessLogOutput = businessLogRpc.list(businessLogQueryInput);
            if(businessLogOutput.isSuccess()){
                modelMap.put("logs",businessLogOutput.getData());
            }
        }catch (Exception e){
            logger.error("日志服务查询异常",e);
        }

        return "passport/view";
    }

    /**
     * 跳转到 新增 页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {

        return "passport/add";
    }

    /**
     * 跳转到 修改 页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap, long id) {

        Passport passport = passportService.get(id);
        modelMap.put("passport",passport);

        return "passport/update";
    }

    /**
     * 分页查询
     *
     * @param  passport
     * @return String
     * @date   2020/7/27
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute Passport passport) throws Exception {
        return passportService.listPage(passport).toString();
    }

    /**
     * 新增通行证
     *
     * @param
     * @return
     * @date   2020/7/27
     */
    @BusinessLogger(businessType = LogBizTypeConst.PASSPORT, content="${businessCode!}", operationType="add", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/add.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput add(@RequestBody PassportDto passportDto) {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<Passport> baseOutput = passportService.addPassport(passportDto, userTicket);

        // 写业务日志
        if (baseOutput.isSuccess()){
            Passport passport = baseOutput.getData();
            LoggerUtil.buildLoggerContext(passport.getId(), passport.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }

    /**
     * 修改通行证
     *
     * @param
     * @return
     * @date   2020/7/27
     */
    @BusinessLogger(businessType = LogBizTypeConst.PASSPORT, content="${businessCode!}", operationType="update", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@RequestBody PassportDto passportDto) throws Exception {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<Passport> baseOutput = passportService.updatePassport(passportDto, userTicket);

        // 写业务日志
        if (baseOutput.isSuccess()){
            Passport passport = baseOutput.getData();
            LoggerUtil.buildLoggerContext(passport.getId(), passport.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }

    /**
     * 提交通行证缴费
     *
     * @param  id
     * @return BaseOutput
     * @date   2020/7/27
     */
    @BusinessLogger(businessType = LogBizTypeConst.PASSPORT, content="${businessCode!}", operationType="submit", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput submit(Long id) throws Exception {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<Passport> baseOutput = passportService.submit(id, userTicket);

        // 写业务日志
        if (baseOutput.isSuccess()){
            Passport passport = baseOutput.getData();
            LoggerUtil.buildLoggerContext(passport.getId(), passport.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }

    /**
     * 取消通行证
     *
     * @param
     * @return
     * @date   2020/7/27
     */
    @BusinessLogger(businessType = LogBizTypeConst.PASSPORT, content="${businessCode!}", operationType="cancle", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/cancle.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput cancle(Long id) throws Exception {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<Passport> baseOutput = passportService.cancle(id, userTicket);

        // 写业务日志
        if (baseOutput.isSuccess()){
            Passport passport = baseOutput.getData();
            LoggerUtil.buildLoggerContext(passport.getId(), passport.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }

    /**
     * 撤回通行证缴费
     *
     * @param  id
     * @return BaseOutput
     * @date   2020/7/27
     */
    @BusinessLogger(businessType = LogBizTypeConst.PASSPORT, content="${businessCode!}", operationType="withdraw", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/withdraw.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput withdraw(Long id) throws Exception {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<Passport> baseOutput = passportService.withdraw(id, userTicket);

        // 写业务日志
        if (baseOutput.isSuccess()){
            Passport passport = baseOutput.getData();
            LoggerUtil.buildLoggerContext(passport.getId(), passport.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }

    /**
     * 退款申请
     *
     * @param passportDto
     * @return BaseOutput
     * @date   2020/7/27
     */
    @RequestMapping(value="/refund.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.PASSPORT, content = "", operationType = "refund", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput refund(@Validated PassportDto passportDto) {
        try {
            passportService.refund(passportDto);
        }catch (BusinessException e) {
            logger.error("通行证{}退款申请异常！",passportDto.getCode(), e);
            return BaseOutput.failure(e.getErrorCode(), e.getErrorMsg());
        }catch (Exception e) {
            logger.error("通行证{}退款申请异常！",passportDto.getCode(), e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
        return BaseOutput.success("退款成功");
    }
}