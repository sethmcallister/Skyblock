package org.pirateislands.skyblock.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.pirateislands.skyblock.SkyBlock;

public class QuestsCommad extends BukkitCommand {
    public QuestsCommad() {
        super("quests");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player player = (Player)sender;
        Inventory inventory = SkyBlock.getInstance().getQuestHandler().createInventory(player);

        player.openInventory(inventory);
        return false;
    }
}
