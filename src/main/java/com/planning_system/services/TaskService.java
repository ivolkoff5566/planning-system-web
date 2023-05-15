package com.planning_system.services;

import com.planning_system.entity.task.Task;
import com.planning_system.entity.task.TaskPriority;
import com.planning_system.entity.task.TaskStatus;
import com.planning_system.entity.user_statistics.UserStatistics;
import com.planning_system.repository.TaskRepository;
import com.planning_system.repository.UserRepository;
import com.planning_system.repository.UserStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequiredArgsConstructor
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserStatisticsRepository userStatisticsRepository;

    public Task createTask(Task task) {
        if (Objects.isNull(task.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_TASK_NAME_ENTERED);
        }
        if (Objects.isNull(task.getDescription())) {
            task.setDescription("default description");
            LOGGER.info("No description provided, using default value");
        }
        if (Objects.isNull(task.getStatus())) {
            task.setStatus(TaskStatus.TODO);
            LOGGER.info("No Task status provided, using default value");
        }
        if (Objects.isNull(task.getPriority())) {
            task.setPriority(TaskPriority.LOW);
            LOGGER.info("No Task priority provided, using default value");
        }

        task.setDate(Instant.now());
        LOGGER.info("Creating a new task: {}", task);
        return taskRepository.save(task);
    }

    public Task assignTaskToUser(int taskId, int userId) {
        var task = taskRepository.findById(taskId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,TASK_NOT_FOUND));
        var user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,USER_NOT_FOUND));

        if (Objects.nonNull(task.getUser()) && task.getUser().getId() == userId) {
            return task;
        }
        LOGGER.info("Assigning Task {} to User {}", task, user);
        task.setUser(user);

        var stat = userStatisticsRepository.findByUserId(userId);
        if (Objects.isNull(stat)) {
            stat = UserStatistics.builder().userId(userId).build();
        }
        var assignedCount = stat.getAssignedTaskCount();
        stat.setAssignedTaskCount(++assignedCount);
        userStatisticsRepository.save(stat);

        return taskRepository.save(task);
    }

    public Task removeUserAssignment(int taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,TASK_NOT_FOUND));
        LOGGER.info("Removing user assignment from Task {}", task);

        var user = task.getUser();
        if (!Objects.isNull(user)) {
            var stat = userStatisticsRepository.findByUserId(user.getId());
            var assignedCount = stat.getAssignedTaskCount();
            stat.setAssignedTaskCount(--assignedCount);
            userStatisticsRepository.save(stat);
        }

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
            var status = task.getStatus();
            taskToUpdate.setStatus(status);
            LOGGER.info("Updating Task status, new value: {}", status);

            if (status.equals(TaskStatus.COMPLETED) && Objects.nonNull(taskToUpdate.getUser())) {
                var userId = taskToUpdate.getUser().getId();
                var stat = userStatisticsRepository.findByUserId(userId);
                var completedCount = stat.getCompletedTaskCount();
                stat.setCompletedTaskCount(++completedCount);
                userStatisticsRepository.save(stat);
                LOGGER.info("User {} completed Task {}", userId, taskToUpdate.getId());
            }
        }

        if (Objects.nonNull(task.getDescription())) {
            taskToUpdate.setDescription(task.getDescription());
            LOGGER.info("Updating Task description, new value: {}", task.getDescription());
        }

        return taskRepository.save(taskToUpdate);
    }

    public Task deleteTask(int id) {
        Task task = getTask(id);
        taskRepository.delete(task);
        return task;
    }
}