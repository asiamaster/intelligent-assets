package com.dili.ia.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dili.ia.service.StockInService;
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
public class StockApi {
	
    private final static Logger LOG = LoggerFactory.getLogger(StockApi.class);

	@Autowired
	private StockInService stockInService;
	
	/**
     * 冷库结算成功回调
     * @param settleOrder
     * @return
     */
    @BusinessLogger(businessType="stock_in", content="${code!}", operationType="pay", systemCode = "INTELLIGENT_ASSETS")
    @RequestMapping(value="/settlementDealHandler", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput<Boolean> settlementDealHandler(@RequestBody SettleOrder settleOrder){
        try{
        	stockInService.settlementDealHandler(settleOrder);
            
            return BaseOutput.success();
        }catch (BusinessException e){
            LOG.error("定金结算成功回调异常！", e);
            return BaseOutput.failure(e.getErrorMsg()).setData(false);
        }catch (Exception e){
            LOG.error("定金结算成功回调异常！", e);
            return BaseOutput.failure(e.getMessage()).setData(false);
        }
    }
    
}
