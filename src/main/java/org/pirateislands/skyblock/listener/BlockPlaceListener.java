package org.pirateislands.skyblock.listener;

import com.islesmc.islandapi.Island;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.util.MessageUtil;

import java.util.Objects;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onHopperBlockPlace(final BlockPlaceEvent event) {
        Player placer = event.getPlayer();
        if (event.isCancelled() || !event.canBuild())
            return;

        Location location = event.getBlockPlaced().getLocation();

        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld())
            return;

        Island island = SkyBlock.getPlugin().getIslandHandler().getIslandAt(location);

        if (island == null) {
            return;
        }

        if (placer.getItemInHand().getType().equals(Material.HOPPER)) {
            if (Objects.equals(island.getHopperAmount(), island.getMaxHoppers())) {
                event.setCancelled(true);
                MessageUtil.sendServerTheme(placer, ChatColor.YELLOW + "You have already placed (%s/%s) hoppers. To place more purchase an upgrade at https://store.pirateislands.org");
                return;
            }
            island.setHopperAmount(island.getHopperAmount() + 1);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player placer = event.getPlayer();
        if (placer.hasPermission("skyblock.bypass")) {
            return;
        }
        Location location = event.getBlockPlaced().getLocation();
        if (!location.getWorld().getUID().equals(SkyBlock.getPlugin().getIslandWorld().getUID())) {
            return;
        }

        Island conflict = SkyBlock.getPlugin().getIslandHandler().getIslandAt(location);
        if (conflict == null) {
            placer.sendMessage(ChatColor.GREEN + "You cannot place outside of your island.");
            MessageUtil.sendServerTheme(placer, ChatColor.YELLOW + "You can purchase a larger island at https://store.pirateislands.org");

            event.setCancelled(true);
            return;
        }
        if (conflict.isAllowed(placer.getUniqueId()))
            return;

        placer.sendMessage(ChatColor.RED + "You can only place blocks on your own island");
        event.setCancelled(true);
    }
}
