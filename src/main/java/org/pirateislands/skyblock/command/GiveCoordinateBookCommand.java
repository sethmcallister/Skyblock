package org.pirateislands.skyblock.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pirateislands.skyblock.island.listeners.isles.CoordinateBookListener;

public class GiveCoordinateBookCommand extends BukkitCommand {
    public GiveCoordinateBookCommand() {
        super("givecoordinatebook");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!sender.hasPermission("island.givecoordinatebook")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /givecoordinatebook <player>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + String.format("No player with the name or UUID of '%s' could be found.", args[0]));
            return true;
        }
        ItemStack itemStack = new ItemStack(Material.BOOK);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(CoordinateBookListener.COORD_BOOK_UNSET);
        itemStack.setItemMeta(meta);
        target.getInventory().addItem(itemStack);
        sender.sendMessage(ChatColor.YELLOW + String.format("You have given %s a coordinate book.", target.getName()));
        target.sendMessage(ChatColor.YELLOW + "You have been given a coordinate book.");
        return true;
    }
}
