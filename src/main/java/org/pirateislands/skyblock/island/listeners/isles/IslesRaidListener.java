package org.pirateislands.skyblock.island.listeners.isles;

import com.islesmc.islandapi.Island;
import org.bukkit.ChatColor;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;
import org.pirateislands.skyblock.SkyBlock;


public class IslesRaidListener implements Listener {

    @EventHandler
    public void onVehicleMove(final VehicleMoveEvent event) {
        if (!event.getVehicle().getType().equals(EntityType.BOAT))
            return;

        Boat boat = (Boat) event.getVehicle();

        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockZ() == event.getTo().getBlockZ() && event.getFrom().getBlockY() == event.getTo().getBlockY())
            return;


        if (event.getVehicle().getPassenger() == null || !(event.getVehicle().getPassenger() instanceof Player))
            return;

        Player player = (Player) event.getVehicle().getPassenger();

        Island toIsland = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(event.getTo());
        if (toIsland == null)
            return;

        Island raidersIsland = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);
        if (!toIsland.getOwner().equals(raidersIsland.getOwner()) && raidersIsland.getIslandLevel() < 5) {
            player.sendMessage(ChatColor.RED + "You cannot raid any islands until your island level is at-least level 5.");
            Vector difference = event.getFrom().subtract(event.getTo()).toVector();
            event.getTo().subtract(difference);
            return;
        }

        if (toIsland.getIslandLevel() < 5) {
            if (!toIsland.getMembers().contains(player.getUniqueId()) || !toIsland.getOwner().equals(player.getUniqueId()))
                return;

            Vector difference = event.getFrom().subtract(event.getTo()).toVector();
            event.getTo().subtract(difference);
            player.sendMessage(ChatColor.RED + "You cannot raid this island until it's level 5; try come back later.");
            return;
        }
    }
}
