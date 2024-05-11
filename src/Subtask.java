public class Subtask extends Task {
        private int epicId;

        public int getEpicId() {
            return epicId;
        }

        void updateFrom(Subtask subtask) {
            super.updateFrom(subtask);
            this.epicId = subtask.epicId;
        }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", epicId=" + epicId +

                '}';
    }
}
