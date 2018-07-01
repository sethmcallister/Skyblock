package org.pirateislands.skyblock.command;

import com.islesmc.modules.api.API;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.util.MessageUtil;

public class SpawnCommand extends BukkitCommand {

    public SpawnCommand() {
        super("spawn");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player player = (Player) sender;

        MessageUtil.sendServerTheme(player, ChatColor.YELLOW + "You are being teleported to spawn in 3 seconds...");

        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
                player.sendMessage(ChatColor.YELLOW + "You have been teleported to spawn");
            }
        }.runTaskLater(API.getPlugin(), 3 * 20L);
        return true;
    }
}
