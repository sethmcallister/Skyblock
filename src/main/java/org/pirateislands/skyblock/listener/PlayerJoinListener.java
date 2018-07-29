package org.pirateislands.skyblock.listener;

import com.islesmc.modules.api.API;
import com.islesmc.modules.api.framework.user.User;
import com.islesmc.modules.api.framework.user.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.pirateislands.skyblock.SkyBlock;

import java.util.ArrayList;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        System.out.println("20 player join event");

        event.setJoinMessage(null);
        Player player = event.getPlayer();
        User user = API.getUserManager().findByUniqueId(player.getUniqueId());
        Profile profile = user.getProfile("skyblock");
        if (profile == null) {
            profile = new Profile("skyblock");
            profile.set("achievements", new ArrayList<>());
            profile.set("kills", 0.0D);
            profile.set("deaths", 0.0D);
            profile.set("killstreak", 0.0D);
            profile.set("completedQuests", new ArrayList<>());
            user.getAllProfiles().add(profile);
            user.update();
        }

        System.out.println("34 player join event");

        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
        player.sendMessage(" ");
        player.sendMessage(ChatColor.YELLOW + " Welcome " + ChatColor.WHITE + player.getName() + ChatColor.YELLOW + " to " + "Pirate" + ChatColor.WHITE + "Islands");
        player.sendMessage(" ");
        player.sendMessage(ChatColor.YELLOW + " Website:" + ChatColor.WHITE + " https://pirateislands.org");
        player.sendMessage(ChatColor.YELLOW + " Discord:" + ChatColor.WHITE + " https://discord.pirateislands.org");
        player.sendMessage(ChatColor.YELLOW + " Shop:" + ChatColor.WHITE + " https://store.pirateislands.org");
        player.sendMessage(" ");
        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");

        if (!player.hasPlayedBefore()) {
            SkyBlock.getInstance().getServerConfig().incrementPlayersJoined();
            int joinNumber = SkyBlock.getInstance().getServerConfig().getPlayersJoined();
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Pirate" + ChatColor.WHITE + "Islands " + ChatColor.GRAY + "\u00BB " + ChatColor.YELLOW + String.format("Welcome %s to the ", player.getName()) + ChatColor.YELLOW + "Pirate" + ChatColor.WHITE + "Islands" + ChatColor.YELLOW + String.format(" (%s)", joinNumber));
            player.teleport(SkyBlock.getInstance().getServerConfig().getSpawnLocation());

            player.sendMessage(ChatColor.YELLOW + "Make sure to redeem the starter kit with /kit starter! Has a 60minute cooldown.");
        }

    }
}
