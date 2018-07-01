package org.pirateislands.skyblock.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtil {
    private LocationUtil() {
    }

    public static Location deserialize(String serialized) {
        String[] split = serialized.split(",");

        World world = Bukkit.getWorld(split[0]);
        double x = Double.valueOf(split[1]);
        double y = Double.valueOf(split[2]);
        double z = Double.valueOf(split[3]);

        return new Location(world, x, y, z);
    }

    public static String serialize(Location loc) {
        if (loc == null)
            return "";

        return loc.getWorld() == null ? "world" : loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }
}
