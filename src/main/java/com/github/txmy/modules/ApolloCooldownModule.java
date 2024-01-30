package com.github.txmy.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.cooldown.Cooldown;
import com.lunarclient.apollo.module.cooldown.CooldownModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import com.lunarclient.apollo.recipients.Recipients;
import org.bukkit.entity.Player;
import com.github.txmy.ApolloLoader;;
import org.bukkit.plugin.java.JavaPlugin;

public class ApolloCooldownModule {

    private final ApolloLoader loader;
    private final JavaPlugin plugin;
    private final CooldownModule module;

    public ApolloCooldownModule(ApolloLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(CooldownModule.class);
    }

    public void displayCooldown(Cooldown cooldown) {
        module.displayCooldown(Recipients.ofEveryone(), cooldown);
    }

    public void removeCooldown(String id) {
        module.removeCooldown(Recipients.ofEveryone(), id);
    }

    public void removeCooldown(Cooldown cooldown) {
        module.removeCooldown(Recipients.ofEveryone(), cooldown);
    }

    public void displayCooldown(ApolloPlayer player, Cooldown cooldown) {
        module.displayCooldown(player, cooldown);
    }

    public void removeCooldown(ApolloPlayer player, String id) {
        module.removeCooldown(player, id);
    }

    public void removeCooldown(ApolloPlayer player, Cooldown cooldown) {
        module.removeCooldown(player, cooldown);
    }

    public void displayCooldown(Player player, Cooldown cooldown) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            module.displayCooldown(apolloPlayer, cooldown);
        });
    }

    public void removeCooldown(Player player, String id) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            module.removeCooldown(apolloPlayer, id);
        });
    }

    public void removeCooldown(Player player, Cooldown cooldown) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            module.removeCooldown(apolloPlayer, cooldown);
        });
    }
}
