package com.planning_system.entity;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Task {
     private int id;
     private Instant date;
     private String name;
     private String description;
     private TaskStatus status;
     private TaskPriority priority;
}