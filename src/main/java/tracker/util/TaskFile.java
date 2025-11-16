package tracker.util;

import java.io.Reader;
import java.io.Writer;
import java.util.Map;

public class TaskFile {
    // Controls the reading and writing of tasks to a file
    // TODO: add JSON to Map and Map to JSON methods
    // TODO: add a save method

    private final String path;
    private Reader reader;
    private Writer writer;

    public TaskFile(String filePath) {
        this.path = filePath;
    }

    public String getPath() {
        return path;
    }

    private Reader getReader() throws Exception {
        if (reader == null) {
            reader = new java.io.FileReader(path);
        }
        return reader;
    }

    private Writer getWriter() throws Exception {
        if (writer == null) {
            writer = new java.io.FileWriter(path, true);
        }
        return writer;
    }

    private void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
        if (writer != null) {
            writer.close();
        }
    }

    public Map<Integer, Task> JSONtoMap() {
        try {
            Reader r = getReader();
            StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = r.read()) != -1) {
                sb.append((char) ch);
            }
            String jsonString = sb.toString();
            Map<Integer, Task> taskMap = TaskMap.fromJSON(jsonString);
            return taskMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String MaptoJSON(Map<Integer, Task> tasks) {
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks.values()) {
            sb.append(task.toJSON()).append("\r\n");
        }
        return sb.toString();
    }

    public void save(Map<Integer, Task> tasks) {
        try {
            Writer w = getWriter();
            String jsonString = MaptoJSON(tasks);
            w.write(jsonString);
            w.flush();
            this.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
