package com.planning_system.services.utility;

import com.planning_system.entity.task.Task;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

import static com.planning_system.controller.task.TaskParams.ID;
import static com.planning_system.controller.task.TaskParams.REVERSE_SORT;
import static com.planning_system.controller.task.TaskParams.SORTED_BY;
import static com.planning_system.services.messages.ServiceErrorMessages.INVALID_SORTING_TYPE;

public class TaskUtil {

    public static Comparator<Task> getTaskComparatorFromParams(Map<String, String> params) {
        Comparator<Task> comp;
        if (!params.isEmpty() && !Objects.isNull(params.get(SORTED_BY))) {
            String reverseSorted = params.get(REVERSE_SORT);
            if (!Objects.isNull(reverseSorted) && reverseSorted.equals(Boolean.TRUE.toString())) {
                comp = getComparator(params.get(SORTED_BY)).reversed();
            } else {
                comp = getComparator(params.get(SORTED_BY));
            }
        } else {
            String reverseSorted = params.get(REVERSE_SORT);
            if (!Objects.isNull(reverseSorted) && reverseSorted.equals(Boolean.TRUE.toString())) {
                comp = getComparator(ID).reversed();
            } else {
                comp = getComparator(ID);
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
}