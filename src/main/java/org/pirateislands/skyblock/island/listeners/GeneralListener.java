package org.pirateislands.skyblock.island.listeners;

import com.islesmc.islandapi.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.configuration.ServerType;
import org.pirateislands.skyblock.goose.GooseLocationHelper;
import org.pirateislands.skyblock.island.ChatMode;
import org.pirateislands.skyblock.misc.MessageUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GeneralListener implements Listener {

    public static final Map<UUID, ChatMode> CHAT_MODE_MAP = new HashMap<>();

    @EventHandler
    public void onAsyncChat(final AsyncPlayerChatEvent event) {
        ChatMode chatMode = CHAT_MODE_MAP.get(event.getPlayer().getUniqueId());
        if (chatMode == null) {
            chatMode = ChatMode.PUBLIC;
            CHAT_MODE_MAP.put(event.getPlayer().getUniqueId(), ChatMode.PUBLIC);
        }

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(event.getPlayer());
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

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
        player.sendMessage(" ");
        player.sendMessage(ChatColor.YELLOW + " Welcome " + ChatColor.WHITE + player.getName() + ChatColor.YELLOW + " to " + "Pirate" + ChatColor.WHITE + "Islands");
        player.sendMessage(" ");
        player.sendMessage(ChatColor.YELLOW + " Website:" + ChatColor.WHITE + " https://pirateislands.org");
        player.sendMessage(ChatColor.YELLOW + " Discord:" + ChatColor.WHITE + " https://discord.pirateislands.org");
        player.sendMessage(ChatColor.YELLOW + " Shop:" + ChatColor.WHITE + " https://store.pirateislands.org");
        player.sendMessage(" ");
        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");

        if (!player.hasPlayedBefore()) {
            SkyBlock.getPlugin().getServerConfig().incrementPlayersJoined();
            int joinNumber = SkyBlock.getPlugin().getServerConfig().getPlayersJoined();
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Pirate" + ChatColor.WHITE + "Islands " + ChatColor.GRAY + "\u00BB " + ChatColor.YELLOW + String.format("Welcome %s to the ", player.getName()) + ChatColor.YELLOW + "Pirate" + ChatColor.WHITE + "Islands" + ChatColor.YELLOW + String.format(" (%s)", joinNumber));
            player.teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location spawn = SkyBlock.getPlugin().getServerConfig().getSpawnLocation();
        player.teleport(spawn);
        event.setRespawnLocation(spawn);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (player.hasPermission("skyblock.keepinv")) {
            event.setKeepInventory(true);
        } else {
            event.setKeepInventory(false);
        }
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player && event.getDamager() instanceof Player))
            return;

        Player damaged = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if (damaged.getWorld().getName().equalsIgnoreCase(SkyBlock.getPlugin().getIslandWorld().getName()) && !SkyBlock.getPlugin().getServerConfig().getServerType().equals(ServerType.ISLES)) {
            event.setCancelled(true);
            event.setDamage(0);
        } else if (damaged.getWorld().getName().equalsIgnoreCase("Spawn")) {
            MessageUtil.sendServerTheme(damager, "You cannot damage other players in spawn.");
            event.setCancelled(true);
            event.setDamage(0);
        }
    }

    @EventHandler
    public void onFoodLevel(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) return;

        Player player = (Player) event.getEntity();

        if (!(player.getWorld() == SkyBlock.getPlugin().getServerConfig().getSpawnLocation().getWorld())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onGiftPlace(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand() == null) {
            return;
        }

        ItemStack item = event.getPlayer().getItemInHand();

        if (item.getType() != Material.SKULL && item.getType() != Material.SKULL_ITEM) {
            return;
        }

        if (item.getItemMeta() == null) {
            return;
        }

        if (item.getItemMeta().getDisplayName() == null) {
            return;
        }

        if (!ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("Christmas Present")) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        event.setCancelled(true);

        ItemStack[] items = new ItemStack[]{new ItemStack(Material.CHEST, 1), new ItemStack(Material.ICE, 2), new ItemStack(Material.MELON, 1),
                new ItemStack(Material.BONE, 1), new ItemStack(Material.LAVA_BUCKET), new ItemStack(Material.MELON_SEEDS, 1),
                new ItemStack(Material.SUGAR_CANE, 1), new ItemStack(Material.RED_MUSHROOM, 1), new ItemStack(Material.BROWN_MUSHROOM, 1),
                new ItemStack(Material.CACTUS, 1), new ItemStack(Material.PUMPKIN_SEEDS, 1)};

        event.getPlayer().getInventory().removeItem(item);
        event.getPlayer().getInventory().addItem(items);

        MessageUtil.sendServerTheme(event.getPlayer(), ChatColor.GREEN + "You have opened your Christmas Present!");
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
            event.getEntity().remove();
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (!(player.getWorld() == SkyBlock.getPlugin().getServerConfig().getSpawnLocation().getWorld()))
            return;

        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            System.out.println(193);
            player.teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onVoid(PlayerMoveEvent event) {
        if (!(SkyBlock.getPlugin().getServerConfig().getServerType() == ServerType.SKY))
            return;

        if (event.getPlayer().getLocation().getY() < -5) {
            if (event.getPlayer().getWorld().getUID().equals(SkyBlock.getPlugin().getIslandWorld().getUID())) {
                Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(event.getPlayer());
                if (island == null) {
                    event.getPlayer().teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
                    return;
                }
                Location location = GooseLocationHelper.toLocation(island.getSpawn());

                Block highest = location.getWorld().getHighestBlockAt(location);

                if (highest.getType().equals(Material.AIR)) {
                    location.getBlock().setType(Material.COBBLESTONE);
                }

                event.getPlayer().teleport(location.getWorld().getHighestBlockAt(location).getLocation());
                event.getPlayer().setFallDistance(0);
                return;
            }
            event.getPlayer().teleport(SkyBlock.getPlugin().getServerConfig().getSpawnLocation());
        }
    }
}

