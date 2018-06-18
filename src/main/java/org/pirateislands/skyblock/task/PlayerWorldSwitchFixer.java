package org.pirateislands.skyblock.task;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerWorldSwitchFixer extends BukkitRunnable {
    private final Map<UUID, UUID> playerWorldMap;
    private final Map<UUID, GameMode> playerGamemodeMap;

    public PlayerWorldSwitchFixer() {
        this.playerWorldMap = new HashMap<>();
        this.playerGamemodeMap = new HashMap<>();
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            World world = player.getWorld();

            UUID cached = playerWorldMap.get(player.getUniqueId());
            GameMode gameMode = playerGamemodeMap.get(player.getUniqueId());
            if (cached == null || gameMode == null) {
                playerWorldMap.put(player.getUniqueId(), world.getUID());
                playerGamemodeMap.put(player.getUniqueId(), player.getGameMode());
                continue;
            }

            if (cached.equals(world.getUID()))
                continue;

            if (player.getGameMode().equals(gameMode))
                continue;

            player.setGameMode(gameMode);
        }
    }
}
