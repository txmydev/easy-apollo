package com.github.txmy;

import com.github.txmy.modules.*;
import com.github.txmy.util.ApolloUtils;
import com.github.txmy.util.DownloadFileThread;
import com.github.txmy.util.DownloadUtils;
import com.github.txmy.util.TaskUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
public class ApolloLoader {

    private final JavaPlugin plugin;
    private final ApolloBorderModule borderModule;
    private final ApolloChatModule chatModule;
    private final ApolloCombatModule combatModule;
    private final ApolloCooldownModule cooldownModule;
    private final ApolloNotificationModule notificationModule;
    private final ApolloServerRuleModule serverRuleModule;
    private final ApolloStaffModule staffModule;
    private final ApolloTitleModule titleModule;
    private final ApolloWaypointModule waypointModule;
    private final ApolloTeamModule teamModule;

    private boolean downloadedLatest;

    public ApolloLoader(JavaPlugin plugin, boolean registerSync) {
        this.plugin = plugin;

        if (!this.isRegistered()) {
            ApolloUtils.logger = plugin.getLogger();
            Path serverPath = plugin.getServer().getWorldContainer().toPath().normalize();

            if (!registerSync) {
                String urlString = ApolloUtils.getLatestVersionDownloadLink();

                String fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
                Path path = Paths.get(serverPath.toString(), "plugins/" + fileName);

                download(urlString, path, fileName);
            } else {
                ApolloUtils.getLatestVersionDownloadLinkAsync().thenAcceptAsync(urlString -> {
                    String fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
                    Path path = Paths.get(serverPath.toString(), "plugins/" + fileName);

                    download(urlString, path, fileName);
                });
            }

            downloadedLatest = true;
        }

        this.borderModule = new ApolloBorderModule(this);
        this.chatModule = new ApolloChatModule(this);
        this.combatModule = new ApolloCombatModule(this);
        this.cooldownModule = new ApolloCooldownModule(this);
        this.notificationModule = new ApolloNotificationModule(this);
        this.serverRuleModule = new ApolloServerRuleModule(this);
        this.staffModule = new ApolloStaffModule(this);
        this.teamModule = new ApolloTeamModule(this);
        this.titleModule = new ApolloTitleModule(this);
        this.waypointModule = new ApolloWaypointModule(this);
    }

    private void download(String urlString, Path path, String fileName) {
        DownloadUtils.download(DownloadFileThread.builder()
                .urlString(urlString)
                .path(path)
                .progressReportHandler(progress -> plugin.getLogger().info("Downloading " + fileName + ",  (" + progress + "% / 100%)"))
                .finishedHandler(bytes -> {
                    plugin.getLogger().info("Successfully downloaded " + fileName + ". (Size: " + (bytes / (1024 * 1024) + " MB)"));

                    try {
                        PluginManager manager = plugin.getServer().getPluginManager();
                        manager.loadPlugin(path.toFile());
                        manager.enablePlugin(this.getApolloPlugin());
                    } catch (Exception ex) {
                        plugin.getLogger().info("Failed to initialize plugin " + fileName + ", restarting the server...");
                        TaskUtil.runLater(plugin, () -> plugin.getServer().shutdown(), 10L);
                    }
                })
                .errorHandler(error -> plugin.getLogger().severe("Error ocurred while downloading " + fileName + ": " + error.getMessage()))
                .build()
        );
    }

    private Plugin getApolloPlugin() {
        return Bukkit.getPluginManager().getPlugin("Apollo-Bukkit");
    }

    public boolean isRegistered() {
        return this.getApolloPlugin() != null; // && plugin.getDescription().getVersion() ;
    }

}
