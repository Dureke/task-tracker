package util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tracker.util.Task;
import tracker.util.Commands;
import tracker.util.TaskMap;

public class UpdateCommandTest {
    static final String TEST_FILE_PATH = "src/test/resources/input/tasks.json";

    @BeforeEach
    void setup() {
        String filePath = TEST_FILE_PATH;
        Task.resetIdCounter();
        try {
            Files.writeString(Paths.get(filePath), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testUpdateCommand() {
        String initialDescription = "Initial Task";
        String updatedDescription = "Updated Task";

        Commands.ADD.execute(TEST_FILE_PATH, initialDescription, null);
        Commands.UPDATE.execute(TEST_FILE_PATH, "1", updatedDescription);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        Task updatedTask = taskMap.getCurrentTasks().get(1);
        assertTrue(updatedTask != null);
        assertTrue(updatedTask.getDescription().equals(updatedDescription));
    }

    @Test
    void testUpdateNonExistentTask() {
        String updatedDescription = "Updated Task";

        Commands.UPDATE.execute(TEST_FILE_PATH, "999", updatedDescription);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertTrue(taskMap.getCurrentTasks().isEmpty());
    }

    @Test
    void testUpdateWithEmptyDescription() {
        String updatedDescription = "";

        Commands.UPDATE.execute(TEST_FILE_PATH, "1", updatedDescription);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertTrue(taskMap.getCurrentTasks().isEmpty());
    }

    @Test
    void testUpdateWithInvalidId() {
        String updatedDescription = "Updated Task";

        Commands.UPDATE.execute(TEST_FILE_PATH, "invalid_id", updatedDescription);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertTrue(taskMap.getCurrentTasks().isEmpty());
    }

    @Test
    void testUpdateWithMultipleEntries() {
        String desc1 = "Task 1";
        String desc2 = "Task 2";
        String updatedDescription = "Updated Task 1";

        Commands.ADD.execute(TEST_FILE_PATH, desc1, null);
        Commands.ADD.execute(TEST_FILE_PATH, desc2, null);
        Commands.UPDATE.execute(TEST_FILE_PATH, "1", updatedDescription);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        Task updatedTask = taskMap.getCurrentTasks().get(1);
        Task unchangedTask = taskMap.getCurrentTasks().get(2);

        assertTrue(updatedTask != null);
        assertTrue(updatedTask.getDescription().equals(updatedDescription));
        assertTrue(unchangedTask != null);
        assertTrue(unchangedTask.getDescription().equals(desc2));
    }
}