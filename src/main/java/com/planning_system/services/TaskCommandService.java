package com.planning_system.services;

import com.planning_system.controller.task.dto.TaskRequestDTO;
import com.planning_system.controller.task.dto.TaskResponseDTO;
import com.planning_system.entity.task.Task;
import com.planning_system.services.utility.TaskRequestDTOToTaskMapper;
import com.planning_system.services.utility.TaskToTaskResponseDTOMapper;
import com.planning_system.services.utility.TaskUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.planning_system.controller.task.TaskParams.REJECTED_TASK;
import static com.planning_system.services.messages.ServiceErrorMessages.ERROR;
import static com.planning_system.services.messages.ServiceErrorMessages.TASK_NOT_FOUND;

/**
 * The class contains main business logic, responsible for {@link Task} processing
 * and preparing data for response (filtering, sorting etc.)
 */
@Service
public class TaskCommandService {

    private final TaskService taskService;
    private final TaskToTaskResponseDTOMapper taskToTaskResponseDTOMapper;
    private final TaskRequestDTOToTaskMapper taskRequestDTOToTaskMapper;

    @Autowired
    public TaskCommandService(final TaskService taskService,
                              final TaskToTaskResponseDTOMapper taskToTaskResponseDTOMapper,
                              final TaskRequestDTOToTaskMapper taskRequestDTOToTaskMapper) {
        this.taskService = taskService;
        this.taskToTaskResponseDTOMapper = taskToTaskResponseDTOMapper;
        this.taskRequestDTOToTaskMapper = taskRequestDTOToTaskMapper;
    }

    /**
     * Method creates a new {@link Task}
     * @param taskRequestDTO {@link TaskRequestDTO} that will be used to create a Task.
     * @return {@link TaskResponseDTO}
     */
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        var task = taskRequestDTOToTaskMapper.mapToTask(taskRequestDTO);
        var taskResult = taskService.createTask(task);
        return taskToTaskResponseDTOMapper.mapToTaskResponseDTO(taskResult);
    }

    /**
     * Method returns all the {@link Task} that exist in the system. The returned Tasks can be sorted by date, status,
     * priority or id. By default, sorted by ID. It is possible to use reverse sort.
     *
     * @param params contain params with sorted type and/or rejected tasks request.
     * @return {@link List<TaskResponseDTO>}
     */
    public List<TaskResponseDTO> getAllTasks(Map<String, String> params) {
        Comparator<Task> comp = TaskUtil.getTaskComparatorFromParams(params);

        if (!Objects.isNull(params.get(REJECTED_TASK))) {
            if (!params.get(REJECTED_TASK).equals(Boolean.TRUE.toString())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR);
            }

            return taskService.getAllTasks()
                              .stream()
                              .filter(Task::isRejected)
                              .sorted(comp)
                              .map(taskToTaskResponseDTOMapper::mapToTaskResponseDTO)
                              .collect(Collectors.toList());
        }
        return taskService.getAllTasks()
                          .stream()
                          .sorted(comp)
                          .filter(task -> !task.isRejected())
                          .map(taskToTaskResponseDTOMapper::mapToTaskResponseDTO)
                          .collect(Collectors.toList());
    }

    /**
     * Method returns the {@link Task} by id.
     * If the Task is rejected, TASK_NOT_FOUND exception will be thrown along with 404 status code
     * @param id requested Task id.
     * @return {@link TaskResponseDTO}
     */
    public TaskResponseDTO getTask(int id) {
        var task = taskService.getTask(id);
        if (task.isRejected()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND ,TASK_NOT_FOUND);
        }
        return taskToTaskResponseDTOMapper.mapToTaskResponseDTO(task);
    }

    /**
     * Method updates the {@link Task}
     * By design user can update only description or status.
     * @param id of the task that will be updated
     * @param taskRequestDTO {@link TaskRequestDTO} that contains fields to be updated
     * @return {@link TaskResponseDTO}
     */
    public TaskResponseDTO updateTask(int id, TaskRequestDTO taskRequestDTO) {
        var taskToUpdate = taskRequestDTOToTaskMapper.mapToTask(taskRequestDTO);
        var resultTask = taskService.updateTask(id, taskToUpdate);
        return taskToTaskResponseDTOMapper.mapToTaskResponseDTO(resultTask);
    }

    /**
     * Method deletes {@link Task}
     * @param id Task id to be deleted
     * @return {@link TaskResponseDTO}
     */
    public TaskResponseDTO deleteTask(int id) {
        var task = taskService.deleteTask(id);
        return taskToTaskResponseDTOMapper.mapToTaskResponseDTO(task);
    }

    public Task assignTaskToUser(int taskId, int userId) {
        return taskService.assignTaskToUser(taskId, userId);
    }
}