package com.planning_system.services.utility;

import com.planning_system.entity.Task;
import com.planning_system.entity.TaskPriority;
import com.planning_system.entity.TaskStatus;
import com.planning_system.handlers.commands.OptionType;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

import static com.planning_system.handlers.commands.OptionType.ID;
import static com.planning_system.handlers.commands.OptionType.REVERSE_SORT;
import static com.planning_system.handlers.commands.OptionType.SORTED;
import static com.planning_system.services.messages.ServiceErrorMessages.ID_NOT_NUMBER;
import static com.planning_system.services.messages.ServiceErrorMessages.INVALID_PRIORITY;
import static com.planning_system.services.messages.ServiceErrorMessages.INVALID_SORTING_TYPE;
import static com.planning_system.services.messages.ServiceErrorMessages.INVALID_STATUS;

public class TaskUtil {

    public static Comparator<Task> getTaskComparatorFromOptions(Map<OptionType, String> options) {
        Comparator<Task> comp;
        if (!options.isEmpty() && !Objects.isNull(options.get(SORTED))) {
            String reverseSorted = options.get(REVERSE_SORT);
            if (!Objects.isNull(reverseSorted) && reverseSorted.equals(Boolean.TRUE.toString())) {
                comp = getComparator(options.get(SORTED)).reversed();
            } else {
                comp = getComparator(options.get(SORTED));
            }
        } else {
            String reverseSorted = options.get(REVERSE_SORT);
            if (!Objects.isNull(reverseSorted) && reverseSorted.equals(Boolean.TRUE.toString())) {
                comp = getComparator(ID.getOptionName()).reversed();
            } else {
                comp = getComparator(ID.getOptionName());
            }
        }

        return comp;
    }

    private static Comparator<Task> getComparator(String field) {
        Comparator<Task> comparator;
        switch (field) {
            case "date":
                comparator = Comparator.comparing(Task::getDate);
                break;
            case "priority":
                comparator = Comparator.comparing(task -> task.getPriority().getValue());
                break;
            case "status":
                comparator = Comparator.comparing(task -> task.getStatus().getValue());
                break;
            case "id":
                comparator = Comparator.comparing(Task::getId);
                break;
            default:
                throw new RuntimeException(INVALID_SORTING_TYPE);
        }

        return comparator;
    }

    public static int parseId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (Exception ex) {
            throw new RuntimeException(ID_NOT_NUMBER);
        }
    }

    public static TaskPriority stringToTaskPriority(String input) {
        try {
            return TaskPriority.valueOf(input.toUpperCase());
        } catch (Exception ex) {
            throw new RuntimeException(INVALID_PRIORITY);
        }
    }

    public static TaskStatus stringToTaskStatus(String input) {
        try {
            return TaskStatus.valueOf(input.toUpperCase());
        } catch (Exception ex) {
            throw new RuntimeException(INVALID_STATUS);
        }
    }
}