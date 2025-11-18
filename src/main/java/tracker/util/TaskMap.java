package tracker.util;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskMap {
    private Map<Integer, Task> currentTasks;
    private TaskFile taskFile;

    public TaskMap(String fileName) {
        this.taskFile = new TaskFile(fileName);
        this.currentTasks = taskFile.JSONtoMap();
    }

    public Map<Integer, Task> getCurrentTasks() { return currentTasks; }
    public TaskFile getTaskFile() { return taskFile; } 

    public void addTask(Task task) {
        if (task.getDescription() == null || task.getDescription().trim().isEmpty()) {
            return;
        }
        currentTasks.put(task.getId(), task);
        this.taskFile.save(currentTasks);
    }

    public void removeTask(String id) {
        if (isValidId(id)) {
            currentTasks.remove(Integer.parseInt(id));
            this.taskFile.save(currentTasks);
        }
    }

    public void updateTask(String id, String newDescription) {
        if (isValidId(id)) {
            Task task = currentTasks.get(Integer.parseInt(id));
            if (task != null) {
                task.setDescription(newDescription);
                currentTasks.put(task.getId(), task);
                this.taskFile.save(currentTasks);
            }
        }
    }

    public void markTaskAsDone(String id) {
        if (isValidId(id)) {
            Task task = currentTasks.get(Integer.parseInt(id));
            if (task != null) {
                task.setStatus(Status.DONE);
                currentTasks.put(task.getId(), task);
                this.taskFile.save(currentTasks);
            }
        }
    }

    public void markTaskAsInProgress(String id) {
        if (isValidId(id)) {
            Task task = currentTasks.get(Integer.parseInt(id));
            if (task != null) {
                task.setStatus(Status.IN_PROGRESS);
                currentTasks.put(task.getId(), task);
                this.taskFile.save(currentTasks);
            }
        }
    }

    public List<Task> listTasksByStatus(Status status) {
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : currentTasks.values()) {
            if (task.getStatus() == status) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }

    public List<Task> listAllTasks() {
        return new ArrayList<>(currentTasks.values());
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

    private boolean isValidId(String id) {
        try {
            Integer.parseInt(id);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Invalid task ID: " + id);
            return false;
        }
    }
}
