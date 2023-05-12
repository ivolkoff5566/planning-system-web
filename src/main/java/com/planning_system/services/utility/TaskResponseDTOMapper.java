package com.planning_system.services.utility;

import com.planning_system.controller.task.dto.TaskResponseDTO;

public interface TaskResponseDTOMapper<T> {
    TaskResponseDTO mapToTaskResponseDTO(T data);
}
