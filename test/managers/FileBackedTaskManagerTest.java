package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    @Test
    public void createFromEmpty() {
        FileBackedTaskManager tmp = FileBackedTaskManager.loadFromFile(tempFile());
        Assertions.assertNotEquals(tmp, null);

    }

    @Test
    public void saveEmpty() {
        FileBackedTaskManager tmp = FileBackedTaskManager.loadFromFile(tempFile());
        Assertions.assertNotEquals(tmp, null);
        tmp.save();
    }

    @Test
    public void saveTasks() {
        File tfile = tempFile();
        FileBackedTaskManager tm = FileBackedTaskManager.loadFromFile(tfile);
        Task task = new Task("task1", "description 1");
        tm.createTask(task);
        Task task2 = new Task("task2", "description 2");
        tm.createTask(task2);
        Epic epic = new Epic("Epic1", "descr epic 1");
        tm.createEpic(epic);

        int epicId = epic.getId();
        assertNotEquals(0, epicId);

        Subtask trueST = new Subtask("Subtask1", "Subtask2", epicId);
        assertTrue(tm.createSubtask(trueST));

        try (BufferedReader br = new BufferedReader(new FileReader(tfile))) {

            assertEquals(br.readLine(), "id,type,name,status,description,epic");
            assertEquals(br.readLine(), "1,TASK,task1,NEW,description 1");
            assertEquals(br.readLine(), "2,TASK,task2,NEW,description 2");
            assertEquals(br.readLine(), "4,SUBTASK,Subtask1,NEW,Subtask2,3");
            assertEquals(br.readLine(), "3,EPIC,Epic1,NEW,descr epic 1");

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    @Test
    public void loadTasks() {
        File tfile = tempFile();
        try (FileWriter fos = new FileWriter(tfile)) {
            fos.write("id,type,name,status,description,epic\n");
            fos.write("1,TASK,task1,NEW,description 1\n");
            fos.write("2,TASK,task2,NEW,description 2\n");
            fos.write("4,SUBTASK,Subtask1,NEW,Subtask2,3\n");
            fos.write("3,EPIC,Epic1,NEW,descr epic 1\n");
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

        FileBackedTaskManager tm = FileBackedTaskManager.loadFromFile(tfile);
        assertEquals(tm.epicsMap.toString(), "{3=tasks.Epic{id=3, name='Epic1', description='descr epic 1', status=NEW, subtasks=[tasks.Subtask{id=4, name='Subtask1', description='Subtask2', status=NEW, epicId=3}]}}");
        String tasksStr = tm.taskMap.toString();
        assertEquals(tasksStr, "{1=tasks.Task{id=1, name='task1', description='description 1', status=NEW}, 2=tasks.Task{id=2, name='task2', description='description 2', status=NEW}}");
    }

    File tempFile() {
        File result = null;
        try {
            result = File.createTempFile("TMP", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
