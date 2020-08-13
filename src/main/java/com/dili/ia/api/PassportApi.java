package com.dili.ia.api;

import com.dili.ia.domain.Passport;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.printDto.PassportPrintDto;
import com.dili.ia.service.PassportService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
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
 * @author: xiaosa
 * @date: 2020/7/27
 * @version: 农批业务系统重构
 * @description: 通行证回调
 */
@RestController
@RequestMapping("/api/passport")
public class PassportApi {

    private final static Logger LOG = LoggerFactory.getLogger(PassportApi.class);

    @Autowired
    private PassportService passportService;

    /**
     * 通行证缴费成功回调
     *
     * @param settleOrder
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.PASSPORT, content = "${code!}", operationType = "pay", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value = "/settlementDealHandler", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput<Boolean> settlementDealHandler(@RequestBody SettleOrder settleOrder) {
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            BaseOutput<Passport> output = passportService.settlementDealHandler(settleOrder);
            if (output.isSuccess()) {
                //记录业务日志
                LoggerUtil.buildLoggerContext(output.getData().getId(), output.getData().getCode(), settleOrder.getOperatorId(), settleOrder.getOperatorName(), output.getData().getMarketId(), null);
                return BaseOutput.success().setData(true);
            }
            return BaseOutput.failure(output.getMessage());
        } catch (BusinessException e) {
            LOG.error("通行证缴费回调异常！", e);
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            LOG.error("通行证缴费回调异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 通行证缴费票据打印
     *
     * @param orderCode
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.PASSPORT, content = "${code!}", operationType = "pay", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value = "/queryPrintData", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput<PrintDataDto<PassportPrintDto>> queryPaymentPrintData(String orderCode, Integer reprint) {
        try {
            if (StringUtils.isBlank(orderCode) || null == reprint) {
                return BaseOutput.failure("参数错误");
            }
            return BaseOutput.success().setData(passportService.receiptPaymentData(orderCode, reprint));
        } catch (BusinessException e) {
            LOG.error("通行证缴费票据打印异常！", e);
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            LOG.error("通行证缴费票据打印异常！", e);
            return BaseOutput.failure("通行证缴费票据打印异常！").setData(false);
        }
    }

    /**
     * 通行证退款票据打印
     *
     * @param orderCode
     * @return
     */
    @RequestMapping(value = "/refundOrder/queryPrintData", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput<PrintDataDto<PassportPrintDto>> queryRefundPrintData(String orderCode, String reprint) {
        try {
            return BaseOutput.success().setData(passportService.receiptRefundPrintData(orderCode, reprint));
        } catch (BusinessException e) {
            LOG.error("通行证退款票据打印异常！", e);
            return BaseOutput.failure(e.getCode(), e.getMessage()).setData(false);
        } catch (Exception e) {
            LOG.error("通行证退款票据打印异常！", e);
            return BaseOutput.failure("通行证退款票据打印异常！").setData(false);
        }
    }

    /**
     * @throws
     * @Title scanEffective
     * @Description 定时任务扫描失效, 待生效马甲单
     */
    @RequestMapping(value = "/scanEffective", method = {RequestMethod.GET, RequestMethod.POST})
    public void scanEffective() {
        LOG.info("--通行证判断是否已生效和已过期的定时任务开始--");
        passportService.passportTasking();
        LOG.info("--通行证判断是否已生效和已过期的定时任务结束--");
    }

}
