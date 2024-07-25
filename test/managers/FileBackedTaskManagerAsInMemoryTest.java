package managers;

import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;

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
