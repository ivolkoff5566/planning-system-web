package com.planning_system.services;

import com.planning_system.entity.task.Task;
import com.planning_system.entity.task.TaskPriority;
import com.planning_system.entity.task.TaskStatus;
import com.planning_system.entity.user.User;
import com.planning_system.repository.TaskRepository;
import com.planning_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static com.planning_system.services.messages.ServiceErrorMessages.NO_TASK_NAME_ENTERED;
import static com.planning_system.services.messages.ServiceErrorMessages.TASK_NOT_FOUND;
import static com.planning_system.services.messages.ServiceErrorMessages.USER_NOT_FOUND;

/**
 * The class responsible for CRUD operations with the Tasks.
 * Some methods may also perform data validation.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(final TaskRepository taskRepository,
                       final UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(Task task) {
        if (Objects.isNull(task.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_TASK_NAME_ENTERED);
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

        task.setDate(Instant.now());
        return taskRepository.save(task);
    }

    public Task assignTaskToUser(int taskId, int userId) {
        Task task = taskRepository.findById(taskId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,TASK_NOT_FOUND));
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,USER_NOT_FOUND));

        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task removeUserAssignment(int taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,TASK_NOT_FOUND));

        task.setUser(null);
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTask(int id) {
        return taskRepository.findById(id)
                         .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,TASK_NOT_FOUND));
    }

    public Task updateTask(int id, Task task) {
        Task taskToUpdate = getTask(id);
        if (Objects.nonNull(task.getStatus())) {
            taskToUpdate.setStatus(task.getStatus());
        }
        if (Objects.nonNull(task.getDescription())) {
            taskToUpdate.setDescription(task.getDescription());
        }

        return taskRepository.save(taskToUpdate);
    }

    public Task deleteTask(int id) {
        Task task = getTask(id);
        taskRepository.delete(task);
        return task;
    }
}