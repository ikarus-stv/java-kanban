package api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.Managers;
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

public class OtherHandler extends BaseHttpHandler implements HttpHandler {


    TaskManager mgr;

    public OtherHandler(TaskManager mgr) {
        this.mgr = mgr;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        System.out.println(path);

        if ("GET".equals(method)) {
            if ("/history".equals(path)) {
                history(exchange);
            } else if ("/prioritized".equals(path)) {
                prioritized(exchange);
            }
        }

            /*
            if ("GET".equals(method)) {
                if (id == NO_ID) {
                    getAll(exchange);
                } else {
                    if(splitPath.length>3 && "subtasks".equals(splitPath[3])) {
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

             */
    }

    private void prioritized(HttpExchange exchange) throws IOException {
        List<Task> prioritizedTasks = mgr.getPrioritizedTasks();
        Gson gson = createGson();
        String jsonString = gson.toJson(prioritizedTasks);
        sendText(exchange, jsonString);
    }

    private void history(HttpExchange exchange) throws IOException {
        List<Task> historyList = Managers.getDefaultHistory().getHistory();
        Gson gson = createGson();
        String jsonString = gson.toJson(historyList);
        sendText(exchange, jsonString);
    }

}
