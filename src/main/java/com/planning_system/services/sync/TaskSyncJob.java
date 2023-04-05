package com.planning_system.services.sync;

import lombok.RequiredArgsConstructor;

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