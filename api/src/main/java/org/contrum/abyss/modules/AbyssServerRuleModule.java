package org.contrum.abyss.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.serverrule.ServerRuleModule;
import org.contrum.abyss.AbyssLoader;
;
import org.bukkit.plugin.java.JavaPlugin;

public class AbyssServerRuleModule {

    private final AbyssLoader loader;
    private final JavaPlugin plugin;
    private final ServerRuleModule module;

    public AbyssServerRuleModule(AbyssLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(ServerRuleModule.class);
    }

    /**
     * Sets whether a player will be able to chat while
     * in the nether portal.
     *
     * @param value true or false
     */
    public void setAntiPortalTraps(boolean value) {
        this.module.getOptions().set(ServerRuleModule.ANTI_PORTAL_TRAPS, value);
    }


    /**
     * Blocks players from reloading chunks (F3 + A) or with Iris
     *
     * @param value true or false
     */
    public void setDisableReloadingChunks(boolean value) {
        this.module.getOptions().set(ServerRuleModule.DISABLE_CHUNK_RELOADING, value);
    }

}
