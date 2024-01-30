package com.github.txmy.modules;

import com.github.txmy.ApolloLoader;
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

public class ApolloBorderModule implements ApolloListener {
    private final ApolloLoader loader;
    private final JavaPlugin plugin;

    private final Map<String, Border> borders;
    private final Map<String, List<UUID>> viewers;
    private final BorderModule module;

    public ApolloBorderModule(ApolloLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.borders = new HashMap<>();
        this.viewers = Maps.newConcurrentMap();
        this.module = Apollo.getModuleManager().getModule(BorderModule.class);

        EventBus.getBus().register(this);
    }

    public void resetBorderForPlayer(Player player, String id) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            this.resetBorderForPlayer(apolloPlayer, id);
        });
    }

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

    public void resetBorderForPlayer(ApolloPlayer player, String id) {
        module.removeBorder(player, id);
    }

    public void setBorderForPlayer(Player player, World world, String id, Cuboid2D bounds, Color color) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            this.setBorderForPlayer(apolloPlayer, world, id, bounds, color);
        });
    }

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
