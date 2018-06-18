package org.pirateislands.skyblock.island;

import com.islesmc.modules.api.API;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.pirateislands.skyblock.SkyBlock;

import java.util.*;

/**
 * Created by Matt on 25/04/2017.
 */
public class IslandOreGens implements Listener {

    private final static List<BlockFace> FACES = Arrays.asList(BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
    private final Map<Material, Double> chances;
    private SkyBlock plugin = SkyBlock.getPlugin();
    private Random random = new Random();

    public IslandOreGens() {
        this.chances = SkyBlock.getPlugin().getOreGenerationConfig().getGenerationMap();
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        if (event.getNewState().getBlock().getType() == Material.COBBLESTONE || event.getNewState().getBlock().getType() == Material.OBSIDIAN) {
            System.out.println("Form event called");
        }
    }


    private Material getNextType() {
        double chance = random.nextDouble() * 100.0;
        double cumulative = 0.0;
        for (Map.Entry<Material, Double> entry : chances.entrySet()) {
            cumulative += entry.getValue();
            if (chance < cumulative)
                return entry.getKey();
        }
        return Material.COBBLESTONE;
    }


    @EventHandler
    public void onChange(BlockFromToEvent event) {
        Block before = event.getBlock();
        int id = before.getTypeId();
        Block to = event.getToBlock();

        if (event.getBlock() == null)
            return;

        if (SkyBlock.getPlugin().getIslandWorld() == null)
            return;

        if (!event.getBlock().getWorld().getName().equalsIgnoreCase(SkyBlock.getPlugin().getIslandWorld().getName())) {
            return;
        }

        if (before.getType() == Material.WATER || before.getType() == Material.STATIONARY_WATER || before.getType() == Material.LAVA || before.getType() == Material.STATIONARY_LAVA) {

            if (!generatesCobble(before, to))
                return;

            List<Block> previousBlocks = new ArrayList<>();
            List<Material> previousMaterials = new ArrayList<>();

            for (BlockFace face : FACES) {
                Block block = to.getRelative(face);
                previousBlocks.add(block);
                previousMaterials.add(block.getType());
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    Iterator<Block> blockIterator = previousBlocks.iterator();
                    Iterator<Material> materialIterator = previousMaterials.iterator();

                    if (blockIterator.hasNext() && materialIterator.hasNext()) {
                        Block block = blockIterator.next();
                        Material material = materialIterator.next();

                        if (block.getType().equals(Material.COBBLESTONE) && !block.getType().equals(material)) {
                            Material toMaterial = getNextType();
                            to.setType(toMaterial);
                        }
                    }
                }
            }.runTask(API.getPlugin());

//            if (generatesCobble(before, to)) {
//
//
//                final List<Block> prevBlock = new ArrayList<>();
//                final List<Material> prevMat = new ArrayList<>();
//                for (BlockFace face : FACES) {
//                    Block r = to.getRelative(face);
//                    prevBlock.add(r);
//                    prevMat.add(r.getType());
//
//                }
//
//                Bukkit.getServer().getScheduler().runTask(API.getPlugin(), () -> {
//                    Iterator<Block> blockIt = prevBlock.iterator();
//                    Iterator<Material> matIt = prevMat.iterator();
//                    while (blockIt.hasNext() && matIt.hasNext()) {
//                        Block block = blockIt.next();
//                        Material material = matIt.next();
//                        if (block.getType().equals(Material.COBBLESTONE) && !block.getType().equals(material)) {
//
//                            double chance = 0;
//
//                            double cobble = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.COBBLESTONE);
//                            double coal = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.COAL_ORE);
//                            double iron = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.IRON_ORE);
//                            double diamond = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.DIAMOND_ORE);
//                            double lapis = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.LAPIS_ORE);
//                            double redstone = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.REDSTONE_ORE);
//                            double gold = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.GOLD_ORE);
//                            double emerald = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.EMERALD_ORE);
//
//                            chance = chance + random.nextInt(100);
//
//                            // spamming chat System.out.println("CHANCE: " + chance);
//
//                            if (chance > 0 && chance <= emerald) {
//                                to.setType(Material.EMERALD_ORE);
//                            }
//                            if (chance > emerald && chance <= diamond) {
//                                to.setType(Material.DIAMOND_ORE);
//                            }
//                            if (chance > diamond && chance <= gold) {
//                                to.setType(Material.GOLD_ORE);
//                            }
//                            if (chance > gold && chance <= iron) {
//                                to.setType(Material.IRON_ORE);
//                            }
//                            if (chance > iron && chance <= redstone) {
//                                to.setType(Material.REDSTONE_ORE);
//                            }
//                            if (chance > redstone && chance <= coal) {
//                                to.setType(Material.COAL_ORE);
//                            }
//                            if (chance > coal && chance <= lapis) {
//                                to.setType(Material.LAPIS_ORE);
//                            }
//                            if (chance > lapis && chance <= 100) {
//                                to.setType(Material.COBBLESTONE);
//                            }
//                        }
//                    }
//                });
//            }
        }
    }

    public boolean generatesCobble(Block block, Block toBlock) {
        Material mirrorID1 = (block.getType().equals(Material.WATER)) || (block.getType().equals(Material.STATIONARY_WATER)) ? Material.LAVA : Material.WATER;
        Material mirrorID2 = (block.getType().equals(Material.WATER)) || (block.getType().equals(Material.STATIONARY_WATER)) ? Material.STATIONARY_LAVA : Material.STATIONARY_WATER;
        for (BlockFace face : FACES) {
            Block r = toBlock.getRelative(face);
            if ((r.getType().equals(mirrorID1)) || (r.getType().equals(mirrorID2))) {
                return true;
            }
        }
        return false;
    }
}
