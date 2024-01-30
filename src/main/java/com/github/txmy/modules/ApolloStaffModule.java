package com.github.txmy.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.staffmod.StaffMod;
import com.lunarclient.apollo.module.staffmod.StaffModModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import org.bukkit.entity.Player;
import com.github.txmy.ApolloLoader;;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class ApolloStaffModule {
    private final ApolloLoader loader;
    private final JavaPlugin plugin;
    private final StaffModModule module;

    public ApolloStaffModule(ApolloLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(StaffModModule.class);
    }

    public void enableXRay(ApolloPlayer player) {
        module.enableStaffMods(player, Collections.singletonList(StaffMod.XRAY));
    }

    public void disableXRay(ApolloPlayer player) {
        module.disableStaffMods(player, Collections.singletonList(StaffMod.XRAY));
    }

    public void enableXRay(Player player) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            enableXRay(apolloPlayer);
        });
    }

    public void disableXRay(Player player) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            disableXRay(apolloPlayer);
        });
    }

}
