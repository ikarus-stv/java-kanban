package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getAllTasks();

    ArrayList<Task> getAllSubtasks();

    ArrayList<Task> getAllEpics();

    void clearAllTasks();

    void clearAllSubtasks();

    void clearAllEpics();

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    boolean createTask(Task task);

    boolean createSubtask(Subtask subtask);

    void createEpic(Epic epic);

    boolean updateTask(Task task);

    boolean updateSubtask(Subtask subtask);

    boolean updateEpic(Epic epic);

    boolean deleteTask(int id);

    boolean deleteEpic(int id);

    boolean deleteSubtask(int id);

    List<Subtask> getSubtasksByEpic(int id);

    public List<Task> getPrioritizedTasks();
}
