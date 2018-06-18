package org.pirateislands.skyblock.goose;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class GooseCommand {

    private final AtomicReference<String> subCommand;
    private final List<String> aliases;
    private final AtomicBoolean playerOnly;

    public GooseCommand(String subCommand, List<String> aliases, Boolean playerOnly) {
        this.subCommand = new AtomicReference<>(subCommand);
        this.aliases = aliases;
        this.playerOnly = new AtomicBoolean(playerOnly);

    }

    public abstract void execute(Player sender, String[] args);

    public AtomicReference<String> getSubCommand() {
        return subCommand;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public AtomicBoolean getPlayerOnly() {
        return playerOnly;
    }
}
