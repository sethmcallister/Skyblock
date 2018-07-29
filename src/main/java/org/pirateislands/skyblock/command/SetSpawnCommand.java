package org.pirateislands.skyblock.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseLocationHelper;

public class SetSpawnCommand extends BukkitCommand {

    public SetSpawnCommand() {
        super("setspawn");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("islesmc.setspawn")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return true;
        }

        Location loc = player.getLocation();

        SkyBlock.getInstance().getServerConfig().setSpawnLocation(GooseLocationHelper.fromLocation(loc));
        SkyBlock.getInstance().getServerConfig().save();
        player.sendMessage(ChatColor.YELLOW + "You have successfully set the server's spawn.");
        return true;
    }
}
