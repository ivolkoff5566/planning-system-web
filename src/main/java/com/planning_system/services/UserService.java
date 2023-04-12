package com.planning_system.services;

import com.planning_system.entity.user.User;
import com.planning_system.repository.UserRepository;
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

    private final UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (Objects.isNull(user.getUserName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_USER_NAME_ENTERED);
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(int id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
    }

    public User updateUser(int id, User user) {
        User userToUpdate = getUser(id);
        if (Objects.nonNull(user.getUserName())) {
            userToUpdate.setUserName(user.getUserName());
        }

        return userRepository.save(userToUpdate);
    }

    public User deleteUser(int id) {
        User user = getUser(id);
        user.getTasks().forEach(t -> t.setUser(null));
        userRepository.delete(user);
        return user;
    }
}