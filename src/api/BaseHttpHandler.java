package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {
    protected static final int NO_ID = -1;

    protected static final String METHOD_GET = "GET";
    protected static final String METHOD_POST = "POST";
    protected static final String METHOD_DELETE = "DELETE";


    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void send404(HttpExchange h) throws IOException {
        sendStat(h, 404);
    }

    protected void send406(HttpExchange h) throws IOException {
        sendStat(h, 406);
    }

    protected void send201(HttpExchange h) throws IOException {
        sendStat(h, 201);
    }

    protected void sendStat(HttpExchange h, int rCode) throws IOException {
        h.sendResponseHeaders(rCode, 0);
        h.close();
    }


    public static Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

    }
}
