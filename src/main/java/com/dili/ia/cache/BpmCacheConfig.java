package com.dili.ia.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务流程缓存
 * @author: WM
 * @time: 2020/11/20 16:03
 */
@RefreshScope
@Configuration
public class BpmCacheConfig {

    private Long leaseOrderEventCacheMaximumSize;

    /**
     * getter
     * @return
     */
    public Long getLeaseOrderEventCacheMaximumSize() {
        return leaseOrderEventCacheMaximumSize;
    }

    /**
     * setter
     * @param leaseOrderEventCacheMaximumSize
     */
    @Value("${ia.cache.leaseOrderEventCacheMaximumSize:500}")
    public void setLeaseOrderEventCacheMaximumSize(Long leaseOrderEventCacheMaximumSize) {
        this.leaseOrderEventCacheMaximumSize = leaseOrderEventCacheMaximumSize;
    }

    /**
     * 租赁单事件缓存， key为processInstanceId_state_approvalState, value为事件名称列表
     * 因为可能多实例部署，但又不想接入MQ，只能每台实例自行负责缓存，按LRU清除
     */
//    public static final Cache<String, List<String>> leaseOrderEventCache = Caffeine.newBuilder().maximumSize(1_000).build();
    @Bean("leaseOrderEventCache")
    public Cache<String, List<String>> leaseOrderEventCache(){
        return Caffeine.newBuilder().maximumSize(leaseOrderEventCacheMaximumSize).build();
    }

}
