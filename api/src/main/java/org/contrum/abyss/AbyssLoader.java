package org.contrum.abyss;

import com.lunarclient.apollo.Apollo;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.contrum.abyss.event.AbyssLoadEvent;
import org.contrum.abyss.modules.*;
import org.contrum.abyss.util.ApolloUtils;
import org.contrum.abyss.util.DownloadResult;
import org.contrum.abyss.util.DownloadUtils;
import org.contrum.abyss.util.TaskUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.function.BiConsumer;

@Getter
public class AbyssLoader {

    private static final Logger log = LogManager.getLogger(AbyssLoader.class);
    private final JavaPlugin plugin;
    private final AbyssLoaderOptions options;

    private AbyssBorderModule borderModule;
    private AbyssChatModule chatModule;
    private AbyssCombatModule combatModule;
    private AbyssCooldownModule cooldownModule;
    private AbyssNotificationModule notificationModule;
    private AbyssServerRuleModule serverRuleModule;
    private AbyssStaffModule staffModule;
    private AbyssTitleModule titleModule;
    private AbyssWaypointModule waypointModule;
    private AbyssTeamModule teamModule;

    private boolean loaded;
    private boolean downloadedLatest;

    public AbyssLoader(JavaPlugin plugin, AbyssLoaderOptions options) {
        this.plugin = plugin;
        this.options = options;

        if (!this.isRegistered()) {
            ApolloUtils.logger = plugin.getLogger();
            plugin.getLogger().info("Didn't find apollo-bukkit in the server plugins, downloading it...");
            Path serverPath = plugin.getServer().getWorldContainer().toPath().normalize();

            String urlString = ApolloUtils.getLatestVersionDownloadLink();

            String fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
            Path path = Paths.get(serverPath.toString(), "plugins/" + fileName);

            download(urlString, path, fileName);
            downloadedLatest = true;
        }

        log.log(Level.DEBUG, "------------------------");
        log.log(Level.DEBUG, "Loading Modules...");
        this.borderModule = new AbyssBorderModule(this);
        log.log(Level.DEBUG, "  - Border Module");
        this.chatModule = new AbyssChatModule(this);
        log.log(Level.DEBUG, "  - Chat Module");
        this.combatModule = new AbyssCombatModule(this);
        log.log(Level.DEBUG, "  - Combat Module");
        this.cooldownModule = new AbyssCooldownModule(this);
        log.log(Level.DEBUG, "  - Cooldown Module");
        this.notificationModule = new AbyssNotificationModule(this);
        log.log(Level.DEBUG, "  - Notification Module");
        this.serverRuleModule = new AbyssServerRuleModule(this);
        log.log(Level.DEBUG, "  - Server Rule Module");
        this.staffModule = new AbyssStaffModule(this);
        log.log(Level.DEBUG, "  - Staff Module");
        this.teamModule = new AbyssTeamModule(this);
        log.log(Level.DEBUG, "  - Team Module");
        this.titleModule = new AbyssTitleModule(this);
        log.log(Level.DEBUG, "  - Title Module");
        this.waypointModule = new AbyssWaypointModule(this);
        log.log(Level.DEBUG, "  - Waypoint Module");

        loaded = true;
        log.log(Level.DEBUG, "------------------------");
        log.log(Level.DEBUG, "Firing up event....");
        AbyssLoadEvent event = new AbyssLoadEvent();
        plugin.getServer().getPluginManager().callEvent(event);
        log.log(Level.DEBUG, "------------------------");
    }

    /**
     * Checks if abyss has fully loaded yet.
     *
     * @return true if abyss already loaded every module.
     */
    public boolean hasLoaded() {
        return loaded;
    }

    private void download(String urlString, Path path, String fileName) {
        DownloadResult result = DownloadUtils.download(
                urlString,
                path,
                progress -> plugin.getLogger().info(options.getProgressReportOrFallback(none -> "Downloading " + fileName + ", (" + progress + "%/100%)").apply(progress)),
                error -> plugin.getLogger().severe(options.getErrorReportOrFallback(ex -> "Error ocurred while downloading " + fileName + ": " + error.getMessage()).apply(error))
        );

        if (!result.isSuccess()) {
            plugin.getPluginLoader().disablePlugin(plugin);
            return;
        }

        plugin.getLogger().info(options.getFinishedReportOrFallback(totalBytes -> "Successfully downloaded " + fileName + ". (Size: " + (totalBytes / (1024 * 1024) + " MB)")).apply(result.getBytes()));

        try {
            PluginManager manager = plugin.getServer().getPluginManager();
            manager.loadPlugin(path.toFile());
            manager.enablePlugin(this.getApolloPlugin());
        } catch (Exception ex) {
            plugin.getLogger().info("Failed to initialize plugin " + fileName + ", restarting the server...");
            if (options.isRestartUponLoadingError())
                TaskUtil.runLater(plugin, () -> plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "restart"), 10L);
        }
    }

    private Plugin getApolloPlugin() {
        return Bukkit.getPluginManager().getPlugin("Apollo-Bukkit");
    }

    public boolean isRegistered() {
        return this.getApolloPlugin() != null; // && plugin.getDescription().getVersion() ;
    }

    /**
     * Checks if a player is running Lunar Client.
     *
     * @param player The bukkit player to check.
     * @return true if the player is running Lunar Client.
     */
    public boolean isRunningLunar(Player player) {
        return Apollo.getPlayerManager().getPlayer(player.getUniqueId()).isPresent();
    }

    /**
     * Performs an iteration through a list, with consumers to accept condition
     *
     * @param players      The colleciton of players to loop through.
     * @param loopConsumer The callback to perform for each player (boolean determines if the player is running Lunar Client or not).
     */
    public void perform(Collection<? extends Player> players, BiConsumer<Player, Boolean> loopConsumer) {
        players.forEach(player -> {
            loopConsumer.accept(player, isRunningLunar(player));
        });
    }


}
