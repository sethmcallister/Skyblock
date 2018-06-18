package org.pirateislands.skyblock.command;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;

public class BalanceCommand extends BukkitCommand {
    public BalanceCommand() {
        super("balance");
        setAliases(Lists.newArrayList("econ", "money", "bal", "economoy"));
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Balance: " + ChatColor.WHITE + "$" + SkyBlock.getPlugin().getEconomy().getBalance((Player) sender));
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + String.format("No player with the name or UUID '%s' was found.", args[0]));
            return true;
        }
        sender.sendMessage(ChatColor.YELLOW + target.getName() + "'s Balance: " + ChatColor.WHITE + "$" + SkyBlock.getPlugin().getEconomy().getBalance(target));
        return true;
    }
}
