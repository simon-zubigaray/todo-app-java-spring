package jsz.myapp.todoapp.controller;

import jsz.myapp.todoapp.controller.dto.TaskDTO;
import jsz.myapp.todoapp.model.TaskEntity;
import jsz.myapp.todoapp.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    // Test 1: Verifica que el controlador devuelva correctamente la lista de tareas
    @Test
    void getAll_ShouldReturnListOfTasks() {
        List<TaskEntity> mockTasks = List.of(
                new TaskEntity(1L, "Task 1", "Desc 1", false, null),
                new TaskEntity(2L, "Task 2", "Desc 2", true, null)
        );

        when(taskService.getAll()).thenReturn(mockTasks);

        ResponseEntity<List<TaskEntity>> response = taskController.getAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockTasks, response.getBody());
        verify(taskService, times(1)).getAll();
    }

    // Test 2: Verifica que se guarde una tarea correctamente y que devuelva el mensaje esperado
    @Test
    void save_ShouldReturnSuccessMessage() {
        TaskDTO dto = new TaskDTO(1L, "Test", "Desc", false);
        Authentication auth = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);

        when(auth.getName()).thenReturn("testUser");
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        ResponseEntity<Map<String, String>> response = taskController.save(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Tarea creada con exito.", response.getBody().get("message"));
        verify(taskService).save(dto, "testUser");
    }

    // Test 3: Verifica que se obtenga correctamente una tarea por ID si existe
    @Test
    void findById_ShouldReturnTaskInfo_WhenFound() {
        TaskDTO dto = new TaskDTO(1L, "Title", "Desc", true);
        when(taskService.findById(1L)).thenReturn(Optional.of(dto));

        ResponseEntity<Map<String, String>> response = taskController.findById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Tarea obtenida con exito.", response.getBody().get("message"));
        assertEquals("1", response.getBody().get("id"));
        assertEquals("Title", response.getBody().get("title"));
        verify(taskService).findById(1L);
    }

    // Test 4: Verifica que si no se encuentra la tarea por ID, el mapa devuelto esté vacío
    @Test
    void findById_ShouldReturnEmptyMap_WhenNotFound() {
        when(taskService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, String>> response = taskController.findById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
        verify(taskService).findById(1L);
    }

    // Test 5: Verifica que se obtengan las tareas del usuario autenticado correctamente
    @Test
    void findByAuthenticatedUser_ShouldReturnList() {
        Authentication auth = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);

        when(auth.getName()).thenReturn("testUser");
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        List<TaskDTO> mockTasks = List.of(
                new TaskDTO(1L, "Task 1", "Desc 1", false),
                new TaskDTO(2L, "Task 2", "Desc 2", true)
        );
        when(taskService.findByUsername("testUser")).thenReturn(mockTasks);

        ResponseEntity<List<TaskDTO>> response = taskController.findByAuthenticatedUser();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockTasks, response.getBody());
        verify(taskService).findByUsername("testUser");
    }

    // Test 6: Verifica que la actualización de una tarea devuelva la información modificada correctamente
    @Test
    void updateTask_ShouldReturnUpdatedTask() {
        TaskDTO input = new TaskDTO(null, "Updated", "New Desc", true);
        TaskDTO updated = new TaskDTO(1L, "Updated", "New Desc", true);

        when(taskService.updateTask(input, 1L)).thenReturn(updated);

        ResponseEntity<Map<String, String>> response = taskController.updateTask(input, 1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Tarea modificada con exito.", response.getBody().get("message"));
        assertEquals("Updated", response.getBody().get("title"));
        verify(taskService).updateTask(input, 1L);
    }

    // Test 7: Verifica que al eliminar una tarea, el controlador devuelva los datos de la tarea eliminada
    @Test
    void deleteTask_ShouldReturnDeletedTaskInfo() {
        TaskDTO deleted = new TaskDTO(1L, "Deleted", "Desc", true);
        when(taskService.deleteTask(1L)).thenReturn(deleted);

        ResponseEntity<Map<String, String>> response = taskController.deleteTask(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Tarea modificada con exito.", response.getBody().get("message"));
        assertEquals("Deleted", response.getBody().get("title"));
        verify(taskService).deleteTask(1L);
    }
}