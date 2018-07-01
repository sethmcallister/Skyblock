package org.pirateislands.skyblock.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    private ItemStack item;

    private ItemUtils() {
    }

    public ItemUtils(Material mat) {
        this.item = new ItemStack(mat);
    }

    public ItemUtils(ItemStack item) {
        this.item = item;
    }

    public ItemUtils amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemUtils clearName() {
        Material falseMaterial = item.getType();
        item = new ItemStack(falseMaterial);
        return this;
    }

    public ItemUtils name(String name) {
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);
        return this;
    }

    public ItemUtils lore(String name) {
        final ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return this;
    }

    public ItemUtils lore(List<String> name) {
        final ItemMeta meta = item.getItemMeta();
        List<String> temp = new ArrayList<>();
        for (String line : name) {
            temp.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        meta.setLore(temp);
        item.setItemMeta(meta);
        return this;
    }

    public ItemUtils clearLore() {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(new ArrayList<>());
        item.setItemMeta(meta);
        return this;
    }

    public ItemUtils enchantBook(Enchantment enchantment, int level) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(enchantment, level, true);
        item.setItemMeta(meta);
        return this;
    }

    public ItemUtils data(int data) {
        item.setDurability((short) data);
        return this;
    }

    public ItemUtils enchant(Enchantment enchantment, final int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemStack build() {
        return item;
    }
}
