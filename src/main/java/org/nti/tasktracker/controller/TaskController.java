package org.nti.tasktracker;

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
    public ResponseEntity<Task> createTask(@RequestBody TaskDto taskDto) {
        Task savedTask = taskService.createTask(taskDto);

        URI location = URI.create("/api/tasks/" + savedTask.getId());

        return ResponseEntity.created(location).body(savedTask);
    }

    @GetMapping
    public List<Task> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size) {
        return taskService.getAllTasks(page, size);
    }

    @GetMapping("/{completed}")
    public ResponseEntity<List<Task>> getCompletedTasks(@RequestParam boolean completed){
        return ResponseEntity.ok().body(taskService.getCompletedTasks(completed));
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
