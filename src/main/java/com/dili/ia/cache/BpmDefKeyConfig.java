package com.dili.ia.cache;

import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.BpmConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务流程定义Key配置
 * @author: WM
 * @time: 2020/12/4 16:08
 */
public class BpmDefKeyConfig {
    //租赁单流程定义KEY缓存
    static Map<String, String> leaseProcessDefinitionKeyCache = new HashMap<>(4);
    //退款单 流程定义KEY缓存
    static Map<String, String> refundProcessDefinitionKeyCache = new HashMap<>(4);
    static {
        //初始化配置
        leaseProcessDefinitionKeyCache.put(BizTypeEnum.BOOTH_LEASE.getCode(), BpmConstants.PK_BOOTH_LEASE_ORDER_PROCESS);
        leaseProcessDefinitionKeyCache.put(BizTypeEnum.LODGING_LEASE.getCode(), BpmConstants.PK_LODGING_LEASE_ORDER_PROCESS);
        leaseProcessDefinitionKeyCache.put(BizTypeEnum.LOCATION_LEASE.getCode(), BpmConstants.PK_LOCATION_LEASE_ORDER_PROCESS);

        refundProcessDefinitionKeyCache.put(BizTypeEnum.BOOTH_LEASE.getCode(), BpmConstants.PK_BOOTH_LEASE_REFUND_ORDER_PROCESS);
        refundProcessDefinitionKeyCache.put(BizTypeEnum.LOCATION_LEASE.getCode(), BpmConstants.PK_LODGING_LEASE_REFUND_ORDER_PROCESS);
        refundProcessDefinitionKeyCache.put(BizTypeEnum.LODGING_LEASE.getCode(), BpmConstants.PK_LOCATION_LEASE_REFUND_ORDER_PROCESS);
    }

    public static String getLeaseDefKey(String bizType){
        return leaseProcessDefinitionKeyCache.get(bizType);
    }

    public static String getRefundDefKey(String bizType){
        return refundProcessDefinitionKeyCache.get(bizType);
    }
}
