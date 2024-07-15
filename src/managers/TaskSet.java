package managers;

import tasks.Task;
import tasks.TaskType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TaskSet extends TreeSet<Task> {
    public TaskSet() {
        super(Comparator.comparing(Task::getStartTime));
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(this);
    }

    public void addOrUpdateTask(Task task) {
        //return task.getStartTime() != null && super.add(task);

        if (contains(task)) {
            remove(task);
        }

        if (task.getStartTime() != null) {
            add(task);
        }
    }

    public boolean canAddUpdateTask(Task task) {
        if (task.getStartTime() == null) {
            return true;
        }

        //return stream().filter( t-> t.getId() != task.getId() && tasksIntersects(t, task)).findFirst().isEmpty();

        return stream().noneMatch(t -> t.getId() != task.getId() && tasksIntersects(t, task));
    }

    static boolean tasksIntersects(Task t1, Task t2) {
        if (t1.getStartTime() == null || t2.getStartTime() == null) {
            return false;
        }

        return (t1.getStartTime().compareTo(t2.getEndTime()) < 0 && t2.getStartTime().compareTo(t1.getEndTime()) < 0);
    }


    public void clearAllTasks() {
        List<Task> list = stream().filter(t -> t.getTaskType() == TaskType.TASK).collect(Collectors.toList());
        list.forEach(t -> remove(t));
    }

    public void clearAllSubtasks() {
        List<Task> list = stream().filter(t -> t.getTaskType() == TaskType.SUBTASK).collect(Collectors.toList());
        list.forEach(t -> remove(t));
    }
}
