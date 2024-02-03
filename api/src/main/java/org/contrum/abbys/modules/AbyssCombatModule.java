package org.contrum.abbys.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.combat.CombatModule;
import org.contrum.abbys.AbyssLoader;;
import org.bukkit.plugin.java.JavaPlugin;

public class AbyssCombatModule {

    private final AbyssLoader loader;
    private final JavaPlugin plugin;
    private final CombatModule module;

    public AbyssCombatModule(AbyssLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(CombatModule.class);
    }

    /**
     * Sets whether the miss penalty present in 1.8 version
     * will be disabled for all lunar client players.
     *
     * @param value true or false
     */
    public void setDisableHitMissPenalty(boolean value)
    {
        module.getOptions().set(CombatModule.DISABLE_MISS_PENALTY, value);
    }

}
