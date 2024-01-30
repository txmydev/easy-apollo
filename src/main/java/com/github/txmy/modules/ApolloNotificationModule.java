package com.github.txmy.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.notification.Notification;
import com.lunarclient.apollo.module.notification.NotificationModule;
import com.lunarclient.apollo.recipients.Recipients;
import org.bukkit.entity.Player;
import com.github.txmy.ApolloLoader;;
import org.bukkit.plugin.java.JavaPlugin;

public class ApolloNotificationModule {

    private final ApolloLoader loader;
    private final JavaPlugin plugin;
    private final NotificationModule module;

    public ApolloNotificationModule(ApolloLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(NotificationModule.class);
    }

    public void sendNotification(Notification notification) {
        module.displayNotification(Recipients.ofEveryone(), notification);
    }

    public void resetNotifications(Recipients recipients) {
        module.resetNotifications(recipients);
    }

    public void sendNotification(Player player, Notification notification)
    {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            module.displayNotification(apolloPlayer, notification);
        });
    }

    public void resetNotifications(Player player) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            module.resetNotifications(apolloPlayer);
        });
    }

}
