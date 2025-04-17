package com.mohistmc.ai.koukou.network;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum RequestPath {
    DEFAULT("/"),
    QQ("/qq"),
    UNKNOWN(null);

    private static final Map<String, RequestPath> parse = new HashMap<>();

    static {
        for (RequestPath r : RequestPath.values()) {
            if (r.path != null) {
                parse.put(r.path, r);
            }
        }
    }

    final String path;

    RequestPath(String path) {
        this.path = path;
    }

    public static RequestPath as(String path) {
        return parse.getOrDefault(path, UNKNOWN);
    }

    public boolean isUnknown() {
        return this == UNKNOWN;
    }
}
