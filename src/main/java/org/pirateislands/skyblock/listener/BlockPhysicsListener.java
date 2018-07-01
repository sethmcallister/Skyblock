package org.pirateislands.skyblock.listener;

import com.islesmc.modules.api.API;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.material.PistonBaseMaterial;
import org.pirateislands.skyblock.task.DelayedBlockRetractTask;

import java.util.ArrayList;
import java.util.List;

public class BlockPhysicsListener implements Listener {
    private final int maxBlockLength = 64;
    private List<Block> preventMoving = new ArrayList<>();


    @EventHandler
    public void onBlockPhyscis(final BlockPhysicsEvent event) {
        Block block = event.getBlock();
        if (((block.getType() == Material.PISTON_BASE) || (block.getType() == Material.PISTON_STICKY_BASE)) && (block.getBlockPower() > 0)) {
            PistonBaseMaterial pistonBaseMaterial = (PistonBaseMaterial) block.getState().getData();
            if (!pistonBaseMaterial.isPowered()) {
                BlockFace facing = pistonBaseMaterial.getFacing();
                World world = block.getWorld();
                Location tmp = block.getRelative(facing).getLocation();
                Block tmpBlock = world.getBlockAt(tmp);
                if ((moveBlock(tmp, facing, 0)) && (tmpBlock != null)) {
                    if (tmpBlock.getType() != Material.AIR) {
                        tmpBlock.setType(Material.AIR);
                    }
                } else {
                    this.preventMoving.add(block);
                }
            }
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent e) {
        Block block = e.getBlock();
        if (this.preventMoving.contains(block)) {
            e.setCancelled(true);
            this.preventMoving.remove(block);
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent e) {
        Block block = e.getBlock();
        if (block.getType() == Material.PISTON_STICKY_BASE) {
            Block tmp = block.getRelative(e.getDirection());
            Block tmp2 = tmp.getRelative(e.getDirection());
            if ((tmp2.getType() != Material.AIR)) {
                new DelayedBlockRetractTask(tmp, tmp2).runTaskLater(API.getPlugin(), 5L);
            }
        }
    }

    private boolean moveBlock(Location location, BlockFace blockFace, int movedBlocks) {
        if (movedBlocks > this.maxBlockLength) {
            return false;
        }
        Block block = location.getWorld().getBlockAt(location);
        Location toLocation;
        if (block != null) {
            if (block.getType() != Material.AIR) {
                Block tmpBlock = block.getRelative(blockFace);
                if (moveBlock(tmpBlock.getLocation(), blockFace, ++movedBlocks)) {
                    tmpBlock.setType(block.getType());
                } else {
                    return false;
                }
            } else {
                toLocation = block.getRelative(blockFace).getLocation();
                for (Player p : location.getWorld().getPlayers()) {
                    Location playerLocation = p.getLocation().clone();
                    double distance = location.subtract(playerLocation).toVector().lengthSquared();
                    if (distance <= 1.0D) {
                        playerLocation.setX(toLocation.getX() + 0.5D);
                        playerLocation.setY(toLocation.getY());
                        playerLocation.setZ(toLocation.getZ() + 0.5D);

                        p.teleport(playerLocation);
                    }
                }
            }
        }
        return true;
    }
}
