package com.mohistmc.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MohistStartDoneEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public MohistStartDoneEvent() {
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
