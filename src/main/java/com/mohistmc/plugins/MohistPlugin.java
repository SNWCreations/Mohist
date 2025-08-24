package com.mohistmc.plugins;

import com.mohistmc.MohistConfig;
// Mohist+ start - Remove AI stuff
/*
import com.mohistmc.ai.koukou.AIConfig;
import com.mohistmc.ai.koukou.ApiController;
*/
// Mohist+ end
// Mohist+ start - Remove this feature as it should be implemented by a plugin
/*
import com.mohistmc.plugins.back.BackCommands;
import com.mohistmc.plugins.back.BackConfig;
*/
// Mohist+ end
import com.mohistmc.plugins.ban.BanConfig;
import com.mohistmc.plugins.ban.BanListener;
import com.mohistmc.plugins.item.ItemsConfig;
// Mohist+ start - Remove these features as it should be implemented by a plugin
/*
import com.mohistmc.plugins.pluginmanager.Control;
import com.mohistmc.plugins.tpa.TpaComamands;
import com.mohistmc.plugins.tpa.TpacceptCommands;
import com.mohistmc.plugins.tpa.TpadenyCommands;
import com.mohistmc.plugins.warps.WarpsCommands;
import com.mohistmc.plugins.warps.WarpsConfig;
import com.mohistmc.plugins.world.WorldManage;
import com.mohistmc.plugins.world.commands.WorldsCommands;
import com.mohistmc.plugins.world.listener.InventoryClickListener;
*/
// Mohist+ end
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

/**
 * @author Mgazul by MohistMC
 * @date 2023/6/14 14:46:34
 */
public class MohistPlugin {

    public static Plugin plugin;

    public static Logger LOGGER = LogManager.getLogger("MohistPlugin");

    public static void init(Server server) {
        // if (MohistConfig.yml.getBoolean("worldmanage", true)) WorldManage.onEnable(); // Mohist+ - Remove this feature as it should be implemented by a plugin
        // Mohist+ start - Remove the internal Mohist plugin, we use our own way to implement this placeholder
        /*
        File out = new File("libraries/com/mohistmc/cache", "libPath.txt");
        if (out.exists()) {
            String data;
            try {
                data = Files.readString(out.toPath());
            } catch (IOException e) {
                data = "libraries";
            }
            File file = new File(data, "com/mohistmc/mohistplugins/mohistplugins-1.20.1.jar");
            if (file.exists()) {
                plugin = Control.loadPlugin(file);
                if (plugin != null) {
                    server.getPluginManager().enablePlugin(plugin);
                } else {
                    LOGGER.error("Failed to load mohistplugins.jar");
                }
            }
        }
        */
        // Mohist+ end
        // EntityClear.start(); // Mohist+ - Remove this feature as it should be implemented by a plugin
        // ApiController.init(); // Mohist+ - Remove AI stuff
    }

    public static void initConfig() {
        ItemsConfig.init();
        // Mohist+ start - Remove these features as it should be implemented by a plugin
        /*
        BackConfig.init();
        WarpsConfig.init();
        */
        // Mohist+ end
        BanConfig.init();
        // AIConfig.init(); // Mohist+ - Remove AI stuff
    }

    public static void registerCommands(Map<String, Command> map) {
        // Mohist+ start - Remove these features as they should be implemented by a plugin
        /*
        if (MohistConfig.yml.getBoolean("worldmanage", true)) {
            map.put("worlds", new WorldsCommands("worlds"));
        }
        map.put("warps", new WarpsCommands("warps"));
        if (MohistConfig.yml.getBoolean("tpa.enable", false)) {
            map.put("tpa", new TpaComamands("tpa"));
            map.put("tpadeny", new TpadenyCommands("tpadeny"));
            map.put("tpaccept", new TpacceptCommands("tpaccept"));
        }
        if (MohistConfig.yml.getBoolean("back.enable", false)) {
            map.put("back", new BackCommands("back"));
        }
        */
        // Mohist+ end
    }

    public static void registerListener(Event event) {
        // Mohist+ start - Remove this feature as it should be implemented by a plugin
        /*
        if (event instanceof InventoryClickEvent inventoryClickEvent) {
            InventoryClickListener.init(inventoryClickEvent);
        }
        */
        // Mohist+ end
        if (event instanceof PrepareAnvilEvent prepareAnvilEvent) {
            EnchantmentFix.anvilListener(prepareAnvilEvent);
        }
        if (event instanceof InventoryCloseEvent event1) {
            BanListener.save(event1);
        }
        // Mohist+ start - Remove these features as it should be implemented by a plugin
        /*
        if (event instanceof PluginEnableEvent event1) {
            PluginHooks.register(event1);
        }
        if (event instanceof PlayerTeleportEvent event1) {
            BackCommands.hookTeleport(event1);
        }
        if (event instanceof PlayerDeathEvent event1) {
            BackCommands.hooktDeath(event1);
        }
        */
        // Mohist+ end
    }

}
