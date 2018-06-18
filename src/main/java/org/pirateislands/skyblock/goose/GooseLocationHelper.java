package org.pirateislands.skyblock.goose;

import com.islesmc.islandapi.goose.GooseLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class GooseLocationHelper {
    public static Location toLocation(final GooseLocation location) {
        return new Location(Bukkit.getWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
    }

    public static GooseLocation fromLocation(final Location location) {
        return new GooseLocation(location.getWorld().getUID(), location.getX(), location.getY(), location.getZ(), (double) location.getYaw());
    }
}
