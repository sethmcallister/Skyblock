package org.pirateislands.skyblock.handler;

import com.islesmc.modules.api.API;
import com.islesmc.modules.api.framework.user.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pirateislands.skyblock.dto.Quest;
import org.pirateislands.skyblock.quest.*;

import java.util.ArrayList;
import java.util.List;

public class QuestHandler {
    private final String inventoryName;
    private final List<Quest> quests;

    public QuestHandler() {
        this.inventoryName = ChatColor.YELLOW + "Quests";
        this.quests = new ArrayList<>();
        this.quests.add(new OneMillionQuest());
        this.quests.add(new OneThousandIslandLevelQuest());
        this.quests.add(new TenThousandMobCoins());
        this.quests.add(new FullWeekQuest());
        this.quests.add(new FiftyThousandBlocksQuest());

        for (Quest quest : this.quests) {
            Bukkit.getLogger().info(String.format("Quest %s loading", quest.getName()));
            quest.onLoad();
            Bukkit.getLogger().info(String.format("Quest %s loaded", quest.getName()));
        }
    }

    public void save() {
        for (Quest quest : this.quests) {
            Bukkit.getLogger().info(String.format("Quest %s saving", quest.getName()));
            quest.onUnload();
            Bukkit.getLogger().info(String.format("Quest %s saved", quest.getName()));
        }
    }

    public void disable() {
        for (Quest quest : this.quests) {
            Bukkit.getLogger().info(String.format("Quest %s unloading", quest.getName()));
            quest.onUnload();
            Bukkit.getLogger().info(String.format("Quest %s unloaded", quest.getName()));
        }
    }

    public Quest findByName(final String name) {
        return this.quests.stream().filter(quest -> quest.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    private List<Quest> findAll() {
        return this.quests;
    }

    public Inventory createInventory(final Player player) {
        Inventory inventory = Bukkit.createInventory(null, getInventorySize(this.quests.size()) * 3, this.inventoryName);
        for (int i = 0; i < 9; i++)
            inventory.setItem(i, getGrayGlass());

        for (Quest quest : quests) {
            inventory.addItem(createQuestItemStack(quest, hasCompletedQuest(player, quest)));
        }

        for (int i = (inventory.getSize() - 9); i < inventory.getSize(); i++)
            inventory.setItem(i, getGrayGlass());
        return inventory;
    }

    public Quest findQuestByItemStack(final ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemMeta() == null || itemStack.getItemMeta().getDisplayName() == null)
            return null;

        String questName = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName().replace(" Quest", ""));
        return findByName(questName);
    }

    public ItemStack createQuestItemStack(final Quest quest, boolean completed) {
        if (completed) {
            ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)14);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + quest.getName() + " Quest");
            List<String> lore = new ArrayList<>();
            lore.addAll(quest.getDescription());
            lore.add(" ");
            lore.add(ChatColor.GREEN + "Completed");
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            return itemStack;
        }
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)13);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + quest.getName() + " Quest");
        List<String> lore = new ArrayList<>();
        lore.addAll(quest.getDescription());
        lore.add(" ");
        lore.add(ChatColor.RED + "Click to complete");
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack getGrayGlass() {
        return new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
    }

    public boolean hasCompletedQuest(final Player player, final Quest quest) {
        Profile profile = API.getUserManager().findByUniqueId(player.getUniqueId()).getProfile("skyblock");
        List<String> completedQuests = (ArrayList<String>) profile.getObject("completedQuests");
        if (completedQuests == null) {
            completedQuests = new ArrayList<>();
            profile.set("completedQuests", completedQuests);
        }
        return completedQuests.contains(quest.getName());
    }

    public String getInventoryName() {
        return inventoryName;
    }

    private int getInventorySize(int max) {
        if (max < 1)
            return 9;
        if (max > 54)
            return 54;
        max += 8;
        return max - (max % 9);
    }
}
