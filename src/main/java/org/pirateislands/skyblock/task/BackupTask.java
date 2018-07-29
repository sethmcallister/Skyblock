package org.pirateislands.skyblock.task;

import com.islesmc.islandapi.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.pirateislands.skyblock.SkyBlock;

public class BackupTask extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.broadcastMessage(ChatColor.RED + "[Backup] We're backing up the server, we apologize for any lag.");
        SkyBlock.getInstance().getIslandWorld().save();
        SkyBlock.getInstance().getIslandHandler().getPlayerIslands().forEach(Island::save);
        SkyBlock.getInstance().getServerConfig().save();
        SkyBlock.getInstance().getQuestHandler().save();
        Bukkit.broadcastMessage(ChatColor.RED + "[Backup] We have successfully saved all of our data.");
    }
}
