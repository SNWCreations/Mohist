package com.mohistmc.bukkit;

import net.minecraftforge.eventbus.api.IEventBus;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author SNWCreations
 */
public final class MarkerPlugin extends PluginBase {
    public static final MarkerPlugin INSTANCE = new MarkerPlugin();
    private final PluginDescriptionFile pdf;

    private MarkerPlugin() {
        this.pdf = new PluginDescriptionFile("Mohist", "1.20.1", "nms");
    }

    @Override
    public @NotNull File getDataFolder() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public @NotNull PluginDescriptionFile getDescription() {
        return pdf;
    }

    @Override
    public @NotNull FileConfiguration getConfig() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public @Nullable InputStream getResource(@NotNull String filename) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void saveConfig() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void saveDefaultConfig() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void saveResource(@NotNull String resourcePath, boolean replace) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void reloadConfig() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public @NotNull PluginLoader getPluginLoader() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public @NotNull Server getServer() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public boolean isNaggable() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setNaggable(boolean canNag) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull String worldName, @Nullable String id) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public @NotNull Logger getLogger() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean callForge() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void initCallForge() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void registerForgeEvent(IEventBus bus, Object target) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void registerForgeEvent(Object target) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void unregisterForgeEvents(IEventBus bus, Object target) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void unregisterForgeEvents(IEventBus bus) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void unregisterAllForgeEvents() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
