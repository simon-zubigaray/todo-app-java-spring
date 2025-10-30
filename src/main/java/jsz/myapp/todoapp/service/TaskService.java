package jsz.myapp.todoapp.service;

import jsz.myapp.todoapp.controller.dto.TaskDTO;
import jsz.myapp.todoapp.model.TaskEntity;
import jsz.myapp.todoapp.model.UserEntity;
import jsz.myapp.todoapp.repository.TaskRepository;
import jsz.myapp.todoapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<TaskEntity> getAll() {
        return taskRepository.findAll();
    }

    public void save(TaskDTO taskDto, String username) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TaskEntity task = new TaskEntity(
                taskDto.getId(),
                taskDto.getTitle(),
                taskDto.getDescription(),
                taskDto.isCompleted(),
                user
        );

        taskRepository.save(task);
    }

    public Optional<TaskDTO> findById(Long id) {
        Optional<TaskEntity> task = taskRepository.findById(id);

        return Optional.of(new TaskDTO(
                task.get().getId(),
                task.get().getTitle(),
                task.get().getDescription(),
                task.get().isCompleted()
        ));
    }

    public List<TaskDTO> findByUsername(String username) {
        List<TaskEntity> tasks = taskRepository.findByUserUsername(username);
        return tasks.stream()
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.isCompleted()
                ))
                .collect(Collectors.toList());
    }

    public TaskDTO updateTask(TaskDTO taskDTO, Long id) {
        TaskEntity task = taskRepository.findById(id).orElseThrow();

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setCompleted(taskDTO.isCompleted());

        TaskEntity updatedTask = taskRepository.save(task);

        return new TaskDTO(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.isCompleted()
        );
    }

    public TaskDTO deleteTask(Long id) {
        TaskEntity task = taskRepository.findById(id).orElseThrow();

        taskRepository.deleteById(id);

        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted()
        );
    }
}

