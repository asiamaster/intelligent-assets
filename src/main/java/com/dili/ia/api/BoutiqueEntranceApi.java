package com.dili.ia.api;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.EarnestOrder;
import com.dili.ia.domain.dto.BoutiqueEntranceRecordDto;
import com.dili.ia.service.BoutiqueEntranceRecordService;
import com.dili.ia.service.MeterDetailService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:       xiaosa
 * @date:         2020/7/14
 * @version:      农批业务系统重构
 * @description:  精品停车回调
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
     * @param recordDto
     * @return BaseOutput
     * @date   2020/7/13
     */
    @RequestMapping(value="/add.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content="${businessCode!}", operationType="confirm", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput add(@RequestBody BoutiqueEntranceRecordDto recordDto) throws Exception {

        BaseOutput<BoutiqueEntranceRecord> baseOutput = boutiqueEntranceService.addBoutique(recordDto);

        // 写业务日志
        if (baseOutput.isSuccess()) {
            LoggerUtil.buildLoggerContext(recordDto.getId(), null, recordDto.getOperatorId(), recordDto.getOperatorName(), recordDto.getMarketId(), null);
        }

        return baseOutput;
    }

    /**
     * 取消(进门取消，可在待确认和计费中取消)
     *
     * @param recordDto
     * @return BaseOutput
     * @date   2020/8/5
     */
    @RequestMapping(value="/cancel.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content="${businessCode!}", operationType="cancel", systemCode = "INTELLIGENT_ASSETS")
    public @ResponseBody BaseOutput cancel(@RequestBody BoutiqueEntranceRecordDto recordDto) throws Exception {

        BaseOutput<BoutiqueFeeOrder> baseOutput = boutiqueEntranceService.cancel(recordDto);

        // 写业务日志
        if (baseOutput.isSuccess()) {
            LoggerUtil.buildLoggerContext(recordDto.getId(), null, recordDto.getOperatorId(), recordDto.getOperatorName(), recordDto.getMarketId(), null);
        }

        return baseOutput;
    }

    /**
     * 精品停车缴费成功回调
     * @param settleOrder
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content="${code!}", operationType="pay", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/settlementDealHandler", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput<Boolean> settlementDealHandler(@RequestBody SettleOrder settleOrder){
        try{
            BaseOutput<BoutiqueFeeOrder> output = boutiqueEntranceService.settlementDealHandler(settleOrder);
            if (output.isSuccess()){
                //记录业务日志
                LoggerUtil.buildLoggerContext(output.getData().getId(), output.getData().getCode(), settleOrder.getOperatorId(), settleOrder.getOperatorName(), output.getData().getMarketId(), null);
                return BaseOutput.success().setData(true);
            }
            return BaseOutput.failure(output.getMessage());
        }catch (BusinessException e){
            LOG.error("精品停车缴费回调异常！", e);
            return BaseOutput.failure(e.getErrorMsg()).setData(false);
        }catch (Exception e){
            LOG.error("精品停车缴费回调异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }

    /**
     * 精品停车缴费票据打印
     * @param orderCode
     * @return
     */
    @BusinessLogger(businessType = LogBizTypeConst.BOUTIQUE_ENTRANCE, content="${code!}", operationType="pay", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/queryPrintData/boutique_entrance", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput<Boolean> queryPaymentPrintData(String orderCode, Integer reprint){
        try{
            if(StringUtils.isBlank(orderCode) || null == reprint){
                return BaseOutput.failure("参数错误");
            }
            return BaseOutput.success().setData(boutiqueEntranceService.receiptPaymentData(orderCode, reprint));
        }catch (BusinessException e){
            LOG.error("精品停车缴费票据打印异常！", e);
            return BaseOutput.failure(e.getErrorMsg()).setData(false);
        }catch (Exception e){
            LOG.error("精品停车缴费票据打印异常！", e);
            return BaseOutput.failure("精品停车缴费票据打印异常！").setData(false);
        }
    }

    /**
     * 精品停车退款票据打印
     * @param orderCode
     * @return
     */
    @RequestMapping(value="/queryPrintData/refund", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput<Boolean> queryRefundPrintData(String orderCode, String reprint){
        try{
            return BaseOutput.success().setData(boutiqueEntranceService.receiptRefundPrintData(orderCode, reprint));
        }catch (BusinessException e){
            LOG.error("精品停车退款票据打印异常！", e);
            return BaseOutput.failure(e.getErrorMsg()).setData(false);
        }catch (Exception e){
            LOG.error("精品停车退款票据打印异常！", e);
            return BaseOutput.failure("精品停车退款票据打印异常！").setData(false);
        }
    }

}
