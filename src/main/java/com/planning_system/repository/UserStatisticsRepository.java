package com.planning_system.repository;

import com.planning_system.entity.user_statistics.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Integer> {
    UserStatistics findByUserId(int userId);
}
