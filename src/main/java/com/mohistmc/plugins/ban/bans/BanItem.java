package com.mohistmc.plugins.ban.bans;

import com.mohistmc.MohistConfig;
import com.mohistmc.api.ItemAPI;
import com.mohistmc.api.PlayerAPI;
import com.mohistmc.plugins.ban.BanConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/27 2:54:23
 */
public class BanItem {

    public static boolean check(UseOnContext use) {
        return check(use.getPlayer(), use.getItemInHand());
    }

    public static boolean check(net.minecraft.world.entity.player.Player player, ItemStack itemStack) {
        if (player == null) return false;
        if (player.getBukkitEntity().isOp()) return false;
        if (BanEnchantment.check(itemStack)) {
            player.containerMenu.sendAllDataToRemote();
            return true;
        }
        if (check(itemStack)) {
            player.containerMenu.sendAllDataToRemote();
            return true;
        }
        return false;
    }

    public static boolean check(net.minecraft.world.entity.player.Player player) {
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getMainHandItem();
        if (player.getBukkitEntity().isOp()) return false;
        if (check(main)) {
            return true;
        }
        if (check(off)) {
            return true;
        }
        if (checkMoShou(main)) {
            if (player.getBukkitEntity().hasPermission("mohist.ban.item.moshou." + main.asBukkitCopy().getType().name())) {
                return false;
            }
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            return true;
        }
        if (checkMoShou(off)) {
            if (player.getBukkitEntity().hasPermission("mohist.ban.item.moshou." + off.asBukkitCopy().getType().name())) {
                return false;
            }
            player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
            return true;
        }
        return false;
    }

    public static boolean check(ItemStack itemStack) {
        if (!MohistConfig.ban_item_enable) return false;
        return ItemAPI.isBan(CraftItemStack.asCraftMirror(itemStack));
    }

    public static boolean checkMoShou(ItemStack itemStack) {
        if (!MohistConfig.ban_item_enable) return false;
        return BanConfig.MOSHOU.getMoShouList().contains(CraftItemStack.asCraftMirror(itemStack).getType().name());
    }

    public static boolean checkMoShou(net.minecraft.world.entity.player.Player player, ItemStack itemStack) {
        if (player.getBukkitEntity().isOp()) return false;
        return checkMoShou(itemStack);
    }
}
