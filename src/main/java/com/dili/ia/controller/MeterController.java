package com.dili.ia.controller;

import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.MeterDto;
import com.dili.ia.service.MeterService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.ss.domain.BaseOutput;

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

import java.util.List;

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
     * 跳转到欢迎页面
     *
     * @param  modelMap
     * @return 欢迎页面地址
     * @date   2020/6/16
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "meter/index";
    }

    /**
     * 跳转到新增页面
     *
     * @param  modelMap
     * @return 新增页面地址
     * @date   2020/6/16
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "meter/add";
    }

    /**
     * 跳转到查看页面
     *
     * @param  id 表主键
     * @return 查看页面地址
     * @date   2020/6/16
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
     * 跳转到修改页面
     *
     * @param  id 表主键
     * @return 修改页面地址
     * @date   2020/6/16
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
     * 查询表的集合(分页)
     *
     * @param  meterDto
     * @return meterDtoList
     * @date   2020/6/16
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute MeterDto meterDto) throws Exception {
        return meterService.listEasyuiPageByExample(meterDto, true).toString();
    }

    /**
     * 新增表信息
     *
     * @param  meterDto
     * @return 是否成功
     * @date   2020/6/16
     */
    @BusinessLogger(businessType = LogBizTypeConst.METER, content="${businessCode!}", operationType="add", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/add.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput addMeter(@ModelAttribute MeterDto meterDto) {

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
     * 修改表信息
     *
     * @param  meterDto
     * @return 是否成功
     * @date   2020/6/29
     */
    @BusinessLogger(businessType = LogBizTypeConst.METER, content="${businessCode!}", operationType="update", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput updateMeter(@ModelAttribute MeterDto meterDto) {

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
     * 根据表类型,获取未绑定的表编号集合(新增表用户关系页面回显)
     *
     * @param  type 表类型,有枚举 meterTypeEnum
     * @return meterList
     * @date   2020/6/16
     */
    @RequestMapping(value="/listUnbindMetersByType.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput listUnbindMetersByType(Integer type) {
        BaseOutput baseOutput = new BaseOutput();

        List<Meter> meterList = meterService.listUnbindMetersByType(type);
        baseOutput.setData(meterList);

        return baseOutput;

    }

    /**
     * 根据表类型、表编号查询表信息(新增缴水电费时页面回显)
     *
     * @param  type   表类型,有枚举 meterTypeEnum
     * @param  number 表编号
     * @return meterList
     * @date   2020/6/28
     */
    @RequestMapping(value="/getMeterLikeNumber.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getMeterLikeNumber(Integer type,String number) throws Exception {
        BaseOutput baseOutput = new BaseOutput();

        Meter meterInfo = meterService.getMeterLikeNumber(type, number);
        baseOutput.setData(meterInfo);

        return baseOutput;
    }

}