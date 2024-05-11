import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int lastId = 0;
    final private HashMap<Integer, Task> taskMap = new HashMap<>();
    final private HashMap<Integer, Subtask> subtasksMap = new HashMap<>();
    final private HashMap<Integer, Epic> epicsMap = new HashMap<>();

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    public ArrayList<Task> getAllSubtasks() {
        return new ArrayList<>(subtasksMap.values());
    }

    public ArrayList<Task> getAllEpics() {
        return new ArrayList<>(epicsMap.values());
    }

    public void clearAllTasks() {
        taskMap.clear();
    }

    public void clearAllSubtasks() {
        subtasksMap.clear();
        for(Epic e : epicsMap.values())
            e.setStatus(TaskStatus.NEW);
    }

    public void clearAllEpics() {
        epicsMap.clear();
        subtasksMap.clear();
    }

    public Task getTaskById(int id) {
        return taskMap.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasksMap.get(id);
    }
    public Epic getEpicById(int id) {
        return epicsMap.get(id);
    }

    public void createTask(Task task) {
        task.setId(getNewId());
        taskMap.put(task.getId(), task);
    }

    public boolean createSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        if(epicsMap.containsKey(epicId)) {
            subtask.setId(getNewId());
            subtasksMap.put(subtask.getId(), subtask);
            recalcEpic(epicId);
            return true;
        } else
            return false;

    }

    public void createEpic(Epic epic) {
        int id = getNewId();
        epic.setId(id);
        epic.setStatus(TaskStatus.NEW);
        epicsMap.put(epic.getId(), epic);
    }

    public boolean updateTask(Task task) {
        int id = task.getId();
        if(taskMap.containsKey(id)) {
            taskMap.get(id).updateFrom(task);
            return true;
        } else
            return false;
    }

    public boolean updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        int newEpicId = subtask.getEpicId();
        if(subtasksMap.containsKey(id) && epicsMap.containsKey(newEpicId)) {
            Subtask oldSubtask = subtasksMap.get(id);
            int oldEpicId = oldSubtask.getEpicId();
            oldSubtask.updateFrom(subtask);
            recalcEpic(oldEpicId);
            if(oldEpicId != newEpicId)
                recalcEpic(newEpicId);
            return true;
        } else
            return false;
    }

    public boolean updateEpic(Epic epic) {
        int id = epic.getId();
        if(epicsMap.containsKey(id)) {
            epicsMap.get(id).updateFrom(epic);
            return true;
        } else
            return false;
    }

    public boolean deleteTask(int id) {
        if(taskMap.containsKey(id)) {
            taskMap.remove(id);
            return true;
        } else
            return false;
    }

    public boolean deleteEpic(int id) {
        if(epicsMap.containsKey(id)) {
            ArrayList<Subtask> subtasks = getSubtasksByEpic(id);
            for (Subtask st : subtasks)
                subtasksMap.remove(st.getId());
            epicsMap.remove(id);
            return true;
        } else
            return false;
    }

    public boolean deleteSubtask(int id) {
        if(subtasksMap.containsKey(id)) {
            int epicId = subtasksMap.get(id).getEpicId();
            subtasksMap.remove(id);
            recalcEpic(epicId);
            return true;
        } else
            return false;

    }


    int getNewId() {
        return ++lastId;
    }

    boolean recalcEpic(int id) {
        if(epicsMap.containsKey(id)) {
            ArrayList<Subtask> subtasks = getSubtasksByEpic(id);
            TaskStatus newStatus;
            if(subtasks.isEmpty())
                newStatus = TaskStatus.NEW;
            else {
                boolean isAllNew = true;
                for(int i=0; i<subtasks.size() && isAllNew; i++)
                    isAllNew = subtasks.get(i).getStatus() == TaskStatus.NEW;
                if(isAllNew)
                    newStatus = TaskStatus.NEW;
                else {
                    boolean isAllDone = true;
                    for(int i=0; i<subtasks.size() && isAllDone; i++)
                        isAllDone = subtasks.get(i).getStatus() == TaskStatus.DONE;
                    if(isAllDone)
                        newStatus = TaskStatus.DONE;
                    else
                        newStatus = TaskStatus.IN_PROGRESS;
                }
            }
            epicsMap.get(id).setStatus(newStatus);
            return true;
        } else
            return false;

    }

    ArrayList<Subtask> getSubtasksByEpic(int id) {
        ArrayList<Subtask> result = new ArrayList<>();
        for(Subtask st : subtasksMap.values())
            if(st.getEpicId() == id)
                result.add(st);
        return result;
    }
}
