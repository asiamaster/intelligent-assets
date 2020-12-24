package com.dili.ia.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

/**
 * 业务流程缓存
 * @author: WM
 * @time: 2020/11/20 16:03
 */
@RefreshScope
@Configuration
public class BpmCacheConfig {

    /**
     * 租赁单事件缓存大小
     */
    private Long leaseOrderEventCacheMaximumSize;

    /**
     * 退款单事件缓存大小
     */
    private Long refundOrderEventCacheMaximumSize;

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
     * getter
     * @return
     */
    public Long getRefundOrderEventCacheMaximumSize() {
        return refundOrderEventCacheMaximumSize;
    }

    /**
     * setter
     * @param refundOrderEventCacheMaximumSize
     */
    @Value("${ia.cache.refundOrderEventCacheMaximumSize:500}")
    public void setRefundOrderEventCacheMaximumSize(Long refundOrderEventCacheMaximumSize) {
        this.refundOrderEventCacheMaximumSize = refundOrderEventCacheMaximumSize;
    }

    /**
     * 租赁单事件缓存， key为processInstanceId_state_approvalState, value为事件名称列表
     * 因为可能多实例部署，但又不想接入MQ，只能每台实例自行负责缓存，按LRU清除
     */
    @Bean("leaseOrderEventCache")
    public Cache<String, List<String>> leaseOrderEventCache(){
        return Caffeine.newBuilder().maximumSize(leaseOrderEventCacheMaximumSize).build();
    }

    /**
     * 退款单事件缓存， key为processInstanceId_state_approvalState, value为事件名称列表
     * 因为可能多实例部署，但又不想接入MQ，只能每台实例自行负责缓存，按LRU清除
     */
    @Bean("refundOrderEventCache")
    public Cache<String, List<String>> refundOrderEventCache(){
        return Caffeine.newBuilder().maximumSize(refundOrderEventCacheMaximumSize).build();
    }

//    public static void main(String[] args) throws InterruptedException {
//        Cache<String, String> cache = Caffeine.newBuilder().initialCapacity(2).maximumSize(2).expireAfterAccess(Duration.ofSeconds(5L)).build();
//        for(int i=0;i<5;i++){
//            cache.get("key"+i, t -> {
//                System.out.println("build:"+t);
//                return t + ":value";
//            });
//        }
//        for(int i=0;i<5;i++){
//            System.out.println(cache.get("key"+i, t -> {
//                System.out.println("build:"+t);
//                return t + ":value";
//            }));
//        }
//    }
}
