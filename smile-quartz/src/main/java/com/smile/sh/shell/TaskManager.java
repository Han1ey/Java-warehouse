package com.smile.sh.shell;

import com.google.common.collect.Maps;
import com.smile.sh.model.TaskJson;
import com.smile.sh.util.BaseUtil;
import com.smile.sh.util.QuartzUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

class TaskManager {

    private static final String fileName = "/task.json";

    private static final String TASK_STATUS_RUN = "run";

    private static final String TASK_STATUS_STOP = "stop";

    private static Map<String, String> runRecordMap = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    void start() {
        try {
            InputStream input = this.getClass().getResourceAsStream(fileName);
            String json = IOUtils.toString(input);
            TaskJson taskJson = BaseUtil.parseJson(json, TaskJson.class);
            if (taskJson != null
                    && taskJson.getTasks() != null
                    && taskJson.getTasks().size() > 0) {
                for (TaskJson.Task t : taskJson.getTasks()) {
                    processTask(t);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processTask(TaskJson.Task t) {
        if (runRecordMap.get(t.getId()) == null) {
            runRecordMap.put(t.getId(), t.getStatus());
            if (StringUtils.equals(TASK_STATUS_RUN, t.getStatus())) {
                try {
                    System.out.println("新任务：" + t.getName() + "，类名：" + t.getClz() + "，初始化启动");
                    QuartzUtil.addJob(t.getName(), Class.forName(t.getClz()), t.getSleep());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            String curStatus = runRecordMap.get(t.getId());
            if (!curStatus.equals(t.getStatus())) {
                runRecordMap.put(t.getId(), t.getStatus());
                if (StringUtils.equals(TASK_STATUS_RUN, t.getStatus())) {
                    try {
                        System.out.println("老任务：" + t.getName() + "，类名：" + t.getClz() + "，切换启动");
                        QuartzUtil.addJob(t.getName(), Class.forName(t.getClz()), t.getSleep());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (StringUtils.equals(TASK_STATUS_STOP, t.getStatus())) {
                    System.out.println("老任务：" + t.getName() + "，类名：" + t.getClz() + "，切换停止");
                    QuartzUtil.removeJob(t.getName());
                }
            }
        }
    }
}
