package org.pirateislands.skyblock.listener;

import com.islesmc.islandapi.Island;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.configuration.ServerType;
import org.pirateislands.skyblock.goose.GooseLocationHelper;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!(SkyBlock.getPlugin().getServerConfig().getServerType() == ServerType.SKY))
            return;

        if (event.getPlayer().getLocation().getY() < -5) {
            if (event.getPlayer().getWorld().getUID().equals(SkyBlock.getPlugin().getIslandWorld().getUID())) {
                Island island = SkyBlock.getPlugin().getIslandHandler().getIslandForPlayer(event.getPlayer());
                if (island == null) {
                    event.getPlayer().teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
                    return;
                }
                Location location = GooseLocationHelper.toLocation(island.getSpawn());

                Block highest = location.getWorld().getHighestBlockAt(location);

                if (highest.getType().equals(Material.AIR)) {
                    location.getBlock().setType(Material.COBBLESTONE);
                }

                event.getPlayer().teleport(location.getWorld().getHighestBlockAt(location).getLocation());
                event.getPlayer().setFallDistance(0);
                return;
            }
            event.getPlayer().teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
        }
    }

    @EventHandler
    public void onPlayerMoveLocked(final PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            Island island = SkyBlock.getPlugin().getIslandHandler().getIslandAt(event.getTo());
            if (island == null)
                return;

            if (island.isAllowed(event.getPlayer().getUniqueId()))
                return;

            if (!island.getLocked())
                return;

            if (!island.isExpelled(event.getPlayer().getUniqueId()))
                return;

            event.setTo(event.getFrom());
            event.getPlayer().sendMessage(ChatColor.RED + "This island is currently locked.");
        }
    }
}
