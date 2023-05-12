package com.planning_system.controller.task.dto;

import com.planning_system.entity.task.TaskPriority;
import com.planning_system.entity.task.TaskStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TaskRequestDTO {
    String name;
    String description;
    TaskStatus status;
    TaskPriority priority;
}
