package com.planning_system.entity;

public enum TaskPriority {
    HIGH(1),
    LOW(2);

    private final int value;

    TaskPriority(int priority) {
        this.value = priority;
    }

    public int getValue() {
        return this.value;
    }
}
