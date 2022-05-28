package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.block.PlatedBrickWallBlock;
import com.sejapoe.techcolonies.block.PortalBlock;
import com.sejapoe.techcolonies.core.structures.PlatedBlockPattern;
import com.sejapoe.techcolonies.core.structures.Structures;
import com.sejapoe.techcolonies.registry.ModBlockEntities;
import com.sejapoe.techcolonies.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WallSide;
import org.jetbrains.annotations.NotNull;

public class PortalControllerBlockEntity extends AbstractStructureControllerBlockEntity {
  public PortalControllerBlockEntity(BlockPos blockPos, BlockState state) {
    super(ModBlockEntities.PORTAL_BE.get(), blockPos, state);
  }

  @Override
  protected PlatedBlockPattern.BlockPatternMatch checkStructure(Level level, BlockPos pos, BlockState state) {
    BlockPos leftFrontBot = pos.offset(2, 0, -2);
    return Structures.PORTAL_PATTERN.matches(level, leftFrontBot, Direction.NORTH);
  }

  @Override
  public void tick(@NotNull Level level, BlockPos blockPos, BlockState blockState) {
    if (this.isComplete()) {
      checkAndRepairPortal(level, blockPos);
    } else {
      checkAndClearPortal(level, blockPos);
    }
  }

  private void checkAndRepairPortal(Level level, BlockPos blockPos) {
    Block portalBlock = ModBlocks.PORTAL_BLOCK.get();
    for (int i = 1; i < 4; ++i) {
      BlockPos blockPos1 = blockPos.above(i);
      BlockState pState = portalBlock.defaultBlockState().setValue(PortalBlock.AXIS, Direction.Axis.Y);
      if (!level.getBlockState(blockPos1).equals(pState)) {
        level.setBlockAndUpdate(blockPos1, pState);
      }
      for (Direction direction : Direction.Plane.HORIZONTAL) {
        BlockPos blockPos2 = blockPos1.offset(direction.getNormal());
        BlockState pState1 = portalBlock.defaultBlockState().setValue(PortalBlock.AXIS, direction.getAxis());
        if (!level.getBlockState(blockPos2).equals(pState1)) {
          level.setBlockAndUpdate(blockPos2, pState1);
        }
        BlockPos blockPos3 = blockPos2.offset(direction.getNormal());
        if (level.getBlockState(blockPos3).getValue(PlatedBrickWallBlock.getPropertyFromDirection(direction.getOpposite())) != WallSide.TALL) {
          level.setBlockAndUpdate(blockPos3, level.getBlockState(blockPos3).setValue(PlatedBrickWallBlock.getPropertyFromDirection(direction.getOpposite()), WallSide.TALL));
        }
      }
    }
  }

  private void checkAndClearPortal(Level level, BlockPos blockPos) {
    Block portalBlock = ModBlocks.PORTAL_BLOCK.get();
    for (int i = 1; i < 4; ++i) {
      BlockPos blockPos1 = blockPos.above(i);
      if (level.getBlockState(blockPos1).is(portalBlock)) {
        level.setBlockAndUpdate(blockPos1, Blocks.AIR.defaultBlockState());
      }
      for (Direction direction : Direction.Plane.HORIZONTAL) {
        BlockPos blockPos2 = blockPos1.offset(direction.getNormal());
        if (level.getBlockState(blockPos2).is(portalBlock)) {
          level.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState());
        }
      }
    }
  }
}
