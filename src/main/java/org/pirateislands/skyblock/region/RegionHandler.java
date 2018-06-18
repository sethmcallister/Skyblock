package org.pirateislands.skyblock.region;

import com.islesmc.islandapi.Region;
import com.islesmc.islandapi.goose.GooseLocation;
import org.bukkit.Location;
import org.pirateislands.skyblock.goose.GooseLocationHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 2017-02-11.
 */
public class RegionHandler {
    private List<Region> regions = new ArrayList<>();


    public Region createRegion(String name, Location min, Location max) {
        if (isRegion(name)) {
            return null;
        }

        Region region = new Region(name, GooseLocationHelper.fromLocation(min), GooseLocationHelper.fromLocation(max));
        this.regions.add(region);
        return region;
    }

    public boolean isRegion(String name) {
        for (Region region : regions) {
            if (region.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public void deleteRegion(Region region) {
        this.regions.remove(region);
    }
}
