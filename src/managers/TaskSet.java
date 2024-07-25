package managers;

import tasks.Task;
import tasks.TaskType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class TaskSet extends TreeSet<Task> {
    public TaskSet() {
        super(Comparator.comparing(Task::getStartTime));
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(this);
    }

    public void addOrUpdateTask(Task task) {
        if (task.getStartTime() == null) {
            return;
        }

        if (contains(task)) {
            remove(task);
        }

        add(task);
    }

    public boolean canAddUpdateTask(Task task) {
        if (task.getStartTime() == null) {
            return true;
        }

        return stream().noneMatch(t -> t.getId() != task.getId() && tasksIntersects(t, task));
    }

    static boolean tasksIntersects(Task t1, Task t2) {
        if (t1.getStartTime() == null || t2.getStartTime() == null) {
            return false;
        }

        return (t1.getStartTime().isBefore(t2.getEndTime()) && t2.getStartTime().isBefore(t1.getEndTime()));
    }


    public void clearAllTasks() {
        List<Task> list = stream().filter(t -> t.getTaskType() == TaskType.TASK).toList();
        list.forEach(this::remove);
    }

    public void clearAllSubtasks() {
        List<Task> list = stream().filter(t -> t.getTaskType() == TaskType.SUBTASK).toList();
        list.forEach(this::remove);
    }
}
