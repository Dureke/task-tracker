package util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import tracker.util.Status;
import tracker.util.Task;
import java.time.LocalDateTime;

class TaskTest {

    @BeforeAll
    static void setupOnce() {
        Task.resetIdCounter();
    }

    @Test
    void testGetId() {
        Task task = new Task();
        assertEquals(1, task.getId());
        
        task = new Task("Test Task");
        assertEquals(2, task.getId());
        
        task = new Task(3, "Test Task", Status.TODO, LocalDateTime.now(), LocalDateTime.now());
        assertEquals(3, task.getId());
    }

    @Test
    void testGetDescription() {
        Task task = new Task("Test Task Description");
        assertEquals("Test Task Description", task.getDescription());

        task.setDescription("Updated Task Description");
        assertEquals("Updated Task Description", task.getDescription());

        Task task2 = new Task(4, "Another Task", Status.TODO, LocalDateTime.now(), LocalDateTime.now());
        assertEquals("Another Task", task2.getDescription());
    }

    @Test
    void testGetStatus() {
        Task task = new Task("Test Task Description");
        assertEquals(Status.TODO, task.getStatus());

        task.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, task.getStatus());

        Task task2 = new Task(4, "Another Task", Status.TODO, LocalDateTime.now(), LocalDateTime.now());
        assertEquals(Status.TODO, task2.getStatus());
    }

    @Test
    void testGetCreatedAt() {
        Task task = new Task("Test Task Description");
        assertEquals(LocalDateTime.now(), task.getCreatedAt());

        Task task2 = new Task(4, "Another Task", Status.TODO, LocalDateTime.now(), LocalDateTime.now());
        assertEquals(LocalDateTime.now(), task2.getCreatedAt());
    }

    @Test
    void testGetUpdatedAt() {
        Task task = new Task("Test Task Description");
        assertEquals(LocalDateTime.now(), task.getUpdatedAt());

        task.setStatus(Status.DONE);
        assertEquals(LocalDateTime.now(), task.getUpdatedAt());

        Task task2 = new Task(4, "Another Task", Status.TODO, LocalDateTime.now(), LocalDateTime.now());
        assertEquals(LocalDateTime.now(), task2.getUpdatedAt());
        task2.setDescription("Updated Description");
        assertEquals(LocalDateTime.now(), task2.getUpdatedAt());
    }
}