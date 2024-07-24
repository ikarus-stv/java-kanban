package api;

import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;


public class HttpTaskServer {
    HttpServer httpServer;
    TaskManager mgr;

    public HttpTaskServer(TaskManager mgr) throws IOException {
        this.mgr = mgr;

        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/task", new TaskHandler(mgr));
        httpServer.createContext("/subtask", new SubtaskHandler(mgr));
        httpServer.createContext("/epics", new EpicHandler(mgr));
        httpServer.createContext("/history", new OtherHandler(mgr));
        httpServer.createContext("/prioritized", new OtherHandler(mgr));
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(10);
    }

    public static void main(String[] args) throws IOException {
        TaskManager tm = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setStartTime(LocalDateTime.of(2024, 7, 10, 12, 30));
        task1.setDuration(Duration.ofMinutes(20));
        tm.createTask(task1);

        Task task2 = new Task("Задача 2", "Описание задачи 2");
        task2.setStartTime(LocalDateTime.of(2021, 2, 12, 10, 15));
        task2.setDuration(Duration.ofMinutes(40));
        tm.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        tm.createEpic(epic1);

        Subtask st11 = new Subtask("Подзадача 1.1", "Описание 1.1", epic1.getId());
        st11.setStartTime(LocalDateTime.of(2024, 7, 3, 12, 30));
        st11.setDuration(Duration.ofMinutes(12));
        tm.createSubtask(st11);
        Subtask st12 = new Subtask("Подзадача 1.2", "Описание 1.2", epic1.getId());
        st12.setStartTime(LocalDateTime.of(2024, 6, 2, 12, 30));
        st12.setDuration(Duration.ofMinutes(13));
        tm.createSubtask(st12);


        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        tm.createEpic(epic2);

        Subtask st21 = new Subtask("Подзадача 2.1", "Описание 2.1", epic2.getId());
        st21.setStartTime(LocalDateTime.of(2024, 5, 1, 11, 20));
        st21.setDuration(Duration.ofMinutes(13));
        tm.createSubtask(st21);

        HttpTaskServer server = new HttpTaskServer(tm);
        server.start();
/*
        Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create(); // завершаем построение объекта

        ArrayList<Task> allTasks = tm.getAllTasks();
        String jsonstr = gson.toJson(allTasks);
        System.out.println(jsonstr);
*/
    }
}
