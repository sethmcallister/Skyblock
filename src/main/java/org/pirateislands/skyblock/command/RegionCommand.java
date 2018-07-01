package org.pirateislands.skyblock.command;


import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

public class RegionCommand extends BukkitCommand {
    public RegionCommand() {
        super("region");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return false;
    }
}
