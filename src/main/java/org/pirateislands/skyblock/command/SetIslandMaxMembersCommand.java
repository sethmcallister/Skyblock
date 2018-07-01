package org.pirateislands.skyblock.command;

import com.islesmc.islandapi.Island;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;

public class SetIslandMaxMembersCommand extends BukkitCommand {
    public SetIslandMaxMembersCommand() {
        super("setmaxmembers");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!sender.hasPermission("island.setmax")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /setmaxmembers <player> <size>");
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + String.format("No player with the name or UUID of '%s' could be found.", args[0]));
            return true;
        }
        if (!target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "No player with the name or UUID of '%s' is online.");
            return true;
        }
        Island island = SkyBlock.getPlugin().getIslandHandler().getIslandForPlayer((Player) target);
        if (island == null) {
            sender.sendMessage(ChatColor.RED + String.format("No island for the player '%s' could be found.", args[0]));
            return true;
        }

        String sizeSTR = args[1];
        if (!StringUtils.isNumeric(sizeSTR)) {
            sender.sendMessage(ChatColor.RED + String.format("The argument '%s' is not a number.", args[1]));
            return true;
        }
        Integer size = Integer.parseInt(sizeSTR);
        island.setMaxPlayers(island.getMaxPlayers() + size);
        island.save();
        sender.sendMessage(ChatColor.YELLOW + String.format("You have set %s's island size to %s.", args[0], args[1]));
        return true;
    }
}
