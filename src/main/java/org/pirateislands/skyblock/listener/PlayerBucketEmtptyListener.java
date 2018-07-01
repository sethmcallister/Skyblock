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
        Location location = event.getBlock().getLocation();

        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld()) return;

        Island island = SkyBlock.getPlugin().getIslandHandler().getIslandAt(location);
        if (island == null)
            return;

        if (!event.getLine(0).equalsIgnoreCase("[welcome]"))
            return;

        island.setWarpLocation(GooseLocationHelper.fromLocation(location));
        event.setLine(0, ChatColor.YELLOW + "[Welcome]");
    }
}
