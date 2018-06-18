package org.pirateislands.skyblock.goose;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Owned by SethyCorp, and KueMedia respectively.
 **/
public class GooseCommandHandler extends BukkitCommand {
    private final Map<String, GooseCommand> commands;
    private final GooseCommand defaultCommand;

    public GooseCommandHandler(final String command, final GooseCommand defaultCommand) {
        super(command);
        this.commands = new ConcurrentHashMap<>();
        this.defaultCommand = defaultCommand;
    }

    public void addSubCommand(final String command, final GooseCommand subCommand) {
        this.commands.put(command, subCommand);
    }

    private GooseCommand getSubCommand(final String subCommand) {
        for (Map.Entry<String, GooseCommand> subCommandEntry : commands.entrySet()) {
            if (subCommandEntry.getValue().getSubCommand().get().toLowerCase().equals(subCommand.toLowerCase())) {
                for (String alias : subCommandEntry.getValue().getAliases())
                    if (alias.toLowerCase().equals(subCommand.toLowerCase()))
                        return subCommandEntry.getValue();
                return subCommandEntry.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        Player sender = (Player) commandSender;
        if (args.length == 0) {
            defaultCommand.execute(sender, args);
            return true;
        }
        final GooseCommand subCommand = getSubCommand(args[0]);
        if (subCommand == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo sub-command with the name '" + args[0] + "' was found."));
            return true;
        }
        List<String> subArgs = new LinkedList<>();
        for (String args1 : args) {
            if (args1.equals(args[0])) {
                continue;
            }

            subArgs.add(args1);
        }
        subCommand.execute(sender, subArgs.toArray(new String[subArgs.size()]));
        return true;
    }
}
