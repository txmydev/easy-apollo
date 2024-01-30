package com.github.txmy.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.BukkitApollo;
import com.lunarclient.apollo.module.team.TeamMember;
import com.lunarclient.apollo.module.team.TeamModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import com.lunarclient.apollo.recipients.Recipients;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import com.github.txmy.ApolloLoader;;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ApolloTeamModule {

    private final ApolloLoader loader;
    private final JavaPlugin plugin;
    private final TeamModule module;

    public ApolloTeamModule(ApolloLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(TeamModule.class);
    }

    public void update(ApolloPlayer player, List<TeamMember> members) {
        module.updateTeamMembers(player, members);
    }

    public void remove(ApolloPlayer player) {
        module.resetTeamMembers(player);
    }

    public void update(Player player, List<TeamMember> members)
    {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            update(apolloPlayer, members);
        });
    }

    public void remove(Player player)  {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            remove(apolloPlayer);
        });
    }

    public void update(Player player,
                       List<Player> teammates,
                       Color color,
                       Function<Player, Component> displayNameFunction) {
        List<TeamMember> list = new ArrayList<>();
        teammates.forEach(teammate -> {
            if (teammate.equals(player))
                return;

            list.add(TeamMember.builder()
                    .playerUuid(teammate.getUniqueId())
                    .markerColor(color)
                    .displayName(displayNameFunction.apply(teammate))
                    .location(BukkitApollo.toApolloLocation(teammate.getLocation()))
                    .build());
        });

        update(player, list);
    }

    public void resetAll() {
        module.resetTeamMembers(Recipients.ofEveryone());
    }
}
