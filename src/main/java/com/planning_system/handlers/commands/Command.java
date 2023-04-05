package com.planning_system.handlers.commands;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class Command {
    @NonNull
    CommandType type;
    Map<OptionType, String> options;
}