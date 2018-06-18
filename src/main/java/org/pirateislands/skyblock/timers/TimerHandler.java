package org.pirateislands.skyblock.timers;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TimerHandler {
    private final Map<UUID, List<org.pirateislands.skyblock.timers.Timer>> timers;

    public TimerHandler() {
        this.timers = new ConcurrentHashMap<>();
    }

    public List<org.pirateislands.skyblock.timers.Timer> getPlayerTimers(Player player) {
        if (!timers.containsKey(player.getUniqueId()))
            this.timers.put(player.getUniqueId(), new ArrayList<>());
        return this.timers.get(player.getUniqueId());
    }

    public boolean hasTimer(Player player, TimerType timerType) {
        return timers.get(player.getUniqueId()) != null && this.timers.get(player.getUniqueId()).stream().filter(timer -> timer.getTimerType() == timerType).anyMatch(timer -> timer.getTime() > 0L);
    }

    public void addTimer(Player player, org.pirateislands.skyblock.timers.Timer defaultTimer) {
        if (timers.get(player.getUniqueId()) == null) {
            List<org.pirateislands.skyblock.timers.Timer> timersList = new LinkedList<>();
            timersList.add(defaultTimer);
            timers.put(player.getUniqueId(), timersList);
            return;
        }
        List<org.pirateislands.skyblock.timers.Timer> timersList = this.timers.get(player.getUniqueId());
        timersList.add(defaultTimer);
        timers.put(player.getUniqueId(), timersList);
    }

    public boolean hasActiveTimers(Player player) {
        return !this.timers.containsKey(player.getUniqueId()) && this.timers.get(player.getUniqueId()).stream().anyMatch(timer -> timer.getTime() > 0);

    }

    public org.pirateislands.skyblock.timers.Timer getTimer(Player player, TimerType timerType) {
        if (timers.get(player.getUniqueId()) == null)
            return null;

        for (org.pirateislands.skyblock.timers.Timer timer : this.timers.get(player.getUniqueId())) {
            if (timer.getTimerType() == timerType) {
                if (timer.getTime() > 0L)
                    return timer;
            }
        }

        return null;
    }

    public Map<UUID, List<org.pirateislands.skyblock.timers.Timer>> getTimers() {
        return this.timers;
    }
}
