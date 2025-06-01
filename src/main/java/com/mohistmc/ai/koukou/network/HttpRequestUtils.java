package com.mohistmc.ai.koukou.network;

import com.mohistmc.ai.koukou.AIConfig;
import com.mohistmc.mjson.Json;
import java.util.Map;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;

public class HttpRequestUtils {

    public static String post(String path, Map<String, String> body) {
        try {
            var json = Json.read(body);
            String url = AIConfig.INSTANCE.post_server() + path;
            HttpResponse<String> response = Unirest.post(url)
                    .header("User-Agent", "Mohist")
                    .header("Content-Type", "application/json")
                    .body(json.toString())
                    .asString();
            return response.getBody();
        }  catch (Exception e) {
            return null;
        }
    }
}
