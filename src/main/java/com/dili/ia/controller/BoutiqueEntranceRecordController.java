package com.dili.ia.controller;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.dto.BoutiqueEntranceRecordDto;
import com.dili.ia.service.BoutiqueEntranceRecordService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author:      xiaosa
 * @date:        2020/7/13
 * @version:     农批业务系统重构
 * @description: 精品停车 web 层
 */
@Controller
@RequestMapping("/boutiqueEntranceRecord")
public class BoutiqueEntranceRecordController {

    private final static Logger logger = LoggerFactory.getLogger(BoutiqueEntranceRecordController.class);

    @Autowired
    BoutiqueEntranceRecordService boutiqueEntranceRecordService;

    /**
     * 跳转到 精品停车 页面
     *
     * @param  modelMap
     * @return String
     * @date   2020/7/13
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "boutiqueEntranceRecord/index";
    }

    /**
     * 跳转到查看页面
     *
     * @param  id
     * @return String
     * @date   2020/7/13
     */
    @RequestMapping(value = "/view.action", method = RequestMethod.GET)
    public String view(ModelMap modelMap, Long id) {
        if (id != null) {
            BoutiqueEntranceRecordDto boutiqueEntranceRecordDto = boutiqueEntranceRecordService.getBoutiqueEntranceDtoById(id);
            modelMap.put("boutiqueEntranceRecord", boutiqueEntranceRecordDto);
        }

        return "boutiqueEntranceRecord/view";
    }

    /**
     * 跳转到确认计费页面
     *
     * @param  id
     * @return String
     * @date   2020/7/13
     */
    @RequestMapping(value = "/confirm.html", method = RequestMethod.GET)
    public String confirm(ModelMap modelMap, Long id) {
        if (id != null) {
            BoutiqueEntranceRecord boutiqueEntranceRecord = boutiqueEntranceRecordService.get(id);
            modelMap.put("boutiqueEntranceRecord", boutiqueEntranceRecord);
        }

        return "boutiqueEntranceRecord/confirm";
    }

    /**
     * 跳转到交费页面
     *
     * @param  id
     * @return String
     * @date   2020/7/13
     */
    @RequestMapping(value = "/submit.html", method = RequestMethod.GET)
    public String pay(ModelMap modelMap, Long id) {
        if (id != null) {
            BoutiqueEntranceRecordDto boutiqueEntranceRecordDto = boutiqueEntranceRecordService.getBoutiqueEntranceDtoById(id);
            modelMap.put("boutiqueEntranceRecord", boutiqueEntranceRecordDto);
        }

        return "boutiqueEntranceRecord/submit";
    }

    /**
     * 分页查询精品停车列表，返回easyui分页信息
     *
     * @param  boutiqueEntranceRecordDto
     * @return String
     * @throws Exception
     * @date   2020/7/13
     */
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(@ModelAttribute BoutiqueEntranceRecordDto boutiqueEntranceRecordDto) throws Exception {
        return boutiqueEntranceRecordService.listBoutiques(boutiqueEntranceRecordDto, true).toString();
    }

    /**
     * 确认计费
     *
     * @param  boutiqueEntranceRecord
     * @return BaseOutput
     * @date   2020/7/13
     */
    @RequestMapping(value = "/confirm.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content = "${businessCode!}", operationType = "confirm", systemCode = "IA")
    public @ResponseBody BaseOutput confirm(@ModelAttribute BoutiqueEntranceRecord boutiqueEntranceRecord) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(boutiqueEntranceRecord.getId(), "主键不能为空！");
            AssertUtils.notNull(boutiqueEntranceRecord.getConfirmTime(), "确认时间不能为空！");

            // 确认计费操作
            BoutiqueEntranceRecord boutiqueEntranceRecordInfo = boutiqueEntranceRecordService.confirm(boutiqueEntranceRecord, userTicket);

            // 写业务日志
            LoggerUtil.buildLoggerContext(boutiqueEntranceRecordInfo.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "精品黄楼确认计费");

            return BaseOutput.success().setData(boutiqueEntranceRecordInfo);
        } catch (BusinessException e) {
            logger.info("精品停车确认计费失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
    }

    /**
     * 交费
     *
     * @param  feeOrder
     * @return BaseOutput
     * @date   2020/7/13
     */
    @RequestMapping(value = "/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_FEE_ORDER, content = "${businessCode!}", operationType = "submit", systemCode = "IA")
    public @ResponseBody
    BaseOutput submit(@RequestBody BoutiqueFeeOrder feeOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(feeOrder.getAmount(), "停车费不能为空！");

            // 缴费逻辑操作
            BoutiqueEntranceRecord boutiqueEntranceRecordInfo = boutiqueEntranceRecordService.submit(feeOrder, userTicket);

            // 写业务日志
            LoggerUtil.buildLoggerContext(boutiqueEntranceRecordInfo.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "精品黄楼费用提交交费。");

            return BaseOutput.success().setData(boutiqueEntranceRecordInfo);
        } catch (BusinessException e) {
            logger.info("精品停车缴费失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误！");
        }
    }

    /**
     * 离场
     *
     * @param  id
     * @return BaseOutput
     * @date   2020/7/13
     */
    @RequestMapping(value = "/leave.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content = "${businessCode!}", operationType = "leave", systemCode = "IA")
    public @ResponseBody
    BaseOutput leave(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(id, "主键不能为空！");

            // 离场逻辑操作
            BoutiqueEntranceRecord boutiqueEntranceRecordInfo = boutiqueEntranceRecordService.leave(id, userTicket);

            // 写业务日志
            LoggerUtil.buildLoggerContext(boutiqueEntranceRecordInfo.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "精品黄楼停车离场。");

            return BaseOutput.success().setData(boutiqueEntranceRecordInfo);
        } catch (BusinessException e) {
            logger.info("精品停车离场失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误！");
        }
    }

    /**
     * 强制离场
     *
     * @param  id
     * @return BaseOutput
     * @date   2020/7/13
     */
    @RequestMapping(value = "/forceLeave.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content = "${businessCode!}", operationType = "forceLeave", systemCode = "IA")
    public @ResponseBody
    BaseOutput forceLeave(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(id, "主键不能为空！");

            // 强制离场操作
            BoutiqueEntranceRecord boutiqueEntranceRecordInfo = boutiqueEntranceRecordService.forceLeave(id, userTicket);

            // 写业务日志
            LoggerUtil.buildLoggerContext(boutiqueEntranceRecordInfo.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), "精品黄楼停车强制离场");

            return BaseOutput.success().setData(boutiqueEntranceRecordInfo);
        } catch (BusinessException e) {
            logger.info("精品停车强制离场失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误！");
        }
    }

    /**
     * 修改状态
     *
     * @param  boutiqueEntranceRecord
     * @return BaseOutput
     * @date   2020/7/13
     */
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(@ModelAttribute BoutiqueEntranceRecord boutiqueEntranceRecord) {
        try {
            // 参数校验
            AssertUtils.notNull(boutiqueEntranceRecord.getId(), "主键不能为空！");

            boutiqueEntranceRecordService.updateSelective(boutiqueEntranceRecord);

            return BaseOutput.success("修改成功");
        } catch (BusinessException e) {
            logger.info("修改精品停车失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误！");
        }
    }
}