package org.pirateislands.skyblock.listener;

import com.islesmc.islandapi.Island;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.pirateislands.skyblock.SkyBlock;

public class PlayerTeleportListener implements Listener {
    @EventHandler
    public void onTeleport(final PlayerTeleportEvent event) {
        Island to = SkyBlock.getInstance().getIslandHandler().getIslandAt(event.getTo());
        if (to == null)
            return;

        if (to.isAllowed(event.getPlayer().getUniqueId()))
            return;

        if (!to.isExpelled(event.getPlayer().getUniqueId()))
            return;


        if (!to.getLocked())
            return;

        event.setTo(event.getFrom());
        event.getPlayer().sendMessage(ChatColor.RED + "You cannot teleport because the island you are teleporting to is locked.");
    }
}
