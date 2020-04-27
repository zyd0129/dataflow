package com.zyd.learn.quartz.simplequartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class Test {
    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
