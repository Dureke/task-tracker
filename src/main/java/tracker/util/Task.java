package tracker.util;

import java.time.LocalDateTime;

public class Task {
    private int uuid;
    private String description;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task() {
        this.uuid = 1;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Task(String description) {
        this();
        this.description = description;
        this.status = Status.TODO;
    }

    public Task(String description, int uuid) {
        this();
        this.description = description;
        this.uuid = uuid;
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
