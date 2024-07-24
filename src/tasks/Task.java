package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected TaskStatus status;

    protected Duration duration;

    protected LocalDateTime startTime;

    public static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task() {
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public Task(String name, String description, TaskStatus taskStatus, Duration duration, LocalDateTime dt) {
        this.name = name;
        this.description = description;
        this.status = taskStatus;
        this.duration = duration;
        this.startTime = dt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            if (duration != null) {
                return startTime.plus(duration);
            } else {
                return startTime;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task oAsTask = (Task) o;
        return getId() == oAsTask.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public void updateFrom(Task task) {
        this.id = task.id;
        this.name = task.name;
        this.description = task.description;
        this.status = task.status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    public List<String> toStreamableList() {
        var result = new ArrayList<String>();
        result.add(Integer.toString(getId()));                          // 0
        result.add(getTaskType().name());                               // 1
        result.add(getName());                                          // 2
        result.add(getStatus().name());                                 // 3
        result.add(getDescription());                                   // 4
        result.add(dateTimeToStream(getStartTime()));                   // 5
        result.add(durationToStream(getDuration()));                    // 6
        return result;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    protected void fromStreamableList(List<String> streamableList) {
        this.setId(Integer.parseInt(streamableList.get(0)));
        this.setName(streamableList.get(2));
        this.setStatus(TaskStatus.valueOf(streamableList.get(3)));
        this.setDescription(streamableList.get(4));
        this.setStartTime(dateTimeFromStream(streamableList.get(5)));
        this.setDuration(durationFromStream(streamableList.get(6)));
    }

    public static Task taskFromStreamableList(List<String> streamableList) {
        Task result = null;
        switch (TaskType.valueOf(streamableList.get(1).toUpperCase())) {
            case TASK:
                result = new Task();
                break;
            case SUBTASK:
                result = new Subtask();
                break;
            case EPIC:
                result = new Epic();
                break;
            default:
                return null;
        }
        result.fromStreamableList(streamableList);

        return result;
    }

    public static String dateTimeToStream(LocalDateTime time) {
        String result = "";
        if (time != null) {
            result = time.format(DT_FORMATTER);
        }
        return result;
    }

    public static LocalDateTime dateTimeFromStream(String stime) {
        return LocalDateTime.parse(stime, DT_FORMATTER);
    }

    public static String durationToStream(Duration duration) {
        String result = "";
        if (duration != null) {
            result = Long.toString(duration.toMinutes());
        }
        return result;
    }

    public static Duration durationFromStream(String sduration) {
        return Duration.ofMinutes(Long.parseLong(sduration));
    }


}
