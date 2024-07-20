package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerAsInMemoryTest extends TaskManagerTest<FileBackedTaskManager> {

    @BeforeEach
    public void beforeEach() {
        try {
            tm = new FileBackedTaskManager(File.createTempFile("TMP", null).getAbsolutePath());
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}
