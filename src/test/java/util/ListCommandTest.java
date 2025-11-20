package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import tracker.util.Commands;
import tracker.util.Status;
import tracker.util.Task;
import tracker.util.TaskMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class ListCommandTest {
    static final String TEST_FILE_PATH = "src/test/resources/input/tasks.json";

    @BeforeEach
    void setup() {
        try {
            Files.writeString(Paths.get(TEST_FILE_PATH), "");
            // Pre-populate with some tasks
            Commands.ADD.execute(TEST_FILE_PATH, "task1", null);
            Commands.ADD.execute(TEST_FILE_PATH, "task2", null);
            Commands.ADD.execute(TEST_FILE_PATH, "task3", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListAllTasks() {
        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        List<Task> allTasks = taskMap.listAllTasks();
        
        assertEquals(3, allTasks.size());
    }

    @Test
    public void testListUpdatedTaskList() {
        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertEquals(3, taskMap.listAllTasks().size());

        Commands.DELETE.execute(TEST_FILE_PATH, "2", null);
        
        taskMap = new TaskMap(TEST_FILE_PATH);
        assertEquals(2, taskMap.listAllTasks().size());
    }

    @Test
    public void testListTasksByStatus() {
        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertEquals(3, taskMap.listTasksByStatus(Status.TODO).size());

        Commands.MARK_DONE.execute(TEST_FILE_PATH, "3", null);
        taskMap = new TaskMap(TEST_FILE_PATH);
        assertEquals(2, taskMap.listTasksByStatus(Status.TODO).size());
        assertEquals(1, taskMap.listTasksByStatus(Status.DONE).size());

        Commands.MARK_IN_PROGRESS.execute(TEST_FILE_PATH, "2", null);
        taskMap = new TaskMap(TEST_FILE_PATH);
        assertEquals(1, taskMap.listTasksByStatus(Status.TODO).size());
        assertEquals(1, taskMap.listTasksByStatus(Status.DONE).size());
        assertEquals(1, taskMap.listTasksByStatus(Status.IN_PROGRESS).size());

        Commands.MARK_DONE.execute(TEST_FILE_PATH, "1", null);
        taskMap = new TaskMap(TEST_FILE_PATH);
        assertEquals(0, taskMap.listTasksByStatus(Status.TODO).size());
    }

    @Test
    public void testListWithInvalidStatus() {
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            Commands.LIST.execute(TEST_FILE_PATH, "INVALID_STATUS", null);
        });
    }
}
