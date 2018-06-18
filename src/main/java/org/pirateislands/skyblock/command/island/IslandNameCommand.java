package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseCommand;
import com.islesmc.islandapi.Island;

public class IslandNameCommand extends GooseCommand {

    public IslandNameCommand() {
        super("name", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player sender, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/island name [name]");
            return;
        }

        String name = args[0];
        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(sender);

        if (island == null) {
            sender.sendMessage(ChatColor.RED + "You do not have an island to name!");
            return;
        }

        if (island.getOwner() != sender.getUniqueId()) {
            sender.sendMessage(ChatColor.RED + "You are not the island owner!");
            return;
        }

        island.setIslandName(name);
        sender.sendMessage(ChatColor.YELLOW + "Set your island name to: " + ChatColor.WHITE + name);
    }
}
