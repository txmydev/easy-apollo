package org.contrum.abyss.listener;

import com.lunarclient.apollo.event.ApolloListener;
import com.lunarclient.apollo.event.EventBus;
import com.lunarclient.apollo.event.Listen;
import com.lunarclient.apollo.event.player.ApolloRegisterPlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.contrum.abyss.event.PlayerRegisterLunarClientEvent;

public class AbyssListener implements ApolloListener {

    private final JavaPlugin plugin;

    public AbyssListener(JavaPlugin plugin) {
        this.plugin = plugin;

        EventBus.getBus().register(this);
    }


    @Listen
    private void onRegisterPlayer(ApolloRegisterPlayerEvent event) {
        Player player = Bukkit.getPlayer(event.getPlayer().getUniqueId());

        PlayerRegisterLunarClientEvent register = new PlayerRegisterLunarClientEvent(player);
        plugin.getServer().getPluginManager().callEvent(register);
    }

}
