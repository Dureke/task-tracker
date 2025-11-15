package tracker;

import tracker.util.Commands;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -jar task-tracker.jar <task description>");
            return;
        }
        
        final Commands COMMAND;
        final String FILE_NAME = "tasks.json";
        final String ARG_1 = args.length > 1 ? args[1] : null;
        final String ARG_2 = args.length > 2 ? args[2] : null;

        try {
            COMMAND = Commands.valueOf(args[0].toUpperCase());
            COMMAND.execute(FILE_NAME, ARG_1, ARG_2);
        } catch (IllegalArgumentException e) {
            System.out.println("Usage: java -jar task-tracker.jar <task description>");
            return;
        }
    }
}