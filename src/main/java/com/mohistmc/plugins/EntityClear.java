package com.mohistmc.plugins;

import com.mohistmc.MohistConfig;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.NamedThreadFactory;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/25 23:56:03
 */
public class EntityClear {

    public static final ScheduledExecutorService ENTITYCLEAR_ITEM = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("EntityClear - Item"));
    public static final ScheduledExecutorService ENTITYCLEAR_NOITEM = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("EntityClear - NoItem"));
    private static final ScheduledExecutorService COUNTDOWN_SERVICE = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("EntityClear-Countdown"));
    private static final AtomicInteger countdownSeconds = new AtomicInteger(31);
    private static ScheduledFuture<?> countdownFuture;

    public static void start() {
        if (MohistConfig.clear_enable) {
            ENTITYCLEAR_ITEM.scheduleAtFixedRate(() -> {
                if (MinecraftServer.getServer().hasStopped()) {
                    return;
                }
                startCountdown();
            }, MohistConfig.clear_time, MohistConfig.clear_time, TimeUnit.SECONDS);
        }
    }

    public static void startCountdown() {
        countdownFuture = COUNTDOWN_SERVICE.scheduleAtFixedRate(() -> {
            int remaining = countdownSeconds.decrementAndGet();
            if (remaining > 0) {
                String msg = MohistConfig.clear_countdown_msg
                        .replace("&", "§")
                        .replace("%seconds%", String.valueOf(remaining));
                if (remaining == 30 || remaining == 14 || remaining == 10 || remaining < 4) Bukkit.broadcastMessage(msg);
            } else {
                countdownSeconds.set(31);
                if (MohistConfig.clear_item)run_item();
                if (MohistConfig.clear_noitem)run_entity();
                countdownFuture.cancel(false);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static void stop() {
        ENTITYCLEAR_ITEM.shutdown();
        ENTITYCLEAR_NOITEM.shutdown();
    }

    public static void run_item() {
        AtomicInteger size_item = new AtomicInteger(0);
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Item item) {
                    String itemName = item.getItemStack().getType().name();
                    String itemRegName = item.getItemStack().getType().name().split("_")[0].toLowerCase() + ":*";
                    if (!MohistConfig.clear_item_whitelist.contains(itemName) && !MohistConfig.clear_item_whitelist.contains(itemRegName)) {
                        entity.remove();
                        size_item.addAndGet(1);
                    }
                }
            }
        }
        if (!MohistConfig.clear_item_msg.isEmpty()){
            Bukkit.broadcastMessage(MohistConfig.clear_item_msg.replace("&", "§").replace("%size%", String.valueOf(size_item.getAndSet(0))));
        }
    }

    public static void run_entity() {
        AtomicInteger size_noitem = new AtomicInteger(0);
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof Item)) {
                    String entityName = entity.getType().name();
                    String entityRegName = entity.getType().name().split("_")[0].toLowerCase() + ":*";
                    if (!MohistConfig.clear_noitem_whitelist.contains(entityName) && !MohistConfig.clear_noitem_whitelist.contains(entityRegName)&& entity.getCustomName() == null) {
                        if (entity instanceof TamableAnimal tamable && tamable.isTame() || entity instanceof Player) {
                           continue;
                        }
                        entity.remove();
                        size_noitem.addAndGet(1);
                    }
                }
            }
        }
        if (!MohistConfig.clear_noitem_msg.isEmpty()){
            Bukkit.broadcastMessage(MohistConfig.clear_noitem_msg.replace("&", "§").replace("%size%", String.valueOf(size_noitem.getAndSet(0))));
        }
    }
}
