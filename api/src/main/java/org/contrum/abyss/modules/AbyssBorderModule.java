package org.contrum.abyss.modules;

import org.contrum.abyss.AbyssLoader;
import com.google.common.collect.Maps;
import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.common.cuboid.Cuboid2D;
import com.lunarclient.apollo.event.ApolloListener;
import com.lunarclient.apollo.event.EventBus;
import com.lunarclient.apollo.event.Listen;
import com.lunarclient.apollo.event.player.ApolloRegisterPlayerEvent;
import com.lunarclient.apollo.event.player.ApolloUnregisterPlayerEvent;
import com.lunarclient.apollo.module.border.Border;
import com.lunarclient.apollo.module.border.BorderModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import com.lunarclient.apollo.recipients.Recipients;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.List;
import java.util.*;

public class AbyssBorderModule implements ApolloListener {
    private final AbyssLoader loader;
    private final JavaPlugin plugin;

    private final Map<String, Border> borders;
    private final Map<String, List<UUID>> viewers;
    private final BorderModule module;

    public AbyssBorderModule(AbyssLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.borders = new HashMap<>();
        this.viewers = Maps.newConcurrentMap();
        this.module = Apollo.getModuleManager().getModule(BorderModule.class);

        EventBus.getBus().register(this);
    }

    /**
     * Removes a border from a player.
     *
     * @param player The player to remove the border from.
     * @param id     The border-id
     */
    public void resetBorderForPlayer(Player player, String id) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            this.resetBorderForPlayer(apolloPlayer, id);
        });
    }

    /**
     * Sets a new border for a specific player.
     *
     * @param player The ApolloPlayer to set the border.
     * @param world  The world that the border will be in.
     * @param id     The border-id
     * @param bounds The region of the border.
     * @param color  The color of the border
     */
    public void setBorderForPlayer(ApolloPlayer player, World world, String id, Cuboid2D bounds, Color color) {
        module.displayBorder(player, Border.builder()
                .id(id)
                .world(world.getName())
                .bounds(bounds)
                .cancelEntry(true)
                .cancelExit(true)
                .canShrinkOrExpand(false)
                .color(color)
                .durationTicks(0)
                .build());
    }

    /**
     * Removes a border from a specific player.
     *
     * @param player The ApolloPlayer to remove the border
     * @param id     The border-id
     */
    public void resetBorderForPlayer(ApolloPlayer player, String id) {
        module.removeBorder(player, id);
    }

    /**
     * Sets a border for a specific player.
     *
     * @param player The bukkit player to send the border.
     * @param world  The world that the border will be in.
     * @param id     The id of the border.
     * @param bounds The region of the border.
     * @param color  The color the border will have.
     */
    public void setBorderForPlayer(Player player, World world, String id, Cuboid2D bounds, Color color) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            this.setBorderForPlayer(apolloPlayer, world, id, bounds, color);
        });
    }

    /**
     * Sets a border for all the players in the world.
     *
     * @param world  The world that the border will be in.
     * @param id     The id of the border
     * @param bounds The region of the border.
     * @param color The color the border will have.
     */
    public void setBorder(World world, String id, Cuboid2D bounds, Color color) {
        boolean existed = borders.containsKey(id);

        if (existed)
            this.borders.remove(id);

        Border border = Border.builder()
                .id(id)
                .world(world.getName())
                .bounds(bounds)
                .cancelEntry(true)
                .cancelExit(true)
                .canShrinkOrExpand(false)
                .color(color)
                .durationTicks(0)
                .build();

        this.viewers.putIfAbsent(id, new ArrayList<>());
        this.borders.put(id, border);

        if (existed) {
            module.removeBorder(Recipients.ofEveryone(), id);
        }

        plugin.getServer().getOnlinePlayers().forEach(player -> {
            Optional<ApolloPlayer> optional = Apollo.getPlayerManager().getPlayer(player.getUniqueId());
            optional.ifPresent(apolloPlayer -> {
                module.displayBorder(apolloPlayer, border);
                viewers.get(id).add(player.getUniqueId());
            });
        });
    }

    /**
     * Removes a border from all the players in the world.
     *
     * @param id The border id to remove.
     */
    public void resetBorder(String id) {
        this.borders.remove(id);

        Apollo.getPlayerManager().getPlayers().forEach(player ->
        {
            Optional<ApolloPlayer> optional = Apollo.getPlayerManager().getPlayer(player.getUniqueId());
            optional.ifPresent(apolloPlayer -> {
                module.removeBorder(apolloPlayer, id);
                viewers.get(id).remove(apolloPlayer.getUniqueId());
            });
        });
    }

    @Listen
    public void onPlayerJoin(ApolloRegisterPlayerEvent event) {
        ApolloPlayer player = event.getPlayer();
        borders.forEach((id, border) ->
        {
            List<UUID> viewers = this.viewers.get(id);
            if (!viewers.contains(player.getUniqueId())) {
                module.displayBorder(player, border);
                viewers.add(player.getUniqueId());
            }
        });
    }

    @Listen
    public void onPlayerLeave(ApolloUnregisterPlayerEvent event) {
        ApolloPlayer player = event.getPlayer();
        borders.forEach((id, border) ->
        {
            List<UUID> viewers = this.viewers.get(id);
            if (viewers.contains(player.getUniqueId())) {
                module.removeBorder(player, id);
                viewers.remove(player.getUniqueId());
            }
        });
    }

}
