package com.planning_system.services;

import com.planning_system.entity.Task;
import com.planning_system.handlers.commands.Command;
import com.planning_system.handlers.commands.OptionType;
import com.planning_system.handlers.response.Response;
import com.planning_system.services.utility.TaskUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.planning_system.handlers.commands.OptionType.REJECTED_TASK;
import static com.planning_system.services.messages.ServiceErrorMessages.ERROR;
import static com.planning_system.services.messages.ServiceMessages.GET_ALL_REJECTED_TASKS;
import static com.planning_system.services.messages.ServiceMessages.GET_ALL_TASKS;
import static com.planning_system.services.messages.ServiceMessages.GET_TASK;
import static com.planning_system.services.messages.ServiceMessages.NO_TASKS_YET;
import static com.planning_system.services.messages.ServiceMessages.TASK_CREATED;
import static com.planning_system.services.messages.ServiceMessages.TASK_DELETED;
import static com.planning_system.services.messages.ServiceMessages.TASK_UPDATED;
import static com.planning_system.services.utility.TaskUtil.parseId;

/**
 * The class contains main business logic, responsible for {@link Task} processing and building {@link Response}.
 */
public class TaskCommandService {

    private final TaskService taskService;
    private final RejectedTaskService rejectedTaskService;

    public TaskCommandService(final TaskService taskService,
                              final RejectedTaskService rejectedTaskService) {
        this.taskService = taskService;
        this.rejectedTaskService = rejectedTaskService;
    }

    /**
     * Method creates a new {@link Task}
     * @throws RuntimeException if the {@link Command} was not processed successfully
     * @param task {@link Task} that will be created.
     * @return {@link Response} that contains created Task and Message
     */
    public Response<Task> createTask(Task task) {

        Task taskResult = taskService.createTask(task);
        return Response.<Task>builder()
                       .message(TASK_CREATED)
                       .data(taskResult)
                       .build();
    }

    /**
     * Method returns all the {@link Task} that exist in the system. The returned Tasks can be sorted by date, status,
     * priority or id. By default, sorted by ID. It is possible to use reverse sort.
     * @throws RuntimeException if the options were not processed successfully.
     * @param options contain options with sorted type and/or rejected tasks request.
     * @return {@link Response} that contains Tasks and Message
     */
    public Response<List<Task>> getAllTask(Map<OptionType, String> options) {
        Comparator<Task> comp = TaskUtil.getTaskComparatorFromOptions(options);

        if (!Objects.isNull(options.get(REJECTED_TASK))) {
            if (!options.get(REJECTED_TASK).equals(Boolean.TRUE.toString())) {
                throw new RuntimeException(ERROR);
            } else {
                List<Task> allRejectedTasks = rejectedTaskService.getAllRejectedTasks()
                                                                 .stream()
                                                                 .sorted(comp)
                                                                 .collect(Collectors.toList());

                if (allRejectedTasks.isEmpty()) {
                    return Response.<List<Task>>builder()
                            .message(NO_TASKS_YET)
                            .build();
                }
                return Response.<List<Task>>builder()
                               .message(GET_ALL_REJECTED_TASKS)
                               .data(allRejectedTasks)
                               .build();
            }
        } else {
            List<Task> allTasks = taskService.getAllTasks()
                                             .stream()
                                             .sorted(comp)
                                             .collect(Collectors.toList());
            if (allTasks.isEmpty()) {
                return Response.<List<Task>>builder()
                               .message(NO_TASKS_YET)
                               .build();
            }
            return Response.<List<Task>>builder()
                           .message(GET_ALL_TASKS)
                           .data(allTasks)
                           .build();
        }
    }

    /**
     * Method returns the {@link Task} by id
     * @throws RuntimeException if the Task was not found or the id was not recognized
     * @param id requested Task id.
     * @return {@link Response} that contains Task and Message
     */
    public Response<Task> getTask(String id) {
        int correctId = parseId(id);
        Task task = taskService.getTask(correctId);

        return Response.<Task>builder()
                        .message(GET_TASK)
                        .data(task)
                        .build();
    }

    /**
     * Method updates the {@link Task}
     * @throws RuntimeException if user passes invalid data. The possible reasons for errors are
     * unrecognized id and/or invalid fields to be updated. By design user can update only description or status.
     * @param task {@link Task} that contains id and fields to be updated.
     * @return {@link Response} that contains updated Task and Message
     */
    public Response<Task> updateTask(Task task) {
        return Response.<Task>builder()
                        .message(TASK_UPDATED)
                        .data(taskService.updateTask(task))
                        .build();

    }

    /**
     * Method deletes {@link Task}
     * @throws RuntimeException if the Task was not found or the id was not parsed successfully, e.g. id was not a number
     * @param id Task id to be deleted
     * @return {@link Response} that contains message and deleted Task
     */
    public Response<Task> deleteTask(String id) {
        int correctId = parseId(id);

        return Response.<Task>builder()
                       .message(TASK_DELETED)
                       .data(taskService.deleteTask(correctId))
                       .build();
    }
}