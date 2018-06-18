package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.configuration.ServerType;
import org.pirateislands.skyblock.goose.GooseCommand;
import com.islesmc.islandapi.Island;
import org.pirateislands.skyblock.misc.MessageUtil;

public class IslandLockCommand extends GooseCommand {
    public IslandLockCommand() {
        super("lock", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player sender, String[] args) {
        if (SkyBlock.getPlugin().getServerConfig().getServerType() == ServerType.ISLES) {
            sender.sendMessage(ChatColor.RED + "That cannot be used on this realm!");
            return;
        }

        if (args.length > 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /island lock");
            return;
        }
        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(sender);

        if (island == null) {
            sender.sendMessage(ChatColor.RED + "You do not currently have an island.");
            return;
        }

        if (island.getLocked()) {
            island.setLocked(false);
            MessageUtil.sendServerTheme(sender, ChatColor.YELLOW + "Your island is no longer locked; anybody can warp to your island.");
            return;
        }
        island.setLocked(true);
        MessageUtil.sendServerTheme(sender, ChatColor.YELLOW + "Your island is now locked; only members can warp to your island.");
    }
}
