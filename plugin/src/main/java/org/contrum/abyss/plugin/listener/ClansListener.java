package org.contrum.abyss.plugin.listener;

import me.ulrich.clans.Clans;
import me.ulrich.clans.data.ClanData;
import me.ulrich.clans.interfaces.UClans;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.contrum.abbys.AbyssLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClansListener implements Listener, Runnable {

    private final JavaPlugin plugin;
    private final AbyssLoader loader;
    private final boolean registered;
    private final Clans clans;


    public ClansListener(JavaPlugin plugin, AbyssLoader loader) {
        this.plugin = plugin;
        this.loader = loader;
        this.registered = plugin.getServer().getPluginManager().getPlugin("UltimateClans") != null;

        if(!registered) {
            clans = null;
            return;
        }

        this.clans = Clans.getPlugin(Clans.class);
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, 3L);
    }



    @Override
    public void run() {
        if(!registered)
            return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            Optional<ClanData> clanOptional  = clans.getPlayerAPI().getPlayerClan(player.getUniqueId());
            clanOptional.ifPresent(clan -> {
                loader.getTeamModule().update(player,
                        new ArrayList<>(clan.getOnlineMembers().stream().map(Bukkit::getPlayer).collect(Collectors.toList())),
                        Color.GREEN,
                        teammate -> net.kyori.adventure.text.Component.text(teammate.getDisplayName()));
            });
        }
    }
}
