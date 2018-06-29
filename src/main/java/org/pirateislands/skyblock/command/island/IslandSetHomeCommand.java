package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import com.islesmc.islandapi.goose.GooseLocation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseCommand;
import com.islesmc.islandapi.Island;
import org.pirateislands.skyblock.goose.GooseLocationHelper;

import java.util.UUID;

public class IslandSetHomeCommand extends GooseCommand {

    public IslandSetHomeCommand() {
        super("sethome", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length > 0) {
            player.sendMessage(ChatColor.RED + "Usage: /island sethome");
            return;
        }

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);

        if (island == null) {
            player.sendMessage(ChatColor.RED + "You must have an island to execute this command.");
            return;
        }

        int x = Math.round(player.getLocation().getBlockX());
        int y = Math.round(player.getLocation().getBlockY());
        int z = Math.round(player.getLocation().getBlockZ());

        if (!SkyBlock.getPlugin().getIslandRegistry().getIslandAt(player.getLocation()).isMember(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You can only set your island home in your island's land.");
            return;
        }

        island.setSpawn(GooseLocationHelper.fromLocation(player.getLocation()));

        player.sendMessage(ChatColor.YELLOW + String.format("You have set your island's home to (%s, %s, %s)", x, y, z));
        for (UUID uuid : island.getMembers()) {
            Player member = Bukkit.getPlayer(uuid);
            if (member != null)
                member.sendMessage(ChatColor.YELLOW + String.format("%s has set your island's home to (%s, %s, %s)", player.getName(), x, y, z));
        }
        return;
    }
}
