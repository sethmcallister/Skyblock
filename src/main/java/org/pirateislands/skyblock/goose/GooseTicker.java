package org.pirateislands.skyblock.goose;

import com.islesmc.islandapi.Island;
import com.islesmc.modules.api.API;
import com.islesmc.modules.api.framework.user.User;
import com.islesmc.modules.api.framework.user.profile.Profile;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.timers.Timer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GooseTicker extends BukkitRunnable {
    private static final DecimalFormat FORMAT = new DecimalFormat("0.0");
    private final String primaryColor;

    public GooseTicker() {
        this.primaryColor = SkyBlock.getPlugin().getServerConfig().getPrimaryColor();
    }

    public synchronized static String formatTime(long time) {
        if (time > 60000L)
            return setLongFormat(time);
        else
            return format(time);
    }

    private synchronized static String format(long millisecond) {
        return FORMAT.format(millisecond / 1000.0D);
    }

    private synchronized static String setLongFormat(long paramMilliseconds) {
        if (paramMilliseconds < TimeUnit.MINUTES.toMillis(1L))
            return FORMAT.format(paramMilliseconds);
        return DurationFormatUtils.formatDuration(paramMilliseconds,
                (paramMilliseconds >= TimeUnit.HOURS.toMillis(1L) ? "HH:" : "") +
                        "mm:ss");
    }

    private synchronized List<String> splitEqually(final String text, final int size) {
        List<String> ret = new ArrayList<>();

        for (int start = 0; start < text.length(); start += size)
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        return ret;
    }

    private synchronized String translateString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @Override
    public synchronized void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            GooseScoreboard scoreboard = SkyBlock.getPlugin().getGooseHandler().getScoreboard(player);
            if (scoreboard == null)
                continue;

            if (SkyBlock.getPlugin().getEconomy() == null)
                continue;

            scoreboard.clear();


            User user = API.getUserManager().findByUniqueId(player.getUniqueId());
            Profile profile = user.getProfile("Skyblock");
            scoreboard.add(translateString("&7&m-----------"), translateString("&7&m-----------"));

            if (player.getLocation().getWorld().getName().equalsIgnoreCase("pvp")) {
                double balance = SkyBlock.getPlugin().getEconomy().getBalance(player);
                scoreboard.add(translateString(primaryColor + "Balance&7: "), translateString("&f$" + SkyBlock.getPlugin().format(balance)));
                scoreboard.add(translateString(primaryColor + "Kills&7: "), translateString("&f" + profile.getDouble("kills").intValue()));
                scoreboard.add(translateString(primaryColor + "Deaths&7: "), translateString("&f" + profile.getDouble("deaths").intValue()));
                if (profile.getDouble("killstreak") != null && profile.getDouble("killstreak") > 0D)
                    scoreboard.add(translateString(primaryColor + "KillStreak"), translateString("&7: &f") + profile.getDouble("killstreak").intValue());

                if (hasAnyTimers(player)) {
                    scoreboard.add("", "");
                    List<Timer> defaultTimers = SkyBlock.getPlugin().getTimerHandler().getPlayerTimers(player);
                    for (Timer timer : defaultTimers) {
                        if (timer.getTime() > 0) {
                            String left = translateString(timer.getTimerType().getScore());
                            String right = translateString("&7:&f ") + formatTime(timer.getTime());
                            scoreboard.add(left, right);
                        }
                    }
                }

            } else {
                double balance = SkyBlock.getPlugin().getEconomy().getBalance(player);
                scoreboard.add(translateString(primaryColor + "Balance&7: "), translateString("&f$" + SkyBlock.getPlugin().format(balance)));
                scoreboard.add(translateString(primaryColor + "Mob Coins&7: "), translateString("&f" + user.getProfile("mobcoins").getDouble("coins").intValue()));
                scoreboard.add(" "," ");
                Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);
                if (island == null) {
                    scoreboard.add(translateString(primaryColor + "Island&7: "), translateString("&fNone"));
                } else {
                    String name = splitEqually(island.getName(), 13).get(0);
                    scoreboard.add(translateString(primaryColor + "Island&7: "), translateString("&f " + name));
                    scoreboard.add(translateString("&7\u00BB" + primaryColor + " Level&7:"), translateString("&f " + island.getIslandLevel()));
                    scoreboard.add(translateString("&7\u00BB" + primaryColor + " Members&7:"), translateString("&f " + island.getMembers().size() + " / " + island.getMaxPlayers()));
                    scoreboard.add(translateString("&7\u00BB" + primaryColor + " Type&7:"), translateString("&f " + island.getType().getRaw()));
                    scoreboard.add(translateString("&7\u00BB" + primaryColor + " Size&7:"), translateString("&f " + island.getSize() + " x " + island.getSize()));

                }
            }


            scoreboard.add("", "");
            scoreboard.add(translateString(primaryColor + "store.pirate"), translateString(primaryColor + "islands.org"));
            scoreboard.add(translateString("&7&m-----------"), translateString("&7&m-----------"));
            scoreboard.update();
        }
    }


    private boolean hasAnyTimers(Player player) {
        return SkyBlock.getPlugin().getTimerHandler().getPlayerTimers(player).stream().filter(timer -> timer.getTime() > 0).count() > 0;
    }
}
