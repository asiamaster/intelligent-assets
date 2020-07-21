package com.dili.ia.controller;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ia.domain.dto.StockInDetailDto;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.MeterDetailService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author:      xiaosa
 * @date:        2020/6/23
 * @version:     农批业务系统重构
 * @description: 水电费
 */
@Controller
@RequestMapping("/meterDetail")
public class MeterDetailController {

    private final static Logger logger = LoggerFactory.getLogger(MeterDetailController.class);

    @Autowired
    MeterDetailService meterDetailService;

    @Autowired
    private BusinessChargeItemService businessChargeItemService;

    /**
     * 跳转到欢迎页面
     * 
     * @param  modelMap
     * @return 欢迎页面地址
     * @date   2020/6/29
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap, int metertype) {
        String meterUrl = "/water";
        if (2 == metertype) {
            meterUrl = "/electricity";
        }
        return "meterDetail" + meterUrl +  "/index";
    }

    /**
     * 跳转到欢迎页面
     *
     * @param  modelMap
     * @return 欢迎页面地址
     * @date   2020/6/29
     */
    @RequestMapping(value="/electricity/index.html", method = RequestMethod.GET)
    public String electricityIndex(ModelMap modelMap) {
        return "meterDetail/electricity/index";
    }

    /**
     * 跳转到欢迎页面
     *
     * @param  modelMap
     * @return 欢迎页面地址
     * @date   2020/6/29
     */
    @RequestMapping(value="/water/index.html", method = RequestMethod.GET)
    public String waterIndex(ModelMap modelMap) {
        return "meterDetail/water/index";
    }

    /**
     * 跳转到新增页面
     *
     * @param  modelMap
     * @return 新增页面地址
     * @date   2020/6/29
     */
    @RequestMapping(value="/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap, int metertype) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        // 动态收费项，根据业务类型查询相关的动态收费项类型
        List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.
                queryBusinessChargeItemConfig(userTicket.getFirmId(), "UTTLITIES", YesOrNoEnum.YES.getCode());
        modelMap.put("chargeItems", chargeItemDtos);

        String meterUrl = "/water";
        if (2 == metertype) {
            meterUrl = "/electricity";
        }
        return "meterDetail" + meterUrl +  "/add";
    }

    /**
     * 跳转到查看页面
     *
     * @param  id 水电费单主键id
     * @return 查看页面地址
     * @date   2020/6/29
     */
    @RequestMapping(value="/view.action", method = {RequestMethod.GET, RequestMethod.POST})
    public String view(ModelMap modelMap, Long id, int metertype) {
        MeterDetailDto meterDetailDto = null;

        if (id != null) {
            meterDetailDto = meterDetailService.getMeterDetailById(id);
        }
        modelMap.put("meterDetail", meterDetailDto);

        String meterUrl = "/water";
        if (2 == metertype) {
            meterUrl = "/electricity";
        }
        return "meterDetail" + meterUrl +  "/view";
    }

    /**
     * 跳转到修改页面
     *
     * @param  id 水电费单主键
     * @return 修改页面地址
     * @date   2020/6/29
     */
    @RequestMapping(value="/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap, Long id, int metertype) {
        MeterDetail meterDetail = null;

        if (id != null) {
            meterDetail = meterDetailService.getMeterDetailById(id);
        }
        modelMap.put("meterDetail", meterDetail);

        String meterUrl = "/water";
        if (2 == metertype) {
            meterUrl = "/electricity";
        }
        return "meterDetail" + meterUrl +  "/update";
    }

    /**
     * 查询水电费单的集合(分页)
     *
     * @param  meterDetailDto
     * @return MeterDetailDtoList
     * @date   2020/6/28
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute MeterDetailDto meterDetailDto) throws Exception {
        return meterDetailService.listMeterDetails(meterDetailDto, true).toString();
    }

    /**
     * 新增水电费单(仅保存收费单信息)
     *
     * @param  meterDetailDto
     * @return 是否成功
     * @date   2020/6/28
     */
    @BusinessLogger(businessType = LogBizTypeConst.UTILITIES, content="${businessCode!}", operationType="add", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/add.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@RequestBody MeterDetailDto meterDetailDto) {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<MeterDetail> baseOutput = meterDetailService.addMeterDetail(meterDetailDto, userTicket);

        // 写业务日志
        if (baseOutput.isSuccess()){
            MeterDetail meterDetail = baseOutput.getData();
            LoggerUtil.buildLoggerContext(meterDetail.getId(), meterDetail.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }


    /**
     * 提交水电费单(生缴费单和结算单)
     *
     * @param  id
     * @return 是否成功
     * @date   2020/7/6
     */
    @BusinessLogger(businessType = LogBizTypeConst.UTILITIES, content="${businessCode!}", operationType="submit", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput submit(Long id) {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<MeterDetail> baseOutput = meterDetailService.submit(id, userTicket);

        // 写业务日志
        if (baseOutput.isSuccess()){
            MeterDetail meterDetail = baseOutput.getData();
            LoggerUtil.buildLoggerContext(meterDetail.getId(), meterDetail.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }

    /**
     * 撤回水电费单(取消缴费单和结算单,将水电费单修改为已创建)
     *
     * @param  id
     * @return 是否成功
     * @date   2020/7/6
     */
    @BusinessLogger(businessType = LogBizTypeConst.UTILITIES, content="${businessCode!}", operationType="withdraw", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/withdraw.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput withdraw(Long id) {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<MeterDetail> baseOutput = meterDetailService.withdraw(id, userTicket);

        // 写业务日志
        if (baseOutput.isSuccess()){
            MeterDetail meterDetail = baseOutput.getData();
            LoggerUtil.buildLoggerContext(meterDetail.getId(), meterDetail.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }

    /**
     * 取消水电费单
     *
     * @param  id
     * @return 是否成功
     * @date   2020/7/6
     */
    @BusinessLogger(businessType = LogBizTypeConst.UTILITIES, content="${businessCode!}", operationType="cancel", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput cancel(Long id) {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<MeterDetail> baseOutput = meterDetailService.cancel(id, userTicket);

        // 写业务日志
        if (baseOutput.isSuccess()){
            MeterDetail meterDetail = baseOutput.getData();
            LoggerUtil.buildLoggerContext(meterDetail.getId(), meterDetail.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }

    /**
     * 修改水电费单
     * 
     * @param  meterDetailDto
     * @return 是否成功
     * @date   2020/6/29
     */
    @BusinessLogger(businessType = LogBizTypeConst.UTILITIES, content="${businessCode!}", operationType="update", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@RequestBody MeterDetailDto meterDetailDto) {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<MeterDetail> baseOutput = meterDetailService.updateMeterDetail(meterDetailDto);

        // 写业务日志
        if (baseOutput.isSuccess()){
            MeterDetail meterDetail = baseOutput.getData();
            LoggerUtil.buildLoggerContext(meterDetail.getId(), meterDetail.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }

    /**
     * 根据 meterId 获取初始值(合并到 customerMeter 中的 getBindInfoByMeterId 方法)
     *
     * @param  meterId
     * @return 初始值(上期指数)
     * @date   2020/6/29
     */
    @RequestMapping(value = "/getLastAmount.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getLastAmount(Long meterId){
        return meterDetailService.getLastAmount(meterId);
    }

    /**
     * 通过计费规则算取费用
     *
     * @param meterDetailDto
     * @return BaseOutput
     * @date   2020/7/17
     */
    @RequestMapping(value="/getCost.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.UTILITIES, content = "", operationType = "refund", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput getCost(@RequestBody MeterDetailDto meterDetailDto) {	        //throw new BusinessException("2000", "errorCode");
        BaseOutput baseOutput = BaseOutput.success();

        try {
            baseOutput.setData(meterDetailService.getCost(meterDetailDto));
        }catch (BusinessException e) {
            logger.error("费用{}计算异常！",meterDetailDto.getCode(), e);
            return BaseOutput.failure(e.getErrorCode(), e.getErrorMsg());
        }catch (Exception e) {
            logger.error("费用{}计算异常！",meterDetailDto.getCode(), e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }

        return baseOutput;
    }
}