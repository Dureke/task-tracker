package tracker.util;

public enum Commands {
    ADD {
        @Override
        public void execute(String fileName, String description, String a) {
            System.out.println("Executing ADD command on file: " + fileName);
            Task task = new Task(description);
            TaskMap taskMap = new TaskMap(fileName);
            taskMap.addTask(task);
            taskMap.getTaskFile().save(taskMap.getCurrentTasks());
        }
    },
    UPDATE {
        @Override
        public void execute(String fileName, String id, String newDescription) {
            System.out.println("Executing UPDATE command on file: " + fileName);
            TaskMap taskMap = new TaskMap(fileName);
            taskMap.updateTask(id, newDescription);
            taskMap.getTaskFile().save(taskMap.getCurrentTasks());
        }
    },
    DELETE {
        @Override
        public void execute(String fileName, String id, String a) {
            System.out.println("Executing DELETE command on file: " + fileName);
        }
    },
    MARK_IN_PROGRESS {
        @Override
        public void execute(String fileName, String id, String a) {
            System.out.println("Executing MARK_IN_PROGRESS command on file: " + fileName);
        }
    },
    MARK_DONE {
        @Override
        public void execute(String fileName, String id, String a) {
            System.out.println("Executing MARK_DONE command on file: " + fileName);
        }
    },
    LIST {
        @Override
        public void execute(String fileName, String status, String a) {
            Status listStatus = status != null ? Status.valueOf(status.toUpperCase()) : null;
            if (listStatus != null) {
                System.out.println("Executing LIST command for status " + listStatus + " on file: " + fileName);
            } else {
                System.out.println("Executing LIST command for all statuses on file: " + fileName);
            }
            System.out.println("Executing LIST command on file: " + fileName);
        }
    };

    private final String command;
    public abstract void execute(String fileName, String arg1, String arg2);

    Commands() {
        this.command = name().toLowerCase();
    }

    public String getCommand() {
        return command;
    }
}
