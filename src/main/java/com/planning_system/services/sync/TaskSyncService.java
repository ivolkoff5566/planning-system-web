package com.planning_system.services.sync;

import com.planning_system.repository.TaskRepository;
import com.planning_system.repository.UserStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * The class responsible for synchronization of the Tasks.
 * The runSyncTask method will be run continuously according the syncInterval time. If there are any task that
 * satisfy past due time condition, they will be removed from the taskRepository and added to the rejectedTaskRepository.
 */

@Component
@RequiredArgsConstructor
public class TaskSyncService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskSyncService.class);
    private final TaskRepository repository;
    private final UserStatisticsRepository userStatisticsRepository;
    @Value("${task.past.due.time}")
    private long pastDueTime;

    @Scheduled(fixedDelayString = "${sync.interval}")
    public void startSync() {
        runSyncTask();
    }

    public void runSyncTask() {
        LOGGER.info("Starting Task sync. Past due time: {} ms", pastDueTime);
        repository.getAllTasksPastDue(pastDueTime)
                  .stream()
                  .filter(task -> !task.isRejected())
                  .forEach(task -> {
                      if (Objects.nonNull(task.getUser())) {
                          var userId = task.getUser()
                                           .getId();
                          var stat = userStatisticsRepository.findByUserId(userId);
                          var rejectedCount = stat.getRejectedTaskCount();
                          stat.setRejectedTaskCount(++rejectedCount);
                          userStatisticsRepository.save(stat);
                      }
                      task.setRejected(true);
                      repository.save(task);
                  });
        LOGGER.info("Task sync has been finished");
    }
}