package tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected TaskStatus status;

    public Task() {
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
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
        return "tasks.Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    public List<String> toStreamableList() {
        var result = new ArrayList<String>();
        result.add(Integer.toString(getId()));                          // 0
        result.add(getTaskType().name());                               // 1
        result.add(getName());                                          // 2
        result.add(getStatus().name());                                 // 3
        result.add(getDescription());                                   // 4
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

}
