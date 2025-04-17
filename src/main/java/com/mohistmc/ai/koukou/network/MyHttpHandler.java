package com.mohistmc.ai.koukou.network;

import com.mohistmc.ai.koukou.ApiController;
import com.mohistmc.ai.koukou.network.event.HttpPostEvent;
import com.mohistmc.mjson.Json;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;

public class MyHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        String requestMethod = t.getRequestMethod();
        String requestPath = t.getRequestURI().getPath();
        RequestPath p = RequestPath.as(requestPath);
        if (requestMethod.equalsIgnoreCase("POST")) {
            t.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            if (p.isUnknown()) {
                close(t);
            } else {
                post(t, p);
            }
        } else {
            close(t);
        }
    }

    @SneakyThrows
    private void close(HttpExchange t) {
        t.sendResponseHeaders(404, 0);
        OutputStream os = t.getResponseBody();
        os.close();
    }

    @SneakyThrows
    private void post(HttpExchange t, RequestPath path) {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        Json json = Json.read(requestBody.toString());
        HttpPostEvent event = new HttpPostEvent(this, json, path);
        ApiController.eventBus.onEvent(event);
        byte[] responseBytes = json.toString().getBytes(StandardCharsets.UTF_8);
        t.sendResponseHeaders(200, responseBytes.length);
        OutputStream os = t.getResponseBody();
        os.write(responseBytes);
        os.close();
    }
}
