package org.pirateislands.skyblock.task;

import com.islesmc.islandapi.Island;
import com.islesmc.islandapi.IslandAPI;
import com.islesmc.islandapi.IslandType;
import com.islesmc.islandapi.Region;
import com.islesmc.islandapi.goose.GooseLocation;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.command.island.IslandCreateCommand;
import org.pirateislands.skyblock.configuration.ServerType;
import org.pirateislands.skyblock.goose.GooseLocationHelper;
import org.pirateislands.skyblock.util.LocationUtil;
import org.pirateislands.skyblock.util.MessageUtil;
import org.pirateislands.skyblock.util.SchematicUtil;

import java.io.IOException;
import java.util.*;

public class IslandCreateQueueTask extends BukkitRunnable {
    private final Queue<QueueItem> islandQueue;

    public IslandCreateQueueTask() {
        this.islandQueue = new PriorityQueue<>((queueItem, t1) -> 0);
    }

    public void queueIsland(final Player player, final IslandType type) {
        this.islandQueue.add(new QueueItem(player, type));
    }

    @Override
    public void run() {
        for (QueueItem item : islandQueue) {

            if (item == null || item.getPlayer() == null || item.getType() == null) {
                continue;
            }

//            int islandNumber = SkyBlock.getPlugin().getServerConfig().getIslandsEverCreated().incrementAndGet();
//            int prime = SkyBlock.getPlugin().getGridUtil().getInts()[islandNumber];
//
//            int xZ = prime * 1000;

            Location center = getLocation();

            Player player = item.getPlayer();

//            Location center;
//
//            if (SkyBlock.getPlugin().getIslandHandler().getLastIsland() == null) {
//                center = new Location(SkyBlock.getPlugin().getIslandWorld(), 0, 100, 0);
//            }
//            else {
//                center = SkyBlock.getPlugin().getIslandHandler().nextIslandLocation(SkyBlock.getPlugin().getIslandHandler().getLastIsland());
//            }

            if (center == null) {
                center = new Location(SkyBlock.getPlugin().getIslandWorld(), 0, 100, 0);
            }

            if (center.getWorld() == null || center.getWorld().getName().equalsIgnoreCase("world")) {
                center = new Location(SkyBlock.getPlugin().getIslandWorld(), 0, 100, 0);
                System.out.println("center null again");
            }

            // For some reason this fixes it??
            center = center.clone().add(0, 0, 0);
            if (SkyBlock.getPlugin().getServerConfig().getServerType().equals(ServerType.ISLES)) {
                center = center.subtract(0, 2, 0);
            }
            System.out.println(String.format("LOCATION SERIALIZED IS %s", LocationUtil.serialize(center)));

            Island island = new Island(generateNewUUID(), player.getName(), player.getUniqueId(), GooseLocationHelper.fromLocation(center), item.getType());
            island.setSize(island.getType().getSize());

            double minX = center.getBlockX() - island.getType().getSize() / 2D;
            double minY = 0D;
            double minZ = center.getBlockZ() - island.getType().getSize() / 2D;

            double maxX = center.getBlockX() + island.getType().getSize() / 2D;
            double maxY = 256D;
            double maxZ = center.getBlockZ() + island.getType().getSize() / 2D;


            Location min = new Location(SkyBlock.getPlugin().getIslandWorld(), minX, minY, minZ);
            Location max = new Location(SkyBlock.getPlugin().getIslandWorld(), maxX, maxY, maxZ);

            Region container = SkyBlock.getPlugin().getRegionHandler().createRegion(island.getName(), min, max);
            island.setContainer(container);
            island.setMembers(new ArrayList<>());
            island.setExpelled(new ArrayList<>());
            island.setIslandLevel(0);


            int maxPlayers = 4;
            if (SkyBlock.getPlugin().getServerConfig().getServerType() == ServerType.ISLES)
                maxPlayers = 5;

            island.setMaxPlayers(maxPlayers);

            int y = SkyBlock.getPlugin().getServerConfig().getServerType().equals(ServerType.ISLES) ? 46 : 100;

            try {
                SchematicUtil.pasteSchematic(item.type.name().toLowerCase() + ".schematic", SkyBlock.getPlugin().getIslandWorld(), center.getBlockX(), y, center.getBlockZ());
            } catch (DataException | IOException | MaxChangedBlocksException e) {
                e.printStackTrace();
            }

            if (item.getType() == IslandType.DEFAULT) {
                center = center.clone().add(4, 8, 6);
                System.out.println("default island");
            }
            island.setSpawn(GooseLocationHelper.fromLocation(center));

            MessageUtil.sendServerTheme(player, ChatColor.GREEN + "Your island has been created.");
            player.sendMessage(ChatColor.GREEN + "Type (/is home) to visit.");
            SkyBlock.getPlugin().getIslandHandler().registerIsland(player.getUniqueId(), island);
            this.islandQueue.remove(item);
            SkyBlock.getPlugin().getIslandHandler().setLastIsland(center);
            SkyBlock.getPlugin().getServerConfig().setLastIslandLocation(GooseLocationHelper.fromLocation(center));
            SkyBlock.getPlugin().getServerConfig().save();

            IslandCreateCommand.ISLAND_MAKING.remove(player.getUniqueId());
            island.save();
        }
    }

    private Location getLocation() {
        GooseLocation last = SkyBlock.getPlugin().getServerConfig().getLastIslandLocation();
        if (last == null) {
            last = GooseLocationHelper.fromLocation(new Location(SkyBlock.getPlugin().getIslandWorld(), 0, 0, 0));
        }
        Location next = SkyBlock.getPlugin().getIslandHandler().getNextLocation(GooseLocationHelper.toLocation(last));
        while (SkyBlock.getPlugin().getIslandHandler().getIslandAt(next) != null) {
            next = SkyBlock.getPlugin().getIslandHandler().getNextLocation(next);
        }
        return next;
    }

    private UUID generateNewUUID() {
        UUID uuid = UUID.randomUUID();
        while (IslandAPI.getRedisIslandDAO().findById(uuid) != null) {
            uuid = UUID.randomUUID();
        }
        return uuid;
    }

    private class QueueItem implements Comparable<QueueItem> {
        private final Player player;
        private final IslandType type;

        QueueItem(final Player player, final IslandType type) {
            this.player = player;
            this.type = type;
        }

        Player getPlayer() {
            return player;
        }

        IslandType getType() {
            return type;
        }

        @Override
        public int compareTo(QueueItem o) {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (o == null)
                return false;

            if (getClass() != o.getClass())
                return false;

            QueueItem item = (QueueItem) o;
            return Objects.equals(this, item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(player, type);
        }
    }
}
