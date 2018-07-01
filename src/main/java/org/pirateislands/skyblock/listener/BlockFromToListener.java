package org.pirateislands.skyblock.listener;

import com.google.common.collect.Lists;
import com.islesmc.modules.api.API;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.pirateislands.skyblock.SkyBlock;

import java.util.*;

public class BlockFromToListener implements Listener {

    private final static List<BlockFace> FACES = Arrays.asList(BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
    private final Map<Material, Double> chances;
    private SkyBlock plugin = SkyBlock.getPlugin();
    private Random random = new Random();

    public BlockFromToListener() {
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
                            if (toMaterial.equals(Material.CHEST)) {
                                Chest chest = (Chest) to.getState();
                                for (ItemStack itemStack : generateLuckyChestItems()) {
                                    chest.getBlockInventory().addItem(itemStack);
                                }
                                chest.update();
                            }
                        }
                    }
                }
            }.runTask(API.getPlugin());
        }
    }

    private ItemStack generateRandomItemStack() {
        int number = random.nextInt(10);
        if (number == 1) {
            return new ItemStack(Material.DIAMOND, random.nextInt(12) + 1);
        } else if (number == 2) {
            ItemStack token = new ItemStack(Material.PAPER);
            ItemMeta meta = token.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&ePioneer Rank &f&lVoucher"));
            meta.setLore(Lists.newArrayList(ChatColor.translateAlternateColorCodes('&', "&7Right Click to redeem your rank!")));
            token.setItemMeta(meta);
            return token;
        } else if (number == 3) {
            ItemStack token = new ItemStack(Material.PAPER);
            ItemMeta meta = token.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&l[&e&k!!&cA&fm&be&cr&fi&bc&ca&e&k!!&8&l] &f&lTag Voucher"));
            meta.setLore(Lists.newArrayList(ChatColor.translateAlternateColorCodes('&', "&7Right Click to redeem your tag!")));
            token.setItemMeta(meta);
            return token;
        } else if (number == 4) {
            ItemStack token = new ItemStack(Material.PAPER);
            ItemMeta meta = token.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&l/fly command &f&lVoucher"));
            meta.setLore(Lists.newArrayList(ChatColor.translateAlternateColorCodes('&', "&7Right Click to redeem your command!")));
            token.setItemMeta(meta);
            return token;
        } else if (number == 5) {
            ItemStack token = new ItemStack(Material.PAPER);
            ItemMeta meta = token.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l$5,000 &f&lVoucher"));
            meta.setLore(Lists.newArrayList(ChatColor.translateAlternateColorCodes('&', "&7Right Click to redeem your $5,000!")));
            token.setItemMeta(meta);
            return token;
        } else if (number == 6) {
            ItemStack token = new ItemStack(Material.PAPER);
            ItemMeta meta = token.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l$500,000 &f&lVoucher"));
            meta.setLore(Lists.newArrayList(ChatColor.translateAlternateColorCodes('&', "&7Right Click to redeem your $500,000!")));
            token.setItemMeta(meta);
            return token;
        } else if (number == 7) {
            ItemStack token = new ItemStack(Material.PAPER);
            ItemMeta meta = token.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&l[&5&k!!&d&lMemelord&5&k!!&8&l] &f&lTag Voucher"));
            meta.setLore(Lists.newArrayList(ChatColor.translateAlternateColorCodes('&', "&7Right Click to redeem your tag!")));
            token.setItemMeta(meta);
            return token;
        } else if (number == 8) {
            ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK);
            ItemMeta meta = key.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&3Pirate Crate Key"));
            meta.setLore(Lists.newArrayList("§7Right-Click on a \"§3Pirate§7\" crate", "§7to win an item!", ""));
        }
        return new ItemStack(Material.AIR, 1);
    }

    private List<ItemStack> generateLuckyChestItems() {
        List<ItemStack> itemStacks = Lists.newArrayList();
        int noThings = random.nextInt(4);
        for (int i = 0; i < noThings; i++) {
            itemStacks.add(generateRandomItemStack());
        }
        return itemStacks;
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
