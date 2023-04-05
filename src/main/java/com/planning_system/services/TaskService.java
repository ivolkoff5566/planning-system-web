package com.planning_system.services;

import com.planning_system.entity.Task;
import com.planning_system.entity.TaskPriority;
import com.planning_system.entity.TaskStatus;
import com.planning_system.repository.TaskRepository;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static com.planning_system.services.messages.ServiceErrorMessages.NO_FIELDS_TO_UPDATE;
import static com.planning_system.services.messages.ServiceErrorMessages.NO_NAME_ENTERED;
import static com.planning_system.services.messages.ServiceErrorMessages.TASK_NOT_FOUND;

/**
 * The class responsible for CRUD operations with the Tasks.
 * Some methods may also perform data validation.
 */
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task createTask(Task task) {
        if (Objects.isNull(task.getName())) {
            throw new RuntimeException(NO_NAME_ENTERED);
        }
        if (Objects.isNull(task.getDescription())) {
            task.setDescription("default description");
        }
        if (Objects.isNull(task.getStatus())) {
            task.setStatus(TaskStatus.TODO);
        }
        if (Objects.isNull(task.getPriority())) {
            task.setPriority(TaskPriority.LOW);
        }

        task.setId(repository.getIncrementedId());
        task.setDate(Instant.now());
        return repository.save(task);
    }

    public List<Task> getAllTasks() {
        return repository.getAll();
    }

    public Task getTask(Integer id) {
        Task task = repository.getById(id);
        if (Objects.isNull(task)) {
            throw new RuntimeException(TASK_NOT_FOUND);
        }
        return task;
    }

    public Task updateTask(Task task) {
        Task taskToUpdate = getTask(task.getId());
        if (Objects.nonNull(task.getStatus()) || Objects.nonNull(task.getDescription())) {
            if (Objects.nonNull(task.getStatus())) {
                taskToUpdate.setStatus(task.getStatus());
            }
            if (Objects.nonNull(task.getDescription())) {
                taskToUpdate.setDescription(task.getDescription());
            }
        } else {
            throw new RuntimeException(NO_FIELDS_TO_UPDATE);
        }
        return repository.update(taskToUpdate);
    }

    public Task deleteTask(Integer id) {
        if (Objects.isNull(repository.getById(id))) {
            throw new RuntimeException(TASK_NOT_FOUND);
        }
        return repository.delete(id);
    }
}