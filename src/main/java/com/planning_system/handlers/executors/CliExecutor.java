package com.planning_system.handlers.executors;

public interface CliExecutor<T,R> extends Executor<T, R> {
    void exit();
}
