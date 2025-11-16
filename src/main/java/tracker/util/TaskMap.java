package tracker.util;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

public class TaskMap {
    // Manages the in-memory representation of tasks
    // TODO: add methods to manipulate the task list

    private Map<Integer, Task> currentTasks;

    public TaskMap(String fileName) {
        this.currentTasks = null;
    }

    public static Map<Integer, Task> fromJSON(String json) {
        Map<Integer, Task> taskMap = new HashMap<>();

        if (json.equals("{}") || json.trim().isEmpty()) {
            return taskMap;
        }

        String[] entries = json.split("\\{");
        for (String entry : entries) {
            if (entry.trim().isEmpty() || entry.equals("}")) {
                continue;
            }

            String[] lines = entry.split(",");
            int id = Integer.parseInt(lines[0].split(":")[1].trim().replaceAll("\"", ""));
            String description = lines[1].split(":")[1].trim().replaceAll("\"", "");
            Status status;
            try {
                status = Status.valueOf(lines[2].split(":")[1].trim().replaceAll("\"", "").toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status value: " + lines[2].split(":")[1].trim().replaceAll("\"", ""), e);
            }

            LocalDateTime createdAt = LocalDateTime.parse(lines[3].split(":")[1].trim().replaceAll("\"", "")
                    + ":" + lines[3].split(":")[2].trim().replaceAll("\"", "")
                    + ":" + lines[3].split(":")[3].trim().replaceAll("\"", ""));
            LocalDateTime updatedAt = LocalDateTime.parse(lines[4].split(":")[1].trim().replaceAll("\"", "")
                    + ":" + lines[4].split(":")[2].trim().replaceAll("\"", "")
                    + ":" + lines[4].split(":")[3].substring(0, 2).trim().replaceAll("\"", ""));
            Task task = new Task(id, description, status, createdAt, updatedAt);
            taskMap.put(task.getId(), task);
        }
        return taskMap;
    }
}
