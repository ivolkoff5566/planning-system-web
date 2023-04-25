package com.planning_system.services;

import com.planning_system.controller.user.dto.BasicUserResponseDTO;
import com.planning_system.entity.user.User;
import com.planning_system.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

import static com.planning_system.services.messages.ServiceErrorMessages.NO_USER_NAME_ENTERED;
import static com.planning_system.services.messages.ServiceErrorMessages.USER_NOT_FOUND;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (Objects.isNull(user.getUserName())) {
            LOGGER.error(NO_USER_NAME_ENTERED);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_USER_NAME_ENTERED);
        }
        LOGGER.info("Creating new user: {}", user);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(int id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
    }

    public BasicUserResponseDTO getBasicUser(int id) {
        var user = getUser(id);
        return BasicUserResponseDTO.builder()
                                   .id(user.getId())
                                   .userName(user.getUserName())
                                   .build();
    }

    public User updateUser(int id, User user) {
        var userToUpdate = getUser(id);
        if (Objects.nonNull(user.getUserName())) {
            userToUpdate.setUserName(user.getUserName());
        }

        return userRepository.save(userToUpdate);
    }

    public User deleteUser(int id) {
        var user = getUser(id);
        user.getTasks().forEach(t -> t.setUser(null));
        userRepository.delete(user);
        return user;
    }
}