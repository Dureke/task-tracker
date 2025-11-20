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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteCommandTest {
    static final String TEST_FILE_PATH = "src/test/resources/input/tasks.json";

    @BeforeEach
    void setup() {
        try {
            Files.writeString(Paths.get(TEST_FILE_PATH), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDeleteCommand() {
        Commands.ADD.execute(TEST_FILE_PATH, "Task to be deleted", null);
        Commands.DELETE.execute(TEST_FILE_PATH, "1", null);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertTrue(taskMap.getCurrentTasks().isEmpty());
    }

    @Test
    void testDeleteNonExistentTask() {
        Commands.ADD.execute(TEST_FILE_PATH, "Task to be deleted", null);
        Commands.DELETE.execute(TEST_FILE_PATH, "999", null);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertEquals(1, taskMap.getCurrentTasks().size());
    }

    @Test
    void testDeleteFromEmptyFile() {
        Commands.DELETE.execute(TEST_FILE_PATH, "1", null);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertTrue(taskMap.getCurrentTasks().isEmpty());
    }

    @Test
    void testDeleteWithInvalidId() {
        Commands.ADD.execute(TEST_FILE_PATH, "Task to be deleted", null);
        Commands.DELETE.execute(TEST_FILE_PATH, "invalid_id", null);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertEquals(1, taskMap.getCurrentTasks().size());
    }

    @Test
    void testDeleteFromListOfTasks() {
        Commands.ADD.execute(TEST_FILE_PATH, "Task 1", null);
        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        for (Task task : taskMap.getCurrentTasks().values()) {
            System.out.println("Task: " + task.toString());
        }
        Commands.ADD.execute(TEST_FILE_PATH, "Task 2", null);
        taskMap = new TaskMap(TEST_FILE_PATH);
        for (Task task : taskMap.getCurrentTasks().values()) {
            System.out.println("Task: " + task.toString());
        }
        Commands.ADD.execute(TEST_FILE_PATH, "Task 3", null);
        taskMap = new TaskMap(TEST_FILE_PATH);
        for (Task task : taskMap.getCurrentTasks().values()) {
            System.out.println("Task: " + task.toString());
        }
        Commands.DELETE.execute(TEST_FILE_PATH, "2", null);

        taskMap = new TaskMap(TEST_FILE_PATH);
        for (Task task : taskMap.getCurrentTasks().values()) {
            System.out.println("Task: " + task.toString());
        }
        assertEquals(2, taskMap.getCurrentTasks().size());
        assertFalse(taskMap.getCurrentTasks().containsKey(2));
    }
}