package com.smile.sh.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TwoTask implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("【TwoTask定时开始...】");
        for (int i = 0; i < 5; i++) {
            System.out.println(i + ".TwoTask执行任务中...");
        }
        System.out.println("【TwoTask定时结束...】");
    }
}
