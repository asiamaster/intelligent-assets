package com.dili.ia.controller;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ia.domain.dto.BoutiqueFeeRefundOrderDto;
import com.dili.ia.service.BoutiqueFeeOrderService;
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
 * @author:       xiaosa
 * @date:         2020/7/31
 * @version:      农批业务系统重构
 * @description:  
 */
@Controller
@RequestMapping("/boutiqueFeeOrder")
public class BoutiqueFeeOrderController {
    private final static Logger logger = LoggerFactory.getLogger(BoutiqueFeeOrderController.class);

    @Autowired
    BoutiqueFeeOrderService boutiqueFeeOrderService;

    /**
     * 跳转到退款页面(跳转到精品停车目录)
     *
     * @param  id 精品停车主键
     * @return 查看页面地址
     * @date   2020/7/13
     */
    @RequestMapping(value="/refundApply.html", method = RequestMethod.GET)
    public String refundApply(ModelMap modelMap, Long id) throws Exception {
        BoutiqueFeeOrderDto boutiqueFeeOrderDto = null;
        if (id != null) {
            boutiqueFeeOrderDto = boutiqueFeeOrderService.getBoutiqueFeeOrderDtoById(id);
        }
        modelMap.put("boutiqueFeeOrder", boutiqueFeeOrderDto);
        return "boutiqueEntranceRecord/refundApply";
    }

    /**
     * 退款申请
     *
     * @param refundOrderDto
     * @return BaseOutput
     * @date   2020/7/23
     */
    @RequestMapping(value="/refund.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput refund(@RequestBody BoutiqueFeeRefundOrderDto refundOrderDto) {
        try {
            boutiqueFeeOrderService.refund(refundOrderDto);
        }catch (BusinessException e) {
            logger.error("精品停车{}退款申请异常！",refundOrderDto.getBusinessCode(), e);
            return BaseOutput.failure(e.getErrorCode(), e.getErrorMsg());
        }catch (Exception e) {
            logger.error("精品停车{}退款申请异常！",refundOrderDto.getBusinessCode(), e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误");
        }
        return BaseOutput.success("退款成功");
    }

    /**
     * 取消
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_FEE_ORDER, content="${businessCode!}", operationType="cancel", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput cancel(Long id) throws Exception {

        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        BaseOutput<BoutiqueFeeOrder> baseOutput = boutiqueFeeOrderService.cancel(id, userTicket);

        // 写业务日志
        if (baseOutput.isSuccess()){
            BoutiqueFeeOrder order = baseOutput.getData();
            LoggerUtil.buildLoggerContext(order.getId(), order.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), null);
        }

        return baseOutput;
    }


    /**
     * 跳转到BoutiqueFeeOrder页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "boutiqueFeeOrder/index";
    }

    /**
     * 分页查询BoutiqueFeeOrder，返回easyui分页信息
     * @param boutiqueFeeOrder
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute BoutiqueFeeOrder boutiqueFeeOrder) throws Exception {
        return boutiqueFeeOrderService.listEasyuiPageByExample(boutiqueFeeOrder, true).toString();
    }

    /**
     * 新增BoutiqueFeeOrder
     * @param boutiqueFeeOrder
     * @return BaseOutput
     */
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute BoutiqueFeeOrder boutiqueFeeOrder) {
        boutiqueFeeOrderService.insertSelective(boutiqueFeeOrder);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改BoutiqueFeeOrder
     * @param boutiqueFeeOrder
     * @return BaseOutput
     */
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute BoutiqueFeeOrder boutiqueFeeOrder) {
        boutiqueFeeOrderService.updateSelective(boutiqueFeeOrder);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除BoutiqueFeeOrder
     * @param id
     * @return BaseOutput
     */
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        boutiqueFeeOrderService.delete(id);
        return BaseOutput.success("删除成功");
    }
}