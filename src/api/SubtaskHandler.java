package api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import managers.TaskNotFoundException;
import managers.TasksIntersectsException;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {


    TaskManager mgr;

    public SubtaskHandler(TaskManager mgr) {
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
                    getById(exchange, id);
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
        }

    }

    private void delete(HttpExchange exchange, int id) throws IOException {
        mgr.deleteSubtask(id);
        sendText(exchange, "OK");
    }

    private void post(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String name = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Gson gson = createGson();
        Subtask task = gson.fromJson(name, Subtask.class);
        Predicate<Subtask> method = task.getId() == 0 ? mgr::createSubtask : mgr::updateSubtask;
        method.test(task);
        send201(exchange);
    }

    private void getById(HttpExchange exchange, int id) throws IOException {
        Subtask task = mgr.getSubtaskById(id);
        Gson gson = createGson();
        String jsonString = gson.toJson(task);
        sendText(exchange, jsonString);
    }

    private void getAll(HttpExchange exchange) throws IOException {
        var allTasks = mgr.getAllSubtasks();
        Gson gson = createGson();
        String jsonString = gson.toJson(allTasks);
        sendText(exchange, jsonString);
    }

}
