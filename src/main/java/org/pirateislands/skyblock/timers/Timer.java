package org.pirateislands.skyblock.timers;

import org.bukkit.entity.Player;

public interface Timer {
    Long getTime();

    void setTime(Long time);

    void freeze();

    void unfreeze();

    boolean isFrozen();

    TimerType getTimerType();

    Player getPlayer();

    Integer getTagLevel();
}
