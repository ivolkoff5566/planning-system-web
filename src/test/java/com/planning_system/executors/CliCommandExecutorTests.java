package com.planning_system.executors;

import com.planning_system.entity.Task;
import com.planning_system.handlers.commands.Command;
import com.planning_system.handlers.commands.CommandType;
import com.planning_system.handlers.commands.OptionType;
import com.planning_system.handlers.executors.CliCommandExecutor;
import com.planning_system.handlers.response.Response;
import com.planning_system.services.TaskCommandService;
import com.planning_system.services.utility.OptionsToTaskMapper;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static com.planning_system.handlers.messages.CliMessages.BYE;
import static com.planning_system.handlers.messages.CliMessages.INFO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class CliCommandExecutorTests {

    private TaskCommandService taskCommandService = mock(TaskCommandService .class);
    private OptionsToTaskMapper mapper = mock(OptionsToTaskMapper .class);

    private CliCommandExecutor executor = new CliCommandExecutor(taskCommandService, mapper);


    @Test
    public void executeCreateCommandTest() {
        Command command = Command.builder()
                                 .type(CommandType.CREATE)
                                 .options(Map.of(OptionType.NAME, "t1"))
                                 .build();

        Task task = Task.builder().build();
        when(mapper.mapToTask(anyMap())).thenReturn(task);
        when(taskCommandService.createTask(task)).thenReturn(Response.<Task>builder().data(task).build());

        Optional<Response<?>> result = executor.executeCommand(command);

        verify(mapper, times(1)).mapToTask(anyMap());
        verify(taskCommandService, times(1)).createTask(task);
        assertTrue(result.isPresent());
        verifyNoMoreInteractions(taskCommandService);
    }

    @Test
    public void executeGetCommandTest() {
        Command command = Command.builder()
                .type(CommandType.GET)
                .options(Map.of(OptionType.ID, "1"))
                .build();

        Task task = Task.builder().build();
        when(taskCommandService.getTask("1")).thenReturn(Response.<Task>builder().data(task).build());

        Optional<Response<?>> result = executor.executeCommand(command);

        verify(taskCommandService, times(1)).getTask("1");
        verifyNoMoreInteractions(taskCommandService);
        assertTrue(result.isPresent());
    }

    @Test
    public void executeGetAllCommandTest() {
        Command command = Command.builder()
                                 .type(CommandType.GET_ALL)
                                 .options(Map.of())
                                 .build();

        List<Task> taskList = Collections.singletonList(mock(Task.class));
        when(taskCommandService.getAllTask(Map.of())).thenReturn(Response.<List<Task>>builder().data(taskList).build());

        Optional<Response<?>> result = executor.executeCommand(command);

        verify(taskCommandService, times(1)).getAllTask(Map.of());
        verifyNoMoreInteractions(taskCommandService);
        assertTrue(result.isPresent());
    }

    @Test
    public void executeDeleteCommandTest() {
        Command command = Command.builder()
                                 .type(CommandType.DELETE                 )
                                 .options(Map.of(OptionType.ID, "1"))
                                 .build();

        Task task = Task.builder().build();
        when(taskCommandService.deleteTask("1")).thenReturn(Response.<Task>builder().data(task).build());

        Optional<Response<?>> result = executor.executeCommand(command);

        verify(taskCommandService, times(1)).deleteTask("1");
        verifyNoMoreInteractions(taskCommandService);
        assertTrue(result.isPresent());
    }

    @Test
    public void executeUpdateCommandTest() {
        Command command = Command.builder()
                                 .type(CommandType.UPDATE)
                                 .options(Map.of(OptionType.ID, "1"))
                                 .build();

        Task task = Task.builder().build();
        when(mapper.mapToTask(anyMap())).thenReturn(task);
        when(taskCommandService.updateTask(task)).thenReturn(Response.<Task>builder().data(task).build());

        Optional<Response<?>> result = executor.executeCommand(command);

        verify(mapper, times(1)).mapToTask(anyMap());
        verify(taskCommandService, times(1)).updateTask(task);
        assertTrue(result.isPresent());
        verifyNoMoreInteractions(taskCommandService);
    }

    @Test
    public void executeHelpCommandTest() {
        Command command = Command.builder()
                                 .type(CommandType.HELP)
                                 .options(Map.of())
                                 .build();
        Response<?> expectedResponse = Response.builder().message(INFO).build();
        Optional<Response<?>> result = executor.executeCommand(command);

        assertTrue(result.isPresent());
        assertEquals(expectedResponse, result.get());
        verifyNoMoreInteractions(taskCommandService);
    }

    @Test
    public void executeExitCommandTest() throws Exception {
        Command command = Command.builder()
                .type(CommandType.EXIT)
                .options(Map.of())
                .build();
        String text = tapSystemOut(() -> {
            int statusCode = catchSystemExit(() -> executor.executeCommand(command));
            assertEquals(0, statusCode);
        });
        assertEquals(BYE, text);
    }
}