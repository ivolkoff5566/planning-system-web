package com.planning_system.services.utility;

import com.planning_system.controller.task.dto.TaskResponseDTO;
import com.planning_system.entity.task.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskToTaskResponseDTOMapper implements TaskResponseDTOMapper<Task> {

    @Override
    public TaskResponseDTO mapToTaskResponseDTO(Task data) {
        return TaskResponseDTO.builder()
                              .id(data.getId())
                              .date(data.getDate())
                              .name(data.getName())
                              .description(data.getDescription())
                              .status(data.getStatus())
                              .priority(data.getPriority())
                              .user(data.getUser())
                              .build();
    }
}