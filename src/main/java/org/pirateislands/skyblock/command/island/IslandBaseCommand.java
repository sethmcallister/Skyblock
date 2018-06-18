package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.goose.GooseCommand;

public class IslandBaseCommand extends GooseCommand {

    public IslandBaseCommand() {
        super("help", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
            player.sendMessage(ChatColor.YELLOW + "Island Help Page");
            player.sendMessage(ChatColor.YELLOW + " /island " + ChatColor.WHITE + "Displays this help page.");
            player.sendMessage(ChatColor.YELLOW + " /island accept " + ChatColor.WHITE + "Accept a current island invite.");
            player.sendMessage(ChatColor.YELLOW + " /island ban " + ChatColor.WHITE + "Ban somebody from your island.");
            player.sendMessage(ChatColor.YELLOW + " /island coop " + ChatColor.WHITE + "Add a Co-op player to your island.");
            player.sendMessage(ChatColor.YELLOW + " /island create " + ChatColor.WHITE + "Creates a new island.");
            player.sendMessage(ChatColor.YELLOW + " /island decline " + ChatColor.WHITE + "Decline a current island invite.");
            player.sendMessage(ChatColor.YELLOW + " /island delete " + ChatColor.WHITE + "Delete your island.");
            player.sendMessage(ChatColor.YELLOW + " /island home " + ChatColor.WHITE + "Teleports you to your island's home.");
            player.sendMessage(ChatColor.YELLOW + " /island invite " + ChatColor.WHITE + "Invite a player to your island.");
            player.sendMessage(ChatColor.YELLOW + " /island kick " + ChatColor.WHITE + "Kick a member from your island.");
            player.sendMessage(ChatColor.YELLOW + " /island leave " + ChatColor.WHITE + "Leave your current island.");
            player.sendMessage(ChatColor.YELLOW + " /island level " + ChatColor.WHITE + "Check your islands current level.");
            player.sendMessage(ChatColor.YELLOW + " /island lock " + ChatColor.WHITE + "Lock your island.");
            player.sendMessage(ChatColor.YELLOW + " /island name " + ChatColor.WHITE + "Give your island a name.");
            player.sendMessage(ChatColor.YELLOW + " /island sethome " + ChatColor.WHITE + "Set your island's home.");
            player.sendMessage(ChatColor.YELLOW + " /island top " + ChatColor.WHITE + "Displays the top islands.");
            player.sendMessage(ChatColor.YELLOW + " /island warp " + ChatColor.WHITE + "Warp to another island.");
            player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
        }
    }
}
