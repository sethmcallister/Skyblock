package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import com.islesmc.islandapi.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseCommand;

import java.util.*;

/**
 * Created by Matt on 17/04/2017.
 */
public class IslandTopCommand extends GooseCommand implements Listener {
    private Inventory islandTop = Bukkit.createInventory(null, 36, "Top Islands");

    public IslandTopCommand() {
        super("top", Lists.newArrayList(), true);
        SkyBlock.getPlugin().registerEvent(this);
    }

    public static List<Map.Entry<Island, Integer>> getTopIslands() {
        Map<Island, Integer> islands = new HashMap<>();

        for (Island island : SkyBlock.getPlugin().getIslandRegistry().getPlayerIslands()) {
            if (island == null)
                continue;


            islands.put(island, island.getIslandLevel());
        }

        int topSize = 10;

        if (islands.size() < 10) {
            topSize = islands.size();
        }

        Set<Map.Entry<Island, Integer>> set = islands.entrySet();

        List<Map.Entry<Island, Integer>> list = new ArrayList<>(set);

        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        return list;
    }

    private Inventory createIslandTopInventory() {
        Inventory inventory = Bukkit.createInventory(null, 36, ChatColor.WHITE + "Top Islands");

        int max = 10;
        if (SkyBlock.getPlugin().getIslandRegistry().playerIslands.size() < 10) {
            max = SkyBlock.getPlugin().getIslandRegistry().playerIslands.size();
        }

        int index = 0;
        for (Map.Entry<Island, Integer> ent : getTopIslands().subList(0, max)) {
            index++;

            UUID uuid = ent.getKey().getOwner();

            OfflinePlayer player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) {
                player = Bukkit.getOfflinePlayer(uuid);
            }


            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + player.getName() + "'s Island");
            meta.setOwner(player.getName());
            List<String> lore = Lists.newArrayList();
            lore.add(ChatColor.translateAlternateColorCodes('&', String.format("&bLevel: &f%s", ent.getKey().getIslandLevel())));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&bMembers:"));
            lore.add(ChatColor.translateAlternateColorCodes('&', String.format("&7 \u00BB &f%s", Bukkit.getOfflinePlayer(ent.getKey().getOwner()).getName() + " (Owner)")));
            ent.getKey().getMembers().forEach(uuid1 -> lore.add(ChatColor.translateAlternateColorCodes('&', String.format("&7 \u00BB &f%s", Bukkit.getOfflinePlayer(uuid1).getName()))));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(getInventoryLocationForIndex(index), item);
        }
        return inventory;
    }


    private int getInventoryLocationForIndex(final int index) {
        switch (index) {
            case 1:
                return 4;
            case 2:
                return 12;
            case 3:
                return 14;
            case 4:
                return 20;
            case 5:
                return 24;
            case 6:
                return 28;
            case 7:
                return 29;
            case 8:
                return 31;
            case 9:
                return 33;
            case 10:
                return 34;
            default:
                return 0;
        }
    }

    @Override
    public void execute(Player player, String[] strings) {
        player.openInventory(createIslandTopInventory());
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase(ChatColor.WHITE + "Top Islands")) {
            event.setResult(Event.Result.DENY);
        }
    }
}
