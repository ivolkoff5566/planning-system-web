package com.planning_system.services.sync;

import com.planning_system.config.ApplicationConfig;
import com.planning_system.repository.RejectedTaskRepository;
import com.planning_system.repository.TaskRepository;

public class RegisteredJobFactory {

    public static Job getJob(ApplicationConfig config) {
        Job job = null;

        if (config.getTaskSyncServiceName().equals("TaskSyncService")) {
            var taskSyncService = new TaskSyncService(TaskRepository.getInstance(),
                                                      RejectedTaskRepository.getInstance(),
                                                      config.getSyncInterval(),
                                                      config.getTaskPastDueTime());

            return new TaskSyncJob(taskSyncService);
        }

        return job;
    }
}