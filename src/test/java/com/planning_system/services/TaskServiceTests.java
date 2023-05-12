package com.planning_system.services;

import com.planning_system.entity.task.Task;
import com.planning_system.entity.task.TaskPriority;
import com.planning_system.entity.task.TaskStatus;
import com.planning_system.repository.TaskRepository;
import com.planning_system.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.planning_system.services.messages.ServiceErrorMessages.TASK_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskServiceTests {

    private final TaskRepository taskRepository = mock(TaskRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final TaskService taskService = new TaskService(taskRepository, userRepository);

    @Test
    public void createTaskWithDefaultFieldsTest() {
        int id = 0;
        Task task = Task.builder().name("t1").build();

        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task);

        verify(taskRepository, times(1)).save(task);
        assertEquals(id, result.getId());
        assertNotNull(result.getDate());
        assertEquals(task.getName(), result.getName());
        assertEquals("default description", result.getDescription());
        assertEquals(TaskStatus.TODO, result.getStatus());
        assertEquals(TaskPriority.LOW, result.getPriority());
    }

    @Test
    public void createTaskTest() {
        int id = 0;
        String taskName = "t1";
        TaskStatus taskStatus = TaskStatus.IN_PROGRESS;
        TaskPriority taskPriority = TaskPriority.HIGH;
        String desc = "task desc";
        Task task = Task.builder()
                        .name(taskName)
                        .description(desc)
                        .priority(taskPriority)
                        .status(taskStatus)
                        .build();

        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task);

        verify(taskRepository, times(1)).save(task);
        assertEquals(id, result.getId());
        assertNotNull(result.getDate());
        assertEquals(taskName, result.getName());
        assertEquals(desc, result.getDescription());
        assertEquals(taskStatus, result.getStatus());
        assertEquals(taskPriority, result.getPriority());
    }

    @Test
    public void getAllTasksTest() {
        List<Task> taskList = Collections.singletonList(mock(Task.class));
        when(taskRepository.findAll()).thenReturn(taskList);

        assertEquals(taskList, taskService.getAllTasks());
    }

    @Test
    public void getTaskTest() {
        Task mockedTask = mock(Task.class);
        int taskId = 1;

        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(mockedTask));
        Task result = taskService.getTask(taskId);

        verify(taskRepository, times(1)).findById(taskId);
        assertEquals(mockedTask, result);
    }

    @Test
    public void getTaskNotFoundTest() {
        int taskId = 1;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                                           () -> taskService.getTask(taskId));
        assertEquals(String.format("404 NOT_FOUND \"%s\"", TASK_NOT_FOUND), exception.getMessage());
    }

    @Test
    public void updateTaskTest() {
        Task taskToUpdate = Task.builder()
                .id(1)
                .name("t1")
                .description("task description")
                .date(Instant.parse("2023-03-30T21:22:19.00Z"))
                .priority(TaskPriority.LOW)
                .status(TaskStatus.TODO)
                .build();

        Task updatedTask = Task.builder()
                .id(1)
                .name("t1")
                .description("updated description")
                .date(Instant.parse("2023-03-30T21:22:19.00Z"))
                .priority(TaskPriority.LOW)
                .status(TaskStatus.IN_PROGRESS)
                .build();

        when(taskRepository.findById(taskToUpdate.getId())).thenReturn(Optional.of(taskToUpdate));
        when(taskRepository.save(taskToUpdate)).thenReturn(updatedTask);


        Task result = taskService.updateTask(taskToUpdate.getId(), taskToUpdate);

        verify(taskRepository, times(1)).findById(taskToUpdate.getId());
        verify(taskRepository, times(1)).save(taskToUpdate);
        Assertions.assertEquals(updatedTask, result);
    }

    @Test
    public void updateTaskWithInvalidIdTest() {
        Task taskToUpdate = Task.builder()
                                .id(1)
                                .name("t1")
                                .description("task description")
                                .date(Instant.parse("2023-03-30T21:22:19.00Z"))
                                .priority(TaskPriority.LOW)
                                .status(TaskStatus.TODO)
                                .build();

        when(taskRepository.findById(taskToUpdate.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> taskService.updateTask(taskToUpdate.getId(), taskToUpdate));
        verify(taskRepository, times(1)).findById(taskToUpdate.getId());
        verify(taskRepository, never()).save(taskToUpdate);
        assertEquals(String.format("404 NOT_FOUND \"%s\"", TASK_NOT_FOUND), exception.getMessage());
    }

    @Test
    public void deleteTaskTest() {
        int taskId = 1;
        Task task = Task.builder().id(taskId).build();
        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task));
        Task deletedTask = taskService.deleteTask(task.getId());

        verify(taskRepository, times(1)).delete(task);
        assertEquals(task, deletedTask);
    }
}