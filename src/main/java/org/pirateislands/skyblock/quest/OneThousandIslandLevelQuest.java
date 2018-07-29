package org.pirateislands.skyblock.quest;

import com.google.common.collect.Lists;
import com.islesmc.islandapi.Island;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.dto.Quest;
import org.pirateislands.skyblock.util.MessageUtil;

public class OneThousandIslandLevelQuest extends Quest {
    public OneThousandIslandLevelQuest() {
        super("1,000 Level Island", Lists.newArrayList(ChatColor.WHITE + "Are you amongst the top islands? Do you have 1,000 island levels?", ChatColor.WHITE + "Complete this quest to earn $10,000, and 20 Diamond Blocks"));
    }

    @Override
    public void checkCompletion(Player player) {
        Island island = SkyBlock.getInstance().getIslandHandler().getIslandForPlayer(player);
        if (island == null) {
            player.sendMessage(ChatColor.RED + "You must be a member of an island to complete this quest.");
            return;
        }

        if (island.getIslandLevel() < 1000) {
            player.sendMessage(ChatColor.RED + "Your island must be of at-least level 1,000 to complete this quest");
            return;
        }

        completeQuest(player);
        SkyBlock.getInstance().getEconomy().depositPlayer(player, 10000);
        player.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK, 20));

        MessageUtil.sendServerTheme(player, ChatColor.YELLOW + String.format("You have completed the %s quest! You have been rewarded $10,000 and 20 Diamond Blocks.", getName()));
        player.sendMessage(ChatColor.YELLOW + "Make sure to checkout more quests by using " + ChatColor.WHITE + "/quests" + ChatColor.YELLOW + ".");
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onUnload() {

    }
}
