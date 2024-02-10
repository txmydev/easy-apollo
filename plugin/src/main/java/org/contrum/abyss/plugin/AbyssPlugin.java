package org.contrum.abyss.plugin;

import de.exlll.configlib.ConfigLib;
import de.exlll.configlib.YamlConfigurationStore;
import de.exlll.configlib.YamlConfigurations;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.contrum.abbys.AbyssLoader;
import org.contrum.abbys.AbyssLoaderOptions;
import org.contrum.abyss.plugin.command.ClanRallyCommand;
import org.contrum.abyss.plugin.listener.ClansListener;
import org.contrum.abyss.plugin.listener.UKothListener;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AbyssPlugin extends JavaPlugin {

    private AbyssLoader loader;

    @Getter
    private AbyssConfig abyssConfig;

    private YamlConfigurationStore<AbyssConfig> storage;
    private Path configPath;

    private ClanRallyCommand rallyCommand;

    @Override
    public void onEnable() {
        this.loader = new AbyssLoader(this, AbyssLoaderOptions.builder()
                .downloadIfMissing(true)
                .restartUponLoadingError(true)
                .build());

        configPath = Paths.get(this.getDataFolder().toPath().normalize().toString(), "config.yml");
        abyssConfig = new AbyssConfig();
        YamlConfigurations.save(configPath, AbyssConfig.class, abyssConfig);

        storage = new YamlConfigurationStore<>(AbyssConfig.class, ConfigLib.BUKKIT_DEFAULT_PROPERTIES
                .toBuilder()
                .build());

        abyssConfig = storage.load(configPath);

        boolean useUKoths = getServer().getPluginManager().getPlugin("UltimateKoth") != null;
        boolean useUClans = getServer().getPluginManager().getPlugin("UltimateClans") != null;

        if (useUClans) {
            rallyCommand = new ClanRallyCommand(this, abyssConfig, loader);
            getLogger().info("Detected UltimateClans, initiating /rally command...");
            getCommand("rally").setExecutor(rallyCommand);

            getServer().getPluginManager().registerEvents(new ClansListener(this, loader), this);
        }

        if (useUKoths) {
            getLogger().info("Detected UltimateKoth, initiating koth waypoints...");
            getServer().getPluginManager().registerEvents(new UKothListener(this, loader, abyssConfig), this);
        }

    }

    public void save() {
        storage.save(abyssConfig, configPath);
    }
}
