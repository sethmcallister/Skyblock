package org.pirateislands.skyblock.quest;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.dto.Quest;
import org.pirateislands.skyblock.dto.Streak;
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
import java.util.concurrent.TimeUnit;

public class FullWeekQuest extends Quest {
    private final Gson gson;
    private final String fileName;
    private static Map<UUID, Streak> playerJoinStreaks;

    public FullWeekQuest() {
        super("7 day join streak", Lists.newArrayList("Are you an active player?", "Join every day for a week to get $1,000,000"));
        playerJoinStreaks = new ConcurrentHashMap<>();
        this.fileName = SkyBlock.getInstance().getModuleDir() + File.separator + "quests" + File.separator + "join-streaks.json";
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public static void incrementPlayerStreak(final Player player) {
        Streak streak = playerJoinStreaks.computeIfAbsent(player.getUniqueId(), k -> new Streak(0, System.currentTimeMillis() + TimeUnit.DAYS.toSeconds(1)));
        if (streak.getCallDown() < System.currentTimeMillis()) {
            streak.setDays(0);
            streak.setCallDown(System.currentTimeMillis() + TimeUnit.DAYS.toSeconds(1));
            player.sendMessage(ChatColor.RED + "You just lost your join streak.");
            return;
        }

        streak.setDays(streak.getDays() + 1);
        streak.setCallDown(System.currentTimeMillis() + TimeUnit.DAYS.toSeconds(1));
        player.sendMessage(ChatColor.YELLOW + "You currently have a join streak of " + streak.getDays() + ".");
        player.sendMessage(ChatColor.YELLOW + "Remember to join every day for a week to win $1,000,000");
    }

    @Override
    public void checkCompletion(Player player) {
        Streak streak = playerJoinStreaks.computeIfAbsent(player.getUniqueId(), k -> new Streak(0, System.currentTimeMillis() + TimeUnit.DAYS.toSeconds(1)));
        if (streak.getDays() < 7) {
            player.sendMessage(ChatColor.RED + "To complete this quest you must have joined the server everyday for at-least one week.");
            player.sendMessage(ChatColor.RED + "You need to join everyday for another " + (7 - streak.getDays()) + " days in a row");
            return;
        }
        SkyBlock.getInstance().getEconomy().depositPlayer(player, 10000);
        completeQuest(player);
        MessageUtil.sendServerTheme(player, ChatColor.YELLOW + String.format("You have completed the %s quest! You have been rewarded $1,000,000", getName()));
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
                Type typeToken = new TypeToken<HashMap<UUID, Streak>>() {}.getType();
                Map<UUID, Streak> map = this.gson.fromJson(element, typeToken);
                if (map == null) {
                    onLoad();
                    return;
                }
                playerJoinStreaks = map;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUnload() {
        String json = this.gson.toJson(playerJoinStreaks);
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

