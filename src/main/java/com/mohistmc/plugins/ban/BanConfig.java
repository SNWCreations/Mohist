package com.mohistmc.plugins.ban;

import com.mohistmc.plugins.config.MohistPluginConfig;
import java.io.File;
import java.util.List;
import org.bukkit.ChatColor;

public class BanConfig extends MohistPluginConfig {

    public static BanConfig MOSHOU;
    public static BanConfig BAN_MESSAGE;

    public BanConfig(File file) {
        super(file);
    }

    public static void init() {
        MOSHOU = new BanConfig(new File("mohist-config/bans", "item-moshou.yml"));
        BAN_MESSAGE = new BanConfig(new File("mohist-config/bans", "item-message.yml"));
    }

    public void addMoShou(String name) {
        if (!has("ITEMS")) {
            put("ITEMS", List.of());
        }
        List<String> list = MOSHOU.yaml.getStringList("ITEMS");
        list.add(name);
        put("ITEMS", list);
    }

    public List<String> getMoShouList() {
        return (!has("ITEMS")) ? List.of() : MOSHOU.yaml.getStringList("ITEMS");
    }

    public String getMessage(String name) {
        String message = (!has(name)) ? "" : BAN_MESSAGE.yaml.getString(name);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public void setBanMessage(String key, Object v) {
        BAN_MESSAGE.yaml.set(key, v);
        save();
    }
}
