package org.pirateislands.skyblock.island.listeners.isles;

import com.islesmc.islandapi.Island;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.pirateislands.skyblock.SkyBlock;

import java.util.ArrayList;

public class CoordinateBookListener implements Listener {
    public static final String COORD_BOOK_UNSET = ChatColor.translateAlternateColorCodes('&', "&eUnset Coordinate Book &7(Right click & Sneak)");
    private static final String COORD_BOOK_SET = ChatColor.translateAlternateColorCodes('&', "&eCoordinate book for %s");

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || !event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getPlayer().isSneaking()) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack hand = player.getItemInHand();
        if (hand == null || hand.getType().equals(Material.AIR))
            return;

        if (!hand.hasItemMeta())
            return;

        ItemMeta meta = hand.getItemMeta();

        if (meta.getDisplayName() == null)
            return;

        if (!meta.getDisplayName().equalsIgnoreCase(COORD_BOOK_UNSET))
            return;

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(player.getLocation());
        if (island == null)
            return;

        ItemStack newBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) newBook.getItemMeta();
        bookMeta.setDisplayName(String.format(COORD_BOOK_SET, island.getName()));
        bookMeta.setAuthor("CONSOLE");
        bookMeta.setTitle(String.format(COORD_BOOK_SET, island.getName()));
        bookMeta.setPages(new ArrayList<>());
        bookMeta.addPage(String.format("Within this book entails the whereabouts of %s", island.getName()));
        bookMeta.addPage(String.format("The location is as follows: \nX: %s \nY: %s \nZ: %s", island.getSpawn().getX().intValue(), island.getSpawn().getY().intValue(), island.getSpawn().getZ().intValue()));
        newBook.setItemMeta(bookMeta);

        player.getInventory().remove(hand);
        player.getInventory().addItem(newBook);
    }
}
