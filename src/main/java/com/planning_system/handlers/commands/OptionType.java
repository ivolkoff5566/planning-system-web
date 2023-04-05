package com.planning_system.handlers.commands;

public enum OptionType {
    NAME("n"),
    DESCRIPTION("d"),
    STATUS("s"),
    PRIORITY("p"),
    REJECTED_TASK("rt"),
    ID("id"),
    SORTED("st"),
    REVERSE_SORT("rs");

    private final String optionTypeName;

    OptionType(String optionTypeName) {
        this.optionTypeName = optionTypeName;
    }

    public static OptionType getOptionTypeFromString(String name) {
        for (OptionType instance : OptionType.values()) {
            if (instance.getOptionName().equals(name)) {
                return instance;
            }
        }
        throw new IllegalArgumentException(String.format("Invalid string value for enum OptionType: %s", name));
    }

    public String getOptionName() {
        return this.optionTypeName;
    }
}