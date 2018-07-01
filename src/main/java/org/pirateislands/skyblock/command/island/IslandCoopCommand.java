package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import com.islesmc.islandapi.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseCommand;

public class IslandCoopCommand extends GooseCommand {

    public IslandCoopCommand() {
        super("coop", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /island <Add, Remove> <Player>");
            return;
        }

        Island island = SkyBlock.getPlugin().getIslandHandler().getIslandForPlayer(sender);

        if (island == null) {
            sender.sendMessage(ChatColor.RED + "You do not have an island to do this!");
            return;
        }

        String playerName = args[1];

        Player player = Bukkit.getPlayer(playerName);

        if (player == null) {
            sender.sendMessage(ChatColor.RED + String.format("No player with the name or UUID of '%s' exists.", playerName));
            return;
        }


        if (args[0].equalsIgnoreCase("add")) {
            if (island.getCoop().contains(player.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "This player is already CO-OP on your island!");
                return;
            }

            island.getCoop().add(player.getUniqueId());
            sender.sendMessage(ChatColor.YELLOW + "Successfully added " + ChatColor.WHITE + player.getName() + ChatColor.YELLOW + " as a CO-OP player to your island!");
            player.sendMessage(ChatColor.YELLOW + "You have been added as a CO-OP player to " + ChatColor.WHITE + sender.getName() + ChatColor.YELLOW + "'s island!");
            return;
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!island.getCoop().contains(player.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "This player is not CO-OP on your island!");
                return;
            }

            island.getCoop().remove(player.getUniqueId());
            sender.sendMessage(ChatColor.YELLOW + "Successfully removed " + ChatColor.WHITE + player.getName() + ChatColor.YELLOW + " as a CO-OP player from your island!");
            player.sendMessage(ChatColor.YELLOW + "You have been removed as a CO-OP player from " + ChatColor.WHITE + sender.getName() + ChatColor.YELLOW + "'s island!");
            return;
        }
    }
}
