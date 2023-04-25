package com.planning_system.services;

import com.planning_system.services.sync.TaskSyncService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class TaskSyncServiceTests {

    @SpyBean
    private TaskSyncService taskSyncService;

    @Test
    public void taskSyncSchedulerTest() {
        await().atMost(Duration.ofSeconds(3))
                .untilAsserted(() -> verify(taskSyncService, times(3)).runSyncTask());
    }
}