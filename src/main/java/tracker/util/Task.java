package tracker.util;

import java.time.LocalDateTime;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class Task {
    private int uuid;
    private String description;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private static final String ID_COUNTER_FILE = "src/main/resources/id_counter.txt";
    private static int idCounter = retrieveIdCounter();

    public Task() {
        this.uuid = incrementIdCounter();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Task(String description) {
        this();
        this.description = description;
        this.status = Status.TODO;
    }

    public Task(int uuid, String description, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.uuid = uuid;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return uuid; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; } 
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setStatus(Status status) {
        this.status = status;
        updatedAt = LocalDateTime.now();
    }

    public void setDescription(String description) {
        this.description = description;
        updatedAt = LocalDateTime.now();
    }
    private static int retrieveIdCounter() {
        try {
            if (!Files.exists(Paths.get(ID_COUNTER_FILE))) {
                Files.createFile(Paths.get(ID_COUNTER_FILE));
                Files.writeString(Paths.get(ID_COUNTER_FILE), "0");
                return 0;
            }
            return Integer.parseInt(Files.readString(Paths.get(ID_COUNTER_FILE)));
        } catch (IOException e) {
            System.out.println("Failed to read id_counter.txt file: " + e.getMessage());
            return 0;
        }
    }

    private static void saveIdCounter(int counter) {
        try {
            Files.writeString(Paths.get(ID_COUNTER_FILE), String.valueOf(counter));
        } catch (IOException e) {
            System.out.println("Failed to write to id_counter.txt file: " + e.getMessage());
        }
    }

    private static int incrementIdCounter() {
        int newCounter = idCounter + 1;
        saveIdCounter(newCounter);
        idCounter = newCounter;
        return newCounter;
    }

    public static void resetIdCounter() {
        saveIdCounter(0);
        idCounter = 0;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + uuid +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public String toJSON() {
        return "{" +
                "\"id\":" + uuid + "," +
                "\"description\":\"" + description + "\"," +
                "\"status\":\"" + status + "\"," +
                "\"createdAt\":\"" + createdAt + "\"," +
                "\"updatedAt\":\"" + updatedAt + "\"" +
                "}";
    }
}
