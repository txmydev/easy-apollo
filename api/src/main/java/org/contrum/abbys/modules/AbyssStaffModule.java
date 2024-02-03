package org.contrum.abbys.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.staffmod.StaffMod;
import com.lunarclient.apollo.module.staffmod.StaffModModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import org.bukkit.entity.Player;
import org.contrum.abbys.AbyssLoader;;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Collections;

public class AbyssStaffModule {
    private final AbyssLoader loader;
    private final JavaPlugin plugin;
    private final StaffModModule module;

    public AbyssStaffModule(AbyssLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(StaffModModule.class);
    }

    /**
     * Enables the xray staff module to a specific player.
     *
     * @param player The @see{@link ApolloPlayer} to enable staff xray module.
     */
    public void enableXRay(ApolloPlayer player) {
        module.enableStaffMods(player, Collections.singletonList(StaffMod.XRAY));
    }

    /**
     * Disables the xray staff module to a specific player.
     *
     * @param player The @see{@link ApolloPlayer} to enable staff xray module.
     */
    public void disableXRay(ApolloPlayer player) {
        module.disableStaffMods(player, Collections.singletonList(StaffMod.XRAY));
    }

    /**
     * Enables the xray staff module to a specific player.
     *
     * @param player The bukkit player to enable staff xray module.
     */
    public void enableXRay(Player player) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            enableXRay(apolloPlayer);
        });
    }

    /**
     * Removes the xray staff module from a specific player.
     *
     * @param player The bukkit player to disable xray staff module.
     */
    public void disableXRay(Player player) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            disableXRay(apolloPlayer);
        });
    }

    /**
     * Enables the xray staff module to a group of players.
     *
     * @param players Group of bukkit players to enable the xray staff module.
     */
    public void enableXRay(Collection<? extends Player> players) {
        players.forEach(this::enableXRay);
    }

    /**
     * Removes the xray staff module from a group of players.
     *
     * @param players Group of bukkit players to disable the xray staff module.
     */
    public void disableXRay(Collection<? extends Player> players) {
        players.forEach(this::disableXRay);
    }
}
