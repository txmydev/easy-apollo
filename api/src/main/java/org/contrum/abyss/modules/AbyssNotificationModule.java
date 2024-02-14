package org.contrum.abyss.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.notification.Notification;
import com.lunarclient.apollo.module.notification.NotificationModule;
import com.lunarclient.apollo.recipients.Recipients;
import org.bukkit.entity.Player;
import org.contrum.abyss.AbyssLoader;
;
import org.bukkit.plugin.java.JavaPlugin;

public class AbyssNotificationModule {

    private final AbyssLoader loader;
    private final JavaPlugin plugin;
    private final NotificationModule module;

    public AbyssNotificationModule(AbyssLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(NotificationModule.class);
    }

    /**
     * Sends a notification to every player running Lunar Client.
     *
     * @param notification The notifiaction object to display.
     */
    public void sendNotification(Notification notification) {
        module.displayNotification(Recipients.ofEveryone(), notification);
    }

    /**
     * Removes any notification preset from a @see{@link Recipients}
     *
     * @param recipients The recipients to reset the notifications from.
     */
    public void resetNotifications(Recipients recipients) {
        module.resetNotifications(recipients);
    }

    /**
     * Sends a notifiaction to a specific player.
     *
     * @param player The bukkit player object to send the notification to.
     * @param notification The notifiaction object to display.
     */
    public void sendNotification(Player player, Notification notification)
    {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            module.displayNotification(apolloPlayer, notification);
        });
    }

    /**
     * Resets a notification from a specific player.
     *
     * @param player The bukkit player to reset the notifications.
     */
    public void resetNotifications(Player player) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            module.resetNotifications(apolloPlayer);
        });
    }

}
