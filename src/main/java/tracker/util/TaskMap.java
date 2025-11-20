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
        String emptyJSON = "{\"tasks\":[]}";

        if (json.equals(emptyJSON) || json.trim().isEmpty()) {
            return taskMap;
        }

        String tasksArrayString = json.substring(json.indexOf("[") + 1, json.lastIndexOf("]"));
        String[] entries = tasksArrayString.split("\\{");
        for (String entry : entries) {
            if (entry.trim().isEmpty() || entry.equals("}")) {
                continue;
            }

            String[] lines = entry.split(",");
            String idAsString = lines[0].split("\"id\":")[1].trim().replaceAll("\"", "");
            String description = lines[1].split("\"description\":")[1].trim().replaceAll("\"", "");
            String statusAsString = lines[2].split("\"status\":")[1].trim().replaceAll("\"", "");
            String createdAtAsString = lines[3].split("\"createdAt\":")[1].trim().replaceAll("\"", "");
            String updatedAtAsString = lines[4].split("\"updatedAt\":")[1].trim().replaceAll("[\"}]", "");
            
            int id = Integer.parseInt(idAsString);
            Status status = Status.valueOf(statusAsString.toUpperCase());
            LocalDateTime createdAt = LocalDateTime.parse(createdAtAsString);
            LocalDateTime updatedAt = LocalDateTime.parse(updatedAtAsString);

            Task task = new Task(id, description, status, createdAt, updatedAt);
            taskMap.put(id, task);
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
