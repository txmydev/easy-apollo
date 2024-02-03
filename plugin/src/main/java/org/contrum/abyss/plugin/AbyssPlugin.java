package org.contrum.abyss.plugin;

import de.exlll.configlib.ConfigLib;
import de.exlll.configlib.YamlConfigurationStore;
import org.bukkit.plugin.java.JavaPlugin;
import org.contrum.abbys.AbyssLoader;
import org.contrum.abbys.AbyssLoaderOptions;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AbyssPlugin extends JavaPlugin {

    private AbyssLoader loader;
    private AbyssConfig config;

    private YamlConfigurationStore<AbyssConfig> storage;
    private Path configurationFile;

    @Override
    public void onEnable() {
        this.loader = new AbyssLoader(this, AbyssLoaderOptions.builder()
                .downloadIfMissing(true)
                .restartUponLoadingError(true)
                .build());

        configurationFile = Paths.get(this.getDataFolder().toPath().normalize().toString(), "config.yml");
        storage = new YamlConfigurationStore<>(AbyssConfig.class, ConfigLib.BUKKIT_DEFAULT_PROPERTIES
                .toBuilder()
                .build());
        config = storage.load(configurationFile);


    }

    public void save() {
        storage.save(config, configurationFile);
    }
}
