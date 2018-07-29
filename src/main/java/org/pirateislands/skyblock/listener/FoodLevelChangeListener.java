package org.pirateislands.skyblock.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.pirateislands.skyblock.SkyBlock;

public class FoodLevelChangeListener implements Listener {
    @EventHandler
    public void onFoodLevel(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) return;

        Player player = (Player) event.getEntity();

        if (!(player.getWorld() == SkyBlock.getInstance().getServerConfig().getSpawnLocation().getWorld())) return;

        event.setCancelled(true);
    }
}
