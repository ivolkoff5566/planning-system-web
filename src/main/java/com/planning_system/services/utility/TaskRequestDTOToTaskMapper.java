package com.planning_system.services.utility;

import com.planning_system.controller.task.dto.TaskRequestDTO;
import com.planning_system.entity.task.Task;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TaskRequestDTOToTaskMapper implements TaskMapper<TaskRequestDTO> {

    @Override
    public Task mapToTask(TaskRequestDTO data) {
        return Task.builder()
                   .date(Instant.now())
                   .name(data.getName())
                   .description(data.getDescription())
                   .status(data.getStatus())
                   .priority(data.getPriority())
                   .build();
    }
}