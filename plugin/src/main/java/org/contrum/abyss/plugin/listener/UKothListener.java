package org.contrum.abyss.plugin.listener;

import com.lunarclient.apollo.BukkitApollo;
import com.lunarclient.apollo.module.waypoint.Waypoint;
import me.ulrich.koth.api.KothAPIManager;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.contrum.abyss.AbyssLoader;
import org.contrum.abyss.plugin.AbyssConfig;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UKothListener implements Listener {

    private final JavaPlugin plugin;
    private final AbyssLoader loader;
    private final AbyssConfig config;

    public UKothListener(JavaPlugin plugin, AbyssLoader loader, AbyssConfig config) {
        this.plugin = plugin;
        this.loader = loader;
        this.config = config;

        this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            List<String> active = new ArrayList<>();

            KothAPIManager.getInstance().getActiveEventListData(true).forEach(koth -> {
                String id = "koth_" + koth.getName();
                active.add(id);
                loader.getWaypointModule().addWaypoint(id, Waypoint.builder()
                        .name(koth.getName() + " KoTH")
                        .color(Color.RED)
                        .location(BukkitApollo.toApolloBlockLocation(getCenter(koth.getLoc1(), koth.getLoc2())))
                        .hidden(false)
                        .preventRemoval(true)
                        .build());
            });

            List<String> toRemove = new ArrayList<>();
            loader.getWaypointModule().getWaypoints().forEach((name, wp) -> {
                if (name.startsWith("koth_") && !active.contains(name))
                    toRemove.add(name);
            });

            toRemove.removeIf(id -> {
                loader.getWaypointModule().removeWaypoint(id);
                return true;
            });

        }, 0L, 20L);
    }

    private Location getCenter(Location min, Location max) {
        return new Location(min.getWorld(), min.getX() + (max.getX() - min.getX()) / 2, min.getY() + (max.getY() - min.getY()) / 2, min.getZ() + (max.getZ() - min.getZ()) / 2);
    }

}
