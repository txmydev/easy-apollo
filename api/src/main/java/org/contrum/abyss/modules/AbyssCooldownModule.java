package org.contrum.abyss.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.cooldown.Cooldown;
import com.lunarclient.apollo.module.cooldown.CooldownModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import com.lunarclient.apollo.recipients.Recipients;
import org.bukkit.entity.Player;
import org.contrum.abyss.AbyssLoader;
;
import org.bukkit.plugin.java.JavaPlugin;

public class AbyssCooldownModule {

    private final AbyssLoader loader;
    private final JavaPlugin plugin;
    private final CooldownModule module;

    public AbyssCooldownModule(AbyssLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(CooldownModule.class);
    }

    /**
     * Displays a cooldown to every player running Lunar Client.
     *
     * @param cooldown The Cooldown object to display.
     */
    public void displayCooldown(Cooldown cooldown) {
        module.displayCooldown(Recipients.ofEveryone(), cooldown);
    }

    /**
     * Removes a cooldown from every player running Lunar Client.
     *
     * @param id The cooldown id
     */
    public void removeCooldown(String id) {
        module.removeCooldown(Recipients.ofEveryone(), id);
    }


    /**
     * Removes a cooldown from every player running
     *
     * @param cooldown The cooldown object to remove.
     */
    public void removeCooldown(Cooldown cooldown) {
        module.removeCooldown(Recipients.ofEveryone(), cooldown);
    }

    /**
     * Displays a cooldown to a specific player.
     *
     * @param player The Lunar Client player wrapper ApolloPlayer
     * @param cooldown The Cooldown object
     */
    public void displayCooldown(ApolloPlayer player, Cooldown cooldown) {
        module.displayCooldown(player, cooldown);
    }

    /**
     * Removes a cooldown from a specific player
     *
     * @param player The Lunar Client player wrapper ApolloPlayer
     * @param id The cooldown id
     */
    public void removeCooldown(ApolloPlayer player, String id) {
        module.removeCooldown(player, id);
    }

    /**
     * Removes a cooldown from a specific player
     *
     * @param player The Lunar Client player wrapper ApolloPlayer
     * @param cooldown The cooldown object to remove.
     */
    public void removeCooldown(ApolloPlayer player, Cooldown cooldown) {
        module.removeCooldown(player, cooldown);
    }

    /**
     * Displays a cooldown to a specific player.
     *
     * @param player The bukkit player to display the cooldown (must be running Lunar Client)
     * @param cooldown The cooldown object to display.
     */
    public void displayCooldown(Player player, Cooldown cooldown) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            module.displayCooldown(apolloPlayer, cooldown);
        });
    }

    /**
     * Removes a cooldown from a specific player.
     *
     * @param player The bukkit player to remove the cooldown (must be running Lunar Client)
     * @param id The cooldown-id to remove.
     */
    public void removeCooldown(Player player, String id) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            module.removeCooldown(apolloPlayer, id);
        });
    }

    /**
     * Removes a cooldown from a specific player.
     *
     * @param player The bukkit player to remove the cooldown (must be running Lunar Client)
     * @param cooldown The cooldown object to remove.
     */

    public void removeCooldown(Player player, Cooldown cooldown) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            module.removeCooldown(apolloPlayer, cooldown);
        });
    }
}
