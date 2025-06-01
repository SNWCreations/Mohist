package com.mohistmc.ai.koukou;

import com.mohistmc.ai.koukou.network.HttpRequestUtils;
import com.mohistmc.mjson.Json;
import java.util.HashMap;
import java.util.Objects;

public class KouKou {

    public static void sendToGroup(String message) {
        if (!AIConfig.INSTANCE.enable()) return;
        for (String groupId : AIConfig.INSTANCE.chat_post_group()) {
            send_group_msg(String.valueOf(groupId), message);
        }
    }

    public static void sendToGroup(String groupId, String message) {
        if (AIConfig.INSTANCE.enable()) {
            send_group_msg(String.valueOf(groupId), message);
        }
    }

    public static void chat(String message) {
        if (AIConfig.INSTANCE.server_to_group()) {
            sendToGroup(message);
        }
    }

    public static void death(String message) {
        if (AIConfig.INSTANCE.death()) {
            sendToGroup(message);
        }
    }

    public static void send_group_msg(String group_id, String message) {
        HashMap<String, String> param = new HashMap<>();
        param.put("group_id", group_id);
        param.put("message", message);
        var string = HttpRequestUtils.post("/send_group_msg", param);
        if (string == null) {
            debug("string == null");
            return;
        }
        debug(string);
        var json = Json.read(string);
        if (Objects.equals(json.asString("status"), "failed")) {
            debug("发送失败");
            return;
        }
        debug("返回数据: " + json);
    }

    public static void debug(String debug_message) {
        if (AIConfig.INSTANCE.debug()) ApiController.LOGGER.info(debug_message);
    }
}
