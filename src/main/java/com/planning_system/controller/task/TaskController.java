package com.planning_system.controller.task;

import com.planning_system.controller.task.dto.TaskRequestDTO;
import com.planning_system.controller.task.dto.TaskResponseDTO;
import com.planning_system.services.TaskCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskCommandService taskCommandService;

    @PostMapping
    public TaskResponseDTO createTask(@RequestBody TaskRequestDTO taskRequestDTO) {
        return taskCommandService.createTask(taskRequestDTO);
    }


    @GetMapping("/{id}")
    public TaskResponseDTO getTaskById(@PathVariable int id) {
        return taskCommandService.getTask(id);
    }

    @GetMapping
    public List<TaskResponseDTO> getAllTasks(@RequestParam(required = false) Map<String, String> params) {
        return taskCommandService.getAllTasks(params);
    }

    @PutMapping("/{id}")
    public TaskResponseDTO updateTask(@PathVariable int id,
                                      @RequestBody TaskRequestDTO taskRequestDTO) {
        return taskCommandService.updateTask(id, taskRequestDTO);
    }

    @PutMapping("/{taskId}/assign/{userId}")
    public TaskResponseDTO assignTaskToUser(@PathVariable int taskId,
                                            @PathVariable int userId) {
        return taskCommandService.assignTaskToUser(taskId, userId);
    }

    @PutMapping("/{taskId}/unassign")
    public TaskResponseDTO removeUserAssignment(@PathVariable int taskId) {
        return taskCommandService.removeUserAssignment(taskId);
    }

    @DeleteMapping("/{id}")
    public TaskResponseDTO deleteTask(@PathVariable int id) {
        return taskCommandService.deleteTask(id);
    }
}