package org.pirateislands.skyblock.command;


import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

/**
 * Created by Matt on 2017-02-11.
 */
public class RegionCommand extends BukkitCommand {
    public RegionCommand() {
        super("region");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return false;
    }
}
