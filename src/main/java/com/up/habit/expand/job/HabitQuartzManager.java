package com.up.habit.expand.job;


import com.jfinal.log.Log;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Map;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/1/9 11:19
 */
public class HabitQuartzManager {

    private Log log = Log.getLog(HabitQuartzManager.class);

    private static HabitQuartzManager me = new HabitQuartzManager();

    private Scheduler scheduler = null;

    public static HabitQuartzManager me() {
        return me;
    }

    private HabitQuartzManager() {
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
        } catch (SchedulerException e) {
            log.error("init quartz error", e);
        }
    }

    /**
     * TODO:添加任务
     *
     * @param name
     * @param group
     * @param clazz
     * @param corn
     * @return
     * @throws SchedulerException
     */
    public boolean add(String name, String group, Class<? extends Job> clazz, String corn, Map para) {
        try {
            JobBuilder jobBuilder = newJob(clazz).withIdentity(name, group);
            if (para != null) {
                jobBuilder.usingJobData(new JobDataMap(para));
            }
            JobDetail job = jobBuilder.build();
            Trigger trg = newTrigger().withIdentity(name, group).withSchedule(CronScheduleBuilder.cronSchedule(corn)).build();
            scheduler.scheduleJob(job, trg);
            return true;
        } catch (Exception e) {
            log.error("add job error", e);
            return false;
        }
    }

    /**
     * TODO:移除任务
     *
     * @param name
     * @param group
     * @return
     */
    public boolean remove(String name, String group) {
        TriggerKey tk = TriggerKey.triggerKey(name, group);
        try {
            scheduler.pauseTrigger(tk);
            scheduler.unscheduleJob(tk);
            JobKey jobKey = JobKey.jobKey(name, group);
            scheduler.deleteJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            log.error("remove job error", e);
            return false;
        }
    }

    /**
     * 启动调度器
     *
     * @return
     */
    public boolean start() {
        try {
            scheduler.start();
            return true;
        } catch (SchedulerException e) {
            log.error("start job error", e);
            return false;
        }
    }


    /**
     * 停止调度器
     *
     * @return
     */
    public boolean shutdown() {
        try {
            scheduler.shutdown();
            return true;
        } catch (SchedulerException e) {
            log.error("shutdown job error", e);
            return false;
        }
    }

    public boolean once(String name, String group, Map para) {
        try {
            JobKey jobKey = JobKey.jobKey(name, group);
            if (para != null) {
                scheduler.triggerJob(jobKey, new JobDataMap(para));
            } else {
                scheduler.triggerJob(jobKey);
            }
            return true;
        } catch (SchedulerException e) {
            return false;
        }
    }


}
