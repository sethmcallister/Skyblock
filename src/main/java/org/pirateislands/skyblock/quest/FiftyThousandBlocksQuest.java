package org.pirateislands.skyblock.quest;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.islesmc.modules.api.API;
import com.islesmc.modules.api.framework.user.User;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.dto.Quest;
import org.pirateislands.skyblock.util.MessageUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FiftyThousandBlocksQuest extends Quest {
    private final Gson gson;
    private final String fileName;
    private static Map<UUID, Integer> minedBlocks;

    public FiftyThousandBlocksQuest() {
        super("50,000 Mined Island Blocks", Lists.newArrayList("Have you been grinding?", "Mine 50,000 blocks on your island to complete this quest", "You will be rewarded 100 Mob Coins, 20x Diamond Blocks, and $10,000"));
        minedBlocks = new ConcurrentHashMap<>();
        this.fileName = SkyBlock.getInstance().getModuleDir() + File.separator + "quests" + File.separator + "fifty.json";
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public static void incrementPlayerBlocks(final Player player) {
        Integer amount = minedBlocks.computeIfAbsent(player.getUniqueId(), k -> 0);
        minedBlocks.put(player.getUniqueId(), amount + 1);
    }

    @Override
    public void checkCompletion(Player player) {
        Integer amount = minedBlocks.computeIfAbsent(player.getUniqueId(), k -> 0);
        if (amount < 50000) {
            player.sendMessage(ChatColor.RED + "You need to mine another " + (50000 - amount) + " blocks on your island to complete this quest.");
            return;
        }
        completeQuest(player);
        SkyBlock.getInstance().getEconomy().depositPlayer(player, 10000);
        User user = API.getUserManager().findByUniqueId(player.getUniqueId());
        int mobcoins = user.getProfile("mobcoins").getDouble("coins").intValue();

        user.getProfile("mobcoins").set("coins", (double) mobcoins + 100);
        player.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK, 20));

        MessageUtil.sendServerTheme(player, ChatColor.YELLOW + String.format("You have completed the %s quest! You have been rewarded $10,000 and 100 Mob Coins.", getName()));
        player.sendMessage(ChatColor.YELLOW + "Make sure to checkout more quests by using " + ChatColor.WHITE + "/quests" + ChatColor.YELLOW + ".");
    }

    @Override
    public void onLoad() {
        File file = new File(this.fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JsonParser parser = new JsonParser();

            try (FileReader fileReader = new FileReader(this.fileName)) {
                JsonElement element = parser.parse(fileReader);
                Type typeToken = new TypeToken<HashMap<UUID, Integer>>() {}.getType();
                Map<UUID, Integer> map = this.gson.fromJson(element, typeToken);
                if (map == null) {
                    onLoad();
                    return;
                }
                minedBlocks = map;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUnload() {
        String json = this.gson.toJson(minedBlocks);
        File file = new File(this.fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
