package org.pirateislands.skyblock.command.island;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseCommand;
import com.islesmc.islandapi.Island;
import org.pirateislands.skyblock.misc.MessageUtil;

import java.util.UUID;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandLeaveCommand extends GooseCommand {
    public IslandLeaveCommand() {
        super("leave", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length > 0) {
            player.sendMessage(ChatColor.RED + "Usage: /island kick");
            return;
        }

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);
        if (island == null) {
            player.sendMessage(ChatColor.RED + "You do not currently have an island.");
            return;
        }

        if (island.getOwner().equals(player.getUniqueId())) {
            if (!island.getMembers().isEmpty()) {
                MessageUtil.sendGood(player, "Opening owner selection...");
                openNewOwnerSelector(player, island);
                return;
            } else {
                SkyBlock.getPlugin().getIslandRegistry().deleteIsland(player, island);
//                Bukkit.broadcastMessage(ChatColor.GRAY + ChatColor.BOLD.toString() + island.getName() + " Status: " + ChatColor.RED + ChatColor.BOLD.toString() + "[FALLEN]");
            }
        } else {

            if (island.getMembers().contains(player.getUniqueId())) {
                island.getMembers().remove(player.getUniqueId());
            }
        }
        player.teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
        MessageUtil.sendServerTheme(player, ChatColor.YELLOW + "You have successfully left your island.");
        return;
    }

    private void openNewOwnerSelector(Player player, Island island) {
        Inventory inventory = Bukkit.createInventory(null, 9, "Owner Selection");
        for (UUID uuid : island.getMembers()) {
            Player pl = Bukkit.getPlayer(uuid);
            if (pl == null || !pl.isOnline()) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta meta = (SkullMeta) is.getItemMeta();
                meta.setOwner(offlinePlayer.getName());
                meta.setDisplayName(ChatColor.YELLOW + offlinePlayer.getName());
                is.setItemMeta(meta);
                inventory.addItem(is);
            } else {
                ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta meta = (SkullMeta) is.getItemMeta();
                meta.setOwner(player.getName());
                meta.setDisplayName(ChatColor.YELLOW + pl.getName());
                is.setItemMeta(meta);
                inventory.addItem(is);
            }
        }
        player.openInventory(inventory);
    }
}
