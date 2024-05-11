public class Epic extends Task {

    public Epic(String name, String description) {
        super(name, description);
    }

    void updateFrom(Epic epic) {
        TaskStatus oldStatus = status;
        super.updateFrom((epic));
        status = oldStatus;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
