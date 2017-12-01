package com.smile.sh.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class OneTask implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("【OneTask定时开始...】");
        for (int i = 0; i < 5; i++) {
            System.out.println(i + ".OneTask执行任务中...");
        }
        System.out.println("【OneTask定时结束...】");
    }
}
