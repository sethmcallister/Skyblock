package org.pirateislands.skyblock.task;

import com.islesmc.islandapi.Island;
import com.islesmc.modules.api.API;
import com.islesmc.modules.api.framework.user.User;
import com.islesmc.modules.api.framework.user.profile.Profile;
import com.keenant.tabbed.item.TabItem;
import com.keenant.tabbed.item.TextTabItem;
import com.keenant.tabbed.tablist.SimpleTabList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.command.island.IslandTopCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TabUpdateTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            SimpleTabList tab = (SimpleTabList) SkyBlock.getPlugin().getTabbed().getTabList(player);

            int i = 0;
            for (TabItem tabItem : getTabItems(player)) {
                if (tab == null || tabItem == null)
                    continue;

                TextTabItem textTabItem = new TextTabItem(ChatColor.translateAlternateColorCodes('&', tabItem.getText()));
                textTabItem.setPing(0);
                tab.set(i, textTabItem);
                i++;
            }
        }
    }

    private List<TabItem> getTabItems(final Player player) {
        String primaryColor = SkyBlock.getPlugin().getServerConfig().getPrimaryColor();
        List<TabItem> items = new ArrayList<>();

        User user = API.getUserManager().findByUniqueId(player.getUniqueId());
        if (user == null)
            return items;

        Profile permissions = user.getProfile("permissions");

        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(primaryColor + "&lIsland"));
        Island island = SkyBlock.getPlugin().getIslandHandler().getIslandForPlayer(player);
        if (island == null) {
            items.add(new TextTabItem("&fNone"));
            items.add(new TextTabItem(" "));
            items.add(new TextTabItem(" "));
            items.add(new TextTabItem(" "));
            items.add(new TextTabItem(" "));
        } else {
            items.add(new TextTabItem(island.getName()));
            items.add(new TextTabItem("&7\u00BB" + primaryColor + " Level&7: &f" + island.getIslandLevel()));
            items.add(new TextTabItem("&7\u00BB" + primaryColor + " Members&7: &f" + island.getMembers().size() + "/" + island.getMaxPlayers()));
            items.add(new TextTabItem("&7\u00BB" + primaryColor + " Type&7: &f" + island.getType().getRaw()));
        }
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(primaryColor + "&lCommunity"));
        items.add(new TextTabItem("&fpirateislands.org"));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));

        items.add(new TextTabItem("&ePirate&fIslands"));
        items.add(new TextTabItem("&fOnline: " + Bukkit.getOnlinePlayers().size()));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(primaryColor + "Top Islands"));
        // 4/21
        int i = 0;
        int max = 10;
        if (SkyBlock.getPlugin().getIslandHandler().playerIslands.size() < 10) {
            max = SkyBlock.getPlugin().getIslandHandler().playerIslands.size();
        }
        for (Map.Entry<Island, Integer> entry : IslandTopCommand.getTopIslands().subList(0, max)) {
            i++;
            items.add(new TextTabItem("&f" + entry.getKey().getName() + " (" + entry.getKey().getIslandLevel() + ")"));
        }
        while (i <= 13) {
            i++;
            items.add(new TextTabItem(" "));
        }

        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(primaryColor + "&lStore"));
        items.add(new TextTabItem("&fshop.skyparadisemc.com"));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));

        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(primaryColor + "&lPlayer Info"));
        if (permissions == null) {
            items.add(new TextTabItem("&7Group: &f" + "Member"));
        } else {
            items.add(new TextTabItem("&7Group: &f" + permissions.getString("group")));
        }
        items.add(new TextTabItem("&7Balance: &f$" + SkyBlock.getPlugin().format(SkyBlock.getPlugin().getEconomy().getBalance(player))));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        items.add(new TextTabItem(" "));
        return items;
    }
}
