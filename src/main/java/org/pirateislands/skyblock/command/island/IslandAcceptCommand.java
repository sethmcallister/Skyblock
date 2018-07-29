package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import com.islesmc.islandapi.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseCommand;

public class IslandAcceptCommand extends GooseCommand {

    public IslandAcceptCommand() {
        super("accept", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] strings) {
        if (!SkyBlock.getInstance().getIslandHandler().hasInvite(player)) {
            player.sendMessage(ChatColor.RED + "You do not have any pending invites.");
            return;
        }

        Island island = SkyBlock.getInstance().getIslandHandler().getIslandForPlayer(player);
        if (island != null) {
            player.sendMessage(ChatColor.RED + "You cannot join this island while you're already in an island.");
            return;
        }

        Island invite = SkyBlock.getInstance().getIslandHandler().getInviteFor(player);

        invite.getMembers().add(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + String.format("You have joined %s island.", invite.getName()));

        Player owner = Bukkit.getPlayer(invite.getOwner());

        if (owner == null) {
            return;
        }

        owner.sendMessage(ChatColor.GREEN + String.format("%s has accepted your island invite.", player.getName()));
        SkyBlock.getInstance().getIslandHandler().getIslandInvites().remove(player.getUniqueId());
        return;
    }
}
