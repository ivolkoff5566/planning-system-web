package com.planning_system.handlers;

import com.planning_system.entity.Task;
import com.planning_system.entity.TaskPriority;
import com.planning_system.entity.TaskStatus;
import com.planning_system.handlers.commands.OptionType;
import com.planning_system.services.utility.OptionsToTaskMapper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.planning_system.handlers.commands.OptionType.DESCRIPTION;
import static com.planning_system.handlers.commands.OptionType.ID;
import static com.planning_system.handlers.commands.OptionType.NAME;
import static com.planning_system.handlers.commands.OptionType.PRIORITY;
import static com.planning_system.handlers.commands.OptionType.STATUS;
import static com.planning_system.services.messages.ServiceErrorMessages.ID_NOT_NUMBER;
import static com.planning_system.services.messages.ServiceErrorMessages.INVALID_PRIORITY;
import static com.planning_system.services.messages.ServiceErrorMessages.INVALID_STATUS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OptionsToTaskMapperTests {

    private final OptionsToTaskMapper mapper = new OptionsToTaskMapper();

    @Test
    public void mapOptionsToTaskTest() {
        String id = "1";
        String name = "t1";
        String desc = "task desc";
        String status = "ready";
        String priority = "high";
        Map<OptionType, String> options = new HashMap<>();
        options.put(ID, id);
        options.put(NAME, name);
        options.put(DESCRIPTION, desc);
        options.put(STATUS, status);
        options.put(PRIORITY, priority);

        Task resultTask = mapper.mapToTask(options);

        assertEquals(1, resultTask.getId());
        assertEquals(name, resultTask.getName());
        assertEquals(desc, resultTask.getDescription());
        assertEquals(TaskStatus.READY, resultTask.getStatus());
        assertEquals(TaskPriority.HIGH, resultTask.getPriority());
    }

    @Test
    public void mapOptionsToTaskInvalidIdTest() {
        String id = "x";
        String name = "t1";
        Map<OptionType, String> options = new HashMap<>();
        options.put(ID, id);
        options.put(NAME, name);

        Exception exception = assertThrows(RuntimeException.class,
                                            () -> mapper.mapToTask(options));
        assertEquals(ID_NOT_NUMBER, exception.getMessage());
    }

    @Test
    public void mapOptionsToTaskInvalidStatusTest() {
        String status = "invalid_status";
        Map<OptionType, String> options = new HashMap<>();
        options.put(STATUS, status);

        Exception exception = assertThrows(RuntimeException.class,
                () -> mapper.mapToTask(options));
        assertEquals(INVALID_STATUS, exception.getMessage());
    }

    @Test
    public void mapOptionsToTaskInvalidPriorityTest() {
        String priority = "invalid_priority";
        Map<OptionType, String> options = new HashMap<>();
        options.put(PRIORITY, priority);

        Exception exception = assertThrows(RuntimeException.class,
                () -> mapper.mapToTask(options));
        assertEquals(INVALID_PRIORITY, exception.getMessage());
    }

    @Test
    public void mapOptionsToTaskEmptyOptionsTest() {
        Map<OptionType, String> options = new HashMap<>();

        Task expectedTask = Task.builder().build();
        Task task =  mapper.mapToTask(options);

        assertEquals(expectedTask, task);
    }
}