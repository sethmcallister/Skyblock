package org.pirateislands.skyblock.command;

import com.islesmc.islandapi.Island;
import com.islesmc.islandapi.Region;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.goose.GooseLocationHelper;

import java.util.UUID;

public class SetIslandSizeCommand extends BukkitCommand {
    public SetIslandSizeCommand() {
        super("setislandsize");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!sender.hasPermission("island.setsize")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /setislandsize <player> <size>");
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + String.format("No player with the name or UUID of '%s' could be found.", args[0]));
            return true;
        }
        if (!target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "No player with the name or UUID of '%s' is online.");
            return true;
        }
        Island island = SkyBlock.getPlugin().getIslandHandler().getIslandForPlayer((Player) target);
        if (island == null) {
            sender.sendMessage(ChatColor.RED + String.format("No island for the player '%s' could be found.", args[0]));
            return true;
        }

        String sizeSTR = args[1];
        if (!StringUtils.isNumeric(sizeSTR)) {
            sender.sendMessage(ChatColor.RED + String.format("The argument '%s' is not a number.", args[1]));
            return true;
        }
        Integer size = Integer.parseInt(sizeSTR);
        island.setSize(size);

        Location center = GooseLocationHelper.toLocation(island.getSpawn()).subtract(4, 8, 6);

        double minX = center.getBlockX() - size / 2D;
        double minY = 0D;
        double minZ = center.getBlockZ() - size / 2D;

        double maxX = center.getBlockX() + size / 2D;
        double maxY = 256D;
        double maxZ = center.getBlockZ() + size / 2D;


        Location min = new Location(SkyBlock.getPlugin().getIslandWorld(), minX, minY, minZ);
        Location max = new Location(SkyBlock.getPlugin().getIslandWorld(), maxX, maxY, maxZ);

//        SkyBlock.getPlugin().getRegionHandler().deleteRegion(island.getContainer());

//        Region container = SkyBlock.getPlugin().getRegionHandler().createRegion(island.getName(), min, max);
        Region container = new Region(island.getName(), GooseLocationHelper.fromLocation(min), GooseLocationHelper.fromLocation(max));
        island.setContainer(container);
        island.save();
        sender.sendMessage(ChatColor.YELLOW + String.format("You have set %s's island size to %s.", args[0], args[1]));
        for (UUID uuid : island.getMembers()) {
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.YELLOW + String.format("Your island size has been set to '%s'", args[1]));
        }
        return true;
    }
}
