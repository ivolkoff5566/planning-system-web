package com.planning_system.handlers.executors;

public interface Executor<T,R> {
    R executeCommand(T command);
}
