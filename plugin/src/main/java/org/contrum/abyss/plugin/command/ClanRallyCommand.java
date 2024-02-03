package org.contrum.abyss.plugin.command;

import me.ulrich.clans.api.ClanAPIManager;
import me.ulrich.clans.interfaces.ClanAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.contrum.abbys.AbyssLoader;

public class ClanRallyCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final AbyssLoader loader;

    public ClanRallyCommand(JavaPlugin plugin, AbyssLoader loader) {
        this.plugin = plugin;
        this.loader = loader;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }

        Player player = (Player) sender;

        return true;
    }
}
