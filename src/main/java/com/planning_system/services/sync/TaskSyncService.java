package com.planning_system.services.sync;

import com.planning_system.entity.Task;
import com.planning_system.repository.RejectedTaskRepository;
import com.planning_system.repository.TaskRepository;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The class responsible for synchronization of the Tasks.
 * The runSyncTask method will be run continuously according the syncInterval time. If there are any task that
 * satisfy past due time condition, they will be removed from the taskRepository and added to the rejectedTaskRepository.
 */
public class TaskSyncService {

    private final TaskRepository repository;
    private final RejectedTaskRepository rejectedTaskRepository;
    private final long syncInterval;
    private final long pastDueTime;
    private final ScheduledExecutorService executorService;

    public TaskSyncService(final TaskRepository repository,
                           final RejectedTaskRepository rejectedTaskRepository,
                           final long syncInterval,
                           final long pastDueTime) {
        this.repository = repository;
        this.rejectedTaskRepository = rejectedTaskRepository;
        this.syncInterval = syncInterval;
        this.pastDueTime = pastDueTime;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void startSync() {
        executorService.scheduleAtFixedRate(this::runSyncTask, 0L, syncInterval, TimeUnit.SECONDS);
    }

    public void stopSync() {
        executorService.shutdown();
    }

    private void runSyncTask() {
        repository.getAllTasksPastDue(pastDueTime).forEach((e) -> {
            Task rejected = repository.delete(e.getId());
            rejectedTaskRepository.save(rejected);
        });
    }
}