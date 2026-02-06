package com.china.soft.system.core;

import com.china.soft.system.api.service.PanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;

@Component
public class DynamicScheduledTask implements SchedulingConfigurer {

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private PanelService panelService;

    // 存储任务的Future对象，用于取消/修改
    private ScheduledFuture<?> future;

    // 动态修改Cron表达式的方法（可通过接口调用）
    public void updateCron(String newCron) {
        // 取消原有任务
        if (future != null) {
            future.cancel(true);
        }
        // 重新注册任务
        future = taskScheduler.schedule(this::dynamicTask, new CronTrigger(newCron));
        System.out.println("【动态修改】Cron表达式已更新为：" + newCron);
    }

    // 实际执行的任务逻辑
    private void dynamicTask() {
        System.out.println("【动态任务】执行时间：" + LocalDateTime.now());
        panelService.executeTask();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 默认每天凌晨2点执行此任务
        future = taskScheduler.schedule(this::dynamicTask, new CronTrigger("0 0 2 * * ?"));
    }
}
