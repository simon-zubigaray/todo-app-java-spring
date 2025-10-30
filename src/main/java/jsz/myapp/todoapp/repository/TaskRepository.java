package jsz.myapp.todoapp.repository;

import jsz.myapp.todoapp.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByUserUsername(String username);
}

