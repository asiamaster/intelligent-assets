package com.dili.ia.api;

import com.dili.ia.domain.OtherFee;
import com.dili.ia.domain.dto.printDto.OtherFeePrintDto;
import com.dili.ia.domain.dto.printDto.PrintDataDto;
import com.dili.ia.service.OtherFeeService;
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
 * @author:       xiaosa
 * @date:         2020/8/19
 * @version:      农批业务系统重构
 * @description:  通行证回调
 */
@RestController
@RequestMapping("/api/otherFee")
public class OtherFeeApi {

    private final static Logger LOG = LoggerFactory.getLogger(OtherFeeApi.class);

    @Autowired
    private OtherFeeService otherFeeService;

    /**
     * 其他收费业务单缴费成功回调
     *
     * @param  settleOrder
     * @return BaseOutput
     */
    @BusinessLogger(businessType = LogBizTypeConst.OTHER_FEE, content = "${code!}", operationType = "pay", systemCode = "IA")
    @RequestMapping(value = "/settlementDealHandler", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput<Boolean> settlementDealHandler(@RequestBody SettleOrder settleOrder) {
        try {
            OtherFee otherFee = otherFeeService.settlementDealHandler(settleOrder);

            //记录业务日志
            LoggerUtil.buildLoggerContext(otherFee.getId(), otherFee.getCode(), settleOrder.getOperatorId(), settleOrder.getOperatorName(), otherFee.getMarketId(), "其他收费业务单交费成功回调");

            return BaseOutput.success().setData(true);
        } catch (BusinessException e) {
            LOG.info("其他收费缴费成功回调失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            LOG.error("其他收费缴费回调异常！", e);
            return BaseOutput.failure(ResultCode.APP_ERROR, "服务器内部错误！");
        }
    }

    /**
     * 其他收费 缴费票据打印
     *
     * @param  orderCode
     * @param  reprint
     * @return BaseOutput
     */
    @RequestMapping(value = "/queryPrintData", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput<PrintDataDto<OtherFeePrintDto>> queryPaymentPrintData(String orderCode, Integer reprint) {
        try {
            if (StringUtils.isBlank(orderCode) || null == reprint) {
                return BaseOutput.failure("参数错误");
            }
            return BaseOutput.success().setData(otherFeeService.receiptPaymentData(orderCode, reprint));
        } catch (BusinessException e) {
            LOG.info("其他收费缴费票据打印失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            LOG.error("其他收费缴费票据打印异常！", e);
            return BaseOutput.failure("其他收费缴费票据打印异常！").setData(false);
        }
    }

    /**
     * 其他收费 退款票据打印
     *
     * @param orderCode
     * @return
     */
    @RequestMapping(value = "/refundOrder/queryPrintData", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput<PrintDataDto<OtherFeePrintDto>> queryRefundPrintData(String orderCode, String reprint) {
        try {
            return BaseOutput.success().setData(otherFeeService.receiptRefundPrintData(orderCode, reprint));
        } catch (BusinessException e) {
            LOG.info("其他收费退款票据打印失败：{}", e.getMessage());
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            LOG.error("其他收费退款票据打印异常！", e);
            return BaseOutput.failure("其他收费退款票据打印异常！").setData(false);
        }
    }
}
