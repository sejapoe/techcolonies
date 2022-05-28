package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.core.structures.PlatedBlockPattern;
import com.sejapoe.techcolonies.core.structures.Structures;
import com.sejapoe.techcolonies.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PortalControllerBlockEntity extends AbstractStructureControllerBlockEntity {
  public PortalControllerBlockEntity(BlockPos blockPos, BlockState state) {
    super(ModBlockEntities.PORTAL_BE.get(), blockPos, state);
  }

  @Override
  protected PlatedBlockPattern.BlockPatternMatch checkStructure(Level level, BlockPos pos, BlockState state) {
    BlockPos leftFrontBot = pos.offset(2, 0, -2);
    return Structures.PORTAL_PATTERN.matches(level, leftFrontBot, Direction.NORTH);
  }
}
