package org.pirateislands.skyblock.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtil {
    private MessageUtil() {

    }

    public static void sendUrgent(Player player, String msg) {
        player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.RED + msg);
    }

    public static void sendInfo(Player player, String msg) {
        player.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "[!] " + ChatColor.GOLD + msg);
    }

    public static void sendGood(Player player, String msg) {
        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] " + ChatColor.GREEN + msg);
    }

    public static void sendServerTheme(Player player, String msg) {
        player.sendMessage(ChatColor.YELLOW + "Pirate" + ChatColor.WHITE + "Islands " + ChatColor.GRAY + "\u00BB " + ChatColor.RESET + msg);
    }
}
