package org.pirateislands.skyblock.listener;

import com.islesmc.islandapi.Island;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.pirateislands.skyblock.SkyBlock;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player placer = event.getPlayer();

        Location location = event.getBlock().getLocation();

        if (!location.getWorld().getUID().equals(SkyBlock.getPlugin().getIslandWorld().getUID()))
            return;

        if (placer.hasPermission("skyblock.bypass"))
            return;

        Island conflict = SkyBlock.getPlugin().getIslandHandler().getIslandAt(location);
        if (conflict == null)
            return;


        if (conflict.isAllowed(placer.getUniqueId())) {
            if (event.getBlock().getType().equals(Material.SIGN) || event.getBlock().getType().equals(Material.SIGN_POST) || event.getBlock().getType().equals(Material.WALL_SIGN)) {
                Sign sign = (Sign) event.getBlock().getState();

                if (sign.getLine(0).equalsIgnoreCase("[welcome]"))
                    conflict.setWarpLocation(null);

            }
            return;
        }

        placer.sendMessage(ChatColor.RED + "You do not have permission to build here!");
        event.setCancelled(true);
    }
}
