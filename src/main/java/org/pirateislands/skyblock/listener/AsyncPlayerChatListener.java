package org.pirateislands.skyblock.listener;

import com.islesmc.islandapi.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.dto.ChatMode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AsyncPlayerChatListener implements Listener {
    public static final Map<UUID, ChatMode> CHAT_MODE_MAP = new HashMap<>();

    @EventHandler
    public void onAsyncChat(final AsyncPlayerChatEvent event) {
        ChatMode chatMode = CHAT_MODE_MAP.get(event.getPlayer().getUniqueId());
        if (chatMode == null) {
            chatMode = ChatMode.PUBLIC;
            CHAT_MODE_MAP.put(event.getPlayer().getUniqueId(), ChatMode.PUBLIC);
        }

        Island island = SkyBlock.getInstance().getIslandHandler().getIslandForPlayer(event.getPlayer());
        if (island == null)
            chatMode = ChatMode.PUBLIC;

        if (chatMode == ChatMode.PUBLIC)
            return;

        event.setCancelled(true);

        for (UUID uuid : island.getMembers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null)
                continue;

            player.sendMessage(ChatColor.AQUA + "[Island] " + ChatColor.WHITE + event.getPlayer().getName() + ChatColor.GOLD + " \u00BB " + ChatColor.WHITE + event.getMessage());
        }

        Player owner = Bukkit.getPlayer(island.getOwner());
        if (owner != null) {
            owner.sendMessage(ChatColor.AQUA + "[Island] " + ChatColor.WHITE + event.getPlayer().getName() + ChatColor.GOLD + " \u00BB " + ChatColor.WHITE + event.getMessage());
        }
    }
}
