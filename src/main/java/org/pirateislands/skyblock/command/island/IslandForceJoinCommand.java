package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import com.islesmc.islandapi.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseCommand;

import java.util.UUID;

public class IslandForceJoinCommand extends GooseCommand {

    public IslandForceJoinCommand() {
        super("force", Lists.newArrayList("join"), true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "/is force <playerInIsland>");
            return;
        }

        if (!player.hasPermission("island.force.join")) {
            player.sendMessage(ChatColor.RED + "You do not have permission for this!");
            return;
        }

        Island island = SkyBlock.getPlugin().getIslandHandler().getIslandForPlayer(player);
        if (island != null) {
            player.sendMessage(ChatColor.RED + "You have to leave your current island first");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        if (offlinePlayer == null) {
            player.sendMessage(ChatColor.RED + String.format("No player with the name '%s' could've been found.", args[0]));
            return;
        }

        UUID playerUUID = offlinePlayer.getUniqueId();

        Island targetIsland = SkyBlock.getPlugin().getIslandHandler().findByUniqueId(playerUUID);

        if (targetIsland == null) {
            player.sendMessage(ChatColor.RED + String.format("The player '%s' does not have an island.", offlinePlayer.getName()));
            player.sendMessage(ChatColor.RED + "Player does not currently have an island");
            return;
        }

        targetIsland.getMembers().add(player.getUniqueId());
        targetIsland.setOwner(player.getUniqueId());
        player.sendMessage(ChatColor.YELLOW + String.format("You have forced joined %s's island.", targetIsland.getName()));
    }
}
