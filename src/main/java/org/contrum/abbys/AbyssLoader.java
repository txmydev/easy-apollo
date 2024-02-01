package org.contrum.abbys;

import com.lunarclient.apollo.Apollo;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.contrum.abbys.event.AbyssLoadEvent;
import org.contrum.abbys.modules.*;
import org.contrum.abbys.util.ApolloUtils;
import org.contrum.abbys.util.DownloadFileThread;
import org.contrum.abbys.util.DownloadUtils;
import org.contrum.abbys.util.TaskUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Getter
public class AbyssLoader {

    private final JavaPlugin plugin;
    private final AbyssLoaderOptions options;
    private final AbyssBorderModule borderModule;
    private final AbyssChatModule chatModule;
    private final AbyssCombatModule combatModule;
    private final AbyssCooldownModule cooldownModule;
    private final AbyssNotificationModule notificationModule;
    private final AbyssServerRuleModule serverRuleModule;
    private final AbyssStaffModule staffModule;
    private final AbyssTitleModule titleModule;
    private final AbyssWaypointModule waypointModule;
    private final AbyssTeamModule teamModule;

    private boolean downloadedLatest;

    public AbyssLoader(JavaPlugin plugin, AbyssLoaderOptions options) {
        this.plugin = plugin;
        this.options = options;

        if (!this.isRegistered()) {
            ApolloUtils.logger = plugin.getLogger();
            Path serverPath = plugin.getServer().getWorldContainer().toPath().normalize();

            String urlString = ApolloUtils.getLatestVersionDownloadLink();

            String fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
            Path path = Paths.get(serverPath.toString(), "plugins/" + fileName);

            download(urlString, path, fileName);
            downloadedLatest = true;
        }

        this.borderModule = new AbyssBorderModule(this);
        this.chatModule = new AbyssChatModule(this);
        this.combatModule = new AbyssCombatModule(this);
        this.cooldownModule = new AbyssCooldownModule(this);
        this.notificationModule = new AbyssNotificationModule(this);
        this.serverRuleModule = new AbyssServerRuleModule(this);
        this.staffModule = new AbyssStaffModule(this);
        this.teamModule = new AbyssTeamModule(this);
        this.titleModule = new AbyssTitleModule(this);
        this.waypointModule = new AbyssWaypointModule(this);

        AbyssLoadEvent event = new AbyssLoadEvent();
        plugin.getServer().getPluginManager().callEvent(event);
    }

    private void download(String urlString, Path path, String fileName) {
        DownloadUtils.download(DownloadFileThread.builder()
                .urlString(urlString)
                .path(path)
                .progressReportHandler(progress -> plugin.getLogger().info(options.getProgressReportOrFallback(progressS -> "Downloading " + fileName + ",  (" + progress + "% / 100%)").apply(progress)))
                .finishedHandler(bytes -> {
                    plugin.getLogger().info(options.getFinishedReportOrFallback(totalBytes -> "Successfully downloaded " + fileName + ". (Size: " + (bytes / (1024 * 1024) + " MB)")).apply(bytes));

                    try {
                        PluginManager manager = plugin.getServer().getPluginManager();
                        manager.loadPlugin(path.toFile());
                        manager.enablePlugin(this.getApolloPlugin());
                    } catch (Exception ex) {
                        plugin.getLogger().info("Failed to initialize plugin " + fileName + ", restarting the server...");
                        if (options.isRestartUponLoadingError())
                            TaskUtil.runLater(plugin, () -> plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "restart"), 10L);
                    }

                })
                .errorHandler(error -> plugin.getLogger().severe(options.getErrorReportOrFallback(ex -> "Error ocurred while downloading " + fileName + ": " + error.getMessage()).apply(error)))
                .build()
        );
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
     * @param players The colleciton of players to loop through.
     * @param loopConsumer The callback to perform for each player (boolean determines if the player is running Lunar Client or not).
     */
    public void perform(Collection<? extends Player> players, BiConsumer<Player, Boolean> loopConsumer) {
        players.forEach(player -> {
            loopConsumer.accept(player, isRunningLunar(player));
        });
    }

}
