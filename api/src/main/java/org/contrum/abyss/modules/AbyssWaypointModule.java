package org.contrum.abyss.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.event.ApolloListener;
import com.lunarclient.apollo.event.EventBus;
import com.lunarclient.apollo.event.Listen;
import com.lunarclient.apollo.event.player.ApolloRegisterPlayerEvent;
import com.lunarclient.apollo.module.waypoint.Waypoint;
import com.lunarclient.apollo.module.waypoint.WaypointModule;
import com.lunarclient.apollo.option.ListOption;
import com.lunarclient.apollo.recipients.Recipients;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.contrum.abyss.AbyssLoader;
;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class AbyssWaypointModule implements ApolloListener {
    private final AbyssLoader loader;
    private final JavaPlugin plugin;
    private final WaypointModule module;

    @Getter
    private final Map<String, Waypoint> waypoints;


    public AbyssWaypointModule(AbyssLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(WaypointModule.class);
        this.waypoints = new HashMap<>();

        EventBus.getBus().register(this);
    }

    /**
     * Adds a waypoint and display's it to every player in the server
     *
     * @param name The name of the waypoint
     * @param waypoint The constructed waypoint to display
     */
    public void addWaypoint(String name, Waypoint waypoint) {
        this.waypoints.put(name, waypoint);
        this.module.displayWaypoint(Recipients.ofEveryone(), waypoint);
    }

    /**
     * Adds a waypoint to a specific player.
     *
     * @param player The player to display the waypoint
     * @param waypoint The constructed waypoint to display
     */
    public void addWaypoint(Player player, Waypoint waypoint) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            this.module.displayWaypoint(apolloPlayer, waypoint);
        });
    }


    /**
     * Removes a waypoint from a specific player.
     *
     * @param player The player to remove the waypoint
     * @param name The name of the waypoint
     */
    public void removeWaypoint(Player player, String name) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            this.module.removeWaypoint(apolloPlayer, name);
        });
    }

    /**
     * Removes a waypoint from every player in the server
     *
     * @param name The name of the waypoint
     */
    public void removeWaypoint(String name) {
        module.removeWaypoint(Recipients.ofEveryone(), this.waypoints.remove(name));
        module.removeWaypoint(Recipients.ofEveryone(), name);
    }

    @Listen
    private void onPlayerRegister(ApolloRegisterPlayerEvent event) {
        waypoints.forEach((name, waypoint) -> {
            module.displayWaypoint(event.getPlayer(), waypoint);
        });
    }


}
