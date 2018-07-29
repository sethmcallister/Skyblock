package org.pirateislands.skyblock.listener;

import com.islesmc.islandapi.Island;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseLocationHelper;

public class PlayerBucketEmtptyListener implements Listener {
    @EventHandler
    public void onSignChange(final SignChangeEvent event) {
        Island island = SkyBlock.getInstance().getIslandHandler().getIslandAt(event.getBlock().getLocation());
        if (island == null)
            return;

        if (island.isAllowed(event.getPlayer().getUniqueId()))
            return;

        event.setCancelled(true);
    }
}
