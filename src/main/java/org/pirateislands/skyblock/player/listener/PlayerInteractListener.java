package org.pirateislands.skyblock.player.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.timers.DefaultTimer;
import org.pirateislands.skyblock.timers.Timer;
import org.pirateislands.skyblock.timers.TimerType;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onEnderpearlClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getPlayer().getItemInHand().getType().equals(Material.ENDER_PEARL)) {
            if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                Timer timer = SkyBlock.getPlugin().getTimerHandler().getTimer(player, TimerType.ENDERPEARL);
                if (timer != null && timer.getTime() > 0) {
                    long millisLeft = timer.getTime();
                    double value = millisLeft / 1000.0D;
                    double sec = Math.round(10.0D * value) / 10.0D;
                    event.setCancelled(true);
                    event.getPlayer().updateInventory();
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot use this for another &c&l" + sec + "&c seconds."));
                    return;
                }
                SkyBlock.getPlugin().getTimerHandler().addTimer(player, new DefaultTimer(TimerType.ENDERPEARL, 16000 + System.currentTimeMillis(), player));
            }
        }
    }
}
