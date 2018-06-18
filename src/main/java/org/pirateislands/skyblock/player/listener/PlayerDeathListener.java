package org.pirateislands.skyblock.player.listener;

import com.islesmc.modules.api.API;
import com.islesmc.modules.api.framework.user.User;
import com.islesmc.modules.api.framework.user.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.timers.Timer;
import org.pirateislands.skyblock.timers.TimerType;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.setDroppedExp(0);
        event.getDrops().clear();

        Player killed = event.getEntity();

        User killedUser = API.getUserManager().findByUniqueId(killed.getUniqueId());

        if (killedUser == null)
            return;

        Profile killedProfile = killedUser.getProfile("Skyblock");

        Double deaths = killedProfile.getDouble("deaths");
        deaths++;
        killedProfile.set("deaths", deaths);

        Timer timer = SkyBlock.getPlugin().getTimerHandler().getTimer(killed, TimerType.COMBAT_TAG);
        if (timer != null && timer.getTime() > 0) {
            timer.setTime(0L);
            SkyBlock.getPlugin().getTimerHandler().getPlayerTimers(killed).remove(timer);
        }

        killedProfile.set("killstreak", 0D);

        Player killer = killed.getKiller();
        if (killer == null) {
            Bukkit.getLogger().info(killed.getName() + " has died!");
            return;
        }

        Bukkit.getLogger().info(killed.getName() + " was killed by " + killer.getName());

        User killerUser = API.getUserManager().findByUniqueId(killer.getUniqueId());
        Profile killerProfile = killerUser.getProfile("Skyblock");

        Double killstreak = killerProfile.getDouble("killstreak");
        if (killstreak == null)
            killstreak = 0D;
        killstreak++;
        killerProfile.set("killstreak", killstreak);

        if (killstreak == 3) {
            killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 3));
            killer.sendMessage(ChatColor.YELLOW + "You have been given " + ChatColor.GREEN + "3x Golden Apple" + ChatColor.YELLOW + " for your killstreak.");
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a" + killer.getName() + "&e has been given &a3x Golden Apples&e for their&a 3 killstreak&e."));
        } else if (killstreak == 5) {
            PotionEffect effect = new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 2);
            killer.addPotionEffect(effect);
            killer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have been given &a5 seconds of Regeneration 3&e for your killstreak."));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a" + killer.getName() + "&e has been given &a5 seconds of Regeneration 3&e for their&a 5 killstreak&e."));
        }

        Double kills = killerProfile.getDouble("kills");
        kills++;
        killerProfile.set("kills", kills);

    }
}
