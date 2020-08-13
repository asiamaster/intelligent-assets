package com.dili.ia.api;

import com.dili.ia.domain.dto.PrintDataDto;
import com.dili.ia.domain.dto.printDto.StockInPrintDto;
import com.dili.ia.domain.dto.printDto.StockOutPrintDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dili.ia.service.StockInService;
import com.dili.ia.service.StockOutService;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年7月2日
 */
@RestController
@RequestMapping("/api/stockIn")
public class StockInApi {
	
    private final static Logger LOG = LoggerFactory.getLogger(StockInApi.class);

	@Autowired
	private StockInService stockInService;
	
	@Autowired
	private StockOutService stockOutService;
	
	/**
     * 冷库结算成功回调
     * @param settleOrder
     * @return
     */
    @BusinessLogger(businessType="stock_in", content="${code!}", operationType="pay", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/settlementDealHandler", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput<Boolean> settlementDealHandler(@RequestBody SettleOrder settleOrder){
        try{
        	stockInService.settlementDealHandler(settleOrder);
            return BaseOutput.success();
        }catch (BusinessException e){
            LOG.error("冷库结算回调异常！结算单{},入库单{}",settleOrder.getCode(),settleOrder.getBusinessCode(), e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }catch (Exception e){
            LOG.error("冷库结算回调异常！", e);
            return BaseOutput.failure("冷库结算回调异常！").setData(false);
        }
    }
    
    /**
     * 冷库结算票据打印
     * @param settleOrder
     * @return
     */
    @BusinessLogger(businessType="stock_in", content="${code!}", operationType="pay", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/queryPrintData/payment", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput<PrintDataDto<StockInPrintDto>> queryPaymentPrintData(String orderCode, String reprint){
        try{
            return BaseOutput.success().setData(stockInService.receiptPaymentData(orderCode, reprint));
        }catch (BusinessException e){
            LOG.error("冷库结算票据打印异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }catch (Exception e){
            LOG.error("冷库结算票据打印异常！", e);
            return BaseOutput.failure("冷库出库票据打印异常！").setData(false);
        }
    }
    
    /**
     * 冷库结算票据打印
     * @param settleOrder
     * @return
     */
    @BusinessLogger(businessType="stock_in", content="${code!}", operationType="pay", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/queryPrintData/stock_out", method = {RequestMethod.POST})
    public @ResponseBody BaseOutput<PrintDataDto<StockOutPrintDto>> queryStockOutPrintData(String orderCode, String reprint){
        try{
            return BaseOutput.success().setData(stockOutService.receiptStockOutData(orderCode, reprint));
        }catch (BusinessException e){
            LOG.error("冷库出库票据打印异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }catch (Exception e){
            LOG.error("冷库出库票据打印异常！", e);
            return BaseOutput.failure("冷库出库票据打印异常！").setData(false);
        }
    }
    
    
    /**
	 * 
	 * @Title scanEffective
	 * @Description 定时任务扫描失效,待生效马甲单
	 * @throws
	 */
	@RequestMapping(value = "/scanEffective", method = { RequestMethod.GET, RequestMethod.POST })
	public void scanEffective() {
		LOG.info("--冷库入库过期单定时任务开始--");
		stockInService.scanStockIn();
		LOG.info("--冷库入库过期单定时任务结束--");
	}
}
