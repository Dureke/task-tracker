package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import tracker.util.Commands;
import tracker.util.Task;
import tracker.util.TaskMap;

public class AddCommandTest {
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
    public void testExecute_ValidDescription() throws IOException {
        Commands.ADD.execute(TEST_FILE_PATH, "Test Task", null);
        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        Task addedTask = taskMap.getCurrentTasks().get(1);
        Assertions.assertNotNull(addedTask);
        Assertions.assertEquals("Test Task", addedTask.getDescription());
    }

    @Test
    public void testExecute_InvalidDescription() throws IOException {
        Commands.ADD.execute(TEST_FILE_PATH, "", null);
        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        Assertions.assertTrue(taskMap.getCurrentTasks().isEmpty());
    }

    @Test
    public void testExecute_NullDescription() throws IOException {
        Commands.ADD.execute(TEST_FILE_PATH, null, null);
        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        Assertions.assertTrue(taskMap.getCurrentTasks().isEmpty());
    }
}
