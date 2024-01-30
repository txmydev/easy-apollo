package com.github.txmy.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.combat.CombatModule;
import com.github.txmy.ApolloLoader;;
import org.bukkit.plugin.java.JavaPlugin;

public class ApolloCombatModule {

    private final ApolloLoader loader;
    private final JavaPlugin plugin;
    private final CombatModule module;

    public ApolloCombatModule(ApolloLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(CombatModule.class);
    }

    public void setDisableHitMissPenalty(boolean value)
    {
        module.getOptions().set(CombatModule.DISABLE_MISS_PENALTY, value);
    }

}
