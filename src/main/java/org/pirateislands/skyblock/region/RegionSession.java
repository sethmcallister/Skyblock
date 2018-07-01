package org.pirateislands.skyblock.region;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RegionSession implements Listener {

    private Player player;
    private ItemStack wand;

    private Location min, max;

    public RegionSession(Player player) {
        this.player = player;

        this.wand = new ItemStack(Material.GOLD_HOE);
        ItemMeta wandMeta = wand.getItemMeta();
        wandMeta.setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Region Wand");
        wand.setItemMeta(wandMeta);
        player.getInventory().addItem(wand);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        ItemStack itemStack = e.getItemDrop().getItemStack();
        if (!itemStack.hasItemMeta())
            return;

        ItemMeta meta = itemStack.getItemMeta();
        if (!meta.getDisplayName().equalsIgnoreCase(wand.getItemMeta().getDisplayName()))
            return;

        if (!e.getPlayer().getName().equalsIgnoreCase(player.getName()))
            return;


    }

    public Player getPlayer() {
        return player;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (player.getName().equalsIgnoreCase(this.player.getName()))
            return;

        if (player.getInventory().getItemInHand() == null || player.getInventory().getItemInHand() != wand)
            return;

        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            this.setMin(e.getClickedBlock().getLocation());
            player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] Set point one.");
            player.sendMessage(ChatColor.GRAY + "Right click the next block to set the second point.");
        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            this.setMax(e.getClickedBlock().getLocation());
            player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] Set point two.");
            player.sendMessage(ChatColor.GRAY + "If both points are set, execute /region create <name>.");
        } else {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] Not a valid action!");
        }
    }

    public Location getMin() {
        return min;
    }

    public void setMin(Location min) {
        this.min = min;
    }

    public Location getMax() {
        return max;
    }

    public void setMax(Location max) {
        this.max = max;
    }
}
