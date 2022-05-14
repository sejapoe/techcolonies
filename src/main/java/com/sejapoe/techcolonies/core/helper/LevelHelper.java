package com.sejapoe.techcolonies.core.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;

public class LevelHelper {
  public static void removeBlock(Level level, BlockPos pos) {
    FluidState state = level.getFluidState(pos);
    level.setBlock(pos, state.createLegacyBlock(), 3);
  }
}
