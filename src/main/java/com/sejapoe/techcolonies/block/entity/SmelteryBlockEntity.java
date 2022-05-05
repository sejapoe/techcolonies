package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.core.ModBlockEntities;
import com.sejapoe.techcolonies.core.properties.ModProperties;
import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import com.sejapoe.techcolonies.core.structures.PlatedBlockPattern;
import com.sejapoe.techcolonies.core.structures.Structures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class SmelteryBlockEntity extends AbstractStructureBlockEntity {

  public SmelteryBlockEntity(BlockPos blockPos, BlockState state) {
    super(ModBlockEntities.SMELTERY_BE.get(), blockPos, state);
  }
  public static void tick(Level level, BlockPos blockPos, BlockState blockState, SmelteryBlockEntity blockEntity) {
    boolean isChanged = false;
    PlatedBlockPattern.BlockPatternMatch match = checkStructure(level, blockPos, blockState);
    int oldStructureLevel = blockState.getValue(ModProperties.PLATING_MATERIAL).getLevel();
    if (match != null) {
      if (match.getLowestMaterial().getLevel() != oldStructureLevel) {
        blockEntity.setComplete(true);
        blockEntity.setStructureLevel(match.getLowestMaterial().getLevel());
        isChanged = true;
      }
    } else {
      if (blockEntity.isComplete() || blockEntity.getStructureLevel() != 0) {
        blockEntity.setComplete(false);
        blockEntity.setStructureLevel(0);
        isChanged = true;
      }
    }

    boolean isBlockStateChanged = false;
    if (blockEntity.getStructureLevel() != oldStructureLevel) {
      blockState = blockState.setValue(ModProperties.PLATING_MATERIAL, PlatingMaterial.valueOf(blockEntity.getStructureLevel()));
      isBlockStateChanged = true;
    }

    if (isBlockStateChanged) {
      level.setBlock(blockPos, blockState, 3);
    }
    if (isChanged) {
      setChanged(level, blockPos, blockState);
    }
  }
  @Nullable
  private static PlatedBlockPattern.BlockPatternMatch checkStructure(Level level, BlockPos pos, BlockState state) {
    Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
    BlockPos leftBotFront = pos.offset(facing.getClockWise().getNormal());
    return Structures.SMELTERY_PATTERN.matches(level, leftBotFront, facing);
  }
}
