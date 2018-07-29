package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import com.islesmc.islandapi.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseCommand;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandDeclineCommand extends GooseCommand {

    public IslandDeclineCommand() {
        super("decline", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] strings) {
        if (!SkyBlock.getInstance().getIslandHandler().hasInvite(player)) {
            player.sendMessage(ChatColor.RED + "You do not have any pending invitations.");
            return;
        }

        Island invite = SkyBlock.getInstance().getIslandHandler().getInviteFor(player);

        player.sendMessage(ChatColor.YELLOW + String.format("You have declined %s's invite.", invite.getName()));
        Player owner = Bukkit.getPlayer(invite.getOwner());

        if (owner == null) {
            return;
        }

        owner.sendMessage(ChatColor.YELLOW + String.format("%s has declined his invitation to your island.", player.getName()));
        SkyBlock.getInstance().getIslandHandler().getIslandInvites().remove(player.getUniqueId());
        return;
    }
}
