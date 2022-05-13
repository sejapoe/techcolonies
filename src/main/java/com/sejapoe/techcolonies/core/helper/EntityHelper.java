package com.sejapoe.techcolonies.core.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;

public class EntityHelper {
  public static void moveMobToBlock(PathfinderMob mob, BlockPos pos) {
    mob.getNavigation().moveTo((double)((float)pos.getX()) + 0.5D, pos.getY(), (double)((float)pos.getZ()) + 0.5D, 1.0f);
  }
}
