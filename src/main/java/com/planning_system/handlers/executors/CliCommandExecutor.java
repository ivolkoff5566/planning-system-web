package com.planning_system.handlers.executors;

import com.planning_system.entity.Task;
import com.planning_system.handlers.commands.Command;
import com.planning_system.handlers.response.Response;
import com.planning_system.services.TaskCommandService;
import com.planning_system.services.utility.OptionsToTaskMapper;

import java.util.Optional;

import static com.planning_system.handlers.commands.OptionType.ID;
import static com.planning_system.handlers.messages.CliMessages.BYE;
import static com.planning_system.handlers.messages.CliMessages.ERROR;
import static com.planning_system.handlers.messages.CliMessages.INFO;

/**
 * The class responsible for executing commands by implementing the executeCommand method.
 * Also contains the exit method for terminating the program.
 */
public class CliCommandExecutor implements CliExecutor<Command, Optional<Response<?>>> {

    private final TaskCommandService taskCommandService;
    private final OptionsToTaskMapper mapper;

    public CliCommandExecutor(final TaskCommandService taskCommandService,
                              final OptionsToTaskMapper mapper) {
        this.taskCommandService = taskCommandService;
        this.mapper = mapper;
    }

    /**
     * Method responsible for executing a {@link Command} received from user, works by invoking methods from the
     * {@link TaskCommandService}
     *
     * @param command {@link Command} to execute
     * @return Optional of {@link Response}, sometimes may produce an empty Optional
     * @throws RuntimeException if the {@link Command} was not processed successfully
     */
    @Override
    public Optional<Response<?>> executeCommand(Command command) {
        switch (command.getType()) {
            case CREATE:
                Task taskToCreate = mapper.mapToTask(command.getOptions());
                return Optional.of(taskCommandService.createTask(taskToCreate));
            case GET:
                return Optional.of(taskCommandService.getTask(command.getOptions().get(ID)));
            case GET_ALL:
                return Optional.of(taskCommandService.getAllTask(command.getOptions()));
            case DELETE:
                return Optional.of(taskCommandService.deleteTask(command.getOptions().get(ID)));
            case UPDATE:
                Task taskToUpdate = mapper.mapToTask(command.getOptions());
                return Optional.of(taskCommandService.updateTask(taskToUpdate));
            case HELP:
                return Optional.of(Response.builder().message(INFO).build());
            case EXIT:
                exit();
                return Optional.empty();
        }
        throw new RuntimeException(ERROR);
    }

    /**
     * The method responsible for terminating the program.
     * Also prints the Goodbye message
     */
    @Override
    public void exit() {
        System.out.print(BYE);
        System.exit(0);
    }
}