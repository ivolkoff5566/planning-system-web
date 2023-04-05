package com.planning_system.handlers;

import com.planning_system.config.ApplicationConfig;
import com.planning_system.handlers.executors.CliCommandExecutor;
import com.planning_system.handlers.messages.CliMessagePrinter;
import com.planning_system.repository.RejectedTaskRepository;
import com.planning_system.repository.TaskRepository;
import com.planning_system.services.RejectedTaskService;
import com.planning_system.services.TaskCommandService;
import com.planning_system.services.TaskService;
import com.planning_system.services.utility.OptionsToTaskMapper;

public class CommandHandlerFactory {

    public static CommandHandler getHandler(ApplicationConfig config) {
        CommandHandler handler = null;

        if (config.getTaskHandlerType().equals("cli")) {
            var cliMessagePrinter = new CliMessagePrinter();
            var parser = new CliCommandParser();

            var taskService = new TaskService(TaskRepository.getInstance());
            var rejectedTaskService = new RejectedTaskService(RejectedTaskRepository.getInstance());

            var mapper = new OptionsToTaskMapper();
            var taskCommandService = new TaskCommandService(taskService, rejectedTaskService);

            var cliCommandExecutor = new CliCommandExecutor(taskCommandService, mapper);

            return new CliCommandHandler(cliMessagePrinter, parser, cliCommandExecutor);
        }

        return handler;
    }
}