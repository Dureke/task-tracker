package tracker.util;

import java.io.Reader;
import java.io.Writer;
import java.util.Map;

public class TaskFile {
    private final String path;
    private Reader reader;
    private Writer writer;

    public TaskFile(String filePath) {
        this.path = filePath;
    }

    public String getPath() { return path; }

    private Reader getReader() throws Exception {
        this.reader = reader == null ? new java.io.FileReader(this.path) : reader;
        return reader;
    }

    private Writer getWriter() throws Exception {
        this.writer = writer == null ? new java.io.FileWriter(this.path, false) : writer;
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
        sb.append("{\"tasks\":[\r\n");
        for (Task task : tasks.values()) {
            sb.append(task.toJSON()).append(",\r\n");
        }
        if (!tasks.isEmpty()) {
            sb.setLength(sb.length() - 3); // Remove the last comma and newline
            sb.append("\r\n");
        }
        sb.append("]}");
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
