package org.pirateislands.skyblock.listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.dto.StackedSpawner;

import java.util.stream.IntStream;

public class SpawnerSpawnListener implements Listener {
    @EventHandler
    public void onSpawnerSpawn(final SpawnerSpawnEvent event) {
        Location location = event.getSpawner().getLocation();
        StackedSpawner stackedSpawner = SkyBlock.getInstance().getStackedSpawnerHandler().findByLocation(location);
        if (stackedSpawner == null)
            return;

        IntStream.range(0, stackedSpawner.getAmount().get()).forEachOrdered(i -> location.getWorld().spawnEntity(location, stackedSpawner.getEntityType()));
    }
}
