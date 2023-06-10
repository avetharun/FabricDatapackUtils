package com.feintha.dpu;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.*;

public class DPUFastAsyncSearch {
    private static class BlockStateChecker implements Callable<Boolean> {
        private final World world;
        private final BlockBox region;
        private final BlockState targetState;

        public BlockStateChecker(World world, BlockBox region, BlockState targetState) {
            this.world = world;
            this.region = region;
            this.targetState = targetState;
        }

        @Override
        public Boolean call() throws ExecutionException {
            for (int x = region.getMinX(); x <= region.getMaxX(); x++) {
                for (int y = region.getMinY(); y <= region.getMaxY(); y++) {
                    for (int z = region.getMinZ(); z <= region.getMaxZ(); z++) {
                        System.out.println(x+" " + y + " " + z);
                        if (Thread.interrupted()) {
                            return false;
                        }
                        world.setBlockState(new BlockPos(x,y,z), Blocks.STONE.getDefaultState());
                        BlockState currentState = world.getBlockState(new BlockPos(x, y, z));
                        if (currentState.equals(targetState)) {
                            return true;
                        }
                    }
                }
            }
            throw new ExecutionException(new Exception("not found"));
        }
    }

    public static boolean CheckBlockStateInRadius(int x1, int y1, int z1, int x2, int y2, int z2, World w, BlockState validState) {
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    BlockState currentState = w.getBlockState(new BlockPos(x, y, z));
                    if (currentState.equals(validState)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}