package org.contrum.abyss.plugin.command;

import com.lunarclient.apollo.BukkitApollo;
import com.lunarclient.apollo.module.waypoint.Waypoint;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.ulrich.clans.Clans;
import me.ulrich.clans.data.ClanData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.contrum.abbys.AbyssLoader;
import org.contrum.abbys.event.PlayerRegisterLunarClientEvent;
import org.contrum.abyss.plugin.AbyssConfig;
import org.contrum.abyss.plugin.AbyssPlugin;

import java.awt.*;
import java.time.Duration;
import java.util.List;
import java.util.*;

public class ClanRallyCommand implements CommandExecutor {

    private final AbyssConfig config;
    private final AbyssLoader loader;

    private final Clans clans;
    private final Map<UUID, RallyInfo> rallys;

    public ClanRallyCommand(AbyssPlugin plugin, AbyssConfig config, AbyssLoader loader) {
        this.config = config;
        this.loader = loader;
        this.clans = Clans.getPlugin(Clans.class);
        this.rallys = new HashMap<>();


        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onJoin(PlayerRegisterLunarClientEvent event) {
                Player player = event.getPlayer();
                clans.getPlayerAPI().getPlayerClan(player.getUniqueId()).ifPresent(clan -> {
                    if (!rallys.containsKey(clan.getId()))
                        return;

                    RallyInfo info = rallys.get(clan.getId());
                    loader.getWaypointModule().addWaypoint(player, build(info.getLocation()));
                });
            }

        }, plugin);

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            List<UUID> toRemove = new ArrayList<>();
            rallys.forEach((uuid, info) -> {
                if (info.getEnd() < System.currentTimeMillis()) {
                    toRemove.add(uuid);
                    clans.getClanAPI().getClanData().get(uuid).getOnlineMembers().stream().map(Bukkit::getPlayer)
                            .forEach(player -> {
                                loader.getWaypointModule().removeWaypoint(player, color(config.getRallyWaypointFormat()));
                            });
                }
            });

            toRemove.forEach(rallys::remove);
            toRemove.clear();
        }, 0L, 20L);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }

        Player player = (Player) sender;
        if (!config.getRallyPermission().isEmpty() && !player.hasPermission(config.getRallyPermission())) {
            sender.sendMessage(ChatColor.RED + "You don't have permissions to execute this command.");
            return true;
        }

        Optional<ClanData> clanOptional = clans.getPlayerAPI().getPlayerClan(player.getUniqueId());
        if (!clanOptional.isPresent()) {
            player.sendMessage(ChatColor.RED + "You must be in a clan to do a rally!");
            return true;
        }

        ClanData clan = clanOptional.get();
        if (!clan.getLeader().equals(player.getUniqueId()) && config.isRallyOnlyLeader()) {
            player.sendMessage(ChatColor.RED + "Only the leader can perform a rally.");
            return true;
        }

        Location location = player.getLocation();

        if (rallys.containsKey(clan.getId())) {
            RallyInfo info = rallys.remove(clan.getId());
            clan.getOnlineMembers().stream().map(Bukkit::getPlayer).forEach(teammate -> {
                loader.getWaypointModule().removeWaypoint(teammate, color(config.getRallyWaypointFormat()));
            });
        }

        clan.getOnlineMembers().stream().map(Bukkit::getPlayer).forEach(teammate -> {
            loader.getWaypointModule().addWaypoint(teammate, build(location));
            ProtectedRegion region = getRegionAt(location);

            teammate.sendMessage(config.getRallyMessage().replace("&", "" + ChatColor.COLOR_CHAR).replace("{location}",
                            location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ())
                    .replace("{player}", player.getName())
                    .replace("{region}", region == null ? "None" : region.getId()));

            rallys.put(clan.getId(), new RallyInfo(player.getName(), location, System.currentTimeMillis() + Duration.ofMinutes(config.getRallyDurationMinutes()).toMillis()));
        });

        return true;
    }

    private String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    private Waypoint build(Location location) {
        return Waypoint.builder()
                .name(color(config.getRallyWaypointFormat()))
                .location(BukkitApollo.toApolloBlockLocation(location))
                .color(Color.decode(config.getRallyWaypointColorHex()))
                .build();
    }

    @Data
    @AllArgsConstructor
    private static class RallyInfo {

        private String creator;
        private Location location;
        private long end;
    }

    private ProtectedRegion getRegionAt(Location location) {
        Set<ProtectedRegion> list = WorldGuard.getInstance().getPlatform().getRegionContainer()
                .get(new BukkitWorld(location.getWorld())).getApplicableRegions(BlockVector3.at(location.getX(), location.getY(), location.getZ())).getRegions();

        return list.stream().findFirst().orElse(null);
    }
}
