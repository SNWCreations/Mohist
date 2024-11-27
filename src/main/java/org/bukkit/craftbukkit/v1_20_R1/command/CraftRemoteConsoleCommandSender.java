package org.bukkit.craftbukkit.v1_20_R1.command;

import net.minecraft.network.chat.Component;
import net.minecraft.server.rcon.RconConsoleSource;
import org.bukkit.command.RemoteConsoleCommandSender;

import java.net.SocketAddress;

public class CraftRemoteConsoleCommandSender extends ServerCommandSender implements RemoteConsoleCommandSender {

    private final RconConsoleSource listener;

    public CraftRemoteConsoleCommandSender(RconConsoleSource listener) {
        this.listener = listener;
    }

    public RconConsoleSource getListener() {
        return listener;
    }

    @Override
    public SocketAddress getAddress() {
        return listener.socketAddress;
    }

    @Override
    public void sendMessage(String message) {
        listener.sendSystemMessage(Component.literal(message + "\n")); // Send a newline after each message, to preserve formatting.
    }

    @Override
    public void sendMessage(String... messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public String getName() {
        return "Rcon";
    }

    // Paper start
    @Override
    public net.kyori.adventure.text.Component name() {
        return net.kyori.adventure.text.Component.text(this.getName());
    }
    // Paper end

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of remote controller.");
    }
}
