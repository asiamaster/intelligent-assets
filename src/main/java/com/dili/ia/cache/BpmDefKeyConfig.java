package com.dili.ia.cache;

import com.dili.bpmc.sdk.domain.ProcessDefinitionMapping;
import com.dili.bpmc.sdk.rpc.restful.RepositoryRpc;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.BpmConstants;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.RemoteException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务流程定义Key配置
 * @author: WM
 * @time: 2020/12/4 16:08
 */
@Component
public class BpmDefKeyConfig {
    //租赁单业务流程定义KEY缓存
    Map<String, String> leaseProcessDefinitionKeyCache = new HashMap<>(64);
    //退款单业务流程定义KEY缓存
    Map<String, String> refundProcessDefinitionKeyCache = new HashMap<>(64);

    //租赁单审批流程定义KEY缓存
    Map<String, String> leaseApprovalProcessDefinitionKeyCache = new HashMap<>(8);
    //退款单审批流程定义KEY缓存
    Map<String, String> refundApprovalProcessDefinitionKeyCache = new HashMap<>(8);

    @Resource
    RepositoryRpc repositoryRpc;

    /**
     * 初始化通用流程配置
     */
    @PostConstruct
    public void init() {
        //租赁流程配置
        leaseProcessDefinitionKeyCache.put(BizTypeEnum.BOOTH_LEASE.getCode(), BpmConstants.PK_BOOTH_LEASE_ORDER_PROCESS);
        leaseProcessDefinitionKeyCache.put(BizTypeEnum.LODGING_LEASE.getCode(), BpmConstants.PK_LODGING_LEASE_ORDER_PROCESS);
        leaseProcessDefinitionKeyCache.put(BizTypeEnum.LOCATION_LEASE.getCode(), BpmConstants.PK_LOCATION_LEASE_ORDER_PROCESS);
        //退款流程配置
        refundProcessDefinitionKeyCache.put(BizTypeEnum.BOOTH_LEASE.getCode(), BpmConstants.PK_BOOTH_LEASE_REFUND_ORDER_PROCESS);
        refundProcessDefinitionKeyCache.put(BizTypeEnum.LOCATION_LEASE.getCode(), BpmConstants.PK_LODGING_LEASE_REFUND_ORDER_PROCESS);
        refundProcessDefinitionKeyCache.put(BizTypeEnum.LODGING_LEASE.getCode(), BpmConstants.PK_LOCATION_LEASE_REFUND_ORDER_PROCESS);
        refundProcessDefinitionKeyCache.put(BizTypeEnum.DEPOSIT_ORDER.getCode(), BpmConstants.PK_DEPOSIT_ORDER_REFUND_ORDER_PROCESS);
        refundProcessDefinitionKeyCache.put(BizTypeEnum.EARNEST.getCode(), BpmConstants.PK_EARNEST_REFUND_ORDER_PROCESS);
        //租赁审批流程配置
        leaseApprovalProcessDefinitionKeyCache.put(BizTypeEnum.BOOTH_LEASE.getCode(), BpmConstants.PK_BOOTH_LEASE_APPROVAL_PROCESS);
        //退款审批流程配置
        refundApprovalProcessDefinitionKeyCache.put(BizTypeEnum.BOOTH_LEASE.getCode(), BpmConstants.PK_BOOTH_REFUND_APPROVAL_PROCESS);
    }

    /**
     * 根据市场编码获取租赁业务流程定义Key，如果该流程未部署，则使用通用流程Key
     * @param bizType
     * @param firmCode
     * @return
     */
    public String getLeaseBizDefKey(String bizType, String firmCode){
        return getDefKey(bizType, firmCode, leaseProcessDefinitionKeyCache);
    }

    /**
     * 根据市场编码获取退款业务流程定义Key，如果该流程未部署，则使用通用流程Key
     * @param bizType
     * @param firmCode
     * @return
     */
    public String getRefundBizDefKey(String bizType, String firmCode){
        return getDefKey(bizType, firmCode, refundProcessDefinitionKeyCache);
    }

    /**
     * 根据市场编码获取租赁审批流程定义Key，如果该流程未部署，则使用通用流程Key
     * @param bizType
     * @param firmCode
     * @return
     */
    public String getLeaseApprovalDefKey(String bizType, String firmCode){
        return getDefKey(bizType, firmCode, leaseApprovalProcessDefinitionKeyCache);
    }

    /**
     * 根据市场编码获取退款审批流程定义Key，如果该流程未部署，则使用通用流程Key
     * @param bizType
     * @param firmCode
     * @return
     */
    public String getRefundApprovalDefKey(String bizType, String firmCode){
        return getDefKey(bizType, firmCode, refundApprovalProcessDefinitionKeyCache);
    }

    /**
     * 获取流程定义Key
     * @param bizType
     * @param firmCode
     * @param processDefinitionKeyCache
     * @return
     */
    private String getDefKey(String bizType, String firmCode, Map<String, String> processDefinitionKeyCache){
        //市场流程定义缓存Key
        String FIRM_DEF_CACHE_KEY = firmCode + "_" + bizType;
        String firmProcessDefinitionKey = processDefinitionKeyCache.get(FIRM_DEF_CACHE_KEY);
        if(firmProcessDefinitionKey != null){
            return firmProcessDefinitionKey;
        }
        //构建市场流程定义Key: 市场编码 + "_" + 通用流程定义Key
        firmProcessDefinitionKey = firmCode + "_" + processDefinitionKeyCache.get(bizType);
        BaseOutput<ProcessDefinitionMapping> latestProcessDefinition = repositoryRpc.getLatestProcessDefinition(firmProcessDefinitionKey);
        if(!latestProcessDefinition.isSuccess()){
            throw new RemoteException();
        }
        //如果该市场未部署流程，则使用通用流程
        if(latestProcessDefinition.getData() == null){
            processDefinitionKeyCache.put(FIRM_DEF_CACHE_KEY, processDefinitionKeyCache.get(bizType));
            return processDefinitionKeyCache.get(bizType);
        }
        processDefinitionKeyCache.put(FIRM_DEF_CACHE_KEY, firmProcessDefinitionKey);
        return firmProcessDefinitionKey;
    }

    /**
     * 获取租赁通用流程定义Key
     * @param bizType
     * @return
     */
    private String getLeaseBizDefKey(String bizType){
        return leaseProcessDefinitionKeyCache.get(bizType);
    }

    /**
     * 获取退款通用流程定义Key
     * @param bizType
     * @return
     */
    private String getRefundBizDefKey(String bizType){
        return refundProcessDefinitionKeyCache.get(bizType);
    }

}
