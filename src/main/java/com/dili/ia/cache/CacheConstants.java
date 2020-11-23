package com.dili.ia.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 缓存常量
 */
@RefreshScope
@Component
public class CacheConstants {

    // sessionId - SessionData的Redis 过期时间(秒)
    // sessionId - userId和UserIdSessionData的Redis 过期时间(秒)
    // 默认为30分钟
    private int loadingCacheMaximumSize;


    public int getLoadingCacheMaximumSize() {
        return loadingCacheMaximumSize;
    }
    @Value("${cache.loadingCacheMaximumSize:1000}")
    public void setLoadingCacheMaximumSize(int loadingCacheMaximumSize) {
        this.loadingCacheMaximumSize = loadingCacheMaximumSize;
    }
}