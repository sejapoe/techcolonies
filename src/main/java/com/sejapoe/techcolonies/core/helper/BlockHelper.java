package com.sejapoe.techcolonies.core.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockHelper {
  public static int getBreakingTimeForTool(Level level, BlockState state, ItemStack tool) {
    //    if (worker.getMainHandItem()) return (int) (state.getDestroySpeed(level, pos));
    return (int) (
            28.6D * (double) state.getDestroySpeed(level, BlockPos.ZERO) / tool.getItem().getDestroySpeed(tool, state)
    ); // Perk reduction?
  }
}
