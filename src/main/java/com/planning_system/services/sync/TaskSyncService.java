package com.planning_system.services.sync;

import com.planning_system.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * The class responsible for synchronization of the Tasks.
 * The runSyncTask method will be run continuously according the syncInterval time. If there are any task that
 * satisfy past due time condition, they will be removed from the taskRepository and added to the rejectedTaskRepository.
 */

@Component
public class TaskSyncService {

//    @Value("${sync.interval}")
//    private long syncInterval;
    @Value("${task.past.due.time}")
    private long pastDueTime;

    private final TaskRepository repository;
    private final ScheduledExecutorService executorService;

    @Autowired
    public TaskSyncService(final TaskRepository repository) {
        this.repository = repository;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Scheduled(fixedDelayString = "${sync.interval}")
    public void startSync() {
        runSyncTask();
    }

    public void stopSync() {
        executorService.shutdown();
    }

    private void runSyncTask() {
        repository.getAllTasksPastDue(pastDueTime)
                .forEach(task -> {
                    task.setRejected(true);
                    repository.save(task);
                });
    }
}