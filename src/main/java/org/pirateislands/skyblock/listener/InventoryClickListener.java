package org.pirateislands.skyblock.listener;

import com.islesmc.islandapi.Island;
import com.islesmc.islandapi.IslandType;
import com.islesmc.modules.api.API;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.command.island.IslandCreateCommand;
import org.pirateislands.skyblock.handler.IslandHandler;
import org.pirateislands.skyblock.util.MessageUtil;

import java.io.File;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player pl = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        if (event.getClickedInventory().getTitle().equalsIgnoreCase("Island Selection")) {


            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();

            IslandType type = IslandType.valueOf(ChatColor.stripColor(item.getItemMeta().getDisplayName().replace(" ", "_")).toUpperCase());

            pl.closeInventory();

            new BukkitRunnable() {
                @Override
                public void run() {
                    IslandCreateCommand.ISLAND_MAKING.add(pl.getUniqueId());
                    SkyBlock.getPlugin().getIslandHandler().createIsland(pl, type);
                }
            }.runTaskAsynchronously(API.getPlugin());
        } else if (event.getClickedInventory().getTitle().equalsIgnoreCase("Owner Selection")) {

            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();

            IslandHandler registry = SkyBlock.getPlugin().getIslandHandler();

            Island playerIsland = registry.getIslandForPlayer(pl);

            String chosen = org.bukkit.ChatColor.stripColor(meta.getDisplayName());

            Player newOwner = Bukkit.getPlayer(chosen);

            if (newOwner == null || !newOwner.isOnline()) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(chosen);
                playerIsland.setOwner(player.getUniqueId());
                registry.playerIslands.get(registry.playerIslands.indexOf(playerIsland)).setOwner(player.getUniqueId());
                playerIsland.getMembers().remove(player.getUniqueId());
                pl.closeInventory();

                File file = new File(SkyBlock.getPlugin().getModuleDir().toString() + File.separator + "islands" + File.separator + pl.getUniqueId().toString().replace("-", "") + ".json");
                file.renameTo(new File(SkyBlock.getPlugin().getModuleDir().toString() + File.separator + "islands" + File.separator + player.getUniqueId().toString().replace("-", "") + ".json"));
                MessageUtil.sendServerTheme(pl, ChatColor.GREEN + String.format("You have promoted %s to your islands owner.", player.getName()));
            } else {
                playerIsland.setOwner(newOwner.getUniqueId());
                registry.playerIslands.get(registry.playerIslands.indexOf(playerIsland)).setOwner(newOwner.getUniqueId());
                playerIsland.getMembers().remove(newOwner.getUniqueId());
                pl.closeInventory();

                File file = new File(SkyBlock.getPlugin().getModuleDir().toString() + File.separator + "islands" + File.separator + pl.getUniqueId().toString().replace("-", "") + ".json");
                file.renameTo(new File(SkyBlock.getPlugin().getModuleDir().toString() + File.separator + "islands" + File.separator + newOwner.getUniqueId().toString().replace("-", "") + ".json"));
                MessageUtil.sendServerTheme(pl, ChatColor.GREEN + String.format("You have promoted %s to your islands owner.", newOwner.getName()));
                MessageUtil.sendServerTheme(newOwner, ChatColor.GREEN + String.format("You have been promoted to owner of %s's island", pl.getName()));
            }
            pl.teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
            pl.sendMessage(org.bukkit.ChatColor.GREEN + org.bukkit.ChatColor.BOLD.toString() + "[!] " + org.bukkit.ChatColor.GRAY + "Successfully left your island!");
        }
    }
}
