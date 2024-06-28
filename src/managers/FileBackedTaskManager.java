package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private String fileName;
    private static final String FILE_HEADER = "id,type,name,status,description,epic";
    private static final String COMMA = ",";

    public FileBackedTaskManager(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public boolean createSubtask(Subtask subtask) {
        boolean result = super.createSubtask(subtask);
        if (result) {
            save();
        }
        return result;
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public boolean updateTask(Task task) {
        boolean result = super.updateTask(task);
        if (result) {
            save();
        }
        return result;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        boolean result = super.updateSubtask(subtask);
        if (result) {
            save();
        }
        return result;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        boolean result = super.updateEpic(epic);
        if (result) {
            save();
        }
        return result;
    }

    @Override
    public boolean deleteTask(int id) {
        boolean result = super.deleteTask(id);
        if (result) {
            save();
        }
        return result;
    }

    @Override
    public boolean deleteEpic(int id) {
        boolean result = super.deleteEpic(id);
        if (result) {
            save();
        }
        return result;
    }

    @Override
    public boolean deleteSubtask(int id) {
        boolean result = super.deleteSubtask(id);
        if (result) {
            save();
        }
        return result;
    }

    public void save() {
        try (FileWriter fos = new FileWriter(fileName)) {
            fos.write(FILE_HEADER);
            fos.write('\n');

            putToFile(fos, taskMap);
            putToFile(fos, subtasksMap);
            putToFile(fos, epicsMap);
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    private void load() {
        taskMap.clear();
        subtasksMap.clear();
        epicsMap.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while (br.ready()) {
                String line = br.readLine();
                if (FILE_HEADER.equals(line)) {
                    continue;
                }
                String[] split = line.split(COMMA);
                List<String> list = Arrays.asList(split);
                Task readedTask = Task.taskFromStreamableList(list);
                if (readedTask != null) {
                    int id = readedTask.getId();
                    switch (readedTask.getTaskType()) {
                        case TASK:
                            taskMap.put(id, readedTask);
                            break;
                        case SUBTASK:
                            subtasksMap.put(id, (Subtask) readedTask);
                            break;
                        case EPIC:
                            epicsMap.put(id, (Epic) readedTask);
                            break;
                    }
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

        resetLastId();
        for (Subtask st : subtasksMap.values()) {
            int id = st.getEpicId();
            Epic epic = epicsMap.get(id);
            if (epic != null) {
                epic.dirtyAddSubtask(st);
            }
        }
    }

    static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager result = new FileBackedTaskManager(file.getPath());
        if (file.exists()) {
            result.load();
        }
        return result;
    }

    private void putToFile(OutputStreamWriter fos, Map<Integer, ? extends Task> map) throws IOException {
        for (Task t : map.values()) {
            List<String> strList = t.toStreamableList();
            String line = String.join(COMMA, strList);
            fos.write(line);
            fos.write('\n');
        }
    }
}
