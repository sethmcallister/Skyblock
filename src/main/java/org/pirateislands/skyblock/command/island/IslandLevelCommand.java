package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseCommand;
import com.islesmc.islandapi.Island;
import org.pirateislands.skyblock.misc.MessageUtil;

public class IslandLevelCommand extends GooseCommand {
    public IslandLevelCommand() {
        super("level", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] strings) {
        if (!SkyBlock.getPlugin().getIslandRegistry().hasIsland(player)) {
            player.sendMessage(ChatColor.RED + "You do not currently have an island.");
            return;
        }

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);

        SkyBlock.getPlugin().getIslandRegistry().calculateIslandLevel(island);
        MessageUtil.sendServerTheme(player, ChatColor.YELLOW + String.format("Your island is currently level %s.", island.getIslandLevel()));
    }
}
