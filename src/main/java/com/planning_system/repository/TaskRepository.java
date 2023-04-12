package com.planning_system.repository;

import com.planning_system.entity.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.planning_system.entity.task.TaskStatus.TODO;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    default List<Task> getAllTasksPastDue(final long pastDueTime) {
        return findAll()
                .stream()
                .filter(task -> Instant.now().getEpochSecond() -
                        task.getDate().getEpochSecond() >= pastDueTime &&
                        task.getStatus().equals(TODO))
                .collect(Collectors.toList());
    }
}