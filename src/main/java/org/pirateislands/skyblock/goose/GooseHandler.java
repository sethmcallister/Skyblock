package org.pirateislands.skyblock.goose;

import com.islesmc.modules.api.API;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.pirateislands.skyblock.SkyBlock;

import java.util.concurrent.ConcurrentHashMap;

public class GooseHandler implements Listener {
    private final ConcurrentHashMap<Player, GooseScoreboard> scoreboards;

    public GooseHandler() {
        this.scoreboards = new ConcurrentHashMap<>();
    }

    public GooseScoreboard getScoreboard(Player player) {
        return scoreboards.get(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        event.getPlayer().setScoreboard(scoreboard);

        new BukkitRunnable() {
            @Override
            public void run() {
                GooseScoreboard gooseScoreboard = new GooseScoreboard(scoreboard, SkyBlock.getPlugin().getServerConfig().getScoreboardName());
                scoreboards.put(event.getPlayer(), gooseScoreboard);
            }
        }.runTaskAsynchronously(API.getPlugin());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        scoreboards.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        scoreboards.remove(event.getPlayer());
    }
}
