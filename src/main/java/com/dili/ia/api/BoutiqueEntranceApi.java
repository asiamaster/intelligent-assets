package com.dili.ia.api;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.dto.BoutiqueEntranceRecordDto;
import com.dili.ia.domain.dto.printDto.BoutiqueEntrancePrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.service.BoutiqueEntranceRecordService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:         xiaosa
 * @date:           2020/7/14
 * @version:        农批业务系统重构
 * @description:    精品停车回调
 */
@RestController
@RequestMapping("/api/boutiqueEntrance")
public class BoutiqueEntranceApi {

    private final static Logger LOG = LoggerFactory.getLogger(BoutiqueEntranceApi.class);

    @Autowired
    private BoutiqueEntranceRecordService boutiqueEntranceService;

    /**
     * 新增计费（提供给其他服务调用者）
     *
     * @param  recordDto
     * @return BaseOutput
     * @date   2020/7/13
     */
    @RequestMapping(value = "/add.action", method = {RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content = "${businessCode!}", operationType = "confirm", systemCode = "IA")
    public @ResponseBody
    BaseOutput add(@RequestBody BoutiqueEntranceRecordDto recordDto){
        try {
            BoutiqueEntranceRecord recordInfo = boutiqueEntranceService.addBoutique(recordDto);

            // 写业务日志
            LoggerUtil.buildLoggerContext(recordDto.getId(), null, recordDto.getOperatorId(), recordDto.getOperatorName(), recordDto.getMarketId(), "新增精品黄楼停车");

            return BaseOutput.success().setData(recordInfo);
        } catch (BusinessException e) {
            LOG.info("进门系统新增精品黄楼停车失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            LOG.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误！");
        }
    }

    /**
     * 取消(进门取消，可在待确认和计费中取消)
     *
     * @param recordDto
     * @return BaseOutput
     * @date 2020/8/5
     */
    @RequestMapping(value = "/cancel.action", method = {RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content = "${businessCode!}", operationType = "cancel", systemCode = "IA")
    public @ResponseBody
    BaseOutput cancel(@RequestBody BoutiqueEntranceRecordDto recordDto){
        try {
            BoutiqueEntranceRecord boutiqueFeeOrder = boutiqueEntranceService.cancel(recordDto);

            // 写业务日志
            LoggerUtil.buildLoggerContext(recordDto.getId(), null, recordDto.getOperatorId(), recordDto.getOperatorName(), recordDto.getMarketId(), "取消精品黄楼停车");

            return BaseOutput.success().setData(boutiqueFeeOrder);
        } catch (BusinessException e) {
            LOG.info("进门系统取消精品黄楼停车失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            LOG.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误！");
        }
    }

    /**
     * 精品停车缴费成功回调
     *
     * @param  settleOrder
     * @return BaseOutput
     * @date   2020/7/13
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content = "${code!}", operationType = "pay", systemCode = "IA")
    @RequestMapping(value = "/settlementDealHandler", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput<Boolean> settlementDealHandler(@RequestBody SettleOrder settleOrder) {
        try {
            BoutiqueFeeOrder boutiqueFeeOrder = boutiqueEntranceService.settlementDealHandler(settleOrder);

            //记录业务日志
            LoggerUtil.buildLoggerContext(boutiqueFeeOrder.getId(), boutiqueFeeOrder.getCode(), settleOrder.getOperatorId(), settleOrder.getOperatorName(), boutiqueFeeOrder.getMarketId(), "精品黄楼停车业务单交费成功回调");

            return BaseOutput.success().setData(true);
        } catch (BusinessException e) {
            LOG.info("精品停车缴费成功回调失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            LOG.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误！");
        }
    }

    /**
     * 精品停车缴费票据打印
     *
     * @param  orderCode
     * @param  reprint
     * @return BaseOutput
     * @date   2020/7/13
     */
    @RequestMapping(value = "/queryPrintData", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    BaseOutput<PrintDataDto<BoutiqueEntrancePrintDto>> queryPaymentPrintData(String orderCode, Integer reprint) {
        try {
            if (StringUtils.isBlank(orderCode) || null == reprint) {
                return BaseOutput.failure("参数错误！");
            }
            return BaseOutput.success().setData(boutiqueEntranceService.receiptPaymentData(orderCode, reprint));
        } catch (BusinessException e) {
            LOG.info("精品停车缴费票据打印失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            LOG.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误！");
        }
    }

    /**
     * 精品停车退款票据打印
     *
     * @param  orderCode
     * @param  reprint
     * @return BaseOutput
     * @date   2020/7/13
     */
    @RequestMapping(value = "/refundOrder/queryPrintData", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    BaseOutput<PrintDataDto<BoutiqueEntrancePrintDto>> queryRefundPrintData(String orderCode, Integer reprint) {
        try {
            return BaseOutput.success().setData(boutiqueEntranceService.receiptRefundPrintData(orderCode, reprint));
        } catch (BusinessException e) {
            LOG.info("精品停车退款票据打印失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            LOG.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误！");
        }
    }
}
