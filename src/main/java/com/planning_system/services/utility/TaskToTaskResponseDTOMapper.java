package com.planning_system.services.utility;

import com.planning_system.controller.task.dto.TaskResponseDTO;
import com.planning_system.controller.user.dto.BasicUserResponseDTO;
import com.planning_system.entity.task.Task;
import com.planning_system.entity.user.User;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TaskToTaskResponseDTOMapper implements TaskResponseDTOMapper<Task> {

    @Override
    public TaskResponseDTO mapToTaskResponseDTO(Task data) {
        var builder = TaskResponseDTO.builder()
                                                         .id(data.getId())
                                                         .date(data.getDate())
                                                         .name(data.getName())
                                                         .description(data.getDescription())
                                                         .status(data.getStatus())
                                                         .priority(data.getPriority());

        if (Objects.nonNull(data.getUser())) {
            builder.user(mapToBasicUserResponseDTO(data.getUser()));
        }

        return builder.build();
    }

    private BasicUserResponseDTO mapToBasicUserResponseDTO(User user) {
        return BasicUserResponseDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .build();
    }
}