package org.contrum.abyss.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.BukkitApollo;
import com.lunarclient.apollo.module.team.TeamMember;
import com.lunarclient.apollo.module.team.TeamModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import com.lunarclient.apollo.recipients.Recipients;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.contrum.abyss.AbyssLoader;
;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AbyssTeamModule {

    private final AbyssLoader loader;
    private final JavaPlugin plugin;
    private final TeamModule module;

    public AbyssTeamModule(AbyssLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(TeamModule.class);
    }

    /**
     * Updates a player's teammates
     *
     * @param player The @see{@link ApolloPlayer} player that will receive the teammate update.
     * @param members The @see{@link TeamMember} list of teammates.
     */
    public void update(ApolloPlayer player, List<TeamMember> members) {
        module.updateTeamMembers(player, members);
    }

    /**
     * Resets a player's teammates
     *
     * @param player The @see{@link ApolloPlayer} player to reset the teammates.
     */
    public void reset(ApolloPlayer player) {
        module.resetTeamMembers(player);
    }

    /**
     * Updates a player's teammates
     *
     * @param player The bukkit player that will receive the teammate update.
     * @param members The @see{@link TeamMember} list of teammates to update.
     */
    public void update(Player player, List<TeamMember> members)
    {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            update(apolloPlayer, members);
        });
    }

    /**
     * Resets a player's teammates.
     *
     * @param player The bukkit player to reset the teammates.
     */
    public void reset(Player player)  {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            reset(apolloPlayer);
        });
    }

    /**
     * Updates a player's teammates
     *
     * @param player The bukkit player that will receive the teammate update.
     * @param teammates List of @see{@link Player} that contains every teammate.
     * @param color The color of the marker that will be above each teammate head.
     * @param displayNameFunction Function to get the display name of the player when hovered at a long range.
     */
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

    /**
     * Resets everyone's teammates.
     */
    public void resetAll() {
        module.resetTeamMembers(Recipients.ofEveryone());
    }
}
