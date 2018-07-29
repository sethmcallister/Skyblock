package org.pirateislands.skyblock.quest;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.dto.Quest;
import org.pirateislands.skyblock.util.MessageUtil;

public class OneMillionQuest extends Quest {
    public OneMillionQuest() {
        super("1 Million", Lists.newArrayList(ChatColor.WHITE + "Are you a real baller? Do you have $1,000,000?", ChatColor.WHITE + "Complete this quest to earn another $250,000 and 2 Pirate Keys"));
    }

    @Override
    public void checkCompletion(final Player player) {
        double balance = SkyBlock.getInstance().getEconomy().getBalance(player);
        if (balance < 1000000) {
            player.sendMessage(ChatColor.RED + "You must have at-least $1,000,000 in your balance to complete this quest.");
            return;
        }

        completeQuest(player);
        SkyBlock.getInstance().getEconomy().depositPlayer(player, 250000);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("crate key %s Pirate 3", player.getName()));

        MessageUtil.sendServerTheme(player, ChatColor.YELLOW + String.format("You have completed the %s quest! You have been rewarded $250,000, and 2 Pirate Keys.", getName()));
        player.sendMessage(ChatColor.YELLOW + "Make sure to checkout more quests by using " + ChatColor.WHITE + "/quests" + ChatColor.YELLOW + ".");
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onUnload() {

    }
}
