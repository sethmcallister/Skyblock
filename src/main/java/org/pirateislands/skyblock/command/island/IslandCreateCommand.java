package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import com.islesmc.islandapi.IslandType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.configuration.ServerType;
import org.pirateislands.skyblock.goose.GooseCommand;
import org.pirateislands.skyblock.misc.MessageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class IslandCreateCommand extends GooseCommand {

    public static final List<UUID> ISLAND_MAKING = new ArrayList<>();

    public IslandCreateCommand() {
        super("create", Lists.newArrayList(), true);
    }

    public void openIslandGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "Island Selection");

        for (IslandType type : IslandType.values()) {
            ItemStack item = new ItemStack(Material.PAPER, 1);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', type.getDisplay()));
            List<String> lore = Lists.newArrayList();

            for (String loreSTR : type.getLore()) {
                lore.add(ChatColor.translateAlternateColorCodes('&', loreSTR));
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.addItem(item);
        }

        player.openInventory(inv);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length > 0) {
            player.sendMessage(ChatColor.RED + "Usage: /island create");
            return;
        }

        if (SkyBlock.getPlugin().getIslandRegistry().hasIsland(player)) {
            player.sendMessage(ChatColor.RED + "You already have an island.");
            return;
        }

        if (ISLAND_MAKING.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You already have an island.");
            return;
        }

        MessageUtil.sendServerTheme(player, ChatColor.YELLOW + "Opening island selection...");
        openIslandGUI(player);
        return;
    }
}
