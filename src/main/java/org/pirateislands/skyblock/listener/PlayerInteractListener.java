package org.pirateislands.skyblock.listener;

import com.islesmc.islandapi.Island;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.timers.DefaultTimer;
import org.pirateislands.skyblock.timers.Timer;
import org.pirateislands.skyblock.timers.TimerType;
import org.pirateislands.skyblock.util.MessageUtil;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onEnderpearlClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getPlayer().getItemInHand().getType().equals(Material.ENDER_PEARL)) {
            if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                Timer timer = SkyBlock.getInstance().getTimerHandler().getTimer(player, TimerType.ENDERPEARL);
                if (timer != null && timer.getTime() > 0) {
                    long millisLeft = timer.getTime();
                    double value = millisLeft / 1000.0D;
                    double sec = Math.round(10.0D * value) / 10.0D;
                    event.setCancelled(true);
                    event.getPlayer().updateInventory();
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot use this for another &c&l" + sec + "&c seconds."));
                    return;
                }
                SkyBlock.getInstance().getTimerHandler().addTimer(player, new DefaultTimer(TimerType.ENDERPEARL, 16000 + System.currentTimeMillis(), player));
            }
        }
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
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Block block = event.getClickedBlock();

        if (block.getWorld() != SkyBlock.getInstance().getIslandWorld())
            return;

        if (!isIntractable(block))
            return;

        Island conflict = SkyBlock.getInstance().getIslandHandler().getIslandAt(block.getLocation());

        if (conflict == null)
            return;

        if (conflict.isAllowed(player.getUniqueId()))
            return;

        if (player.hasPermission("skyblock.bypass"))
            return;


        player.sendMessage(ChatColor.RED + "You do not have permission to open containers here!");
        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
    }

    private boolean isIntractable(Block block) {
        return block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.CHEST
                || block.getType() == Material.FENCE_GATE || block.getType() == Material.TRAP_DOOR;
    }
}
