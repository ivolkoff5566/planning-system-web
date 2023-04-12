package com.planning_system.services.utility;

import com.planning_system.entity.task.Task;

public interface TaskMapper<T> {
    Task mapToTask(T input);
}