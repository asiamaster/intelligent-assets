package com.dili.ia.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 业务流程缓存
 * @author: WM
 * @time: 2020/11/20 16:03
 */
public class BpmCache {
    /**
     * 租赁单事件缓存， key为processInstanceId_state, value为事件名称列表
     * 因为可能多实例部署，但又不想接入MQ，只能每台实例自行负责每天清空缓存
     */
    public static final Map<String, List<String>> leaseOrderEventCache = new ConcurrentHashMap<>(64);
}
