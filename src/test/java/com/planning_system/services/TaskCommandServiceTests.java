package com.planning_system.services;

import com.planning_system.entity.Task;
import com.planning_system.entity.TaskPriority;
import com.planning_system.handlers.commands.OptionType;
import com.planning_system.handlers.response.Response;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.planning_system.services.messages.ServiceErrorMessages.ID_NOT_NUMBER;
import static com.planning_system.services.messages.ServiceErrorMessages.INVALID_SORTING_TYPE;
import static com.planning_system.services.messages.ServiceMessages.GET_ALL_REJECTED_TASKS;
import static com.planning_system.services.messages.ServiceMessages.GET_ALL_TASKS;
import static com.planning_system.services.messages.ServiceMessages.GET_TASK;
import static com.planning_system.services.messages.ServiceMessages.TASK_CREATED;
import static com.planning_system.services.messages.ServiceMessages.TASK_DELETED;
import static com.planning_system.services.messages.ServiceMessages.TASK_UPDATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskCommandServiceTests {

    private final TaskService taskService = mock(TaskService.class);
    private final RejectedTaskService rejectedTaskService = mock(RejectedTaskService.class);

    private final Task task1 = Task.builder()
                                   .id(1)
                                   .name("t1")
                                   .date(Instant.parse("2023-03-30T21:22:19.00Z"))
                                   .priority(TaskPriority.LOW)
                                   .build();
    private final Task task2 = Task.builder()
                                   .id(2)
                                   .name("t2")
                                   .date(Instant.parse("2023-03-30T22:22:19.00Z"))
                                   .priority(TaskPriority.HIGH)
                                   .build();
    private final Task task3 = Task.builder()
                                   .id(3)
                                   .name("t3")
                                   .date(Instant.parse("2023-03-30T20:22:19.00Z"))
                                   .priority(TaskPriority.LOW)
                                   .build();


    private final TaskCommandService taskCommandService = new TaskCommandService(taskService, rejectedTaskService);

    @Test
    public void createTaskTest() {
        Task task = mock(Task.class);
        Task taskResult = mock(Task.class);
        when(taskService.createTask(task)).thenReturn(taskResult);

        Response<Task> expectedResponse = Response.<Task>builder()
                                                        .message(TASK_CREATED)
                                                        .data(taskResult)
                                                        .build();

        Response<Task> result = taskCommandService.createTask(task);
        assertEquals(expectedResponse, result);
    }

    @Test
    public void getAllTasksTest() {
        Map<OptionType, String> options = new HashMap<>();
        List<Task> taskList = Collections.singletonList(mock(Task.class));
        when(taskService.getAllTasks()).thenReturn(taskList);

        Response<List<Task>> expectedResponse = Response.<List<Task>>builder()
                                                        .message(GET_ALL_TASKS)
                                                        .data(taskList)
                                                        .build();

        Response<List<Task>> result = taskCommandService.getAllTask(options);
        assertEquals(expectedResponse, result);
    }

    @Test
    public void getAllRejectedTasksTest() {
        Map<OptionType, String> options = new HashMap<>();
        options.put(OptionType.REJECTED_TASK, Boolean.TRUE.toString());
        List<Task> taskList = Collections.singletonList(mock(Task.class));

        when(rejectedTaskService.getAllRejectedTasks()).thenReturn(taskList);

        Response<List<Task>> expectedResponse = Response.<List<Task>>builder()
                                                        .message(GET_ALL_REJECTED_TASKS)
                                                        .data(taskList)
                                                        .build();

        Response<List<Task>> result = taskCommandService.getAllTask(options);
        assertEquals(expectedResponse, result);
    }

    @Test
    public void getAllTasksSortedByPriorityTest() {
        Map<OptionType, String> options = new HashMap<>();
        options.put(OptionType.SORTED, "priority");

        List<Task> taskListUnsorted = List.of(task1, task2, task3);

        when(taskService.getAllTasks()).thenReturn(taskListUnsorted);

        Response<List<Task>> expectedResponse = Response.<List<Task>>builder()
                                                        .message(GET_ALL_TASKS)
                                                        .data(List.of(task2, task1, task3))
                                                        .build();

        Response<List<Task>> result = taskCommandService.getAllTask(options);
        assertEquals(expectedResponse, result);
    }

    @Test
    public void getAllTasksSortedByDateTest() {
        Map<OptionType, String> options = new HashMap<>();
        options.put(OptionType.SORTED, "date");

        List<Task> taskListUnsorted = List.of(task1, task2, task3);

        when(taskService.getAllTasks()).thenReturn(taskListUnsorted);

        Response<List<Task>> expectedResponse = Response.<List<Task>>builder()
                                                        .message(GET_ALL_TASKS)
                                                        .data(List.of(task3, task1, task2))
                                                        .build();

        Response<List<Task>> result = taskCommandService.getAllTask(options);
        assertEquals(expectedResponse, result);
    }

    @Test
    public void getAllTasksSortedByIdReversedTest() {
        // id is default sorting type, we don't need to specify it
        Map<OptionType, String> options = new HashMap<>();
        options.put(OptionType.REVERSE_SORT, Boolean.TRUE.toString());

        List<Task> taskListUnsorted = List.of(task1, task2, task3);

        when(taskService.getAllTasks()).thenReturn(taskListUnsorted);

        Response<List<Task>> expectedResponse = Response.<List<Task>>builder()
                                                        .message(GET_ALL_TASKS)
                                                        .data(List.of(task3, task2, task1))
                                                        .build();

        Response<List<Task>> result = taskCommandService.getAllTask(options);
        assertEquals(expectedResponse, result);
    }

    @Test
    public void getAllTasksInvalidSortingTypeTest() {
        Map<OptionType, String> options = new HashMap<>();
        options.put(OptionType.SORTED, "name");

        List<Task> taskListUnsorted = List.of(task1, task2, task3);
        when(taskService.getAllTasks()).thenReturn(taskListUnsorted);

        Exception exception = assertThrows(RuntimeException.class,
                                           () -> taskCommandService.getAllTask(options));
        assertEquals(INVALID_SORTING_TYPE, exception.getMessage());
    }

    @Test
    public void gatTaskTest() {
        Task taskResult = mock(Task.class);
        when(taskService.getTask(1)).thenReturn(taskResult);

        Response<Task> expectedResponse = Response.<Task>builder()
                                                  .message(GET_TASK)
                                                  .data(taskResult)
                                                  .build();

        Response<Task> result = taskCommandService.getTask("1");
        assertEquals(expectedResponse, result);
    }

    @Test
    public void gatTaskInvalidIdTest() {
        Task taskResult = mock(Task.class);
        when(taskService.getTask(1)).thenReturn(taskResult);

        Exception exception = assertThrows(RuntimeException.class,
                                           () -> taskCommandService.getTask("x"));
        assertEquals(ID_NOT_NUMBER, exception.getMessage());
    }

    @Test
    public void updateTaskTest() {
        Task task = mock(Task.class);
        Task taskResult = mock(Task.class);
        when(taskService.updateTask(task)).thenReturn(taskResult);

        Response<Task> expectedResponse = Response.<Task>builder()
                                                  .message(TASK_UPDATED)
                                                  .data(taskResult)
                                                  .build();

        Response<Task> result = taskCommandService.updateTask(task);
        assertEquals(expectedResponse, result);
    }

    @Test
    public void deleteTaskTest() {
        Task taskResult = mock(Task.class);
        when(taskService.deleteTask(1)).thenReturn(taskResult);

        Response<Task> expectedResponse = Response.<Task>builder()
                                                  .message(TASK_DELETED)
                                                  .data(taskResult)
                                                  .build();

        Response<Task> result = taskCommandService.deleteTask("1");
        assertEquals(expectedResponse, result);
    }

    @Test
    public void deleteTaskInvalidIdTest() {
        Task taskResult = mock(Task.class);
        when(taskService.deleteTask(1)).thenReturn(taskResult);

        Exception exception = assertThrows(RuntimeException.class,
                                           () -> taskCommandService.deleteTask("x"));
        assertEquals(ID_NOT_NUMBER, exception.getMessage());
    }
}