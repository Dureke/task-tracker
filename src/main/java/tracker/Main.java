package tracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import tracker.util.Commands;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -jar task-tracker.jar <task description>");
            return;
        }
        
        final Commands COMMAND;
        final String FILE_NAME = "src/main/resources/tasks.json";
        final String ARG_1 = args.length > 1 ? args[1] : null;
        final String ARG_2 = args.length > 2 ? args[2] : null;

        try {
            if (!Files.exists(Paths.get(FILE_NAME))) {
                Files.createFile(Paths.get(FILE_NAME));
            }

            COMMAND = Commands.valueOf(args[0].toUpperCase());
            COMMAND.execute(FILE_NAME, ARG_1, ARG_2);
        } catch (IllegalArgumentException e) {
            System.out.println("Usage: java -jar task-tracker.jar <task description>");
            return;
        } catch (IOException e) {
            System.out.println("Failed to create task.json file: " + e.getMessage());
            return;
        }
    }
}