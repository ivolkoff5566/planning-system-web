package com.planning_system.services;

import com.planning_system.repository.RejectedTaskRepository;
import com.planning_system.repository.TaskRepository;
import com.planning_system.services.sync.TaskSyncService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TaskSyncServiceTests {

    private final TaskRepository taskRepository = mock(TaskRepository.class);
    private final RejectedTaskRepository rejectedTaskRepository = mock(RejectedTaskRepository.class);

    private final TaskSyncService taskSyncService = new TaskSyncService(taskRepository,
                                                                        rejectedTaskRepository,
                                                                        1,
                                                                        30);

    @Test
    public void testStartSync() throws InterruptedException {
        taskSyncService.startSync();
        CountDownLatch latch = new CountDownLatch(2);

        Executors.newSingleThreadScheduledExecutor().schedule(latch::countDown, 2, TimeUnit.SECONDS);
        Executors.newSingleThreadScheduledExecutor().schedule(latch::countDown, 2, TimeUnit.SECONDS);
        latch.await();

        verify(taskRepository, times(3)).getAllTasksPastDue(30L);
    }
}