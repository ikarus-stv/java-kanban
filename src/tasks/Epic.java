package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new ArrayList<>();
    }

    public void updateFrom(Epic epic) {
        TaskStatus oldStatus = status;
        super.updateFrom((epic));
        status = oldStatus;
    }


    @Override
    public String toString() {
        return "tasks.Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtasks=" + subtasks +
                '}';
    }

    public void addSubtask(Subtask subtask) {
        if (!subtasks.contains(subtask)) {
            subtasks.add(subtask);
            recalcStatus();
        }
    }

    public void recalcStatus() {
        tasks.TaskStatus newStatus;
        if(subtasks.isEmpty())
            newStatus = tasks.TaskStatus.NEW;
        else {
            boolean isAllNew = true;
            for(int i=0; i<subtasks.size() && isAllNew; i++)
                isAllNew = subtasks.get(i).getStatus() == tasks.TaskStatus.NEW;
            if(isAllNew)
                newStatus = tasks.TaskStatus.NEW;
            else {
                boolean isAllDone = true;
                for(int i=0; i<subtasks.size() && isAllDone; i++)
                    isAllDone = subtasks.get(i).getStatus() == tasks.TaskStatus.DONE;
                if(isAllDone)
                    newStatus = tasks.TaskStatus.DONE;
                else
                    newStatus = tasks.TaskStatus.IN_PROGRESS;
            }
        }
        setStatus(newStatus);
    }

    public void removeSubtask(Subtask subtask) {
        if (subtasks.contains(subtask)) {
            subtasks.remove(subtask);
            recalcStatus();
        }
    }

    public void clearAllSubtasks() {
        subtasks.clear();
        recalcStatus();
    }

}
