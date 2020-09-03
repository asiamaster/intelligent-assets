package com.dili.ia.controller;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.dto.BoutiqueEntranceRecordDto;
import com.dili.ia.service.BoutiqueEntranceRecordService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: xiaosa
 * @date: 2020/7/13
 * @version: 农批业务系统重构
 * @description: 精品停车
 */
@Controller
@RequestMapping("/boutiqueEntranceRecord")
public class BoutiqueEntranceRecordController {

    private final static Logger logger = LoggerFactory.getLogger(BoutiqueEntranceRecordController.class);

    @Autowired
    BoutiqueEntranceRecordService boutiqueEntranceRecordService;

    /**
     * 跳转到BoutiqueEntranceRecord页面
     *
     * @param modelMap
     * @return String
     * @date 2020/7/13
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "boutiqueEntranceRecord/index";
    }

    /**
     * 跳转到查看页面
     *
     * @param id 精品停车主键
     * @return 查看页面地址
     * @date 2020/7/13
     */
    @RequestMapping(value = "/view.action", method = RequestMethod.GET)
    public String view(ModelMap modelMap, Long id) {
        BoutiqueEntranceRecordDto boutiqueEntranceRecordDto = null;

        if (id != null) {
            boutiqueEntranceRecordDto = boutiqueEntranceRecordService.getBoutiqueEntranceDtoById(id);
        }
        modelMap.put("boutiqueEntranceRecord", boutiqueEntranceRecordDto);

        return "boutiqueEntranceRecord/view";
    }

    /**
     * 跳转到确认计费页面
     *
     * @param id 精品停车主键
     * @return 查看页面地址
     * @date 2020/7/13
     */
    @RequestMapping(value = "/confirm.html", method = RequestMethod.GET)
    public String confirm(ModelMap modelMap, Long id) {
        BoutiqueEntranceRecord boutiqueEntranceRecord = null;

        if (id != null) {
            boutiqueEntranceRecord = boutiqueEntranceRecordService.get(id);
        }
        modelMap.put("boutiqueEntranceRecord", boutiqueEntranceRecord);

        return "boutiqueEntranceRecord/confirm";
    }

    /**
     * 跳转到交费页面
     *
     * @param id 精品停车主键
     * @return 查看页面地址
     * @date 2020/7/13
     */
    @RequestMapping(value = "/submit.html", method = RequestMethod.GET)
    public String pay(ModelMap modelMap, Long id) {
        BoutiqueEntranceRecordDto boutiqueEntranceRecordDto = null;

        if (id != null) {
            boutiqueEntranceRecordDto = boutiqueEntranceRecordService.getBoutiqueEntranceDtoById(id);
        }
        modelMap.put("boutiqueEntranceRecord", boutiqueEntranceRecordDto);

        return "boutiqueEntranceRecord/submit";
    }

    /**
     * 确认计费
     *
     * @param boutiqueEntranceRecord
     * @return BaseOutput
     * @date 2020/7/13
     */
    @RequestMapping(value = "/confirm.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content = "${businessCode!}", operationType = "confirm", systemCode = "IA")
    public @ResponseBody BaseOutput confirm(@ModelAttribute BoutiqueEntranceRecord boutiqueEntranceRecord) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(boutiqueEntranceRecord.getId(), "主键不能为空");

            // 确认计费操作
            BaseOutput<BoutiqueEntranceRecord> baseOutput = boutiqueEntranceRecordService.confirm(boutiqueEntranceRecord, userTicket);

            // 写业务日志
            if (baseOutput.isSuccess()) {
                BoutiqueEntranceRecord boutiqueEntranceRecordInfo = baseOutput.getData();
                LoggerUtil.buildLoggerContext(boutiqueEntranceRecordInfo.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }
            return baseOutput;
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            logger.info("服务器内部错误！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 交费
     *
     * @param feeOrder 交费单
     * @return BaseOutput
     * @date 2020/7/13
     */
    @RequestMapping(value = "/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_FEE_ORDER, content = "${businessCode!}", operationType = "submit", systemCode = "IA")
    public @ResponseBody
    BaseOutput submit(@RequestBody BoutiqueFeeOrder feeOrder) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(feeOrder.getId(), "主键不能为空");
            BaseOutput<BoutiqueEntranceRecord> baseOutput = boutiqueEntranceRecordService.submit(feeOrder, userTicket);

            // 写业务日志
            if (baseOutput.isSuccess()) {
                BoutiqueEntranceRecord boutiqueEntranceRecordInfo = baseOutput.getData();
                LoggerUtil.buildLoggerContext(boutiqueEntranceRecordInfo.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }

            return baseOutput;
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            logger.info("服务器内部错误！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 离场
     *
     * @param id 主键
     * @return BaseOutput
     * @date 2020/7/13
     */
    @RequestMapping(value = "/leave.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content = "${businessCode!}", operationType = "leave", systemCode = "IA")
    public @ResponseBody
    BaseOutput leave(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(id, "主键不能为空");

            // 离场操作
            BaseOutput<BoutiqueEntranceRecord> baseOutput = boutiqueEntranceRecordService.leave(id, userTicket);

            // 写业务日志
            if (baseOutput.isSuccess()) {
                BoutiqueEntranceRecord boutiqueEntranceRecordInfo = baseOutput.getData();
                LoggerUtil.buildLoggerContext(boutiqueEntranceRecordInfo.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }

            return baseOutput;
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            logger.info("服务器内部错误！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 强制离场
     *
     * @param id 主键
     * @return BaseOutput
     * @date 2020/7/13
     */
    @RequestMapping(value = "/forceLeave.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content = "${businessCode!}", operationType = "forceLeave", systemCode = "IA")
    public @ResponseBody
    BaseOutput forceLeave(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            // 参数校验
            AssertUtils.notNull(id, "主键不能为空");

            // 强制离场操作
            BaseOutput<BoutiqueEntranceRecord> baseOutput = boutiqueEntranceRecordService.forceLeave(id, userTicket);

            // 写业务日志
            if (baseOutput.isSuccess()) {
                BoutiqueEntranceRecord boutiqueEntranceRecordInfo = baseOutput.getData();
                LoggerUtil.buildLoggerContext(boutiqueEntranceRecordInfo.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }
            return baseOutput;
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            logger.info("服务器内部错误！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 分页查询精品停车列表，返回easyui分页信息
     *
     * @param boutiqueEntranceRecordDto
     * @return String
     * @throws Exception
     * @date 2020/7/13
     */
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(@ModelAttribute BoutiqueEntranceRecordDto boutiqueEntranceRecordDto) throws Exception {
        return boutiqueEntranceRecordService.listBoutiques(boutiqueEntranceRecordDto, true).toString();
    }

    /**
     * 修改状态
     *
     * @param boutiqueEntranceRecord
     * @return BaseOutput
     * @date 2020/7/13
     */
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(@ModelAttribute BoutiqueEntranceRecord boutiqueEntranceRecord) {
        try {
            // 参数校验
            AssertUtils.notNull(boutiqueEntranceRecord.getId(), "主键不能为空");

            boutiqueEntranceRecordService.updateSelective(boutiqueEntranceRecord);
            return BaseOutput.success("修改成功");
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            logger.info("服务器内部错误！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }
}