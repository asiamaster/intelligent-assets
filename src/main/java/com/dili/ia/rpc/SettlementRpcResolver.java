package com.dili.ia.rpc;

import com.alibaba.fastjson.JSON;
import com.dili.ia.domain.Labor;
import com.dili.ia.domain.dto.SettleOrderInfoDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleWayDetail;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description 结算回调解析
 * @author yangfan
 * @date 2020年7月1日
 */
@Component
public class SettlementRpcResolver {
	private final static Logger LOG = LoggerFactory.getLogger(SettlementRpcResolver.class);
	@Autowired
	private SettlementRpc settlementRpc;
	
	@Value("${settlement.app-id}")
    private Long settlementAppId;
    
    //private String settlerHandlerUrl = "http://10.28.1.187:8381/api/labor/vest/settlementDealHandler";
	
    /**
     * 【提交】结算单 --- 到结算中心
     * @param settleOrder
     * @return
     */
    public SettleOrder submit(SettleOrderDto settleOrder){
    	settleOrder.setAppId(settlementAppId);
    	//settleOrder.setReturnUrl(settlerHandlerUrl);
    	LOG.info("结算成功!业务号:" + JSON.toJSONString(settleOrder));
    	BaseOutput<SettleOrder> result = settlementRpc.submit(settleOrder);
        if(!result.isSuccess()){
        	LOG.info("结算调用失败!业务号:" + settleOrder.getBusinessCode()+",message"+result.getErrorData());
            throw new BusinessException(ResultCode.APP_ERROR, "结算调用失败!");
        }
        LOG.info("结算成功!业务号:" + settleOrder.getBusinessCode());
		return result.getData();	
    };
 
    /**
     * 【撤回】结算单 ---根据业务缴费单code取消
     * @param appId 应用ID
     * @param orderCode 业务缴费单code
     * @return
     */
    public String cancel(Long appId, String orderCode){
    	BaseOutput<String> result = settlementRpc.cancel(settlementAppId,orderCode);
        if(!result.isSuccess()){
        	LOG.info("结算撤回失败!业务号:" + orderCode);
            throw new BusinessException(ResultCode.APP_ERROR, "结算调用失败!");
        }
        LOG.info("结算撤回成功!业务号:" + orderCode);
		return result.getData();
    };
    
    /**
     * 【撤回】结算单 ---根据业务缴费单code取消
     * @param orderCode 业务缴费单code
     * @return
     */
    public String cancel(String orderCode){
    	BaseOutput<String> result = settlementRpc.cancel(settlementAppId,orderCode);
        if(!result.isSuccess()){
        	LOG.info("结算撤回失败!业务号:" + orderCode);
            throw new BusinessException(ResultCode.APP_ERROR, "结算调用失败!");
        }
        LOG.info("结算撤回成功!业务号:" + orderCode);
		return result.getData();
    };
    
    /**
     * 【查询】结算单 ---结算单查询
     * @param appId
     * @param orderCode
     * @return
     */
    public SettleOrder get(Long appId, String businessCode){
    	BaseOutput<SettleOrder> result = settlementRpc.get(appId,businessCode);
        if(!result.isSuccess()){
        	LOG.info("获取结算单失败!业务号:" + businessCode);
            throw new BusinessException(ResultCode.APP_ERROR, "获取结算单失败!");
        }
		return result.getData();
    };
    
    public SettleOrderDto buildSettleOrderDto(UserTicket userTicket, Labor labor,String orderCode,Long amount,BizTypeEnum bizTypeEnum) {
		SettleOrderInfoDto settleOrderInfoDto = 
				new SettleOrderInfoDto(userTicket, bizTypeEnum,SettleTypeEnum.PAY,SettleStateEnum.WAIT_DEAL);
		settleOrderInfoDto.setBusinessCode(labor.getCode());
		settleOrderInfoDto.setOrderCode(orderCode);
		settleOrderInfoDto.setAmount(amount);
		//settleOrderInfoDto.setAppId(settlementAppId);
		settleOrderInfoDto.setBusinessDepId(labor.getDepartmentId());
		settleOrderInfoDto.setBusinessDepName(labor.getDepartmentName());
		settleOrderInfoDto.setCustomerId(labor.getCustomerId());
		settleOrderInfoDto.setCustomerName(labor.getCustomerName());
		settleOrderInfoDto.setCustomerPhone(labor.getCustomerCellphone());
		//settleOrderInfoDto.setReturnUrl(settlerHandlerUrl);
		return settleOrderInfoDto;
	}
}
