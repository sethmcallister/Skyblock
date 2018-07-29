package org.pirateislands.skyblock.listener;

import com.islesmc.islandapi.Island;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.configuration.ServerType;
import org.pirateislands.skyblock.util.MessageUtil;

public class EntityDamageByEntityListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player && event.getDamager() instanceof Player))
            return;

        Player damaged = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if (damaged.getWorld().getName().equalsIgnoreCase(SkyBlock.getInstance().getIslandWorld().getName()) && !SkyBlock.getInstance().getServerConfig().getServerType().equals(ServerType.ISLES)) {
            event.setCancelled(true);
            event.setDamage(0);
        } else if (damaged.getWorld().getName().equalsIgnoreCase("Spawn")) {
            MessageUtil.sendServerTheme(damager, "You cannot damage other players in spawn.");
            event.setCancelled(true);
            event.setDamage(0);
        }
    }

    @EventHandler
    public void onEntityDamageIsland(final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;

        Player damager = (Player) event.getDamager();

        Island island = SkyBlock.getInstance().getIslandHandler().getIslandAt(event.getEntity().getLocation());
        if (island == null)
            return;

        if (island.isAllowed(damager.getUniqueId()))
            return;

        event.setDamage(0D);
        event.setCancelled(true);
        damager.sendMessage(ChatColor.RED + "You cannot attack entities on this island.");
    }


    @EventHandler
    public void onIslandMemberDamage(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!(event.getDamager() instanceof Player))
            return;

        Player damager = (Player) event.getDamager();
        if (damager == null)
            return;

        Player damaged = (Player) event.getEntity();

        Island island = SkyBlock.getInstance().getIslandHandler().getIslandForPlayer(damaged);

        if (island == null)
            return;

        if (!island.isAllowed(damager.getUniqueId()))
            return;

        event.setDamage(0);
        event.setCancelled(true);
        damager.sendMessage(ChatColor.RED + String.format("Warning: %s is in your island.", damaged.getName()));
    }
}
