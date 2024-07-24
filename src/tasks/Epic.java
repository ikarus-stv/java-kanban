package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private LocalDateTime endTime = LocalDateTime.MAX;

    transient private final List<Subtask> subtasks = new ArrayList<>();

    public Epic() {
    }

    public Epic(String name, String description) {
        super(name, description);
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

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    public void addSubtask(Subtask subtask) {
        if (!subtasks.contains(subtask)) {
            subtasks.add(subtask);
            recalc();
        }
    }

    public void dirtyAddSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void recalc() {
        recalcStatus();
        recalcTimes();
    }

    public void recalcTimes() {
        startTime = LocalDateTime.MAX;
        duration = Duration.ofMinutes(0);
        endTime = LocalDateTime.MIN;

        for (Subtask st : subtasks) {
            if (st.startTime != null && st.startTime.isBefore(startTime)) {
                startTime = st.startTime;
            }
            var et = st.getEndTime();
            if (et != null && et.isAfter(endTime)) {
                endTime = et;
            }
            Duration stDuration = st.getDuration();
            if (stDuration != null) {
                duration = duration.plus(stDuration);
            }
        }
    }

    public void recalcStatus() {
        tasks.TaskStatus newStatus;
        if (subtasks.isEmpty())
            newStatus = tasks.TaskStatus.NEW;
        else {
            boolean isAllNew = true;
            for (int i = 0; i < subtasks.size() && isAllNew; i++)
                isAllNew = subtasks.get(i).getStatus() == tasks.TaskStatus.NEW;
            if (isAllNew)
                newStatus = tasks.TaskStatus.NEW;
            else {
                boolean isAllDone = true;
                for (int i = 0; i < subtasks.size() && isAllDone; i++)
                    isAllDone = subtasks.get(i).getStatus() == tasks.TaskStatus.DONE;
                if (isAllDone)
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
            recalc();
        }
    }

    public void clearAllSubtasks() {
        subtasks.clear();
        recalc();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public List<String> toStreamableList() {
        List<String> result = super.toStreamableList();
        result.add(dateTimeToStream(endTime));
        return result;
    }

    @Override
    protected void fromStreamableList(List<String> streamableList) {
        super.fromStreamableList(streamableList);
        endTime = dateTimeFromStream(streamableList.get(7));
    }
}
