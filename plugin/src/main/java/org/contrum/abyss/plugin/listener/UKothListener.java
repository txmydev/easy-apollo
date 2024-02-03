package org.contrum.abyss.plugin.listener;

import com.lunarclient.apollo.BukkitApollo;
import com.lunarclient.apollo.module.waypoint.Waypoint;
import lombok.AllArgsConstructor;
import me.ulrich.koth.api.KothAPIManager;
import me.ulrich.koth.data.KothData;
import me.ulrich.koth.events.KothEndEvent;
import me.ulrich.koth.events.KothStartEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.contrum.abbys.AbyssLoader;
import org.contrum.abyss.plugin.AbyssConfig;
import org.contrum.abyss.plugin.wrapper.KothWrapper;

import java.util.Optional;

@AllArgsConstructor
public class UKothListener implements Listener {

    private final JavaPlugin plugin;
    private final AbyssLoader loader;
    private final AbyssConfig config;

    @EventHandler
    public void onKothStart(KothStartEvent event) {
        Optional<KothData> kothOptional = KothAPIManager.getInstance().getKoth(event.getKothUUID());
        if (!kothOptional.isPresent()) {
            return;
        }
        KothData koth = kothOptional.get();
        Optional<KothWrapper> wrapperOptional = config.getKothWrapper(koth.getName());
        wrapperOptional.ifPresent(wrapper -> {
            loader.getWaypointModule().addWaypoint(koth.getUuid().toString(), Waypoint.builder()
                    .name(koth.getName())
                    .color(wrapper.getColor())
                    .location(BukkitApollo.toApolloBlockLocation(getCenter(koth.getLoc1(), koth.getLoc2())))
                    .hidden(false)
                    .preventRemoval(true)
                    .build());
        });
    }

    @EventHandler
    public void onKothEnd(KothEndEvent event) {
        Optional<KothData> kothOptional = KothAPIManager.getInstance().getKoth(event.getKothUUID());
        if (!kothOptional.isPresent()) {
            return;
        }
        KothData koth = kothOptional.get();
        Optional<KothWrapper> wrapperOptional = config.getKothWrapper(koth.getName());
        wrapperOptional.ifPresent(wrapper -> {
            loader.getWaypointModule().removeWaypoint(koth.getUuid().toString());
        });
    }
    private Location getCenter(Location min, Location max) {
        return new Location(min.getWorld(), min.getX() + (max.getX() - min.getX()) / 2, min.getY() + (max.getY() - min.getY()) / 2, min.getZ() + (max.getZ() - min.getZ()) / 2);
    }

}
