package org.pirateislands.skyblock.island.listeners;

import com.islesmc.islandapi.Island;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.pirateislands.skyblock.SkyBlock;
import org.pirateislands.skyblock.configuration.ServerType;
import org.pirateislands.skyblock.goose.GooseLocationHelper;
import org.pirateislands.skyblock.misc.MessageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by Matt on 2017-02-25.
 */
public class IslandListener implements Listener {
    private final List<Material> canMine;

    public IslandListener() {
        this.canMine = new ArrayList<>();
        this.canMine.add(Material.EMERALD_BLOCK);
        this.canMine.add(Material.IRON_BLOCK);
        this.canMine.add(Material.DIAMOND_BLOCK);
        this.canMine.add(Material.GOLD_BLOCK);
        this.canMine.add(Material.LAPIS_BLOCK);
        this.canMine.add(Material.REDSTONE_BLOCK);
    }

    @EventHandler
    public void onHopperPlace(final BlockPlaceEvent event) {
        Player placer = event.getPlayer();
        if (event.isCancelled() || !event.canBuild())
            return;

        Location location = event.getBlockPlaced().getLocation();

        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld())
            return;

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(location);

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
    public void onBlockPlace(BlockPlaceEvent event) {
        Player placer = event.getPlayer();
        if (placer.hasPermission("skyblock.bypass")) {
            return;
        }
        Location location = event.getBlockPlaced().getLocation();
        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld()) {
            return;
        }
        Island conflict = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(location);
        if (conflict == null) {
            event.setCancelled(true);
            if (location.getWorld().getName().equalsIgnoreCase(SkyBlock.getPlugin().getIslandWorld().getName())) {
                placer.sendMessage(ChatColor.GREEN + "You cannot place outside of your island.");
                MessageUtil.sendServerTheme(placer, ChatColor.YELLOW + "You can purchase a larger island at https://store.pirateislands.org");
            }
            return;
        }
        if (!conflict.isAllowed(placer.getUniqueId()))
        {
            placer.sendMessage(ChatColor.RED + "You do not have permission to build here!");
            event.setCancelled(true);
        }
    }

//    @EventHandler
//    public void onBlockPlace(BlockPlaceEvent event) {
//        Player placer = event.getPlayer();
//
//        if (placer.getItemInHand().getType().equals(Material.BARRIER)) {
//            placer.setItemInHand(new ItemStack(Material.AIR));
//            event.setBuild(false);
//        }
//
//        if (placer.hasPermission("skyblock.bypass"))
//            return;
//
//        Location location = event.getBlockPlaced().getLocation();
//
//        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld())
//            return;
//
//        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(location);
//
//        if (island == null) {
//            System.out.println("island is null");
//            if (location.getWorld().getName().equalsIgnoreCase(SkyBlock.getPlugin().getIslandWorld().getName())) {
//                event.setCancelled(true);
//                placer.sendMessage(ChatColor.GREEN + "You cannot place outside of your island.");
//                MessageUtil.sendServerTheme(placer, ChatColor.YELLOW + "You can purchase a larger island at https://store.pirateislands.org");
//            }
//            return;
//        }
//
//        if (!island.isAllowed(placer.getUniqueId())) {
//            placer.sendMessage(ChatColor.RED + "You do not have permission to build here!");
//            event.setCancelled(true);
//            return;
//        }
//
//        if (placer.getItemInHand().getType().equals(Material.HOPPER)) {
//            if (Objects.equals(island.getHopperAmount(), island.getMaxHoppers())) {
//                event.setCancelled(true);
//                MessageUtil.sendServerTheme(placer, ChatColor.YELLOW + "You have already placed (%s/%s) hoppers. To place more purchase an upgrade at https://store.pirateislands.org");
//                return;
//            }
//            island.setHopperAmount(island.getHopperAmount() + 1);
//        }
//    }

    @EventHandler
    public void onSignChange(final SignChangeEvent event) {
        Location location = event.getBlock().getLocation();

        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld()) return;

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(location);
        if (island == null)
            return;

        if (!event.getLine(0).equalsIgnoreCase("[welcome]"))
            return;

        island.setWarpLocation(GooseLocationHelper.fromLocation(location));
        event.setLine(0, ChatColor.YELLOW + "[Welcome]");
    }

    @EventHandler
    public void onBucketPlace(final PlayerBucketEmptyEvent event) {
        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(event.getBlockClicked().getLocation());
        if (island == null)
            return;

        if (island.isAllowed(event.getPlayer().getUniqueId()))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBucketFill(final PlayerBucketFillEvent event) {
        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(event.getBlockClicked().getLocation());
        if (island == null)
            return;

        if (island.isAllowed(event.getPlayer().getUniqueId()))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(event.getTo());
            if (island == null)
                return;

            if (island.isAllowed(event.getPlayer().getUniqueId()))
                return;

            if (!island.getLocked())
                return;

            if (!island.isExpelled(event.getPlayer().getUniqueId()))
                return;

            event.setTo(event.getFrom());
            event.getPlayer().sendMessage(ChatColor.RED + "This island is currently locked.");
        }
    }

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent event) {
        Island to = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(event.getTo());
        if (to == null)
            return;

        if (to.isAllowed(event.getPlayer().getUniqueId()))
            return;

        if (!to.isExpelled(event.getPlayer().getUniqueId()))
            return;


        if (!to.getLocked())
            return;

        event.setTo(event.getFrom());
        event.getPlayer().sendMessage(ChatColor.RED + "You cannot teleport because the island you are teleporting to is locked.");
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;

        Player damager = (Player) event.getDamager();

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(event.getEntity().getLocation());
        if (island == null)
            return;

        if (island.isAllowed(damager.getUniqueId()))
            return;

        event.setDamage(0D);
        event.setCancelled(true);
        damager.sendMessage(ChatColor.RED + "You cannot attack entities on this island.");
    }

    @EventHandler
    public void onPlayerDamage(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!(event.getDamager() instanceof Player))
            return;

        Player damager = (Player) event.getDamager();
        if (damager == null)
            return;

        Player damaged = (Player) event.getEntity();

        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(damaged);

        if (island == null)
            return;

        if (!island.isAllowed(damager.getUniqueId()))
            return;

        event.setDamage(0);
        event.setCancelled(true);
        damager.sendMessage(ChatColor.RED + String.format("Warning: %s is in your island.", damaged.getName()));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player placer = event.getPlayer();

        Location location = event.getBlock().getLocation();

        //Added to allow SkyBattles to work. -IcyRelic
        if (location.getWorld() != SkyBlock.getPlugin().getIslandWorld())
            return;

        if (placer.hasPermission("skyblock.bypass"))
            return;

        Island conflict = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(location);
        if (conflict == null)
            return;

        if (conflict.isAllowed(placer.getUniqueId()))
            return;


        // System.out.println(String.format("conflicting w/ %s & !member", conflict.getName()));

        if (SkyBlock.getPlugin().getServerConfig().getServerType().equals(ServerType.ISLES) && (conflict.getIslandLevel() < 5)) {
            // Can raid
            // System.out.println("can raid");
            if (!this.canMine.contains(event.getBlock().getType())) {
                placer.sendMessage(ChatColor.RED + "You cannot mine this block while raiding; you must blow it up.");
                event.setCancelled(true);
                return;
            }
            return;
        }

        if (event.getBlock().getType().equals(Material.SIGN) || event.getBlock().getType().equals(Material.SIGN_POST) || event.getBlock().getType().equals(Material.WALL_SIGN)) {
            Sign sign = (Sign) event.getBlock().getState();

            if (sign.getLine(0).equalsIgnoreCase("[welcome]"))
                conflict.setWarpLocation(null);

        }

        placer.sendMessage(ChatColor.RED + "You do not have permission to build here!");
        event.setCancelled(true);
    }

    private boolean isInteractable(Block block) {
        return block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.CHEST
                || block.getType() == Material.FENCE_GATE || block.getType() == Material.TRAP_DOOR;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();

            //Added to allow SkyBattles to work. -IcyRelic
            if (block.getWorld() != SkyBlock.getPlugin().getIslandWorld())
                return;

            if (!isInteractable(block))
                return;

            if (SkyBlock.getPlugin().getServerConfig().getServerType().equals(ServerType.ISLES)) {
                return;
            }

            if (SkyBlock.getPlugin().getIslandRegistry().conflicts(block.getLocation())) {

                Island conflict = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(block.getLocation());
                if (conflict.isAllowed(player.getUniqueId()))
                    return;

                if (player.hasPermission("skyblock.bypass"))
                    return;


                player.sendMessage(ChatColor.RED + "You do not have permission to open containers here!");
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
            }
        }
    }
}