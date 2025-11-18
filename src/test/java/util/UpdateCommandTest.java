package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import tracker.util.Commands;
import tracker.util.Task;
import tracker.util.TaskMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateCommandTest {
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
    void testUpdateCommand() {
        Commands.ADD.execute(TEST_FILE_PATH, "Initial Task", null);
        Commands.UPDATE.execute(TEST_FILE_PATH, "1", "Updated Task");

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        Task updatedTask = taskMap.getCurrentTasks().get(1);
        assertNotNull(updatedTask);
        assertEquals("Updated Task", updatedTask.getDescription());
    }

    @Test
    void testUpdateNonExistentTask() {
        Commands.UPDATE.execute(TEST_FILE_PATH, "999", "Updated Task");

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertTrue(taskMap.getCurrentTasks().isEmpty());
    }

    @Test
    void testUpdateWithEmptyDescription() {
        Commands.UPDATE.execute(TEST_FILE_PATH, "1", "");

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertTrue(taskMap.getCurrentTasks().isEmpty());
    }

    @Test
    void testUpdateWithInvalidId() {
        Commands.UPDATE.execute(TEST_FILE_PATH, "invalid_id", "Updated Task");

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertTrue(taskMap.getCurrentTasks().isEmpty());
    }

    @Test
    void testUpdateWithMultipleEntries() {
        Commands.ADD.execute(TEST_FILE_PATH, "Task 1", null);
        Commands.ADD.execute(TEST_FILE_PATH, "Task 2", null);
        Commands.UPDATE.execute(TEST_FILE_PATH, "1", "Updated Task 1");

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        Task updatedTask = taskMap.getCurrentTasks().get(1);
        Task unchangedTask = taskMap.getCurrentTasks().get(2);

        assertNotNull(updatedTask);
        assertEquals("Updated Task 1", updatedTask.getDescription());
        assertNotNull(unchangedTask);
        assertEquals("Task 2", unchangedTask.getDescription());
    }
}