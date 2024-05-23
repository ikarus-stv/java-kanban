package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    InMemoryTaskManager tm;
    @BeforeEach
    public void beforeEach() {
        tm = new InMemoryTaskManager();
    }

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

        Subtask wrongST = new Subtask("1", "2", epicId+100);
        assertFalse(tm.createSubtask(wrongST));


    }

    // создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test

    void taskIsUnchangedWhenRegister() {
        String taskName = "task1";
        String taskDescription = "description 1";

        Task task = new Task( taskName, taskDescription);
        tm.createTask(task);
        int id = task.getId();
        assertNotEquals(0, id);
        assertEquals(taskName, task.getName());
        assertEquals(taskDescription, task.getDescription());
    }

}