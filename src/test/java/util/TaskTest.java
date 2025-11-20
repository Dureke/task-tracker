
package util;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import tracker.util.Status;
import tracker.util.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskTest {

    @Test
    void testGetId() {
        Task task = new Task();
        assertEquals(1, task.getId());
        
        task = new Task("Test Task", 2);
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
        Duration diff = Duration.between(task.getCreatedAt(), LocalDateTime.now()).abs();
        assertTrue(diff.toMillis() < 1000);

        Task task2 = new Task(4, "Another Task", Status.TODO, LocalDateTime.now(), LocalDateTime.now());
        diff = Duration.between(task2.getCreatedAt(), LocalDateTime.now()).abs();
        assertTrue(diff.toMillis() < 1000);
    }

    @Test
    void testGetUpdatedAt() {
        Task task = new Task("Test Task Description");
        Duration diff = Duration.between(task.getUpdatedAt(), LocalDateTime.now()).abs();
        assertTrue(diff.toMillis() < 1000);

        task.setStatus(Status.DONE);
        diff = Duration.between(task.getUpdatedAt(), LocalDateTime.now()).abs();
        assertTrue(diff.toMillis() < 1000);

        Task task2 = new Task(4, "Another Task", Status.TODO, LocalDateTime.now(), LocalDateTime.now());
        diff = Duration.between(task2.getUpdatedAt(), LocalDateTime.now()).abs();
        assertTrue(diff.toMillis() < 1000);

        task2.setDescription("Updated Description");
        diff = Duration.between(task2.getUpdatedAt(), LocalDateTime.now()).abs();
        assertTrue(diff.toMillis() < 1000);
    }
}