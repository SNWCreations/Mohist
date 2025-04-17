package com.mohistmc.ai.koukou;

import com.mohistmc.ai.koukou.entity.MessageRequest;
import com.mohistmc.ai.koukou.network.event.BaseListener;
import com.mohistmc.ai.koukou.network.event.HttpPostEvent;
import com.mohistmc.mjson.Json;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.command.ColouredConsoleSender;

public class KouKouPostListener implements BaseListener {

    @SneakyThrows
    public void onEvent(HttpPostEvent event) {
        Json json = event.getJson();
        if (event.isQQ()) {
            MessageRequest request = json.asBean(MessageRequest.class);
            String t = request.getMessage_type();
            var group = request.getGroup_id();
            String msg = request.getRaw_message();
            String sender_nickname = request.getSender().getNickname();
            if (t != null && t.equals("group")) {
                if (AIConfig.INSTANCE.chat_post_group().contains(String.valueOf(group))) {
                    if (AIConfig.INSTANCE.command_enable() && AIConfig.INSTANCE.command_owners().contains(String.valueOf(request.getUser_id()))) {
                        String label = AIConfig.INSTANCE.command_name();
                        if (msg.startsWith(label)) {
                            String cmd = msg.replace(label + " ", "");
                            Bukkit.dispatchCommand(new ColouredConsoleSender().group(group), cmd);
                            return;
                        }
                    }
                    msg = msg.replaceAll("§\\S", "");
                    msg = msg.replace("__color__", "§");
                    String pattern = "\\[CQ:.*?]";
                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(msg);
                    if (!m.find() && AIConfig.INSTANCE.group_to_server()) {
                        Bukkit.broadcastMessage("[%s] <%s>: %s".formatted(AIConfig.INSTANCE.server_name(), sender_nickname, msg));
                    }

                }
            }
        }
    }
}
