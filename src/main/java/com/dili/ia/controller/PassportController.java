package com.dili.ia.controller;


import com.dili.ia.domain.Passport;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ia.domain.dto.PassportDto;
import com.dili.ia.service.PassportService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
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
}