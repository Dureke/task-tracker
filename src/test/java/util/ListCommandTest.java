package util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import tracker.util.Task;
import tracker.util.TaskMap;
import tracker.util.Commands;
import tracker.util.Status;


public class ListCommandTest {
    @BeforeEach
    void setup() {
        String filePath = "src/test/resources/input/tasks.json";
        Task.resetIdCounter();
        try {
            Files.writeString(Paths.get(filePath), "");
            // Pre-populate with some tasks
            Commands.ADD.execute(filePath, "task1", null);
            Commands.ADD.execute(filePath, "task2", null);
            Commands.ADD.execute(filePath, "task3", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListAllTasks() {
        String filePath = "src/test/resources/input/tasks.json";

        TaskMap taskMap = new TaskMap(filePath);
        List<Task> allTasks = taskMap.listAllTasks();
        Assertions.assertEquals(3, allTasks.size());
    }

    @Test
    public void testListUpdatedTaskList() {
        String filePath = "src/test/resources/input/tasks.json";

        TaskMap taskMap = new TaskMap(filePath);
        List<Task> allTasks = taskMap.listAllTasks();
        Assertions.assertEquals(3, allTasks.size());

        Commands.DELETE.execute(filePath, "2", null);
        taskMap = new TaskMap(filePath);
        allTasks = taskMap.listAllTasks();
        Assertions.assertEquals(2, allTasks.size());
    }

    @Test
    public void testListTasksByStatus() {
        String filePath = "src/test/resources/input/tasks.json";

        TaskMap taskMap = new TaskMap(filePath);
        List<Task> todoTasks = taskMap.listTasksByStatus(Status.TODO);
        Assertions.assertEquals(3, todoTasks.size());

        Commands.MARK_DONE.execute(filePath, "3", null);
        taskMap = new TaskMap(filePath);
        todoTasks = taskMap.listTasksByStatus(Status.TODO);
        List<Task> doneTasks = taskMap.listTasksByStatus(Status.DONE);
        Assertions.assertEquals(2, todoTasks.size());
        Assertions.assertEquals(1, doneTasks.size());

        Commands.MARK_IN_PROGRESS.execute(filePath, "2", null);
        taskMap = new TaskMap(filePath);
        todoTasks = taskMap.listTasksByStatus(Status.TODO);
        doneTasks = taskMap.listTasksByStatus(Status.DONE);
        List<Task> inProgressTasks = taskMap.listTasksByStatus(Status.IN_PROGRESS);
        Assertions.assertEquals(1, todoTasks.size());
        Assertions.assertEquals(1, doneTasks.size());
        Assertions.assertEquals(1, inProgressTasks.size());

        Commands.MARK_DONE.execute(filePath, "1", null);
        taskMap = new TaskMap(filePath);
        todoTasks = taskMap.listTasksByStatus(Status.TODO);
        Assertions.assertEquals(0, todoTasks.size());
    }

    @Test
    public void testListWithInvalidStatus() {
        String filePath = "src/test/resources/input/tasks.json";

        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
            Commands.LIST.execute(filePath, "INVALID_STATUS", null);
        });
    }
    
}
