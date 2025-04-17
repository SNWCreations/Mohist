package com.mohistmc.ai.deepseek;

import com.mohistmc.MohistConfig;
import com.mohistmc.mjson.Json;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DeepSeek {

    public static void init(Player player, String msg) {
        if (MohistConfig.deepseek_enable && player.hasPermission("mohist.ai.deepseek")) {
            String cmd = MohistConfig.deepseek_command + " ";
            if (msg.startsWith(cmd)) {
                String message = msg.replace(cmd, "");
                CompletableFuture.supplyAsync(() -> chat(message))
                        .thenAccept(reply -> Bukkit.broadcastMessage(MohistConfig.deepseek_chatfromat.formatted(reply)));
            }
        }
    }

    public static String chat(String msg) {
        ChatRequest request = new ChatRequest();
        request.setModel(MohistConfig.deepseek_model);
        request.setFrequency_penalty(0);
        request.setMax_tokens(2048);
        request.setPresence_penalty(0);
        ChatRequest.ResponseFormat responseFormat = new ChatRequest.ResponseFormat();
        responseFormat.setType("text");
        request.setResponse_format(responseFormat);
        request.setStop(null);
        request.setStream(false);
        request.setStream_options(null);
        request.setTemperature(1);
        request.setTop_p(1);
        request.setTools(null);
        request.setTool_choice("none");
        request.setLogprobs(false);
        request.setTop_logprobs(null);

        ChatRequest.Message systemMessage = new ChatRequest.Message();
        systemMessage.setRole("system");
        systemMessage.setContent(MohistConfig.deepseek_system);

        ChatRequest.Message userMessage = new ChatRequest.Message();
        userMessage.setRole("user");
        userMessage.setContent(msg);

        request.setMessages(List.of(systemMessage, userMessage));
        HttpResponse<String> response = Unirest.post("https://api.deepseek.com/chat/completions")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer %s".formatted(MohistConfig.deepseek_apikey))
                .body(Json.readBean(request).toString())
                .asString();
        Json json = Json.read(response.getBody());
        ChatCompletion chatCompletion = json.asBean(ChatCompletion.class);
        return chatCompletion.getChoices()[0].getMessage().getContent();
    }
}
