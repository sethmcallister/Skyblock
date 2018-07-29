package org.pirateislands.skyblock.task;

import com.google.common.collect.Lists;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EnvoyCrateTask extends BukkitRunnable {
    private final Random random;

    public EnvoyCrateTask() {
        this.random = new Random();
    }

    @Override
    public void run() {
        World world = Bukkit.getWorld("Warzone");
        if (world == null)
            return;

        int r = random.nextInt(100);
        int x = random.nextInt(r);
        int z = (int) Math.sqrt(Math.pow(r,2) - Math.pow(x,2));
        if(random.nextBoolean())
            x *= -1;
        if(random.nextBoolean())
            z *= -1;


        int y = world.getHighestBlockYAt(x, z);
        Location location = new Location(world, x, y, z);
        location.getBlock().setType(Material.CHEST);

        Chest chest = (Chest)location.getBlock().getState();
        Inventory inventory = chest.getBlockInventory();
        List<ItemStack> chestItems = generateChestItems();
        chestItems.forEach(inventory::addItem);
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Pirate" + ChatColor.WHITE + "Islands " + ChatColor.GRAY + "\u00BB " + ChatColor.YELLOW + "An " + ChatColor.DARK_RED + ChatColor.BOLD + "Envoy Crate " + ChatColor.YELLOW + " has spawned in Warzone! Go and find it.");
    }

    private List<ItemStack> generateChestItems() {
        List<ItemStack> list;

        int amount = random.nextInt(3) + 1;
        list = IntStream.range(0, amount).map(i -> random.nextInt(4) + 1).mapToObj(this::getRandomItem).collect(Collectors.toList());
        return list;
    }

    private ItemStack getRandomItem(int no) {
        if (no == 1 || no == 2) {
            return new ItemStack(Material.DIAMOND_BLOCK, 20);
        } else if (no == 3) {
            ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
            itemStack.addEnchantment(Enchantment.FIRE_ASPECT, 1);
            itemStack.addEnchantment(Enchantment.DAMAGE_ALL, 5);
            itemStack.addEnchantment(Enchantment.DURABILITY, 3);

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Envoy Sword");
            itemMeta.setLore(Lists.newArrayList("§4§lEnvoy Sword"));
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        } else if (no == 4) {
            return new ItemStack(Material.EMERALD_BLOCK, 7);
        } else if (no == 5) {
            ItemStack itemStack = new ItemStack(351, 1, (byte)13);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "haste Enchantment Token");

            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.LIGHT_PURPLE + "This token grants: ");
            lore.add(ChatColor.LIGHT_PURPLE + "  Enchantment: haste");
            lore.add(ChatColor.LIGHT_PURPLE + "  Level: " + 1);
            lore.add("");
            lore.add(ChatColor.YELLOW + "Right click to use.");
            itemMeta.setLore(lore);
            return itemStack;
        }
        return new ItemStack(Material.AIR, 1);
    }
}
