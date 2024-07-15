package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

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
        task.setStartTime(LocalDateTime.of(2024, 07, 10, 12, 30));
        task.setDuration(Duration.ofMinutes(20));

        tm.createTask(task);
        Task task2 = new Task("task2", "description 2");
        task2.setStartTime(LocalDateTime.of(2024, 07, 11, 17, 15));
        task2.setDuration(Duration.ofMinutes(15));
        tm.createTask(task2);
        Epic epic = new Epic("Epic1", "descr epic 1");
        tm.createEpic(epic);

        int epicId = epic.getId();
        assertNotEquals(0, epicId);

        Subtask trueST = new Subtask("Subtask1", "Subtask2", epicId);
        trueST.setStartTime(LocalDateTime.of(2024, 07, 12, 15, 20));
        trueST.setDuration(Duration.ofMinutes(15));
        assertTrue(tm.createSubtask(trueST));

        try (BufferedReader br = new BufferedReader(new FileReader(tfile))) {

            assertEquals(br.readLine(), "id,type,name,status,description,epic");
            assertEquals(br.readLine(), "1,TASK,task1,NEW,description 1,10.07.2024 12:30,20");
            assertEquals(br.readLine(), "2,TASK,task2,NEW,description 2,11.07.2024 17:15,15");
            assertEquals(br.readLine(), "4,SUBTASK,Subtask1,NEW,Subtask2,12.07.2024 15:20,15,3");
            assertEquals(br.readLine(), "3,EPIC,Epic1,NEW,descr epic 1,12.07.2024 15:20,0,12.07.2024 15:35");

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    @Test
    public void loadTasks() {
        File tfile = tempFile();
        try (FileWriter fos = new FileWriter(tfile)) {
            fos.write("id,type,name,status,description,epic\n");
            fos.write("1,TASK,task1,NEW,description 1,10.07.2024 12:30,20\n");
            fos.write("2,TASK,task2,NEW,description 2,11.07.2024 17:15,15\n");
            fos.write("4,SUBTASK,Subtask1,NEW,Subtask2,12.07.2024 15:20,15,3\n");
            fos.write("3,EPIC,Epic1,NEW,descr epic 1,12.07.2024 15:20,0,12.07.2024 15:35\n");
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }


        FileBackedTaskManager tm = FileBackedTaskManager.loadFromFile(tfile);
        assertEquals(tm.epicsMap.toString(), "{3=tasks.Epic{id=3, name='Epic1', description='descr epic 1', status=NEW, subtasks=[tasks.Subtask{id=4, name='Subtask1', description='Subtask2', status=NEW, epicId=3}]}}");
        String tasksStr = tm.taskMap.toString();
        assertEquals(tasksStr, "{1=Task{id=1, name='task1', description='description 1', status=NEW, duration=PT20M, startTime=2024-07-10T12:30}, 2=Task{id=2, name='task2', description='description 2', status=NEW, duration=PT15M, startTime=2024-07-11T17:15}}");


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


    @Test
    public void createFromNotExists() {

        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager tmp = FileBackedTaskManager.loadFromFile(new File("---"));
            ;
        }, "Инициализация несуществующим файлом должна приводить к исключению");

    }

}
