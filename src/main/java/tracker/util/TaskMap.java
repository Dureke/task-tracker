package tracker.util;

import java.util.Map;

public class TaskMap {
    // Manages the in-memory representation of tasks
    // TODO: add methods to manipulate the task list

    private Map<Integer, Task> currentTasks;

    public TaskMap(String fileName) {
        this.currentTasks = null;
    }
}
