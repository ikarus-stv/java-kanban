package api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import managers.TaskNotFoundException;
import managers.TasksIntersectsException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {


    TaskManager mgr;

    public EpicHandler(TaskManager mgr) {
        this.mgr = mgr;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        String[] splitPath = path.split("/");
        int id = NO_ID;

        if (splitPath.length > 2) {
            id = Integer.parseInt(splitPath[2]);
        }

        try {
            if ("GET".equals(method)) {
                if (id == NO_ID) {
                    getAll(exchange);
                } else {
                    if (splitPath.length > 3 && "subtasks".equals(splitPath[3])) {
                        getSubtasksByEpic(exchange, id);
                    } else {
                        getById(exchange, id);
                    }
                }
            } else if ("POST".equals(method)) {
                post(exchange);
            } else if ("DELETE".equals(method) && id != NO_ID) {
                delete(exchange, id);
            }
        } catch (TaskNotFoundException e) {
            send404(exchange);
        } catch (TasksIntersectsException e) {
            send406(exchange);
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private void getSubtasksByEpic(HttpExchange exchange, int id) throws IOException {
        List<Subtask> subtasksByEpic = mgr.getSubtasksByEpic(id);
        Gson gson = createGson();
        String jsonString = gson.toJson(subtasksByEpic);
        sendText(exchange, jsonString);
    }

    private void delete(HttpExchange exchange, int id) throws IOException {
        mgr.deleteEpic(id);
        sendText(exchange, "OK");
    }

    private void post(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String name = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Gson gson = createGson();
        Epic epic = gson.fromJson(name, Epic.class);
        if (epic.getId() == 0) {
            mgr.createEpic(epic);
        } else {
            mgr.updateEpic(epic);
        }
        send201(exchange);
    }

    private void getById(HttpExchange exchange, int id) throws IOException {
        Epic epic = mgr.getEpicById(id);
        Gson gson = createGson();
        String jsonString = gson.toJson(epic);
        sendText(exchange, jsonString);
    }

    private void getAll(HttpExchange exchange) throws IOException {
        List<Task> allEpic = mgr.getAllEpics();
        Gson gson = createGson();
        String jsonString = gson.toJson(allEpic);
        sendText(exchange, jsonString);
    }
}
