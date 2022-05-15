package com.sejapoe.techcolonies.core.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;

public class EntityHelper {
  public static void moveMobToBlock(PathfinderMob mob, BlockPos pos) {
    mob.getNavigation().moveTo((double)((float)pos.getX()), pos.getY(), (double)((float)pos.getZ()), 1.0f);
  }
}
