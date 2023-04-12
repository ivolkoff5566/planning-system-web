package com.planning_system.controller.task.dto;

import com.planning_system.entity.task.TaskPriority;
import com.planning_system.entity.task.TaskStatus;
import com.planning_system.entity.user.User;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class TaskResponseDTO {
    int id;
    Instant date;
    String name;
    String description;
    TaskStatus status;
    TaskPriority priority;
    User user;
}