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

public class AddCommandTest {
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
    public void testExecute_ValidDescription() throws IOException {
        Commands.ADD.execute(TEST_FILE_PATH, "Test Task", null);
        
        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        Task addedTask = taskMap.getCurrentTasks().get(1);
        assertNotNull(addedTask);
        assertEquals("Test Task", addedTask.getDescription());
    }

    @Test
    public void testExecute_InvalidDescription() throws IOException {
        Commands.ADD.execute(TEST_FILE_PATH, "", null);
        
        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertTrue(taskMap.getCurrentTasks().isEmpty());
    }

    @Test
    public void testExecute_NullDescription() throws IOException {
        Commands.ADD.execute(TEST_FILE_PATH, null, null);
        
        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertTrue(taskMap.getCurrentTasks().isEmpty());
    }
}
