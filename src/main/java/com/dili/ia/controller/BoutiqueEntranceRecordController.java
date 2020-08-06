package com.dili.ia.controller;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.Passport;
import com.dili.ia.domain.dto.BoutiqueEntranceRecordDto;
import com.dili.ia.service.BoutiqueEntranceRecordService;
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
 * @author:       xiaosa
 * @date:         2020/7/13
 * @version:      农批业务系统重构
 * @description:  精品停车
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
     * @date   2020/7/13
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "boutiqueEntranceRecord/index";
    }

    /**
     * 跳转到查看页面
     *
     * @param  id 精品停车主键
     * @return 查看页面地址
     * @date   2020/7/13
     */
    @RequestMapping(value="/view.action", method = RequestMethod.GET)
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
     * @param  id 精品停车主键
     * @return 查看页面地址
     * @date   2020/7/13
     */
    @RequestMapping(value="/confirm.html", method = RequestMethod.GET)
    public String confirm(ModelMap modelMap, Long id) {
        BoutiqueEntranceRecord boutiqueEntranceRecord = null;

        if (id != null) {
            boutiqueEntranceRecord = boutiqueEntranceRecordService.get(id);
        }
        modelMap.put("boutiqueEntranceRecord", boutiqueEntranceRecord);

        return "boutiqueEntranceRecord/confirm";
    }

    /**
     * 新增计费（提供给其他服务调用者）
     *
     * @param boutiqueEntranceRecord
     * @return BaseOutput
     * @date   2020/7/13
     */
    @RequestMapping(value="/add.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content="${businessCode!}", operationType="confirm", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput add(@ModelAttribute BoutiqueEntranceRecord boutiqueEntranceRecord) throws Exception {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<BoutiqueEntranceRecord> baseOutput = boutiqueEntranceRecordService.addBoutique(boutiqueEntranceRecord);

        // 写业务日志
        if (baseOutput.isSuccess()) {
            LoggerUtil.buildLoggerContext(boutiqueEntranceRecord.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }

    /**
     * 确认计费
     *
     * @param boutiqueEntranceRecord
     * @return BaseOutput
     * @date   2020/7/13
     */
    @RequestMapping(value="/confirm.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content="${businessCode!}", operationType="confirm", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput confirm(@ModelAttribute BoutiqueEntranceRecord boutiqueEntranceRecord) throws Exception {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<BoutiqueEntranceRecord> baseOutput = boutiqueEntranceRecordService.confirm(boutiqueEntranceRecord, userTicket);

        // 写业务日志
        if (baseOutput.isSuccess()){
            BoutiqueEntranceRecord boutiqueEntranceRecordInfo = baseOutput.getData();
            LoggerUtil.buildLoggerContext(boutiqueEntranceRecordInfo.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }
        return baseOutput;
    }

    /**
     * 跳转到交费页面
     *
     * @param  id 精品停车主键
     * @return 查看页面地址
     * @date   2020/7/13
     */
    @RequestMapping(value="/submit.html", method = RequestMethod.GET)
    public String pay(ModelMap modelMap, Long id) {
        BoutiqueEntranceRecordDto boutiqueEntranceRecordDto = null;

        if (id != null) {
            boutiqueEntranceRecordDto = boutiqueEntranceRecordService.getBoutiqueEntranceDtoById(id);
        }
        modelMap.put("boutiqueEntranceRecord", boutiqueEntranceRecordDto);

        return "boutiqueEntranceRecord/submit";
    }

    /**
     * 交费
     *
     * @param  feeOrder 交费单
     * @return BaseOutput
     * @date   2020/7/13
     */
    @RequestMapping(value="/submit.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_FEE_ORDER, content="${businessCode!}", operationType="submit", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput submit(@RequestBody BoutiqueFeeOrder feeOrder) throws Exception {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<BoutiqueEntranceRecord> baseOutput = boutiqueEntranceRecordService.submit(feeOrder, userTicket);

        // 写业务日志
        if (baseOutput.isSuccess()){
            BoutiqueEntranceRecord boutiqueEntranceRecordInfo = baseOutput.getData();
            LoggerUtil.buildLoggerContext(boutiqueEntranceRecordInfo.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }

    /**
     * 取消(进门取消，可在待确认和计费中取消)
     *
     * @param id
     * @return BaseOutput
     * @date   2020/8/5
     */
    @RequestMapping(value="/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content="${businessCode!}", operationType="cancel", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput cancel(Long id) throws Exception {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<BoutiqueFeeOrder> baseOutput = boutiqueEntranceRecordService.cancel(id, userTicket);

        // 写业务日志
        if (baseOutput.isSuccess()){
            BoutiqueFeeOrder order = baseOutput.getData();
            LoggerUtil.buildLoggerContext(order.getId(), order.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }

    /**
     * 离场
     *
     * @param id 主键
     * @return BaseOutput
     * @date   2020/7/13
     */
    @RequestMapping(value="/leave.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content="${businessCode!}", operationType="leave", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput leave(Long id) throws Exception {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<BoutiqueEntranceRecord> baseOutput = boutiqueEntranceRecordService.leave(id, userTicket);

        // 写业务日志
        if (baseOutput.isSuccess()){
            BoutiqueEntranceRecord boutiqueEntranceRecordInfo = baseOutput.getData();
            LoggerUtil.buildLoggerContext(boutiqueEntranceRecordInfo.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }

    /**
     * 强制离场
     *
     * @param id 主键
     * @return BaseOutput
     * @date   2020/7/13
     */
    @RequestMapping(value="/forceLeave.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content="${businessCode!}", operationType="forceLeave", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput forceLeave(Long id) throws Exception {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        try{
            BaseOutput<BoutiqueEntranceRecord> baseOutput = boutiqueEntranceRecordService.forceLeave(id, userTicket);

            // 写业务日志
            if (baseOutput.isSuccess()){
                BoutiqueEntranceRecord boutiqueEntranceRecordInfo = baseOutput.getData();
                LoggerUtil.buildLoggerContext(boutiqueEntranceRecordInfo.getId(), null, userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
            }
            return baseOutput;
        }catch (BusinessException e){
            logger.error("精品停车强制离场异常！", e);
            return BaseOutput.failure(e.getErrorMsg()).setData(false);
        }catch (Exception e){
            logger.error("精品停车强制离场异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }


    /**
     * 分页查询精品停车列表，返回easyui分页信息
     *
     * @param boutiqueEntranceRecord
     * @return String
     * @throws Exception
     * @date   2020/7/13
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute BoutiqueEntranceRecord boutiqueEntranceRecord) throws Exception {
        return boutiqueEntranceRecordService.listEasyuiPageByExample(boutiqueEntranceRecord, true).toString();
    }

    /**
     * 修改状态
     *
     * @param boutiqueEntranceRecord
     * @return BaseOutput
     * @date   2020/7/13
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute BoutiqueEntranceRecord boutiqueEntranceRecord) {
        boutiqueEntranceRecordService.updateSelective(boutiqueEntranceRecord);
        return BaseOutput.success("修改成功");
    }
}