package org.contrum.abyss.example;

import com.lunarclient.apollo.BukkitApollo;
import com.lunarclient.apollo.common.cuboid.Cuboid2D;
import com.lunarclient.apollo.module.waypoint.Waypoint;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.contrum.abyss.AbyssLoader;
import org.contrum.abyss.AbyssLoaderOptions;
import org.contrum.abyss.modules.AbyssBorderModule;
import org.contrum.abyss.modules.AbyssWaypointModule;
import org.contrum.abyss.event.AbyssLoadEvent;
import org.contrum.abyss.modules.AbyssCombatModule;
import org.contrum.abyss.modules.AbyssTitleModule;

import java.awt.*;
import java.time.Duration;

public class ExampleAbbysPlugin extends JavaPlugin implements Listener {

    private AbyssLoader loader;

    @Override
    public void onEnable() {
        this.loader = new AbyssLoader(this,
                AbyssLoaderOptions.builder()
                        .downloadIfMissing(true)
                        .restartUponLoadingError(true)
                        .progressReport(progress -> "Downloading apollo-bukkit... (" + progress + "%/100%)")
                        .errorReport(throwable -> "Error ocurred while downloading apollo-bukkit, " + throwable.getLocalizedMessage())
                        .finishedReport(bytes -> "Completed downloading apollo-bukkit, attempting to initiate the plugin restarting the server..")
                        .build());

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onAbyssLoad(AbyssLoadEvent event) {
        // You can start using the api
        AbyssBorderModule borderModule = loader.getBorderModule();
        borderModule.setBorder(Bukkit.getWorld("world"), "world-border", Cuboid2D.builder()
                .minX(-1000)
                .minZ(-1000)
                .maxX(1000)
                .maxZ(1000)
                .build(), Color.decode("#ffc0cb"));

        AbyssCombatModule combatModule = loader.getCombatModule();
        combatModule.setDisableHitMissPenalty(true);

        AbyssWaypointModule waypointModule = loader.getWaypointModule();
        Location kothLocation = new Location(this.getServer().getWorld("world"), 1500, 64, 1500);
        waypointModule.addWaypoint("koth", Waypoint.builder()
                .name("Koth")
                .color(Color.RED)
                .location(BukkitApollo.toApolloBlockLocation(kothLocation))
                .preventRemoval(false)
                .hidden(false)
                .build());

        loader.perform(Bukkit.getOnlinePlayers(), (player, runningLunar) -> {
            if (!runningLunar) {
                player.sendMessage(ChatColor.RED + "You are not running Lunar Client, please consider using it to improve your experience.");
            } else {
                AbyssTitleModule titleModule = loader.getTitleModule();
                titleModule.sendTitle(player, "You are running", "Lunar Client!", Duration.ofSeconds(1), Duration.ofSeconds(1), Duration.ofSeconds(1), 1.0f);
            }
        });
    }
}
