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
    private static int idCounter = retrieveIdCounter();
    private static String idCounterFile = "src/main/resources/id_counter.txt";

    static private int retrieveIdCounter() {
        try {
            if (!Files.exists(Paths.get(idCounterFile))) {
                Files.createFile(Paths.get(idCounterFile));
                Files.writeString(Paths.get(idCounterFile), "0");
                return 1;
            }
            return Integer.parseInt(Files.readString(Paths.get(idCounterFile)));
        } catch (IOException e) {
            System.out.println("Failed to read id_counter.txt file: " + e.getMessage());
        }
        return idCounter;
    }

    static private void saveIdCounter(int counter) {
        try {
            Files.writeString(Paths.get(idCounterFile), Integer.toString(counter));
        } catch (IOException e) {
            System.out.println("Failed to write to id_counter.txt file: " + e.getMessage());
        }
    }

    static private int incrementIdCounter() {
        int currentID = retrieveIdCounter();
        int newCounter = currentID + 1;
        try {
            Files.writeString(Paths.get(idCounterFile), Integer.toString(newCounter));
        } catch (IOException e) {
            System.out.println("Failed to write to id_counter.txt file: " + e.getMessage());
        }
        return newCounter;
    }

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

    public static void resetIdCounter() {
        saveIdCounter(0);
        idCounter = 0;
    }

    public int getId() {
        return this.uuid;
    }

    public String getDescription() {
        return this.description;
    }

    public Status getStatus() {
        return this.status;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setStatus(Status status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public String toString() {
        return "Task{" +
                "id='" + uuid + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public String toJSON() {
        return "{" +
                "\"id\":\"" + uuid + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"status\":\"" + status + "\"," +
                "\"createdAt\":\"" + createdAt + "\"," +
                "\"updatedAt\":\"" + updatedAt + "\"" +
                "}";
    }
}
