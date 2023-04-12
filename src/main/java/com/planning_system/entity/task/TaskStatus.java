package com.planning_system.entity.task;

public enum TaskStatus {
    TODO(1),
    READY(2),
    IN_PROGRESS(3),
    COMPLETED(4);

    private final int value;

    TaskStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}