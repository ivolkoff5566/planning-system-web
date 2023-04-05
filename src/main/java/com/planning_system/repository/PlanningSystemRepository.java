package com.planning_system.repository;

import java.util.List;

public interface PlanningSystemRepository<T, ID> {
    T save(T entity);
    T getById(ID id);
    List<T> getAll();
    T update(T entity);
    T delete(ID id);
}
