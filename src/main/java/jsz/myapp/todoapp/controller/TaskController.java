package jsz.myapp.todoapp.controller;

import jsz.myapp.todoapp.controller.dto.TaskDTO;
import jsz.myapp.todoapp.model.TaskEntity;
import jsz.myapp.todoapp.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskEntity>> getAll() {
        return ResponseEntity.ok(taskService.getAll());
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> save(@RequestBody TaskDTO task) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        taskService.save(task, username);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Tarea creada con exito.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> findById(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        Optional<TaskDTO> task = taskService.findById(id);

        if(task.isPresent()){
            response.put("message", "Tarea obtenida con exito.");
            response.put("id", String.valueOf(task.get().getId()));
            response.put("title", task.get().getTitle());
            response.put("description", task.get().getDescription());
            response.put("completed", String.valueOf(task.get().isCompleted()));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<TaskDTO>> findByAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        List<TaskDTO> tasks = taskService.findByUsername(username);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> updateTask(@RequestBody TaskDTO taskDTO, @PathVariable Long id){
        TaskDTO task = taskService.updateTask(taskDTO, id);

        return getMapResponseEntity(task);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable Long id) {
        TaskDTO task = taskService.deleteTask(id);

        return getMapResponseEntity(task);
    }

    private ResponseEntity<Map<String, String>> getMapResponseEntity(TaskDTO task) {
        Map<String, String> response = new HashMap<>();

        response.put("message", "Tarea modificada con exito.");
        response.put("id", String.valueOf(task.getId()));
        response.put("title", task.getTitle());
        response.put("description", task.getDescription());
        response.put("completed", String.valueOf(task.isCompleted()));

        return ResponseEntity.ok(response);
    }
}
