package com.planning_system.services;

import com.planning_system.entity.user_statistics.UserStatistics;
import com.planning_system.repository.UserStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static com.planning_system.services.messages.ServiceErrorMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserStatisticsService {

    private final UserStatisticsRepository userStatisticsRepository;

    public UserStatistics getUserStatisticsByUserId(int userId) {
        var stat = userStatisticsRepository.findByUserId(userId);
        if (Objects.isNull(stat)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND ,USER_NOT_FOUND);
        }
        return stat;
    }
}