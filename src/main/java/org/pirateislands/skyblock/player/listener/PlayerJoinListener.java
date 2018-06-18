package org.pirateislands.skyblock.player.listener;

import com.islesmc.modules.api.API;
import com.islesmc.modules.api.framework.user.User;
import com.islesmc.modules.api.framework.user.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.pirateislands.skyblock.SkyBlock;

import java.util.ArrayList;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = API.getUserManager().findByUniqueId(player.getUniqueId());
        Profile profile = user.getProfile("skyblock");
        if (profile == null) {
            profile = new Profile("skyblock");
            profile.set("achievements", new ArrayList<>());
            profile.set("kills", 0.0D);
            profile.set("deaths", 0.0D);
            profile.set("killstreak", 0.0D);
            user.getAllProfiles().add(profile);
        }

        if (profile.getDouble("kills") == null) {
            profile.set("achievements", new ArrayList<>());
            profile.set("kills", 0.0D);
            profile.set("deaths", 0.0D);
            profile.set("killstreak", 0.0D);
        }
        user.update();

        SkyBlock.getPlugin().getTabbed().newSimpleTabList(player, 80);
    }
}
