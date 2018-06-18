package org.pirateislands.skyblock.player.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {
    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (event.getFrom().getWorld().getUID().equals(event.getTo().getWorld().getUID()))
            return;


    }
}
