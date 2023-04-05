package com.planning_system.services.sync;

import com.planning_system.config.ApplicationConfig;

public class Scheduler {

    private final ApplicationConfig config;

    private Scheduler(ApplicationConfig config) {
       this.config = config;
    }

    public static Scheduler createScheduler(ApplicationConfig config) {
        return new Scheduler(config);
    }

    public void load() {
        var job = RegisteredJobFactory.getJob(config);
        job.start();
    }
}