package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import com.islesmc.islandapi.Island;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseCommand;
import org.pirateislands.skyblock.misc.MessageUtil;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandDeleteCommand extends GooseCommand implements Listener {

    public IslandDeleteCommand() {
        super("delete", Lists.newArrayList(), true);
    }


    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 0) {
            player.sendMessage(ChatColor.RED + "Usage: /island delete");
        }

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);

        if (island == null) {
            player.sendMessage(ChatColor.RED + "You do not have an island to delete.");
            return;
        }

        if (!island.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Only the owner of your island can delete it.");
            return;
        }

        SkyBlock.getPlugin().getIslandRegistry().deleteIsland(player, island);
        MessageUtil.sendServerTheme(player, "You have successfully deleted your island.");
    }
}
