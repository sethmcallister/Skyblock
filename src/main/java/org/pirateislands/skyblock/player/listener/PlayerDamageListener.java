package org.pirateislands.skyblock.player.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.timers.DefaultTimer;
import org.pirateislands.skyblock.timers.Timer;
import org.pirateislands.skyblock.timers.TimerType;

import java.util.concurrent.TimeUnit;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        Player damaged = (Player) event.getEntity();

        if (damaged.isFlying()) {
            damaged.setFlying(false);
        }

        if (damaged.getAllowFlight()) {
            damaged.setAllowFlight(false);
        }


        Timer timer = SkyBlock.getPlugin().getTimerHandler().getTimer(damaged, TimerType.COMBAT_TAG);
        if (timer != null && timer.getTime() > 0)
            timer.setTime(TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis());
        else
            SkyBlock.getPlugin().getTimerHandler().addTimer(damaged, new DefaultTimer(TimerType.COMBAT_TAG, TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis(), damaged));

        if (!(event.getDamager() instanceof Player))
            return;

        Player damager = (Player) event.getDamager();

        if (damager.isFlying()) {
            damager.setFlying(false);
        }

        if (damager.getAllowFlight()) {
            damager.setAllowFlight(false);
        }

        Timer timer1 = SkyBlock.getPlugin().getTimerHandler().getTimer(damager, TimerType.COMBAT_TAG);
        if (timer1 != null && timer1.getTime() > 0)
            timer1.setTime(TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis());
        else
            SkyBlock.getPlugin().getTimerHandler().addTimer(damager, new DefaultTimer(TimerType.COMBAT_TAG, TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis(), damager));
    }
}
