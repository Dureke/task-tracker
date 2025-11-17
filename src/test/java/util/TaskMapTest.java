package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;

import tracker.util.Task;
import tracker.util.TaskMap;
import tracker.util.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskMapTest {

    @BeforeEach
    void setup() {
        String filePath = "src/test/resources/input/tasks.json";
        Task.resetIdCounter();
        try {
            Files.writeString(Paths.get(filePath), "");
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

        Map<Integer, Task> taskMap = TaskMap.fromJSON(json);

        assertEquals(0, taskMap.size());
    }

    @Test
    void testGetCurrentTasksEmpty() {
        TaskMap taskMap = new TaskMap("src/test/resources/input/tasks.json");
        Map<Integer, Task> currentTasks = taskMap.getCurrentTasks();
        Assertions.assertEquals(0, currentTasks.size());
    }

    @Test
    void testGetCurrentTasks() {
        TaskMap taskMap = new TaskMap("src/test/resources/input/tasks.json");
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        taskMap.addTask(task1);
        taskMap.addTask(task2);
        taskMap.markTaskAsInProgress(String.valueOf(task2.getId()));
        taskMap.getTaskFile().save(taskMap.getCurrentTasks());
        Map<Integer, Task> currentTasks = taskMap.getCurrentTasks();
        Assertions.assertEquals(2, currentTasks.size());
        Assertions.assertTrue(currentTasks.containsKey(task1.getId()));
        Assertions.assertTrue(currentTasks.containsKey(task2.getId()));
    }

    @Test
    void testGetCurrentTasksFromFile() {
        TaskMap taskMap = new TaskMap("src/test/resources/input/tasks.json");
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        taskMap.addTask(task1);
        taskMap.addTask(task2);
        taskMap.markTaskAsInProgress(String.valueOf(task2.getId()));
        taskMap.getTaskFile().save(taskMap.getCurrentTasks());

        TaskMap newTaskMap = new TaskMap("src/test/resources/input/tasks.json");
        Map<Integer, Task> currentTasks = newTaskMap.getCurrentTasks();
        Assertions.assertEquals(2, currentTasks.size());
        Task grabbedTask1 = currentTasks.get(1);
        Assertions.assertEquals("Task 1", grabbedTask1.getDescription());
        Assertions.assertEquals(Status.TODO, grabbedTask1.getStatus());
        Task grabbedTask2 = currentTasks.get(2);
        Assertions.assertEquals("Task 2", grabbedTask2.getDescription());
        Assertions.assertEquals(Status.IN_PROGRESS, grabbedTask2.getStatus());
    }

    @Test
    void testFromJSONInvalidJSON() {
        String json = "{\"id\":1,\"description\":\"Task 1\",\"status\":\"TODO\",\"createdAt\":\"2022-01-01T00:00:00\",\"updatedAt\":\"2022-01-01T00:00:00\"}\r\n" + //
            "{\"id\":2,\"description\":\"Task 2\",\"status\":\"IN_PROGRESS\",\"createdAt\":\"2022-01-02T00:00:00\",\"updatedAt\":\"2022-01-02T00:00:00\"}\r\n" + //
            "{\"id\":3,\"description\":\"Task 3\",\"status\":\"DONE\",\"createdAt\":\"2022-01-03T00:00:00\",\"updatedAt\":\"2022-01-03T00:00:00\"}\r\n";

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TaskMap.fromJSON(json + "{\"id\":4,\"description\":\"Task 4\",\"status\":\"INVALID\",\"createdAt\":\"2022-01-04T00:00:00\",\"updatedAt\":\"2022-01-04T00:00:00\"}");
        });
    }

    @Test
    void testFileUpdatesCorrectly() {
        String filePath = "src/test/resources/input/tasks.json";
        TaskMap taskMap = new TaskMap(filePath);
        Task task = new Task("Test Task");
        taskMap.addTask(task);
        taskMap.getTaskFile().save(taskMap.getCurrentTasks());

        TaskMap newTaskMap = new TaskMap(filePath);
        Map<Integer, Task> currentTasks = newTaskMap.getCurrentTasks();
        Assertions.assertEquals(1, currentTasks.size());
        Task savedTask = currentTasks.get(task.getId());
        Assertions.assertNotNull(savedTask);
        Assertions.assertEquals("Test Task", savedTask.getDescription());
        Assertions.assertEquals(Status.TODO, savedTask.getStatus());
        Task newTask = new Task("Another Task");
        newTaskMap.addTask(newTask);
        newTaskMap.getTaskFile().save(newTaskMap.getCurrentTasks());

        TaskMap updatedTaskMap = new TaskMap(filePath);
        Map<Integer, Task> updatedTasks = updatedTaskMap.getCurrentTasks();
        Assertions.assertEquals(2, updatedTasks.size());
        Task updatedTask = updatedTasks.get(newTask.getId());
        Assertions.assertNotNull(updatedTask);
        Assertions.assertEquals("Another Task", updatedTask.getDescription());
        Assertions.assertEquals(Status.TODO, updatedTask.getStatus());

    }
}