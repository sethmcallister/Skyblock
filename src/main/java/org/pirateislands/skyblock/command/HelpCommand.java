package org.pirateislands.skyblock.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.pirateislands.skyblock.SkyBlock;

public class HelpCommand extends BukkitCommand {
    public HelpCommand() {
        super("help");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkyBlock.getInstance().getServerConfig().getPrimaryColor()) + " Island help: " + ChatColor.WHITE + "/is help");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkyBlock.getInstance().getServerConfig().getPrimaryColor()) + " Shop: " + ChatColor.WHITE + "/shop or visit the Shop NPC");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkyBlock.getInstance().getServerConfig().getPrimaryColor()) + " Auction House: " + ChatColor.WHITE + "/ah or visit the Auction House NPC");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkyBlock.getInstance().getServerConfig().getPrimaryColor()) + " Custom Enchants: " + ChatColor.WHITE + "/ce or visit the enchanter NPC");
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkyBlock.getInstance().getServerConfig().getPrimaryColor()) + " Website: " + ChatColor.WHITE + "https://pirateislands.org");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkyBlock.getInstance().getServerConfig().getPrimaryColor()) + " Discord: " + ChatColor.WHITE + "https://discord.priateislands.org");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkyBlock.getInstance().getServerConfig().getPrimaryColor()) + " Shop: " + ChatColor.WHITE + "store.pirateislands.org");
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
        return false;
    }
}
