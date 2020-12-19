package com.dili.ia.controller;

import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.MeterDto;
import com.dili.ia.service.MeterService;
import com.dili.ia.util.AssertUtils;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author:      xiaosa
 * @date:        2020/6/12
 * @version:     农批业务系统重构
 * @description: 表管理 web 层
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
     * @return String
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
     * @return String
     * @date   2020/6/16
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "meter/add";
    }

    /**
     * 跳转到复制页面
     *
     * @param  id
     * @return String
     * @date   2020/6/16
     */
    @RequestMapping(value="/copy.action", method = RequestMethod.GET)
    public String copy(ModelMap modelMap, Long id) {
        if (id != null) {
            Meter meter = meterService.get(id);
            modelMap.put("meter", meter);
        }
        return "meter/copy";
    }

    /**
     * 跳转到查看页面
     *
     * @param  id
     * @return String
     * @date   2020/6/16
     */
    @RequestMapping(value="/view.action", method = RequestMethod.GET)
    public String view(ModelMap modelMap, Long id) {
        if (id != null) {
            Meter meter = meterService.get(id);
            modelMap.put("meter", meter);
        }
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
        if (id != null) {
            Meter meter = meterService.get(id);
            modelMap.put("meter", meter);
        }
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
     * @return BaseOutput
     * @date   2020/6/16
     */
    @BusinessLogger(businessType = LogBizTypeConst.METER, content="${businessCode!}", operationType="add", systemCode = "IA")
    @RequestMapping(value="/add.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput addMeter(@ModelAttribute MeterDto meterDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(meterDto.getType(), "表类型不能为空！");
            AssertUtils.notNull(meterDto.getPrice(), "单价不能为空！");
            AssertUtils.notNull(meterDto.getAssetsId(), "表地址不能为空！");
            AssertUtils.notNull(meterDto.getThisAmount(), "表编号不能为空！");
            AssertUtils.notNull(meterDto.getAssetsType(), "表类别不能为空！");
            AssertUtils.notEmpty(meterDto.getNumber(), "表编号不能为空！");

            // 新增
            Meter meter = meterService.addMeter(meterDto, userTicket);

            // 写业务日志
            LoggerUtil.buildLoggerContext(meter.getId(), null, userTicket.getId(), userTicket.getRealName(),
                    userTicket.getFirmId(), "新增水电表信息。");

            return BaseOutput.success().setData(meter);
        } catch (BusinessException e) {
            logger.info("新增表信息失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误！");
        }
    }

    /**
     * 修改表信息
     *
     * @param  meterDto
     * @return BaseOutput
     * @date   2020/6/29
     */
    @BusinessLogger(businessType = LogBizTypeConst.METER, content="${businessCode!}", operationType="update", systemCode = "IA")
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput updateMeter(@ModelAttribute MeterDto meterDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        try {
            // 参数校验
            AssertUtils.notNull(meterDto.getId(), "表主键不能为空！");
            AssertUtils.notNull(meterDto.getPrice(), "单价不能为空！");
            AssertUtils.notNull(meterDto.getAssetsId(), "表地址不能为空！");
            AssertUtils.notNull(meterDto.getThisAmount(), "表编号不能为空！");
            AssertUtils.notNull(meterDto.getAssetsType(), "表类别不能为空！");
            AssertUtils.notEmpty(meterDto.getNumber(), "表编号不能为空！");

            // 修改
            Meter meter = meterService.updateMeter(meterDto, userTicket);

            // 写业务日志
            LoggerUtil.buildLoggerContext(meter.getId(), null, userTicket.getId(), userTicket.getRealName(),
                    userTicket.getFirmId(), "修改水电表信息。");

            return BaseOutput.success().setData(meter);
        } catch (BusinessException e) {
            logger.info("修改表信息失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误！");
        }
    }

    /**
     * 根据表类型，输入的关键字，模糊获取"未绑定"的表编号集合(新增表用户关系页面回显)
     *
     * @param  type 表类型，有枚举 meterTypeEnum
     * @return meterList
     * @date   2020/6/16
     */
    @RequestMapping(value="/listUnbindMetersByType.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput listUnbindMetersByType(Integer type, String keyword) {
        try {
            // 参数校验
            AssertUtils.notNull(type, "表类型不能为空！");

            // 查询
            List<Meter> meterList = meterService.listUnbindMetersByType(type, keyword);

            return BaseOutput.success().setData(meterList);
        } catch (Exception e) {
            logger.info("服务器内部错误！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }

    }
}