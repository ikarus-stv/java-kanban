package managers;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    T tm;

    @Test
    void createTaskIsSuccess() {
        Task task = new Task("task1", "description 1");
        tm.createTask(task);
        int id = task.getId();
        assertNotEquals(0, id);
        assertNotEquals(null, tm.getTaskById(id));
    }

    @Test
    void createEpicIsSuccess() {
        Epic epic = new Epic("Epic1", "descr1");
        tm.createEpic(epic);
        int id = epic.getId();
        assertNotEquals(0, id);
        assertNotEquals(null, tm.getEpicById(id));
    }


    @Test
    void createSubtaskWithoutEpicIsUnSuccess() {
        Subtask st = new Subtask("1", "2", 2);
        assertFalse(tm.createSubtask(st));
    }

    @Test
    void createSubtaskWithEpicIsSuccess() {

        Epic epic = new Epic("1", "2");
        tm.createEpic(epic);
        int epicId = epic.getId();
        assertNotEquals(0, epicId);

        Subtask trueST = new Subtask("1", "2", epicId);
        assertTrue(tm.createSubtask(trueST));

        Subtask wrongST = new Subtask("1", "2", epicId + 100);
        assertFalse(tm.createSubtask(wrongST));


    }

    // создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    void taskIsUnchangedWhenRegister() {
        String taskName = "task1";
        String taskDescription = "description 1";

        Task task = new Task(taskName, taskDescription);
        tm.createTask(task);
        int id = task.getId();
        assertNotEquals(0, id);
        assertEquals(taskName, task.getName());
        assertEquals(taskDescription, task.getDescription());
    }

    // Удаляемые подзадачи не должны хранить внутри себя старые id
    @Test
    void deletingSubtaskIsClearsId() {
        Epic epic = new Epic("1", "2");
        tm.createEpic(epic);
        int epicId = epic.getId();
        assertNotEquals(0, epicId);

        Subtask trueST = new Subtask("1", "2", epicId);
        assertTrue(tm.createSubtask(trueST));

        int subTaskID = trueST.getId();
        assertNotEquals(0, subTaskID);

        tm.deleteSubtask(subTaskID);
        assertEquals(0, trueST.getId());

    }

    @Test
    void calcEpicStatusAllSubtasksAreNew() {

        Epic epic = new Epic("1", "2");
        tm.createEpic(epic);
        int epicId = epic.getId();
        assertNotEquals(0, epicId);

        Subtask st1 = new Subtask("Subtask1", "Subtask1_description", epicId);
        st1.setStatus(TaskStatus.NEW);
        assertTrue(tm.createSubtask(st1));

        Subtask st2 = new Subtask("Subtask2", "Subtask2_description", epicId);
        st2.setStatus(TaskStatus.NEW);
        assertTrue(tm.createSubtask(st2));

        assertEquals(TaskStatus.NEW, epic.getStatus());

    }

    @Test
    void calcEpicStatusAllSubtasksAreDone() {

        Epic epic = new Epic("1", "2");
        tm.createEpic(epic);
        int epicId = epic.getId();
        assertNotEquals(0, epicId);

        Subtask st1 = new Subtask("Subtask1", "Subtask1_description", epicId);
        st1.setStatus(TaskStatus.DONE);
        assertTrue(tm.createSubtask(st1));

        Subtask st2 = new Subtask("Subtask2", "Subtask2_description", epicId);
        st2.setStatus(TaskStatus.DONE);
        assertTrue(tm.createSubtask(st2));

        assertEquals(TaskStatus.DONE, epic.getStatus());

    }

    @Test
    void calcEpicStatusSubtasksAreNewAndDone() {

        Epic epic = new Epic("1", "2");
        tm.createEpic(epic);
        int epicId = epic.getId();
        assertNotEquals(0, epicId);

        Subtask st1 = new Subtask("Subtask1", "Subtask1_description", epicId);
        st1.setStatus(TaskStatus.NEW);
        assertTrue(tm.createSubtask(st1));

        Subtask st2 = new Subtask("Subtask2", "Subtask2_description", epicId);
        st2.setStatus(TaskStatus.DONE);
        assertTrue(tm.createSubtask(st2));

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());

    }

    @Test
    void calcEpicStatusSubtasksAreINProgress() {

        Epic epic = new Epic("1", "2");
        tm.createEpic(epic);
        int epicId = epic.getId();
        assertNotEquals(0, epicId);

        Subtask st1 = new Subtask("Subtask1", "Subtask1_description", epicId);
        st1.setStatus(TaskStatus.IN_PROGRESS);
        assertTrue(tm.createSubtask(st1));

        Subtask st2 = new Subtask("Subtask2", "Subtask2_description", epicId);
        st2.setStatus(TaskStatus.IN_PROGRESS);
        assertTrue(tm.createSubtask(st2));

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());

    }

    @Test
    void TimesAreIntersects() {

        Epic epic = new Epic("1", "2");
        tm.createEpic(epic);
        int epicId = epic.getId();
        assertNotEquals(0, epicId);

        Subtask st1 = new Subtask("Subtask1", "Subtask1_description", epicId);
        st1.setStatus(TaskStatus.IN_PROGRESS);
        st1.setStartTime(LocalDateTime.of(2024, 1, 10, 12, 15));
        st1.setDuration(Duration.ofMinutes(10));
        assertTrue(tm.createSubtask(st1));

        Subtask st2 = new Subtask("Subtask2", "Subtask2_description", epicId);
        st2.setStatus(TaskStatus.IN_PROGRESS);
        st2.setStartTime(LocalDateTime.of(2024, 1, 10, 12, 15));
        st2.setDuration(Duration.ofMinutes(10));
        assertFalse(tm.createSubtask(st2));

    }
}
