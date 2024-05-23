package managers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    // убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
    @Test
    void getDefaultIsNotNull() {
        TaskManager tm = Managers.getDefault();
        assertNotEquals(null, tm);
        assertEquals(0, tm.getAllEpics().size());
    }

    @Test
    void getDefaultHistory() {
        HistoryManager hm = Managers.getDefaultHistory();
        assertNotEquals(null, hm);
        assertEquals(0, hm.getHistory().size());
    }
}