package org.contrum.abyss.plugin;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.contrum.abyss.AbyssLoader;
import org.contrum.abyss.AbyssLoaderOptions;
import org.contrum.abyss.plugin.command.ClanRallyCommand;
import org.contrum.abyss.plugin.listener.ClansListener;
import org.contrum.abyss.plugin.listener.UKothListener;
import org.contrum.abyss.plugin.serializable.BorderSerializer;
import org.contrum.abyss.plugin.serializable.WaypointSerializer;
import org.contrum.abyss.plugin.wrapper.KothWrapper;

import java.util.stream.Collectors;

public class AbyssPlugin extends JavaPlugin {

    private AbyssLoader loader;

    @Getter
    private AbyssConfig abyssConfig;

    private ClanRallyCommand rallyCommand;

    @Override
    public void onEnable() {
        this.loader = new AbyssLoader(this, AbyssLoaderOptions.builder()
                .downloadIfMissing(true)
                .restartUponLoadingError(true)
                .build());

        saveDefaultConfig();

        abyssConfig = new AbyssConfig();
        FileConfiguration config = getConfig();

        if (config.contains("borders")) {
            abyssConfig.setBorders(config.getStringList("borders").stream().map(BorderSerializer::deserialize).collect(Collectors.toList()));
        }

        if (config.contains("waypoints")) {
            abyssConfig.setWaypoints(config.getStringList("waypoints").stream().map(WaypointSerializer::deserialize).collect(Collectors.toList()));
        }

        if (config.contains("kothDisplays")) {
            abyssConfig.setKothDisplays(config.getStringList("kothDisplays").stream().map(KothWrapper::deserialize).collect(Collectors.toList()));
        }

        if (config.contains("rallyPermission")) {
            abyssConfig.setRallyPermission(config.getString("rallyPermission"));
        }

        if (config.contains("rallyMessage")) {
            abyssConfig.setRallyMessage(config.getString("rallyMessage"));
        }

        if (config.contains("rallyDurationMinutes")) {
            abyssConfig.setRallyDurationMinutes(config.getInt("rallyDurationMinutes"));
        }

        if (config.contains("rallyWaypointFormat")) {
            abyssConfig.setRallyWaypointFormat(config.getString("rallyWaypointFormat"));
        }

        if (config.contains("rallyWaypointColorHex")) {
            abyssConfig.setRallyWaypointColorHex(config.getString("rallyWaypointColorHex"));
        }

        if (config.contains("rallyOnlyLeader")) {
            abyssConfig.setRallyOnlyLeader(config.getBoolean("rallyOnlyLeader"));
        }

        boolean useUKoths = getServer().getPluginManager().getPlugin("UltimateKoth") != null;
        boolean useUClans = getServer().getPluginManager().getPlugin("UltimateClans") != null;

        if (useUClans) {
            rallyCommand = new ClanRallyCommand(this, abyssConfig, loader);
            getLogger().info("Detected UltimateClans, initiating /rally command...");
            getCommand("rally").setExecutor(rallyCommand);

            getServer().getPluginManager().registerEvents(new ClansListener(this, loader), this);
        }

        if (useUKoths) {
            getLogger().info("Detected UltimateKoth, initiating koth waypoints...");
            getServer().getPluginManager().registerEvents(new UKothListener(this, loader, abyssConfig), this);
        }

    }
}
