package org.pirateislands.skyblock.dto;

import com.islesmc.islandapi.goose.GooseLocation;
import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.pirateislands.skyblock.goose.GooseLocationHelper;

import java.util.concurrent.atomic.AtomicInteger;

public class StackedSpawner {
    private final EntityType entityType;
    private final AtomicInteger amount;
    private final GooseLocation location;
    private transient Hologram hologram;

    public StackedSpawner(final EntityType entityType, final GooseLocation gooseLocation) {
        this.entityType = entityType;
        this.amount = new AtomicInteger(0);
        this.location = gooseLocation;

        createHologram();
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public AtomicInteger getAmount() {
        return amount;
    }

    public Location getLocation() {
        return GooseLocationHelper.toLocation(this.location);
    }

    public Hologram getHologram() {
        return hologram;
    }

    public void createHologram() {
        this.hologram = HologramAPI.createHologram(getLocation().add(0.5, 1.5, 0.5), "");
        this.hologram.addViewHandler((hologram, player, s) -> String.format("%sx %s Spawner", amount.get(), entityType.getName()));
        this.hologram.spawn();
    }
}
