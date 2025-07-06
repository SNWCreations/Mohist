package com.mohistmc.ai.koukou.network;

import com.mohistmc.ai.koukou.AIConfig;
import com.mohistmc.mjson.Json;
import java.util.HashMap;
import java.util.Map;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;

public class HttpRequestUtils {

    private static final Map<String, Long> ERROR_URL_CACHE = new HashMap<>();
    private static final long ERROR_URL_EXPIRE_TIME = 10 * 60 * 1000;

    public static String post(String path, Map<String, String> body) {
        String url = AIConfig.INSTANCE.post_server() + path;
        try {
            if (isErrorUrlCached(url)) {
                return null;
            }

            var json = Json.read(body);
            HttpResponse<String> response = Unirest.post(url)
                    .header("User-Agent", "Mohist")
                    .header("Content-Type", "application/json")
                    .body(json.toString())
                    .asString();

            removeErrorUrlCache(url);

            return response.getBody();
        } catch (Exception e) {
            addErrorUrlToCache(url);
            return null;
        }
    }

    private static boolean isErrorUrlCached(String url) {
        Long timestamp = ERROR_URL_CACHE.get(url);
        if (timestamp == null) {
            return false;
        }
        return System.currentTimeMillis() - timestamp < ERROR_URL_EXPIRE_TIME;
    }

    private static void addErrorUrlToCache(String url) {
        ERROR_URL_CACHE.put(url, System.currentTimeMillis());
    }

    private static void removeErrorUrlCache(String url) {
        ERROR_URL_CACHE.remove(url);
    }
}
