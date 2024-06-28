package tasks;

import java.util.List;

public class Subtask extends Task {
    private int epicId;

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    void updateFrom(Subtask subtask) {
        super.updateFrom(subtask);
        this.epicId = subtask.epicId;
    }

    public Subtask() {
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    @Override
    public List<String> toStreamableList() {
        List<String> streamableList = super.toStreamableList();
        streamableList.add(Integer.toString(getEpicId()));   // 5
        return streamableList;
    }

    @Override
    protected void fromStreamableList(List<String> streamableList) {
        super.fromStreamableList(streamableList);
        setEpicId(Integer.parseInt(streamableList.get(5)));
    }

    @Override
    public String toString() {
        return "tasks.Subtask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", epicId=" + epicId +

                '}';
    }
}
