package com.planning_system.controller.user;

import com.planning_system.entity.user.User;
import com.planning_system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createTask(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id,
                            @RequestParam(required = false) boolean includeTask) {
        if (includeTask) {
            return userService.getUser(id);
        }
        var basicUserResponseDTO = userService.getBasicUser(id);
        return User.builder()
                   .id(basicUserResponseDTO.getId())
                   .userName(basicUserResponseDTO.getUserName())
                   .build();
    }

    @PutMapping("/{id}")
    public User updateTask(@PathVariable int id,
                           @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public User deleteTask(@PathVariable int id) {
        return userService.deleteUser(id);
    }
}