package com.planning_system.services.utility;

import com.planning_system.entity.Task;
import com.planning_system.handlers.commands.OptionType;

import java.util.Map;
import java.util.Objects;

import static com.planning_system.handlers.commands.OptionType.DESCRIPTION;
import static com.planning_system.handlers.commands.OptionType.ID;
import static com.planning_system.handlers.commands.OptionType.NAME;
import static com.planning_system.handlers.commands.OptionType.PRIORITY;
import static com.planning_system.handlers.commands.OptionType.STATUS;
import static com.planning_system.services.utility.TaskUtil.parseId;
import static com.planning_system.services.utility.TaskUtil.stringToTaskPriority;
import static com.planning_system.services.utility.TaskUtil.stringToTaskStatus;

/**
 * The class implements TaskMapper interfaces to allow mapping <Map<OptionType, String> options to Task.
 * Inserts null values if nothing was passed to the options.
 */
public class OptionsToTaskMapper implements TaskMapper<Map<OptionType, String>> {

    public Task mapToTask(Map<OptionType, String> options) {
        var builder = Task.builder();

        if (Objects.nonNull(options.get(ID))) {
            builder.id(parseId(options.get(ID)));
        }
        if (Objects.nonNull(options.get(NAME))) {
            builder.name(options.get(NAME));
        }
        if (Objects.nonNull(options.get(DESCRIPTION))) {
            builder.description(options.get(DESCRIPTION));
        }
        if (Objects.nonNull(options.get(STATUS))) {
            builder.status(stringToTaskStatus(options.get(STATUS)));
        }
        if (Objects.nonNull(options.get(PRIORITY))) {
            builder.priority(stringToTaskPriority(options.get(PRIORITY)));
        }

        return builder.build();
    }
}