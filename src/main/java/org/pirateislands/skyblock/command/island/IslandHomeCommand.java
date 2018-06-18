package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseCommand;
import com.islesmc.islandapi.Island;
import org.pirateislands.skyblock.goose.GooseLocationHelper;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandHomeCommand extends GooseCommand {
    public IslandHomeCommand() {
        super("home", Lists.newArrayList("go"), true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length > 0) {
            player.sendMessage(ChatColor.RED + "Usage: /island home");
            return;
        }
        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);
        if (island == null) {
            player.sendMessage(ChatColor.RED + "You do not have an island to teleport to.");
            return;
        }


        Location spawn = GooseLocationHelper.toLocation(island.getSpawn());
        Location teleport = new Location(spawn.getWorld(), spawn.getBlockX(), spawn.getWorld().getHighestBlockYAt(spawn.getBlockX(), spawn.getBlockZ()) + 2, spawn.getZ());

        player.teleport(teleport);
        player.sendMessage(ChatColor.YELLOW + "You have been teleported to your island's home.");
        return;
    }

}
