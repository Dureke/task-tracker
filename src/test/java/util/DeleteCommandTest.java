package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import tracker.util.Task;
import tracker.util.Commands;
import tracker.util.TaskMap;

public class DeleteCommandTest {
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
    void testDeleteCommand() {
        String taskDescription = "Task to be deleted";

        // Add a task first
        Commands.ADD.execute(TEST_FILE_PATH, taskDescription, null);
        // Now delete the task
        Commands.DELETE.execute(TEST_FILE_PATH, "1", null);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertTrue(taskMap.getCurrentTasks().isEmpty());
    }

    @Test
    void testDeleteNonExistentTask() {
        String taskDescription = "Task to be deleted";

        // Add a task first
        Commands.ADD.execute(TEST_FILE_PATH, taskDescription, null);
        // Now delete the task
        Commands.DELETE.execute(TEST_FILE_PATH, "999", null);
        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertTrue(taskMap.getCurrentTasks().size() == 1);
    }

    @Test
    void testDeleteFromEmptyFile() {
        // Attempt to delete a task from an empty file
        Commands.DELETE.execute(TEST_FILE_PATH, "1", null);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertTrue(taskMap.getCurrentTasks().isEmpty());
    }

    @Test
    void testDeleteWithInvalidId() {
        String taskDescription = "Task to be deleted";

        // Add a task first
        Commands.ADD.execute(TEST_FILE_PATH, taskDescription, null);
        // Now delete the task with invalid id
        Commands.DELETE.execute(TEST_FILE_PATH, "invalid_id", null);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertTrue(taskMap.getCurrentTasks().size() == 1);
    }

    @Test
    void testDeleteFromListOfTasks() {
        // Add multiple tasks
        Commands.ADD.execute(TEST_FILE_PATH, "Task 1", null);
        Commands.ADD.execute(TEST_FILE_PATH, "Task 2", null);
        Commands.ADD.execute(TEST_FILE_PATH, "Task 3", null);

        // Delete the second task
        Commands.DELETE.execute(TEST_FILE_PATH, "2", null);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertTrue(taskMap.getCurrentTasks().size() == 2);
        assertTrue(!taskMap.getCurrentTasks().containsKey(2));
    }
}