package util;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;

import tracker.util.Commands;
import tracker.util.Status;
import tracker.util.Task;
import tracker.util.TaskMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class TaskMapTest {
    static final String TEST_FILE_PATH = "src/test/resources/input/tasks.json";

    @BeforeEach
    void setup() {
        Task.resetIdCounter();
        try {
            Files.writeString(Paths.get(TEST_FILE_PATH), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testFromJSON() {
        String json = "{\"id\":1,\"description\":\"Task 1\",\"status\":\"TODO\",\"createdAt\":\"2022-01-01T00:00:00\",\"updatedAt\":\"2022-01-01T00:00:00\"}\r\n" + //
            "{\"id\":2,\"description\":\"Task 2\",\"status\":\"IN_PROGRESS\",\"createdAt\":\"2022-01-02T00:00:00\",\"updatedAt\":\"2022-01-02T00:00:00\"}\r\n";

        Map<Integer, Task> taskMap = TaskMap.fromJSON(json);

        assertEquals(2, taskMap.size());

        Task task1 = taskMap.get(1);
        assertEquals(1, task1.getId());
        assertEquals("Task 1", task1.getDescription());
        assertEquals(Status.TODO, task1.getStatus());
        assertEquals(LocalDateTime.parse("2022-01-01T00:00:00"), task1.getCreatedAt());
        assertEquals(LocalDateTime.parse("2022-01-01T00:00:00"), task1.getUpdatedAt());

        Task task2 = taskMap.get(2);
        assertEquals(2, task2.getId());
        assertEquals("Task 2", task2.getDescription());
        assertEquals(Status.IN_PROGRESS, task2.getStatus());
        assertEquals(LocalDateTime.parse("2022-01-02T00:00:00"), task2.getCreatedAt());
        assertEquals(LocalDateTime.parse("2022-01-02T00:00:00"), task2.getUpdatedAt());
    }

    @Test
    void testFromJSONEmpty() {
        String json = "{}";

        assertEquals(0, TaskMap.fromJSON(json).size());
    }

    @Test
    void testGetCurrentTasksEmpty() {
        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertEquals(0, taskMap.getCurrentTasks().size());
    }

    @Test
    void testGetCurrentTasks() {
        Commands.ADD.execute(TEST_FILE_PATH, "Task 1", null);
        Commands.ADD.execute(TEST_FILE_PATH, "Task 2", null);
        Commands.MARK_IN_PROGRESS.execute(TEST_FILE_PATH, "2", null);
        
        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        Map<Integer, Task> currentTasks = taskMap.getCurrentTasks();
        assertEquals(2, currentTasks.size());
        assertTrue(currentTasks.containsKey(1));
        assertTrue(currentTasks.containsKey(2));
    }

    @Test
    void testGetCurrentTasksFromFile() {
        Commands.ADD.execute(TEST_FILE_PATH, "Task 1", null);
        Commands.ADD.execute(TEST_FILE_PATH, "Task 2", null);
        Commands.MARK_IN_PROGRESS.execute(TEST_FILE_PATH, "2", null);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        Map<Integer, Task> currentTasks = taskMap.getCurrentTasks();
        assertEquals(2, currentTasks.size());

        Task grabbedTask1 = currentTasks.get(1);
        assertEquals("Task 1", grabbedTask1.getDescription());
        assertEquals(Status.TODO, grabbedTask1.getStatus());

        Task grabbedTask2 = currentTasks.get(2);
        assertEquals("Task 2", grabbedTask2.getDescription());
        assertEquals(Status.IN_PROGRESS, grabbedTask2.getStatus());
    }

    @Test
    void testFromJSONInvalidJSON() {
        String json = "{\"id\":1,\"description\":\"Task 1\",\"status\":\"TODO\",\"createdAt\":\"2022-01-01T00:00:00\",\"updatedAt\":\"2022-01-01T00:00:00\"}\r\n" + //
            "{\"id\":2,\"description\":\"Task 2\",\"status\":\"IN_PROGRESS\",\"createdAt\":\"2022-01-02T00:00:00\",\"updatedAt\":\"2022-01-02T00:00:00\"}\r\n" + //
            "{\"id\":3,\"description\":\"Task 3\",\"status\":\"DONE\",\"createdAt\":\"2022-01-03T00:00:00\",\"updatedAt\":\"2022-01-03T00:00:00\"}\r\n";

        assertThrows(IllegalArgumentException.class, () -> {
            TaskMap.fromJSON(json + "{\"id\":4,\"description\":\"Task 4\",\"status\":\"INVALID\",\"createdAt\":\"2022-01-04T00:00:00\",\"updatedAt\":\"2022-01-04T00:00:00\"}");
        });
    }

    @Test
    void testFileUpdatesCorrectly() {
        Commands.ADD.execute(TEST_FILE_PATH, "Test Task", null);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        Map<Integer, Task> currentTasks = taskMap.getCurrentTasks();
        Task savedTask = currentTasks.get(1);

        assertEquals(1, currentTasks.size());
        assertNotNull(savedTask);
        assertEquals("Test Task", savedTask.getDescription());
        assertEquals(Status.TODO, savedTask.getStatus());

        Commands.ADD.execute(TEST_FILE_PATH, "Another Task", null);
        
        taskMap = new TaskMap(TEST_FILE_PATH);
        Map<Integer, Task> updatedTasks = taskMap.getCurrentTasks();
        Task updatedTask = updatedTasks.get(2);

        assertEquals(2, updatedTasks.size());
        assertNotNull(updatedTask);
        assertEquals("Another Task", updatedTask.getDescription());
        assertEquals(Status.TODO, updatedTask.getStatus());
    }
}