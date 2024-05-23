package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Subtask;
import tasks.Task;
import tasks.Epic;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager hm;

    @BeforeEach
    public void initEnv() {
        hm = new InMemoryHistoryManager();
    }

    @Test
    public void addIsSuccess() {
        hm.add(new Task("1", "11"));
        hm.add(new Epic("2", "22"));
        hm.add(new Subtask("3", "333", 1));
        assertEquals(3, hm.getHistory().size());
    }

}