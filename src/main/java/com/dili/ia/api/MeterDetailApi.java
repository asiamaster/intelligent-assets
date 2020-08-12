package com.dili.ia.api;

import com.dili.ia.domain.EarnestOrder;
import com.dili.ia.domain.MeterDetail;
import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.printDto.MeterDetailPrintDto;
import com.dili.ia.service.MeterDetailService;
import com.dili.ia.util.LogBizTypeConst;
import com.dili.ia.util.LoggerUtil;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.settlement.domain.SettleOrder;
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
 * @date:         2020/7/6
 * @version:      农批业务系统重构
 * @description:  水电费缴费后回调
 */
@RestController
@RequestMapping("/api/meterDetail")
public class MeterDetailApi {

    private final static Logger LOG = LoggerFactory.getLogger(MeterDetailApi.class);

    @Autowired
    private MeterDetailService meterDetailService;

    /**
     * 水电费缴费成功回调
     * @param settleOrder
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.WATER_ELECTRICITY_CODE, content="${code!}", operationType="pay", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/settlementDealHandler", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput<Boolean> settlementDealHandler(@RequestBody SettleOrder settleOrder){
        try{
            BaseOutput<MeterDetail> output = meterDetailService.settlementDealHandler(settleOrder);
            if (output.isSuccess()){
                //记录业务日志
                LoggerUtil.buildLoggerContext(output.getData().getId(), output.getData().getCode(), settleOrder.getOperatorId(), settleOrder.getOperatorName(), output.getData().getMarketId(), null);
                return BaseOutput.success().setData(true);
            }
            return BaseOutput.failure(output.getMessage());
        }catch (BusinessException e){
            LOG.error("水电费缴费回调异常！", e);
            return BaseOutput.failure(e.getErrorMsg()).setData(false);
        }catch (Exception e){
            LOG.error("水电费缴费回调异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 水电费缴费票据打印
     * @param orderCode
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.WATER_ELECTRICITY_CODE, content="${code!}", operationType="pay", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/queryPrintData", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput<PrintDataDto<MeterDetailPrintDto>> queryPaymentPrintData(String orderCode, Integer reprint){
        try{
            if(StringUtils.isBlank(orderCode) || null == reprint){
                return BaseOutput.failure("参数错误");
            }
            return BaseOutput.success().setData(meterDetailService.receiptPaymentData(orderCode, reprint));
        }catch (BusinessException e){
            LOG.error("水电费缴费票据打印异常！", e);
            return BaseOutput.failure(e.getErrorMsg()).setData(false);
        }catch (Exception e){
            LOG.error("水电费缴费票据打印异常！", e);
            return BaseOutput.failure("水电费缴费票据打印异常！").setData(false);
        }
    }

}
