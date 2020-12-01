package com.dili.ia.controller;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.MeterTypeEnum;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.MeterDetailService;
import com.dili.ia.util.AssertUtils;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    BusinessLogRpc businessLogRpc;

    @Autowired
    private BusinessChargeItemService businessChargeItemService;

    /**
     * 跳转到水费列表页面
     *
     *
     * @param  modelMap
     * @return String
     * @date   2020/6/29
     */
    @RequestMapping(value = "/water/index.html", method = RequestMethod.GET)
    public String waterIndex(ModelMap modelMap) {
        return "meterDetail/water/index";
    }

    /**
     * 跳转到水费新增页面
     *
     * @param  modelMap
     * @return String
     * @date   2020/6/29
     */
    @RequestMapping(value = "/water/add.html", method = RequestMethod.GET)
    public String addWater(ModelMap modelMap) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        // 动态收费项，根据业务类型查询相关的动态收费项类型
        List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.
                queryBusinessChargeItemConfig(userTicket.getFirmId(), BizTypeEnum.WATER.getCode(), YesOrNoEnum.YES.getCode());
        modelMap.put("chargeItems", chargeItemDtos);

        return "meterDetail/water/add";
    }

    /**
     * 跳转到水费修改页面
     *
     * @param  id
     * @return
     * @date   2020/6/29
     */
    @RequestMapping(value = "/water/update.html", method = RequestMethod.GET)
    public String updateWater(ModelMap modelMap, Long id) {
        if (id != null) {
            MeterDetailDto meterDetail = meterDetailService.getMeterDetailDtoById(id);
            modelMap.put("meterDetail", meterDetail);
        }

        return "meterDetail/water/update";
    }

    /**
     * 跳转到电费列表页面
     *
     * @param  modelMap
     * @return String
     * @date   2020/6/29
     */
    @RequestMapping(value = "/electricity/index.html", method = RequestMethod.GET)
    public String electricityIndex(ModelMap modelMap) {
        return "meterDetail/electricity/index";
    }

    /**
     * 跳转到电费新增页面
     *
     * @param  modelMap
     * @return String
     * @date   2020/6/29
     */
    @RequestMapping(value = "/electricity/add.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        // 动态收费项，根据业务类型查询相关的动态收费项类型
        List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.
                queryBusinessChargeItemConfig(userTicket.getFirmId(), BizTypeEnum.ELECTRICITY.getCode(), YesOrNoEnum.YES.getCode());
        modelMap.put("chargeItems", chargeItemDtos);

        return "meterDetail/electricity/add";
    }

    /**
     * 跳转到电费修改页面
     *
     * @param  id
     * @return
     * @date   2020/6/29
     */
    @RequestMapping(value = "/electricity/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap, Long id) {
        if (id != null) {
            MeterDetailDto meterDetail = meterDetailService.getMeterDetailDtoById(id);
            modelMap.put("meterDetail", meterDetail);
        }

        return "meterDetail/electricity/update";
    }

    /**
     * 跳转到查看页面
     *
     * @param  id
     * @return String
     * @date   2020/6/29
     */
    @RequestMapping(value = "/view.action", method = {RequestMethod.GET, RequestMethod.POST})
    public String view(ModelMap modelMap, Long id) {
        MeterDetailDto meterDetailDto = null;

        if (id != null) {
            meterDetailDto = meterDetailService.getMeterDetailDtoById(id);
        }
        modelMap.put("meterDetail", meterDetailDto);

        try {
            //日志查询
            BusinessLogQueryInput businessLogQueryInput = new BusinessLogQueryInput();
            businessLogQueryInput.setBusinessId(id);
            // 注：水电费日志类型拆分，需要传日志类型
            businessLogQueryInput.setBusinessType(LogBizTypeConst.ELECTRICITY_CODE);
            if (MeterTypeEnum.WATER_METER.getCode().equals(meterDetailDto.getType())) {
                businessLogQueryInput.setBusinessType(LogBizTypeConst.WATER_CODE);
            }

            BaseOutput list = businessLogRpc.list(businessLogQueryInput);
            if (list.isSuccess()) {
                modelMap.put("logs", list.getData());
            }
        } catch (Exception e) {
            logger.error("日志服务查询异常", e);
        }

        String meterUrl = "meterDetail/water/view";
        if (MeterTypeEnum.ELECTRIC_METER.getCode().equals(meterDetailDto.getType())) {
            meterUrl = "meterDetail/electricity/view";
        }
        return meterUrl;
    }

    /**
     * 查询水电费单的集合(分页)
     *
     * @param meterDetailDto
     * @return MeterDetailDtoList
     * @date 2020/6/28
     */
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(@ModelAttribute MeterDetailDto meterDetailDto, String meterType) throws Exception {
        meterDetailDto.setType(Integer.valueOf(meterType));
        return meterDetailService.listMeterDetails(meterDetailDto, true).toString();
    }

    /**
     * 新增 水电费单(仅保存收费单信息)
     *
     * @param  meterDetailDto
     * @return BaseOutput
     * @date   2020/6/28
     */
    @BusinessLogger(content = "${businessCode!}", operationType = "add", systemCode = "IA")
    @RequestMapping(value = "/add.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput add(@RequestBody MeterDetailDto meterDetailDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            this.ParamValidate(meterDetailDto);

            // 新增水电费
            MeterDetailDto meterDetailDtoInfo = meterDetailService.addMeterDetail(meterDetailDto, userTicket);

            // 写业务日志，默认电费
            return getBaseOutput(userTicket, meterDetailDtoInfo);
        } catch (BusinessException e) {
            logger.info("新增水电费单失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 修改水电费单
     *
     * @param  meterDetailDto
     * @return BaseOutput
     * @date   2020/6/29
     */
    @BusinessLogger(content = "${businessCode!}", operationType = "update", systemCode = "IA")
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(@RequestBody MeterDetailDto meterDetailDto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(meterDetailDto.getId(), "主键不能为空");
            this.ParamValidate(meterDetailDto);

            // 修改操作
            MeterDetailDto meterDetailDtoInfo = meterDetailService.updateMeterDetail(meterDetailDto);

            // 写业务日志，默认电费
            return getBaseOutput(userTicket, meterDetailDtoInfo);
        } catch (BusinessException e) {
            logger.info("修改水电费单失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }



    /**
     * 提交 水电费单(生缴费单和结算单)
     *
     * @param  ids
     * @return BaseOutput
     * @date   2020/7/6
     */
    @BusinessLogger(content = "${businessCode!}", operationType = "submit", systemCode = "IA")
    @RequestMapping(value = "/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput submit(String ids) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notEmpty(ids, "主键不能为空");

            // 提交操作
            List<Long> idList = Arrays.stream(ids.split(",")).map(Long::valueOf).collect(Collectors.toList());
            List<MeterDetailDto> meterDetailDtoList = meterDetailService.submit(idList, userTicket);

            return getBaseOutput(userTicket, meterDetailDtoList);
        } catch (BusinessException e) {
            logger.info("提交水电费单失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 全部提交 水电费单(生缴费单和结算单)
     *
     * @return BaseOutput
     * @date   2020/7/29
     */
    @BusinessLogger(content = "${businessCode!}", operationType = "submit", systemCode = "IA")
    @RequestMapping(value = "/submitAll.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput submitAll(Integer metertype) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(metertype, "表类型不能为空");

            // 全部提交操作
            List<MeterDetailDto> meterDetailDtoList = meterDetailService.submitAll(userTicket, metertype);

            // 写业务日志，默认电费
            return getBaseOutput(userTicket, meterDetailDtoList);
        } catch (BusinessException e) {
            logger.info("全部提交水电费单失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 撤回 水电费单(取消缴费单和结算单,将水电费单修改为已创建)
     *
     * @param  id
     * @return BaseOutput
     * @date   2020/7/6
     */
    @BusinessLogger(content = "${businessCode!}", operationType = "withdraw", systemCode = "IA")
    @RequestMapping(value = "/withdraw.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput withdraw(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(id, "主键不能为空");

            // 撤回操作
            MeterDetailDto meterDetailDtoInfo = meterDetailService.withdraw(id, userTicket);

            // 写业务日志，默认电费
            return getBaseOutput(userTicket, meterDetailDtoInfo);
        } catch (BusinessException e) {
            logger.info("撤回水电费单失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 取消 水电费单
     *
     * @param   id
     * @return  BaseOutput
     * @date    2020/7/6
     */
    @BusinessLogger(content = "${businessCode!}", operationType = "cancel", systemCode = "IA")
    @RequestMapping(value = "/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput cancel(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(id, "主键不能为空");

            // 取消操作
            MeterDetailDto meterDetailDtoInfo = meterDetailService.cancel(id, userTicket);

            // 写业务日志，默认电费
            return getBaseOutput(userTicket, meterDetailDtoInfo);
        } catch (BusinessException e) {
            logger.info("取消水电费单失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 单个写日志抽取（用于修改、新增、撤回、取消）
     */
    private BaseOutput getBaseOutput(UserTicket userTicket, MeterDetailDto meterDetailDto) {
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE, LogBizTypeConst.ELECTRICITY_CODE);
        if (MeterTypeEnum.WATER_METER.getCode().equals(meterDetailDto.getType())) {
            // 水费
            LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE, LogBizTypeConst.WATER_CODE);
        }
        LoggerUtil.buildLoggerContext(meterDetailDto.getId(), meterDetailDto.getCode(), userTicket.getId(), userTicket.getRealName(),
                userTicket.getFirmId(), null);

        return BaseOutput.success().setData(meterDetailDto);
    }

    /**
     * 集合写日志抽取（用于提交、批量提交）
     */
    @NotNull
    private BaseOutput getBaseOutput(UserTicket userTicket, List<MeterDetailDto> meterDetailDtoList) {
        if (CollectionUtils.isNotEmpty(meterDetailDtoList)) {
            // 写业务日志，默认电费
            meterDetailDtoList.forEach(meterDetailDto -> {
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE, LogBizTypeConst.ELECTRICITY_CODE);
                if (MeterTypeEnum.WATER_METER.getCode().equals(meterDetailDto.getType())) {
                    // 水费
                    LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE, LogBizTypeConst.WATER_CODE);
                }
                LoggerUtil.buildLoggerContext(meterDetailDto.getId(), meterDetailDto.getCode(), userTicket.getId(), userTicket.getRealName(),
                        userTicket.getFirmId(), null);
            });
        }

        return BaseOutput.success();
    }

    /**
     * 根据 meterId 获取初始值
     *
     * @param  meterId
     * @return BaseOutput 初始值(上期指数)
     * @date   2020/6/29
     */
    @RequestMapping(value = "/getLastAmount.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput getLastAmount(Long meterId) {
        return meterDetailService.getLastAmount(meterId);
    }

    /**
     * 参数校验抽取
     *
     * @param meterDetailDto
     */
    private void ParamValidate(MeterDetailDto meterDetailDto) {
        AssertUtils.notNull(meterDetailDto.getCustomerId(), "客户不能为空");
        AssertUtils.notNull(meterDetailDto.getAssetsName(), "表地址不能为空");
        AssertUtils.notNull(meterDetailDto.getAssetsType(), "表类别不能为空");
        AssertUtils.notNull(meterDetailDto.getDepartmentId(), "部门不能为空");
        AssertUtils.notNull(meterDetailDto.getUsageTime(), "截止月份不能为空");
        AssertUtils.notNull(meterDetailDto.getThisAmount(), "本期指数不能为空");
        AssertUtils.notEmpty(meterDetailDto.getNumber(), "表编号不能为空");
        AssertUtils.notEmpty(meterDetailDto.getCustomerCellphone(), "客户联系电话不能为空");
    }


}