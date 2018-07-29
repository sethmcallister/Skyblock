package org.pirateislands.skyblock.handler;

import com.islesmc.islandapi.Island;
import com.islesmc.islandapi.IslandAPI;
import com.islesmc.islandapi.IslandType;
import com.islesmc.modules.api.API;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.configuration.ServerType;
import org.pirateislands.skyblock.goose.GooseLocationHelper;
import org.pirateislands.skyblock.task.IslandCreateQueueTask;
import org.pirateislands.skyblock.util.MessageUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public class IslandHandler {

    public final List<Island> playerIslands = new ArrayList<>();

    private final int islandDistance = 1000;

    private final int baseIslandSize = 80;

    private final File islandDir = new File(SkyBlock.getInstance().getModuleDir().toString(), "islands");

    private final Map<UUID, Island> islandInvites = new HashMap<>();

    private final IslandCreateQueueTask queueTask;

    private Location lastIsland = null;

    public IslandHandler() {
        this.queueTask = new IslandCreateQueueTask();
        this.queueTask.runTaskTimer(API.getPlugin(), 40L, 40L);
        loadIslands();
    }

    public static Location alignToDistance(Location loc, int distance) {
        if (loc == null) {
            return null;
        }
        int x = (int) (Math.round(loc.getX() / distance) * distance);
        int z = (int) (Math.round(loc.getZ() / distance) * distance);
        loc.setX(x);
        loc.setY(100);
        loc.setZ(z);
        return loc;
    }

    public void registerIsland(UUID owner, Island island) {
        this.playerIslands.add(island);
    }

    public void disable() {
        for (Island island : playerIslands) {

//            File islandFile = getFileForIsland(island);
//            YamlConfiguration config = YamlConfiguration.loadConfiguration(islandFile);
//
//            if (island == null) {
//                return;
//            }
//
//            config.set("owner", island.getOwner());
//            config.set("min", LocationUtil.serialize(island.getContainer().getMin()));
//            config.set("max", LocationUtil.serialize(island.getContainer().getMax()));
//            List<String> mems = config.getStringList("members");
//            for (UUID uuid : island.getMembers()) {
//                if (!mems.contains(uuid.toString())) {
//                    continue;
//                }
//                mems.add(uuid.toString());
//            }
//            config.set("members", mems);
//            config.set("spawn", island.getSpawn());
//            config.set("maxPlayers", island.getMaxPlayers());
//            config.set("balance", island.getBankBalance());
            island.save();
        }
    }

    public boolean hasIsland(Player player) {
        for (Island island : playerIslands) {
            if (island.getOwner().equals(player.getUniqueId()) || island.getMembers().contains(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public List<Island> getPlayerIslands() {
        return playerIslands;
    }

    private void loadIslands() {

        IslandAPI.getRedisIslandDAO().findAll().stream().filter(Objects::nonNull).forEach(this.playerIslands::add);

//        if (!islandDir.exists()) {
//            islandDir.mkdir();
//        }
//
//        File[] files = islandDir.listFiles();
//
//        for (int i = 0; i < files.length; i++) {
//            File file = files[i];
//
//            JsonParser parser = new JsonParser();
//
//
//            try (FileReader reader = new FileReader(file)) {
//                JsonElement element = parser.parse(reader);
//                Island island = new GsonBuilder().setPrettyPrinting().create().fromJson(element, Island.class);
//                this.playerIslands.add(island);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

//        for (File islandFile : islandDir.listFiles()) {
//            if (islandFile.getName().endsWith(".yml")) {
//                //ISLAND!!
//
//                YamlConfiguration islandConfig = YamlConfiguration.loadConfiguration(islandFile);
//
//                UUID ownerID = UUID.fromString(islandConfig.getString("ownerID"));
//                Location spawn = LocationUtil.deserialize(islandConfig.getString("spawn"));
//
//                Location min = LocationUtil.deserialize(islandConfig.getString("min"));
//                Location max = LocationUtil.deserialize(islandConfig.getString("max"));
//
//                IslandType type = IslandType.valueOf(islandConfig.getString("type"));
//
//                int bankBalance = islandConfig.getInt("balance");
//                int islandLevel = islandConfig.getInt("islandLevel");
//                int maxPlayers = islandConfig.getInt("maxPlayers");
//
//                List<UUID> members = new ArrayList<>();
//
//                for (String uuid : islandConfig.getStringList("members")) {
//                    UUID id = UUID.fromString(uuid);
//                    members.add(id);
//                }
//
//                Island island = new Island(ownerID, spawn, type);
//                island.setContainer(SkyBlock.getInstance().getRegionHandler().createRegion(island.getName(), min, max));
//                island.setMembers(members);
//                island.setMaxPlayers(maxPlayers);
//                island.setBankBalance(bankBalance);
//                island.setIslandLevel(islandLevel);
//
//                registerIsland(ownerID, island);
//            }
//        }
        if (playerIslands.isEmpty()) {
            this.lastIsland = new Location(SkyBlock.getInstance().getIslandWorld(), 0, 100, 0);
        } else {
            if (SkyBlock.getInstance() == null) {
                System.out.println("Skyblock instance null");
            }

            if (SkyBlock.getInstance().getServerConfig() == null) {
                System.out.println("config null");
            }

            if (SkyBlock.getInstance().getServerConfig().getLastIslandLocation() == null) {
                this.lastIsland = new Location(SkyBlock.getInstance().getIslandWorld(), 0, 100, 0);
                return;
            }


            this.lastIsland = GooseLocationHelper.toLocation(SkyBlock.getInstance().getServerConfig().getLastIslandLocation());
        }
    }

    private boolean isIslandLevelBlock(Block block) {
        Material type = block.getType();

        return type == Material.IRON_BLOCK
                || type == Material.GOLD_BLOCK
                || type == Material.LAPIS_BLOCK
                || type == Material.REDSTONE_BLOCK
                || type == Material.DIAMOND_BLOCK
                || type == Material.COAL_BLOCK
                || type == Material.EMERALD_BLOCK;
    }

    public void calculateIslandLevel(final Island island) {
        Location point1 = GooseLocationHelper.toLocation(island.getContainer().getMin());
        Location point2 = GooseLocationHelper.toLocation(island.getContainer().getMax());


        Vector max = Vector.getMaximum(point1.toVector(), point2.toVector());
        Vector min = Vector.getMinimum(point1.toVector(), point2.toVector());

        List<Block> blocks = new ArrayList<>();

        for (int i = min.getBlockX(); i <= max.getBlockX(); i++) {
            for (int j = min.getBlockY(); j <= max.getBlockY(); j++) {
                for (int k = min.getBlockZ(); k <= max.getBlockZ(); k++) {
                    Block block = point1.getWorld().getBlockAt(i, j, k);

                    if (isIslandLevelBlock(block)) {
                        blocks.add(block);
                    }

                    // do what you want because a pirate is free
                }
            }
        }

        island.setIslandLevel(handleBlock(blocks));
    }

//    public void calculateIslandLevel(final Island island) {
//        Location min = GooseLocationHelper.toLocation(island.getContainer().getMin());
//        Location max = GooseLocationHelper.toLocation(island.getContainer().getMax());
//
//        int minX = min.getBlockX();
//        int minY = min.getBlockY();
//        int minZ = min.getBlockZ();
//
//        int maxX = max.getBlockX();
//        int maxY = max.getBlockY();
//        int maxZ = max.getBlockZ();
//
//        List<Block> blocks = new ArrayList<>();
//
//        for (int x = minX; x < maxX; x++) {
//            for (int y = 0; y < 256; y++) {
//                for (int z = minZ; z < maxZ; z++) {
//
//                    Block block = SkyBlock.getInstance().getIslandWorld().getBlockAt(x, y, z);
//                    if (block.getType().equals(Material.BARRIER))
//                        block.setType(Material.AIR);
//
//                    if (isIslandLevelBlock(block)) {
//                        blocks.add(block);
//                    }
//                }
//            }
//        }
//        island.setIslandLevel(handleBlock(blocks));
//    }

    private int handleBlock(List<Block> blocks) {
        double islandLevel = 0;

        for (Block block : blocks) {

            switch (block.getType()) {

                case LAPIS_BLOCK:
                    islandLevel += 0.25;
                    break;
                case GOLD_BLOCK:
                    islandLevel += 0.5;
                    break;
                case IRON_BLOCK:
                    islandLevel += 0.5;
                    break;
                case REDSTONE_BLOCK:
                    islandLevel += 0.25;
                    break;
                case DIAMOND_BLOCK:
                    islandLevel += 0.75;
                    break;
                case COAL_BLOCK:
                    islandLevel += 0.5;
                    break;
                case EMERALD_BLOCK:
                    islandLevel += 1.0;
                    break;
            }
        }
        return (int) Math.ceil(islandLevel);
    }

    public void createIsland(final Player player, final IslandType type) {
        if (hasIsland(player)) {
            player.sendMessage(ChatColor.RED + "You already have an island.");
            return;
        }

        this.queueTask.queueIsland(player, type);

        ItemStack item = getSkull("http://textures.minecraft.net/texture/b5651a18f54714b0b8f7f011c018373b33fd1541ca6f1cfe7a6c97b65241f5");
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Christmas Present");
        item.setItemMeta(meta);

//        player.getInventory().addItem(item);

        MessageUtil.sendServerTheme(player, ChatColor.GREEN + "Please wait while we create your island.");
//        MessageUtil.sendServerTheme(player, ChatColor.GREEN + "Merry Christmas! Have a starter present :)");
    }

    public ItemStack getSkull(String url) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (url.isEmpty()) return head;


        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = org.apache.commons.codec.binary.Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

//    public void createIsland(Player player, IslandType type) throws MaxChangedBlocksException {
//
//        if (hasIsland(player)) {
//            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You already have an island!");
//            return;
//        }
//
//        Location center = findEmptySpace();
////        Location center = nextIslandLocation(lastIsland == null ? new Location(SkyBlock.getInstance().getIslandWorld(), 0, 100, 0) : lastIsland);
//
//        Island island = new Island(player.getUniqueId(), center, type);
//        island.setSize(baseIslandSize);
//
//        double minX = center.getBlockX() - island.getType().getSize() / 2;
//        double minY = 0;
//        double minZ = center.getBlockZ() - island.getType().getSize() / 2;
//
//        int maxX = center.getBlockX() + island.getType().getSize() / 2;
//        int maxY = 256;
//        int maxZ = center.getBlockZ() + island.getType().getSize() / 2;
//
//
//        Location min = new Location(SkyBlock.getInstance().getIslandWorld(), minX, minY, minZ);
//        Location max = new Location(SkyBlock.getInstance().getIslandWorld(), maxX, maxY, maxZ);
//
//        Region container = SkyBlock.getInstance().getRegionHandler().createRegion(island.getName(), min, max);
//        island.setContainer(container);
//        island.setMembers(new ArrayList<>());
//        island.setIslandLevel(0);
//
//        island.setMaxPlayers(4);
//
////        File islandFile = new File(islandDir, player.getUniqueId().toString() + ".yml");
////
////        if (!islandFile.exists()) {
////            try {
////                islandFile.createNewFile();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }
////
////        YamlConfiguration config = YamlConfiguration.loadConfiguration(islandFile);
////
////        config.set("ownerID", player.getUniqueId().toString());
////        config.set("min", LocationUtil.serialize(min));
////        config.set("max", LocationUtil.serialize(max));
////        config.set("members", "");
////        config.set("spawn", LocationUtil.serialize(island.getSpawn()));
////        config.set("type", island.getType().toString());
////        config.set("balance", 0);
////        config.set("islandLevel", 0);
////        config.set("maxPlayers", 4);
//
//        island.save();
//
//        try {
//            SkyBlock.getInstance().getSchematicUtil().pasteSchematic(type.name().toLowerCase() + ".schematic", SkyBlock.getInstance().getIslandWorld(), center.getBlockX(), 100, center.getBlockZ());
//        } catch (DataException | IOException e) {
//            e.printStackTrace();
//        }
//
//        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] Your Island is ready!");
//        player.sendMessage(ChatColor.GRAY + "Type (/is home) to visit.");
//        registerIsland(player.getUniqueId(), island);
//
//        this.lastIsland = center.clone();
//    }

    public Location findEmptySpace() {
        if (playerIslands.isEmpty()) {
            return new Location(SkyBlock.getInstance().getIslandWorld(), 0, 100, 0);
        }

        Location base = null;

        for (Island island : playerIslands) {

            base = GooseLocationHelper.toLocation(island.getSpawn()).clone().add(islandDistance, 0, 0);
            int i = 0;
            while (conflicts(base) && i < 500000) {
                System.out.println("Conflicts");
                base = base.clone().add(islandDistance, 0, 0);
                i++;
            }
            System.out.println("Found base: " + base.getBlockX() + ", " + base.getBlockZ());
            break;
        }
        return base;
    }

    public boolean conflicts(Island island) {
        for (Island conflict : playerIslands) {
            if (GooseLocationHelper.toLocation(conflict.getSpawn()).toVector().isInAABB(GooseLocationHelper.toLocation(island.getContainer().getMin()).toVector(), GooseLocationHelper.toLocation(island.getContainer().getMax()).toVector()) ||
                    GooseLocationHelper.toLocation(island.getSpawn()).toVector().isInAABB(GooseLocationHelper.toLocation(conflict.getContainer().getMin()).toVector(), GooseLocationHelper.toLocation(island.getContainer().getMax()).toVector())) {
                return true;
            }
        }
        return false;
    }

    public boolean conflicts(Location loc) {
        for (Island conflict : playerIslands) {
            if (conflict == null || conflict.getContainer() == null || loc.toVector() == null)
                return true;

            if (loc.toVector().isInAABB(GooseLocationHelper.toLocation(conflict.getContainer().getMin()).toVector(), GooseLocationHelper.toLocation(conflict.getContainer().getMax()).toVector())) {
                return true;
            }
        }
        return false;
    }

    private boolean inInside(Location location, Location loc1, Location loc2) {
        double x1 = loc1.getX();
        double y1 = loc1.getY();
        double z1 = loc1.getZ();

        double x2 = loc2.getX();
        double y2 = loc2.getY();
        double z2 = loc2.getZ();

        return (location.getX() > x1) && (location.getY() > y1) && (location.getZ() > z1) && (location.getX() < x2) && (location.getY() < y2) && (location.getZ() < z2);
    }

    public Island getIslandAt(Location location) {
        for (Island island : this.playerIslands) {
            if ((island != null) && (location != null) && (island.getContainer() != null) && (island.getContainer().getMin() != null) && (island.getContainer().getMax() != null)) {
                Location min = GooseLocationHelper.toLocation(island.getContainer().getMin());
                Location max = GooseLocationHelper.toLocation(island.getContainer().getMax());

                if (location.toVector().isInAABB(max.toVector(), min.toVector())) {
                    return island;
                }
            }
        }
        return null;
    }

    public Island findByUniqueId(final UUID uuid) {
        for (Island island : this.playerIslands) {
            if (island.getOwner().equals(uuid) || island.getMembers().contains(uuid)) {
                return island;
            }
        }
        return null;
    }

    public void deleteIsland(Player player, Island island) {

        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.getWorld().getName().equalsIgnoreCase(SkyBlock.getInstance().getIslandWorld().getName())) {
                if (isInIslandRegion(island, pl.getLocation())) {
                    pl.teleport(SkyBlock.getInstance().getServerConfig().getSpawnLocation());
                    MessageUtil.sendServerTheme(pl, ChatColor.RED + "The island you where in was deleted, you are now in spawn.");
                }
            }
        }

        IslandAPI.getRedisIslandDAO().delete(island);

//        File file = new File(SkyBlock.getInstance().getModuleDir().toString() + File.separator + "islands" + File.separator + island.getOwner().toString().replace("-", "") + ".json");
//
//        if (!file.exists()) {
//            return;
//        }
//        file.delete();


//        int minX = island.getContainer().getMin().getBlockX();
//        int minY = island.getContainer().getMin().getBlockY();
//        int minZ = island.getContainer().getMin().getBlockZ();
//
//        int maxX = island.getContainer().getMax().getBlockX();
//        int maxY = island.getContainer().getMax().getBlockY();
//        int maxZ = island.getContainer().getMax().getBlockZ();
//
//        for (int x = minX; x < maxX; x++) {
//
//            for (int y = minY; y < maxY; y++) {
//
//                for (int z = minZ; z < maxZ; z++) {
//
//                    Block block = SkyBlock.getInstance().getIslandWorld().getBlockAt(x, y, z);
//                    block.setType(Material.AIR);
//                }
//            }
//        }


//        SkyBlock.getInstance().getRegionHandler().deleteRegion(island.getContainer());
        playerIslands.remove(island);
    }

    public boolean isInIslandRegion(Island island, Location loc) {
        Vector min = GooseLocationHelper.toLocation(island.getContainer().getMin()).toVector();
        Vector max = GooseLocationHelper.toLocation(island.getContainer().getMax()).toVector();

        return loc.toVector().isInAABB(max, min) && loc.getWorld().getName().equalsIgnoreCase(GooseLocationHelper.toLocation(island.getSpawn()).getWorld().getName());
    }

    public Island getIslandForPlayer(Player player) {
        for (Island island : playerIslands) {
            if (island == null)
                continue;

            if (island.getOwner().equals(player.getUniqueId()) || island.getMembers().contains(player.getUniqueId())) {
                return island;
            }
        }
        return null;
    }

    public boolean hasInvite(Player player) {
        return islandInvites.get(player.getUniqueId()) != null;
    }

    public Island getInviteFor(Player player) {
        if (hasInvite(player)) {
            return islandInvites.get(player.getUniqueId());
        }
        return null;
    }

    public Location getNextLocation(final Location last) {
        final int x = last.getBlockX();
        final int z = last.getBlockZ();

        final int spacing = (SkyBlock.getInstance().getServerConfig().getServerType() == ServerType.ISLES ? 450 : 1000);

        final Location next = last;
        if (x < z) {
            if (-1 * x < z) {
                next.setX(next.getX() + spacing);
                return next;
            }
            next.setZ(last.getZ() + spacing);
            return next;
        }
        if (x > z) {
            if (-1 * x >= z) {
                next.setX(next.getX() - spacing);
                return next;
            }
            next.setZ(next.getZ() - spacing);
            return next;
        }
        if (x <= 0) {
            next.setZ(next.getZ() + spacing);
            return next;
        }
        next.setZ(next.getZ() - spacing);
        return next;
    }

    public Location nextIslandLocation(final Location lastIsland) {
        int d = islandDistance;

        alignToDistance(lastIsland, d);
        int x = lastIsland.getBlockX();
        int z = lastIsland.getBlockZ();
        if (x < z) {
            if (-1 * x < z) {
                x += d;
            } else {
                z += d;
            }
        } else if (x > z) {
            if (-1 * x >= z) {
                x -= d;
            } else {
                z -= d;
            }
        } else { // x == z
            if (x <= 0) {
                z += d;
            } else {
                z -= d;
            }
        }
        lastIsland.setX(x);
        lastIsland.setZ(z);
        return lastIsland;
    }

    public Map<UUID, Island> getIslandInvites() {
        return islandInvites;
    }

    @Deprecated
    public File getFileForIsland(Island island) {
        return new File(islandDir, island.getOwner().toString() + ".yml");
    }

    public int getBaseIslandSize() {
        return baseIslandSize;
    }

    public Location getLastIsland() {
        return lastIsland;
    }

    public void setLastIsland(Location lastIsland) {
        this.lastIsland = lastIsland;
    }
}
