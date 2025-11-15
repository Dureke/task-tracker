package tracker.util;

import java.util.List;

public class TaskMap {
    // Manages the in-memory representation of tasks
    // TODO: add methods to manipulate the task list

    private List<Task> currentTasks;

    public TaskMap(String fileName) {
        this.currentTasks = null;
    }
}
