package com.dili.ia.controller;

import com.dili.ia.domain.Meter;
import com.dili.ia.domain.dto.MeterDto;
import com.dili.ia.service.MeterService;
import com.dili.ia.util.AssertUtils;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
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
     * 跳转到复制页面
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
        try {
            // 参数校验
            AssertUtils.notNull(meterDto.getType(), "表类型不能为空");
            AssertUtils.notNull(meterDto.getPrice(), "单价不能为空");
            AssertUtils.notEmpty(meterDto.getNumber(), "表编号不能为空");
            AssertUtils.notNull(meterDto.getAssetsId(), "表地址不能为空");
            AssertUtils.notNull(meterDto.getThisAmount(), "表编号不能为空");
            AssertUtils.notNull(meterDto.getAssetsType(), "表类别不能为空");

            // 新增
            BaseOutput<Meter> baseOutput = meterService.addMeter(meterDto);

            // 写业务日志
            if (baseOutput.isSuccess()){
                Meter meter = baseOutput.getData();
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(meter.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "新增水电表信息");
            }
            return baseOutput;
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            logger.info("修改表信息异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
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
        try {
            // 参数校验
            AssertUtils.notNull(meterDto.getId(), "表主键不能为空");
            AssertUtils.notNull(meterDto.getType(), "表类型不能为空");
            AssertUtils.notNull(meterDto.getPrice(), "单价不能为空");
            AssertUtils.notEmpty(meterDto.getNumber(), "表编号不能为空");
            AssertUtils.notNull(meterDto.getAssetsId(), "表地址不能为空");
            AssertUtils.notNull(meterDto.getThisAmount(), "表编号不能为空");
            AssertUtils.notNull(meterDto.getAssetsType(), "表类别不能为空");

            // 修改
            BaseOutput<Meter> baseOutput = meterService.updateMeter(meterDto);

            // 写业务日志
            if (baseOutput.isSuccess()){
                Meter meter = baseOutput.getData();
                UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
                LoggerUtil.buildLoggerContext(meter.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "修改水电表信息");
            }
            return baseOutput;
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            logger.info("修改表信息异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 根据表类型, 输入的关键字, 模糊获取"未绑定"的表编号集合(新增表用户关系页面回显)
     *
     * @param  type 表类型,有枚举 meterTypeEnum
     * @return meterList
     * @date   2020/6/16
     */
    @RequestMapping(value="/listUnbindMetersByType.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput listUnbindMetersByType(Integer type, String keyword) {
        try {
            AssertUtils.notNull(type, "表类型不能为空");
            // 查询
            List<Meter> meterList = meterService.listUnbindMetersByType(type, keyword);
            return BaseOutput.success().setData(meterList);
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            logger.info("修改表信息异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }

    }
}