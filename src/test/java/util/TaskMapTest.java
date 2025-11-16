package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import tracker.util.Task;
import tracker.util.TaskMap;
import tracker.util.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskMapTest {

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
    void testFromJSONInvalidJSON() {
        String json = "{\"id\":1,\"description\":\"Task 1\",\"status\":\"TODO\",\"createdAt\":\"2022-01-01T00:00:00\",\"updatedAt\":\"2022-01-01T00:00:00\"}\r\n" + //
            "{\"id\":2,\"description\":\"Task 2\",\"status\":\"IN_PROGRESS\",\"createdAt\":\"2022-01-02T00:00:00\",\"updatedAt\":\"2022-01-02T00:00:00\"}\r\n" + //
            "{\"id\":3,\"description\":\"Task 3\",\"status\":\"DONE\",\"createdAt\":\"2022-01-03T00:00:00\",\"updatedAt\":\"2022-01-03T00:00:00\"}\r\n";

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TaskMap.fromJSON(json + "{\"id\":4,\"description\":\"Task 4\",\"status\":\"INVALID\",\"createdAt\":\"2022-01-04T00:00:00\",\"updatedAt\":\"2022-01-04T00:00:00\"}");
        });
    }
}