package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private int lastId = 0;
    protected final Map<Integer, Task> taskMap = new HashMap<>();
    protected final Map<Integer, Subtask> subtasksMap = new HashMap<>();
    protected final Map<Integer, Epic> epicsMap = new HashMap<>();

    protected final TaskSet taskSet = new TaskSet();

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public ArrayList<Task> getAllSubtasks() {
        return new ArrayList<>(subtasksMap.values());
    }

    @Override
    public ArrayList<Task> getAllEpics() {
        return new ArrayList<>(epicsMap.values());
    }

    public List<Task> getPrioritizedTasks() {
        return taskSet.getPrioritizedTasks();
    }

    @Override
    public void clearAllTasks() {
        taskMap.clear();
        taskSet.clearAllTasks();
    }

    @Override
    public void clearAllSubtasks() {
        subtasksMap.clear();
        taskSet.clearAllSubtasks();
        for (Epic e : epicsMap.values()) {
            e.clearAllSubtasks();
        }
    }

    @Override
    public void clearAllEpics() {
        epicsMap.clear();
        subtasksMap.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task result = taskMap.get(id);
        Managers.getDefaultHistory().add(result);
        return result;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask result = subtasksMap.get(id);
        Managers.getDefaultHistory().add(result);
        return result;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic result = epicsMap.get(id);
        Managers.getDefaultHistory().add(result);
        return result;
    }

    @Override
    public boolean createTask(Task task) {
        task.setId(getNewId());
        if (taskSet.canAddUpdateTask(task)) {
            taskMap.put(task.getId(), task);
            taskSet.addOrUpdateTask(task);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean createSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        if (epicsMap.containsKey(epicId)) {
            Epic epic = epicsMap.get(epicId);
            subtask.setId(getNewId());
            if (taskSet.canAddUpdateTask(subtask)) {
                taskSet.addOrUpdateTask(subtask);
                subtasksMap.put(subtask.getId(), subtask);
                epic.addSubtask(subtask);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    @Override
    public void createEpic(Epic epic) {
        int id = getNewId();
        epic.setId(id);
        epic.setStatus(tasks.TaskStatus.NEW);
        epicsMap.put(epic.getId(), epic);
    }

    @Override
    public boolean updateTask(Task task) {
        int id = task.getId();
        if (taskMap.containsKey(id)) {
            if (taskSet.canAddUpdateTask(task)) {
                taskSet.addOrUpdateTask(task);
                taskMap.get(id).updateFrom(task);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        int newEpicId = subtask.getEpicId();
        if (subtasksMap.containsKey(id) && epicsMap.containsKey(newEpicId) && taskSet.canAddUpdateTask(subtask)) {
            Subtask oldSubtask = subtasksMap.get(id);
            int oldEpicId = oldSubtask.getEpicId();
            Epic oldEpic = epicsMap.get(oldEpicId);
            Epic newEpic = epicsMap.get(newEpicId);
            oldEpic.removeSubtask(oldSubtask);
            oldSubtask.updateFrom(subtask);
            newEpic.addSubtask(oldSubtask);
            taskSet.addOrUpdateTask(subtask);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateEpic(Epic epic) {
        int id = epic.getId();
        if (epicsMap.containsKey(id)) {
            epicsMap.get(id).updateFrom(epic);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteTask(int id) {
        if (taskMap.containsKey(id)) {
            taskSet.remove(taskMap.get(id));
            taskMap.remove(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteEpic(int id) {
        if (epicsMap.containsKey(id)) {
            List<Subtask> subtasks = getSubtasksByEpic(id);
            for (Subtask st : subtasks) {
                subtasksMap.remove(st.getId());
            }
            epicsMap.remove(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteSubtask(int id) {
        if (subtasksMap.containsKey(id)) {
            Subtask subtask = subtasksMap.get(id);
            int epicId = subtask.getEpicId();
            Epic epic = epicsMap.get(epicId);
            epic.removeSubtask(subtasksMap.get(id));
            subtasksMap.remove(id);
            taskSet.remove(subtask);
            subtask.setId(0);
            return true;
        } else {
            return false;
        }
    }


    private int getNewId() {
        return ++lastId;
    }

    public boolean recalcEpic(int id) {
        if (epicsMap.containsKey(id)) {
            Epic epic = epicsMap.get(id);
            epic.recalc();
            return true;
        } else {
            return false;
        }

    }

    @Override
    public List<Subtask> getSubtasksByEpic(int id) {
/*
        ArrayList<Subtask> result = new ArrayList<>();
        for (Subtask st : subtasksMap.values()) {
            if (st.getEpicId() == id) {
                result.add(st);
            }
        }
        return result;

 */
        return subtasksMap.values().stream().filter(st -> st.getEpicId() == id).collect(Collectors.toList());
    }

    protected void resetLastId() {
        lastId = 0;
        for (int id : taskMap.keySet()) {
            if (id > lastId) {
                lastId = id;
            }
        }
        for (int id : subtasksMap.keySet()) {
            if (id > lastId) {
                lastId = id;
            }
        }
        for (int id : epicsMap.keySet()) {
            if (id > lastId) {
                lastId = id;
            }
        }
    }
}
