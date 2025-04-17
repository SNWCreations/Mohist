package com.mohistmc.ai.koukou.network.event;

import java.util.EventListener;

public interface BaseListener extends EventListener {
    void onEvent(HttpPostEvent e);
}
