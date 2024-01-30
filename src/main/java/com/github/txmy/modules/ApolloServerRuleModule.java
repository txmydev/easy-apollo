package com.github.txmy.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.serverrule.ServerRuleModule;
import com.github.txmy.ApolloLoader;;
import org.bukkit.plugin.java.JavaPlugin;

public class ApolloServerRuleModule {

    private final ApolloLoader loader;
    private final JavaPlugin plugin;
    private final ServerRuleModule module;

    public ApolloServerRuleModule(ApolloLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(ServerRuleModule.class);
    }

    public void setAntiPortalTraps(boolean value) {
        this.module.getOptions().set(ServerRuleModule.ANTI_PORTAL_TRAPS, value);
    }


    public void setDisableReloadingChunks(boolean value) {
        this.module.getOptions().set(ServerRuleModule.DISABLE_CHUNK_RELOADING, value);
    }

}
