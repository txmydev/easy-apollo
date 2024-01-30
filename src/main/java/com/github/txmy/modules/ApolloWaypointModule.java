package com.github.txmy.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.event.ApolloListener;
import com.lunarclient.apollo.event.EventBus;
import com.lunarclient.apollo.event.Listen;
import com.lunarclient.apollo.event.player.ApolloRegisterPlayerEvent;
import com.lunarclient.apollo.module.waypoint.Waypoint;
import com.lunarclient.apollo.module.waypoint.WaypointModule;
import com.lunarclient.apollo.recipients.Recipients;
import com.github.txmy.ApolloLoader;;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ApolloWaypointModule implements ApolloListener {
    private final ApolloLoader loader;
    private final JavaPlugin plugin;
    private final WaypointModule module;

    private final Map<String, Waypoint> waypoints;


    public ApolloWaypointModule(ApolloLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(WaypointModule.class);
        this.waypoints = new HashMap<>();

        EventBus.getBus().register(this);
    }

    public void addWaypoint(String name, Waypoint waypoint) {
        this.waypoints.put(name, waypoint);
        this.module.displayWaypoint(Recipients.ofEveryone(), waypoint);
    }

    public void removeWaypoint(String name) {
        this.waypoints.remove(name);
        this.module.removeWaypoint(Recipients.ofEveryone(), name);
    }

    @Listen
    public void onPlayerRegister(ApolloRegisterPlayerEvent event) {
        waypoints.forEach((name, waypoint) -> {
            module.displayWaypoint(event.getPlayer(), waypoint);
        });
    }


}
