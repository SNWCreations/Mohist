package com.mohistmc.commands;

import com.mohistmc.MohistConfig;
import com.mohistmc.api.EntityAPI;
import com.mohistmc.api.ItemAPI;
import com.mohistmc.api.gui.GUIItem;
import com.mohistmc.api.gui.ItemStackFactory;
import com.mohistmc.api.gui.Warehouse;
import com.mohistmc.plugins.ban.BanConfig;
import com.mohistmc.plugins.ban.BanListener;
import com.mohistmc.plugins.ban.BanType;
import com.mohistmc.plugins.ban.utils.BanSaveInventory;
import com.mohistmc.plugins.ban.utils.BanUtils;
import com.mohistmc.util.I18n;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/27 5:33:43
 */
public class BansCommand extends Command {

    private final List<String> params = Arrays.asList("add", "show");
    private final List<String> params1 = Arrays.asList("item", "item-moshou", "entity", "enchantment");
    public BansCommand(String name) {
        super(name);
        this.description = I18n.as("banscmd.description");
        this.usageMessage = "/bans [add|show] [item|item-moshou|entity|enchantment]";
        this.setPermission("mohist.command.bans");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!testPermission(sender)) {
            return false;
        }
        String check = I18n.as("banscmd.check");

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to perform this command.");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "add" -> {
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + usageMessage);
                    return false;
                }
                switch (args[1]) {
                    case "item" -> {
                        if (!MohistConfig.ban_item_enable) {
                            sender.sendMessage(ChatColor.RED + check);
                            return false;
                        }
                        BanSaveInventory banSaveInventory = new BanSaveInventory(BanType.ITEM, "§4Add bans item");
                        Inventory inventory = banSaveInventory.getInventory();
                        player.openInventory(inventory);
                        BanListener.openInventory = banSaveInventory;
                        return true;
                    }
                    case "item-moshou" -> {
                        if (!MohistConfig.ban_item_enable) {
                            sender.sendMessage(ChatColor.RED + check);
                            return false;
                        }
                        BanSaveInventory banSaveInventory = new BanSaveInventory(BanType.ITEM_MOSHOU, "§4Add bans moshou item");
                        Inventory inventory = banSaveInventory.getInventory();
                        player.openInventory(inventory);
                        BanListener.openInventory = banSaveInventory;
                        return true;
                    }
                    case "entity" -> {
                        if (!MohistConfig.ban_entity_enable) {
                            sender.sendMessage(ChatColor.RED + check);
                            return false;
                        }
                        BanSaveInventory banSaveInventory = new BanSaveInventory(BanType.ITEM, "§4Add bans entity");
                        Inventory inventory = banSaveInventory.getInventory();
                        player.openInventory(inventory);
                        BanListener.openInventory = banSaveInventory;
                        return true;
                    }
                    case "enchantment" -> {
                        if (!MohistConfig.ban_enchantment_enable) {
                            sender.sendMessage(ChatColor.RED + check);
                            return false;
                        }
                        BanSaveInventory banSaveInventory = new BanSaveInventory(BanType.ITEM, "§4Add bans enchantment");
                        Inventory inventory = banSaveInventory.getInventory();
                        player.openInventory(inventory);
                        BanListener.openInventory = banSaveInventory;
                        return true;
                    }
                    default -> {
                        sender.sendMessage(ChatColor.RED + usageMessage);
                        return false;
                    }
                }
            }
            case "show" -> {
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + usageMessage);
                    return false;
                }
                switch (args[1]) {
                    case "item" -> {
                        Warehouse wh = new Warehouse(I18n.as("banscmd.show.item"));
                        List<String> old = MohistConfig.ban_item_materials;
                        for (String s : MohistConfig.ban_item_materials) {
                            Material material = ItemAPI.getMaterial(s);
                            if (!material.isEmpty()) {
                                wh.addItem(new GUIItem(new ItemStackFactory(material)
                                        .setDisplayName(s)
                                        .toItemStack()) {
                                    @Override
                                    public void ClickAction(ClickType type, Player u, ItemStack itemStack) {
                                        if (type.isRightClick()) {
                                            old.remove(s);
                                            BanUtils.saveToYaml(old, BanType.ITEM);
                                            wh.removeItem(this);
                                            wh.openGUI(player);
                                        }
                                    }
                                });
                            }
                        }
                        wh.openGUI(player);
                        return true;
                    }
                    case "item-moshou" -> {
                        Warehouse wh = new Warehouse(I18n.as("banscmd.show.item-moshou"));
                        List<String> old = BanConfig.MOSHOU.getMoShouList();
                        for (String s : BanConfig.MOSHOU.getMoShouList()) {
                            Material material = ItemAPI.getMaterial(s);
                            if (!material.isEmpty()) {
                                wh.addItem(new GUIItem(new ItemStackFactory(material)
                                        .setDisplayName(s)
                                        .toItemStack()) {
                                    @Override
                                    public void ClickAction(ClickType type, Player u, ItemStack itemStack) {
                                        if (type.isRightClick()) {
                                            old.remove(s);
                                            BanUtils.saveToYaml(old, BanType.ITEM_MOSHOU);
                                            wh.removeItem(this);
                                            wh.openGUI(player);
                                        }
                                    }
                                });
                            }
                        }
                        wh.openGUI(player);
                        return true;
                    }
                    case "entity" -> {
                        Warehouse wh = new Warehouse(I18n.as("banscmd.show.entity"));
                        List<String> old = MohistConfig.ban_entity_types;
                        for (String s : MohistConfig.ban_entity_types) {
                            wh.addItem(new GUIItem(new ItemStackFactory(ItemAPI.getEggMaterial(EntityAPI.entityType(s)))
                                    .setDisplayName(s)
                                    .toItemStack()) {
                                @Override
                                public void ClickAction(ClickType type, Player u, ItemStack itemStack) {
                                    if (type.isRightClick()) {
                                        old.remove(s);
                                        BanUtils.saveToYaml(old, BanType.ENTITY);
                                        wh.removeItem(this);
                                        wh.openGUI(player);
                                    }
                                }
                            });
                        }
                        wh.openGUI(player);
                        return true;
                    }
                    case "enchantment" -> {
                        Warehouse wh = new Warehouse(I18n.as("banscmd.show.enchantment"));
                        List<String> old = MohistConfig.ban_enchantment_list;
                        for (String s : MohistConfig.ban_enchantment_list) {
                            wh.addItem(new GUIItem(new ItemStackFactory(Material.ENCHANTED_BOOK)
                                    .setDisplayName(s)
                                    .setEnchantment(ItemAPI.getEnchantmentByKey(s))
                                    .toItemStack()) {
                                @Override
                                public void ClickAction(ClickType type, Player u, ItemStack itemStack) {
                                    if (type.isRightClick()) {
                                        old.remove(s);
                                        BanUtils.saveToYaml(old, BanType.ENCHANTMENT);
                                        wh.removeItem(this);
                                        wh.openGUI(player);
                                    }
                                }
                            });
                        }
                        wh.openGUI(player);
                        return true;
                    }
                    default -> {
                        sender.sendMessage(ChatColor.RED + usageMessage);
                        return false;
                    }
                }
            }
            default -> {
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
            }
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1 && (sender.isOp() || testPermission(sender))) {
            for (String param : params) {
                if (param.toLowerCase().startsWith(args[0].toLowerCase())) {
                    list.add(param);
                }
            }
        }
        if (args.length == 2 && (sender.isOp() || testPermission(sender))) {
            for (String param : params1) {
                if (param.toLowerCase().startsWith(args[1].toLowerCase())) {
                    list.add(param);
                }
            }
        }

        return list;
    }
}
