package com.dili.ia.api;

import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.dto.MeterDetailDto;
import com.dili.ia.domain.dto.printDto.MeterDetailPrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.glossary.MeterTypeEnum;
import com.dili.ia.service.MeterDetailService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
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
 * @date:           2020/7/6
 * @version:        农批业务系统重构
 * @description:    水电费业务单缴费后回调
 */
@RestController
@RequestMapping("/api/meterDetail")
public class MeterDetailApi {

    private final static Logger LOG = LoggerFactory.getLogger(MeterDetailApi.class);

    @Autowired
    private MeterDetailService meterDetailService;

    /**
     * 水电费缴费成功回调
     *
     * @param settleOrder
     * @return
     */
    @BusinessLogger(content = "${code!}", operationType = "pay", systemCode = "IA")
    @RequestMapping(value = "/settlementDealHandler", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput<Boolean> settlementDealHandler(@RequestBody SettleOrder settleOrder) {
        try {
            MeterDetailDto meterDetailDto = meterDetailService.settlementDealHandler(settleOrder);

            //记录业务日志
            LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE, LogBizTypeConst.ELECTRICITY_CODE);
            if (MeterTypeEnum.WATER_METER.getCode().equals(meterDetailDto.getType())) {
                // 水费
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_TYPE, LogBizTypeConst.WATER_CODE);
            }
            LoggerUtil.buildLoggerContext(meterDetailDto.getId(), meterDetailDto.getCode(), settleOrder.getOperatorId(), settleOrder.getOperatorName(),
                    meterDetailDto.getMarketId(), "水电费业务单交费成功回调");

            return BaseOutput.success().setData(true);
        } catch (BusinessException e) {
            LOG.info("水电费缴费成功回调失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            LOG.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误！");
        }
    }

    /**
     * 水电费缴费票据打印
     *
     * @param orderCode
     * @return
     */
    @RequestMapping(value = "/queryPrintData", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput<PrintDataDto<MeterDetailPrintDto>> queryPaymentPrintData(String orderCode, Integer reprint) {
        try {
            if (StringUtils.isBlank(orderCode) || null == reprint) {
                return BaseOutput.failure("参数错误！");
            }
            return BaseOutput.success().setData(meterDetailService.receiptPaymentData(orderCode, reprint));
        } catch (BusinessException e) {
            LOG.info("水电费缴费票据打印失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            LOG.error("服务器内部错误！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误！");
        }
    }

}
