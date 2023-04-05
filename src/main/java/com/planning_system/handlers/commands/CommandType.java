package com.planning_system.handlers.commands;

public enum CommandType {
    CREATE("create"),
    GET("get"),
    GET_ALL("get-all"),
    UPDATE("update"),
    DELETE("delete"),
    HELP("help"),
    EXIT("exit");

    private final String typeName;

    CommandType(String typeName) {
        this.typeName = typeName;
    }

    public static CommandType getCommandTypeFromString(String name) {
        for (CommandType instance : CommandType.values()) {
            if (instance.getTypeName().equals(name)) {
                return instance;
            }
        }
        throw new IllegalArgumentException(String.format("Invalid string value for enum CommandType: %s", name));
    }

    public String getTypeName() {
        return this.typeName;
    }
}