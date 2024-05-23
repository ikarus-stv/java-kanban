package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    // проверьте, что экземпляры класса Task равны друг другу, если равен их id;
    @Test
    void taskEqualsIfIDEquals() {
        Task task1 = new Task("1", "desc 1");
        task1.setId(1);

        Task task2 = new Task("2", "desc 2");
        task2.setId(1);

        assertEquals(task1, task2);
    }

    // проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    void epicsEqualsIfIDEquals() {
        Epic epic1 = new Epic("1", "desc 1");
        epic1.setId(1);

        Epic epic2 = new Epic("2", "desc 2");
        epic2.setId(1);

        assertEquals(epic1, epic2);
    }

    @Test
    void subtasksEqualsIfIDEquals() {
        Subtask subtask1 = new Subtask("1", "desc 1", 5);
        subtask1.setId(1);

        Subtask subtask2 = new Subtask("2", "desc 2", 7);
        subtask2.setId(1);

        assertEquals(subtask1, subtask2);
    }

}