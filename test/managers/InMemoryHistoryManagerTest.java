package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Subtask;
import tasks.Task;
import tasks.Epic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager hm;

    @BeforeEach
    public void initEnv() {
        hm = new InMemoryHistoryManager();
    }

    @Test
    public void addIsSuccess() {
        Task t1 = new Task("1", "11");
        t1.setId(1);

        hm.add(t1);

        Task epic1 = new Epic("2", "22");
        epic1.setId(2);
        hm.add(epic1);

        Task subtask1 = new Subtask("3", "333", 1);
        subtask1.setId(3);

        hm.add(subtask1);
        assertEquals(3, hm.getHistory().size());
    }

    @Test
    public void taskIsReplacing() {
        Task t1 = new Task("1", "11");
        t1.setId(1);

        hm.add(t1);

        Task epic1 = new Epic("2", "22");
        epic1.setId(2);
        hm.add(epic1);

        Task subtask1 = new Subtask("3", "333", 1);
        subtask1.setId(3);
        hm.add(subtask1);

        t1.setName("New 1");
        hm.add(t1);

        List<Task> history = hm.getHistory();

        assertEquals(3, history.size());
        assertEquals("New 1", history.getLast().getName());

    }


}