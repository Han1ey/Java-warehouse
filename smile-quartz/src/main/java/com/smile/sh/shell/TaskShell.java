package com.smile.sh.shell;

import java.io.IOException;

@SuppressWarnings("InfiniteLoopStatement")
public class TaskShell {

    private static final TaskManager taskManager = new TaskManager();

    public static void main(String[] args) throws IOException, InterruptedException {
        taskRun();
    }

    private static void taskRun() throws IOException, InterruptedException {

        Thread quartzThread = new Thread(TaskShell::quartzTask);
        System.out.println("quartz定时任务线程启动");
        quartzThread.start();

        while (true) {
            if (quartzThread.getState().equals(Thread.State.TERMINATED)) {
                System.out.println("quartz定时任务运行过程中出现异常退出，请检查日志");
                System.out.println("quartz定时任务重新启动定时任务。。。");
                quartzThread = new Thread(TaskShell::quartzTask);
                quartzThread.start();
                System.out.println("quartz定时任务重新启动完成");
            }
            Thread.sleep(30 * 1000L);
        }
    }

    private static void quartzTask() {
        while (true) {
            taskManager.start();
            try {
                Thread.sleep(30 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
