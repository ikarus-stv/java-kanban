package api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.Managers;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
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

        if (METHOD_GET.equals(method)) {
            if ("/history".equals(path)) {
                history(exchange);
            } else if ("/prioritized".equals(path)) {
                prioritized(exchange);
            }
        }
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
