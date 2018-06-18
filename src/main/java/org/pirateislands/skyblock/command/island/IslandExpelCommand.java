package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.configuration.ServerType;
import org.pirateislands.skyblock.goose.GooseCommand;
import com.islesmc.islandapi.Island;

public class IslandExpelCommand extends GooseCommand {

    public IslandExpelCommand() {
        super("ban", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (SkyBlock.getPlugin().getServerConfig().getServerType() == ServerType.ISLES) {
            player.sendMessage(ChatColor.RED + "That cannot be used on this realm!");
            return;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "You must expel a player: /is ban <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + String.format("No player with the name '%s' could be found.", args[0]));
            return;
        }

        handleExpell(player, target);
    }

    private void handleExpell(Player owner, Player player) {
        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(owner);

        if (!island.getOwner().equals(owner.getUniqueId())) {
            owner.sendMessage(ChatColor.RED + "You cannot ban a player!");
            return;
        }

        if (island.isMember(player.getUniqueId())) {
            owner.sendMessage(ChatColor.RED + "You cannot ban a member of your own island.");
            return;
        }

        if (island.getExpelled().contains(player.getUniqueId())) {
            owner.sendMessage(ChatColor.YELLOW + "You have un-banned " + player.getName());
            player.sendMessage(ChatColor.YELLOW + "You have been unbanned from " + island.getName() + "'s island");
            island.getExpelled().remove(player.getUniqueId());
            return;
        }

        island.getExpelled().add(player.getUniqueId());
        owner.sendMessage(ChatColor.YELLOW + "You have banned " + player.getName());
        player.sendMessage(ChatColor.YELLOW + "You have been banned from " + island.getName() + "'s island");

        Island currentIsland = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(player.getLocation());

        if (currentIsland == null || currentIsland != island) {
            return;
        }

        player.teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
        player.sendMessage(ChatColor.YELLOW + "You can no longer be on that island.");
    }
}
