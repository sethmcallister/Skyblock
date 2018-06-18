package org.pirateislands.skyblock.util;

import java.util.BitSet;

public class GridUtil {
    private int size;
    private int[] ints;

    public GridUtil(int size) {
        this.size = size;
        this.ints = new int[size];
        createSet(size * size);
    }

    private void createSet(int n) {
        boolean[] is_composite = new boolean[n >> 1];
        BitSet result = new BitSet((int) Math.ceil(1.25506 * n / Math.log(n)));
        result.set(2);
        for (int i = 1; i < is_composite.length; i++) {
            if (!is_composite[i]) {
                int cur = (i * 2) + 1;
                result.set(cur);
                ints[ints.length - 1] = cur;
            }
        }
    }

    public int[] getInts() {
        return ints;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
