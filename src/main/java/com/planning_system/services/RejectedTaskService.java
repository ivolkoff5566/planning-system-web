package com.planning_system.services;

import com.planning_system.entity.Task;
import com.planning_system.repository.RejectedTaskRepository;

import java.util.List;

public class RejectedTaskService {

    private final RejectedTaskRepository repository;

    public RejectedTaskService(RejectedTaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getAllRejectedTasks() {
        return repository.getAll();
    }
}