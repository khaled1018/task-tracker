package org.nti.tasktracker.controller;

import jakarta.validation.Valid;
import org.nti.tasktracker.entity.Task;
import org.nti.tasktracker.dto.TaskDto;
import org.nti.tasktracker.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody @Valid TaskDto taskDto) {
        Task savedTask = taskService.createTask(taskDto);

        URI location = URI.create("/api/tasks/" + savedTask.getId());

        return ResponseEntity.created(location).body(savedTask);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Boolean completed) {

        if(completed != null){
            return ResponseEntity.ok().body(taskService.getCompletedTasks(completed));

        }
        return ResponseEntity.ok().body(taskService.getAllTasks(page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id){
        return ResponseEntity.ok().body(taskService.getTask(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto){
        return  ResponseEntity.ok().body(taskService.updateTask(id, taskDto));
    }

    @PatchMapping("/{id}/completed")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable Long id){
        return ResponseEntity.ok().body(taskService.markTaskAsCompleted(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
