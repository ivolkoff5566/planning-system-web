package com.planning_system.controller.user_statistics;

import com.planning_system.entity.user_statistics.UserStatistics;
import com.planning_system.services.UserStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-stat")
public class UserStatisticsController {

    private final UserStatisticsService userStatisticsService;

    @GetMapping("/{id}")
    public UserStatistics getUserStatByUserId(@PathVariable int id) {
        return userStatisticsService.getUserStatisticsByUserId(id);
    }
}