package org.pirateislands.skyblock.dto;

import com.islesmc.modules.api.API;
import com.islesmc.modules.api.framework.user.User;
import com.islesmc.modules.api.framework.user.profile.Profile;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Quest {
    private final String name;
    private final List<String> description;

    public Quest(final String name, final List<String> description) {
        this.name = name;
        this.description = description;
    }

    public abstract void checkCompletion(final Player player);

    public void completeQuest(final Player player) {
        User user = API.getUserManager().findByUniqueId(player.getUniqueId());
        Profile profile = user.getProfile("skyblock");
        List<String> completedQuests = (ArrayList<String>) profile.getObject("completedQuests");
        completedQuests.add(getName());
        profile.set("completedQuests", completedQuests);
        user.update();
    }

    public abstract void onLoad();

    public abstract void onUnload();

    public String getName() {
        return name;
    }

    public List<String> getDescription() {
        return description;
    }
}
