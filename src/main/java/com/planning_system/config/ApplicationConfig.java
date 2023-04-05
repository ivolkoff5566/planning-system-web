package com.planning_system.config;

import lombok.Data;

import java.io.InputStream;
import java.util.Properties;

@Data
public class ApplicationConfig {

    private long syncInterval = 1L;
    private long taskPastDueTime = 10L;
    private String taskHandlerType = "cli"; // default values if the config was not loaded
    private String taskSyncServiceName;

    private final static ApplicationConfig INSTANCE = new ApplicationConfig();

    private ApplicationConfig() {
    }

    public static ApplicationConfig getInstance() {
        return INSTANCE;
    }

    public void loadConfig(String configPath) {
        ClassLoader classLoader = ApplicationConfig.class.getClassLoader();

        try (InputStream input = classLoader.getResourceAsStream(configPath)) {
            Properties properties = new Properties();
            properties.load(input);
            syncInterval = Long.parseLong(properties.getProperty("sync.interval"));
            taskPastDueTime = Long.parseLong(properties.getProperty("task.past.due.time"));
            taskHandlerType = properties.getProperty("task.handler.type");
            taskSyncServiceName = properties.getProperty("application.schedule.job.task.sync");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
