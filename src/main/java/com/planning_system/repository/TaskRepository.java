package com.planning_system.repository;

import com.planning_system.entity.Task;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.planning_system.entity.TaskStatus.TODO;

public class TaskRepository implements PlanningSystemRepository<Task, Integer>{

    private static int counter = 1;
    private static final TaskRepository INSTANCE = new TaskRepository();

    private final ConcurrentHashMap<Integer, Task> tasks = new ConcurrentHashMap<>();

    private TaskRepository() {}

    public static TaskRepository getInstance() {
        return INSTANCE;
    }

    public List<Task> getAllTasksPastDue(final long pastDueTime) {
        return tasks.values()
                    .stream()
                    .filter(task -> Instant.now().getEpochSecond() -
                            task.getDate().getEpochSecond() >= pastDueTime &&
                            task.getStatus().equals(TODO))
                    .collect(Collectors.toList());
    }

    public int getIncrementedId() {
        return counter++;
    }

    @Override
    public Task save(Task task) {
        tasks.put(task.getId(), task);
        return getById(task.getId());
    }

    @Override
    public Task getById(Integer id) {
        return tasks.get(id);
    }

    @Override
    public List<Task> getAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task update(Task task) {
        tasks.put(task.getId(), task);
        return getById(task.getId());
    }

    @Override
    public Task delete(Integer id) {
        return tasks.remove(id);
    }
}