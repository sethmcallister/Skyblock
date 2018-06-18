package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseCommand;
import com.islesmc.islandapi.Island;

import java.util.UUID;

public class IslandMembersCommand extends GooseCommand {
    public IslandMembersCommand() {
        super("members", Lists.newArrayList("online"), true);
    }

    @Override
    public void execute(Player sender, String[] args) {
        if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /island members");
            return;
        }

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(sender);
        if (island == null) {
            sender.sendMessage(ChatColor.RED + "You must be in an island to execute this command.");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + String.format("%s's Members:", island.getName()));

        OfflinePlayer owner = Bukkit.getOfflinePlayer(island.getOwner());
        sender.sendMessage(ChatColor.GRAY + " \u00BB " + (!owner.isOnline() ? ChatColor.RED + owner.getName() + " (Owner)" : ChatColor.GREEN + owner.getName() + " (Owner)"));

        for (UUID uuid : island.getMembers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                sender.sendMessage(ChatColor.GRAY + " \u00BB " + ChatColor.GREEN + player.getName());
                continue;
            }

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            if (offlinePlayer == null)
                continue;

            sender.sendMessage(ChatColor.GRAY + " \u00BB " + ChatColor.RED + offlinePlayer.getName());
        }
    }
}
