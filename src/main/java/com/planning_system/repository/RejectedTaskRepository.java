package com.planning_system.repository;

import com.planning_system.entity.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RejectedTaskRepository implements PlanningSystemRepository<Task, Integer> {

    private static final RejectedTaskRepository INSTANCE = new RejectedTaskRepository();
    private final ConcurrentHashMap<Integer, Task> rejectedTasks = new ConcurrentHashMap<>();

    private RejectedTaskRepository() {}

    public static RejectedTaskRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Task save(Task task) {
        rejectedTasks.put(task.getId(), task);
        return getById(task.getId());
    }

    @Override
    public Task getById(Integer id) {
        return rejectedTasks.get(id);
    }

    @Override
    public List<Task> getAll() {
        return new ArrayList<>(rejectedTasks.values());
    }

    @Override
    public Task update(Task task) {
        rejectedTasks.put(task.getId(), task);
        return getById(task.getId());
    }

    @Override
    public Task delete(Integer id) {
        return rejectedTasks.remove(id);
    }
}
