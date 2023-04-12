package com.planning_system.services.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

public class Scheduler implements ApplicationListener<ApplicationReadyEvent> {
//
//    private final Job job;
//
//    @Autowired
//    public Scheduler(final TaskSyncJob job) {
//        this.job = job;
//    }
//
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        //job.start();
    }
//
//    @PreDestroy
//    public void stopSync() {
//        job.stop();
//    }
}