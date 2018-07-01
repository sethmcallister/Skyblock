package org.pirateislands.skyblock.region;

import com.islesmc.modules.api.API;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class RegionSessionFactory {

    private List<RegionSession> activeSessions = new ArrayList<>();

    public void createSession(Player player) {
        if (hasActiveSession(player)) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] You're in an active region session!");
            return;
        }

        RegionSession session = new RegionSession(player);
        this.activeSessions.add(session);
        Bukkit.getServer().getPluginManager().registerEvents(session, (Plugin) API.getPlugin());

        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] You are now in an active region session.");
        player.sendMessage(ChatColor.GRAY + "Use the wand to select the minimum and maximum point of the region.");
    }

    public boolean hasActiveSession(Player player) {
        for (RegionSession session : activeSessions) {
            if (session.getPlayer().getName().equalsIgnoreCase(player.getName())) {
                return true;
            }
        }
        return false;
    }


}
