package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tracker.util.Commands;
import tracker.util.Task;
import tracker.util.TaskMap;
import tracker.util.Status;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MarkCommandTest {
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
    void testMarkInProgressCommand() {
        String filePath = "src/test/resources/input/tasks.json";
        TaskMap taskMap = new TaskMap(filePath);
        Task task = new Task("Test Task");
        taskMap.addTask(task);
        taskMap.getTaskFile().save(taskMap.getCurrentTasks());

        Commands.MARK_IN_PROGRESS.execute(filePath, String.valueOf(task.getId()), null);

        TaskMap updatedTaskMap = new TaskMap(filePath);
        Task updatedTask = updatedTaskMap.getCurrentTasks().get(task.getId());
        assertTrue(updatedTask.getStatus() == Status.IN_PROGRESS);
    }

    @Test
    void testMarkInProgressCommandInvalidId() {
        String filePath = "src/test/resources/input/tasks.json";
        TaskMap taskMap = new TaskMap(filePath);
        Task task = new Task("Test Task");
        taskMap.addTask(task);
        taskMap.getTaskFile().save(taskMap.getCurrentTasks());

        Commands.MARK_IN_PROGRESS.execute(filePath, "999", null);

        TaskMap updatedTaskMap = new TaskMap(filePath);
        Task unchangedTask = updatedTaskMap.getCurrentTasks().get(task.getId());
        assertTrue(unchangedTask.getStatus() == Status.TODO);
    }
}
