package com.dili.ia.controller;

import com.alibaba.fastjson.JSON;
import com.dili.ia.domain.EarnestOrder;
import com.dili.ia.domain.EarnestOrderDetail;
import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.MeterDto;
import com.dili.ia.service.MeterService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.ss.domain.BaseOutput;
import java.util.List;

import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author:      xiaosa
 * @date:        2020/6/12
 * @version:     农批业务系统重构
 * @description: 水电费 - 表的相关业务 web 层
 */
@Controller
@RequestMapping("/meter")
public class MeterController {

    private final static Logger logger = LoggerFactory.getLogger(MeterController.class);

    @Autowired
    MeterService meterService;

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        modelMap
     * @return       String
     * @description：跳转到Meter页面
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "meter/index";
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        modelMap
     * @return       String
     * @description：跳转到水电表-新增页面
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "meter/add";
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        modelMap
     * @return       String
     * @description：跳转到水电表-查看页面
     */
    @RequestMapping(value="/view.action", method = RequestMethod.GET)
    public String view(ModelMap modelMap, Long id) {
        Meter meter = null;
        if (id != null) {
            meter = meterService.get(id);
        }
        logger.info(meter.toString());
        modelMap.put("meter", meter);
        return "meter/view";
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        modelMap
     * @return       String
     * @description：跳转到水电表-修改页面
     */
    @RequestMapping(value="/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap, Long id) {
        Meter meter = null;
        if (id != null) {
            meter = meterService.get(id);
        }
        logger.info(meter.toString());
        modelMap.put("meter", meter);
        return "meter/update";
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param
     * @return       String
     * @description：分页查询Meter，返回easyui分页信息
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute MeterDto meterDto) throws Exception {
        return meterService.listEasyuiPageByExample(meterDto, true).toString();
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        meterDto
     * @return       BaseOutput
     * @description：新增 Meter
     */
    @BusinessLogger(businessType = LogBizTypeConst.METER, content="${businessCode!}", operationType="add", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/add.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute MeterDto meterDto) {

        // 新增
        BaseOutput<Meter> output = meterService.addMeter(meterDto);

        // 写业务日志
        if (output.isSuccess()){
            Meter meter = output.getData();
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            LoggerUtil.buildLoggerContext(meter.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return output;
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/16
     * @param        meterDto
     * @return       BaseOutput
     * @description：修改Meter
     */
    @BusinessLogger(businessType = LogBizTypeConst.METER, content="${businessCode!}", operationType="update", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute MeterDto meterDto) {

        // 修改
        BaseOutput<Meter> output = meterService.updateMeter(meterDto);

        // 写业务日志
        if (output.isSuccess()){
            Meter meter = output.getData();
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            LoggerUtil.buildLoggerContext(meter.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return output;
    }

    /**
     * @author:      xiaosa
     * @date:        2020/6/28
     * @param        type, name
     * @return       String
     * @description：根据表类型、表编号查询表信息
     */
    @RequestMapping(value="/getMeterLikeNumber.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getMeterLikeNumber(Integer type,String name) throws Exception {
        return meterService.getMeterLikeNumber(type, name);
    }

}