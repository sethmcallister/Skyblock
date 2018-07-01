package org.pirateislands.skyblock.task;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class DelayedBlockRetractTask extends BukkitRunnable {
    private Block toMove;
    private Block fromMove;

    public DelayedBlockRetractTask(Block toMove, Block fromMove) {
        this.toMove = toMove;
        this.fromMove = fromMove;
    }

    public void run() {
        if ((this.fromMove.getType() != Material.AIR) && (this.toMove.getType() == Material.AIR)) {
            this.toMove.setType(this.fromMove.getType());
            this.fromMove.setType(Material.AIR);
        }
    }
}
