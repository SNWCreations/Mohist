package com.mohistmc.ai.koukou.network.event;

import com.mohistmc.ai.koukou.network.RequestPath;
import com.mohistmc.mjson.Json;
import java.util.EventObject;

public class HttpPostEvent extends EventObject {

    private final Json json;
    private final RequestPath requestPath;

    public HttpPostEvent(Object source, Json json, RequestPath requestPath) {
        super(source);
        this.json = json;
        this.requestPath = requestPath;
    }

    public Json getJson() {
        return json;
    }

    public boolean isQQ() {
        return requestPath == RequestPath.QQ;
    }
}
