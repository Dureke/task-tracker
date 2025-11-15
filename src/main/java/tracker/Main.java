package tracker;

import tracker.util.Task;

public class Main {
    public static void main(String[] args) {
        Task task = new Task("Implement task tracker");
        System.out.println("Task ID: " + task.getId());
        System.out.println(task.toString());
    }
}