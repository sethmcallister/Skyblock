package org.pirateislands.skyblock.quest;

import com.google.common.collect.Lists;
import com.islesmc.modules.api.API;
import com.islesmc.modules.api.framework.user.User;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.dto.Quest;
import org.pirateislands.skyblock.util.MessageUtil;

public class TenThousandMobCoins extends Quest {
    public TenThousandMobCoins() {
        super("10,000 Mob Coins", Lists.newArrayList(ChatColor.WHITE + "You have been grinding those mobs? Have you earn't yourself 10,000 mob coins?", ChatColor.WHITE + "Complete this quest to earn $10,000, and 100 Mob Coins."));
    }

    @Override
    public void checkCompletion(Player player) {

        User user = API.getUserManager().findByUniqueId(player.getUniqueId());
        int mobcoins = user.getProfile("mobcoins").getDouble("coins").intValue();

        if (mobcoins < 10000) {
            player.sendMessage(ChatColor.RED + "Your must have at-least 10,000 mob coins to complete this quest");
            return;
        }

        completeQuest(player);
        SkyBlock.getInstance().getEconomy().depositPlayer(player, 10000);
        user.getProfile("mobcoins").set("coins", (double) mobcoins + 100);

        MessageUtil.sendServerTheme(player, ChatColor.YELLOW + String.format("You have completed the %s quest! You have been rewarded $10,000 and 100 Mob Coins.", getName()));
        player.sendMessage(ChatColor.YELLOW + "Make sure to checkout more quests by using " + ChatColor.WHITE + "/quests" + ChatColor.YELLOW + ".");
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onUnload() {

    }
}
