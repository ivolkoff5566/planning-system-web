package com.planning_system;

import com.planning_system.config.ApplicationConfig;
import com.planning_system.handlers.CommandHandlerFactory;
import com.planning_system.services.sync.Scheduler;

public class Application {
    public static void main(String[] args) {
        new Application().run(ApplicationConfig.getInstance());
    }

    private void run(ApplicationConfig config) {
        config.loadConfig("application.properties");
        Scheduler.createScheduler(config).load();

        CommandHandlerFactory.getHandler(config)
                             .start();
    }
}