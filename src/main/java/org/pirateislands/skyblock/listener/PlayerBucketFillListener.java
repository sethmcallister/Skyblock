package org.pirateislands.skyblock.listener;

import com.islesmc.islandapi.Island;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.pirateislands.skyblock.SkyBlock;

public class PlayerBucketFillListener implements Listener {
    @EventHandler
    public void onPlayerBucketFill(final PlayerBucketFillEvent event) {
        Island island = SkyBlock.getPlugin().getIslandHandler().getIslandAt(event.getBlockClicked().getLocation());
        if (island == null)
            return;

        if (island.isAllowed(event.getPlayer().getUniqueId()))
            return;

        event.setCancelled(true);
    }
}
