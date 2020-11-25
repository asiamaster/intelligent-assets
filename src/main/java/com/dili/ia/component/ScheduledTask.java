package com.dili.ia.component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时调度
 * @author: WM
 * @time: 2020/11/20 16:26
 */
@Component
public class ScheduledTask {

    /**
     * 定期清理租赁流程缓存
     * 每天 00:00:00
     * @throws InterruptedException
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearBpmCache() {
        System.out.println(String.format("------------"));
    }
}
