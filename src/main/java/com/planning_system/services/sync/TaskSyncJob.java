package com.planning_system.services.sync;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public class TaskSyncJob implements Job {

    private final TaskSyncService syncService;

    @Override
    public void start() {
        syncService.startSync();
    }

    @Override
    public void stop() {
        syncService.stopSync();
    }
}