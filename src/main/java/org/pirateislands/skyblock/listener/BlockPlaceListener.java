package org.pirateislands.skyblock.listener;

import com.avaje.ebeaninternal.server.core.Message;
import com.islesmc.islandapi.Island;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.dto.StackedSpawner;
import org.pirateislands.skyblock.goose.GooseLocationHelper;
import org.pirateislands.skyblock.util.MessageUtil;

import java.util.Objects;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onHopperBlockPlace(final BlockPlaceEvent event) {
        if (event.isCancelled() || !event.canBuild())
            return;

        Player placer = event.getPlayer();
        Location location = event.getBlockPlaced().getLocation();

        if (location.getWorld() != SkyBlock.getInstance().getIslandWorld())
            return;

        Island island = SkyBlock.getInstance().getIslandHandler().getIslandAt(location);

        if (island == null) {
            return;
        }

        if (placer.getItemInHand().getType().equals(Material.HOPPER)) {
            if (Objects.equals(island.getHopperAmount(), island.getMaxHoppers())) {
                event.setCancelled(true);
                MessageUtil.sendServerTheme(placer, ChatColor.YELLOW + "You have already placed (%s/%s) hoppers. To place more purchase an upgrade at https://store.pirateislands.org");
                return;
            }
            island.setHopperAmount(island.getHopperAmount() + 1);
        }
    }

    @EventHandler
    public void onSpawnerBlockPlace(final BlockPlaceEvent event) {
        if (event.isCancelled() || !event.canBuild())
            return;

        Player player = event.getPlayer();
        ItemStack hand = player.getItemInHand();
        if (!hand.getType().equals(Material.MOB_SPAWNER))
            return;

        Block placedAgainst = event.getBlockAgainst();
        if (!placedAgainst.getType().equals(Material.MOB_SPAWNER))
            return;

        CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlockAgainst().getState();

        Location location = placedAgainst.getLocation();
        StackedSpawner stackedSpawner = SkyBlock.getInstance().getStackedSpawnerHandler().findByLocation(location);
        if (stackedSpawner == null) {
            stackedSpawner = new StackedSpawner(creatureSpawner.getSpawnedType(), GooseLocationHelper.fromLocation(location));
            SkyBlock.getInstance().getStackedSpawnerHandler().findAll().add(stackedSpawner);
        }

        stackedSpawner.getAmount().addAndGet(hand.getAmount());
        if (stackedSpawner.getHologram() == null) {

        }
        stackedSpawner.getHologram().update();
        event.setCancelled(true);
        player.getInventory().remove(hand);
        MessageUtil.sendServerTheme(player, ChatColor.YELLOW + String.format("This %s spawner is now stacked at %s spawners.", stackedSpawner.getEntityType().getName(), stackedSpawner.getAmount().get()));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled() || !event.canBuild())
            return;

        Player placer = event.getPlayer();
        if (placer.hasPermission("skyblock.bypass")) {
            return;
        }
        Location location = event.getBlockPlaced().getLocation();
        if (!location.getWorld().getUID().equals(SkyBlock.getInstance().getIslandWorld().getUID())) {
            return;
        }

        Island conflict = SkyBlock.getInstance().getIslandHandler().getIslandAt(location);
        if (conflict == null) {
            placer.sendMessage(ChatColor.GREEN + "You cannot place outside of your island.");
            MessageUtil.sendServerTheme(placer, ChatColor.YELLOW + "You can purchase a larger island at https://store.pirateislands.org");

            event.setCancelled(true);
            return;
        }
        if (conflict.isAllowed(placer.getUniqueId()))
            return;

        placer.sendMessage(ChatColor.RED + "You can only place blocks on your own island");
        event.setCancelled(true);
    }
}
