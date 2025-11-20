package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import tracker.util.Commands;
import tracker.util.Status;
import tracker.util.TaskMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarkCommandTest {
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
    void testMarkInProgressCommand() {
        Commands.ADD.execute(TEST_FILE_PATH, "Test Task", null);
        Commands.MARK_IN_PROGRESS.execute(TEST_FILE_PATH, "1", null);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertEquals(Status.IN_PROGRESS, taskMap.getCurrentTasks().get(1).getStatus());
    }

    @Test
    void testMarkInProgressCommandInvalidId() {
        Commands.ADD.execute(TEST_FILE_PATH, "Test Task", null);
        Commands.MARK_IN_PROGRESS.execute(TEST_FILE_PATH, "999", null);

        TaskMap taskMap = new TaskMap(TEST_FILE_PATH);
        assertEquals(Status.TODO,  taskMap.getCurrentTasks().get(1).getStatus());
    }
}
