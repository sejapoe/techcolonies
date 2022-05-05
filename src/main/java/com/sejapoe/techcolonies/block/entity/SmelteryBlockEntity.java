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
import java.util.ArrayList;

public class SmelteryBlockEntity extends AbstractStructureControllerBlockEntity {

  public SmelteryBlockEntity(BlockPos blockPos, BlockState state) {
    super(ModBlockEntities.SMELTERY_BE.get(), blockPos, state);
  }
  public static void tick(Level level, BlockPos blockPos, BlockState blockState, SmelteryBlockEntity blockEntity) {
    if (level.isClientSide()) return;
    PlatedBlockPattern.BlockPatternMatch match = checkStructure(level, blockPos, blockState);
    if (match != null) {
      blockEntity.updateStatus(true, match.getLowestMaterial().getLevel());
      blockEntity.setInterfaces(match.getInterfaces());
      blockEntity.updateInterfaces();
    } else {
      blockEntity.updateStatus(false, 0);
      blockEntity.updateInterfaces();
      blockEntity.setInterfaces(new ArrayList<>());
    }
  }
  @Nullable
  private static PlatedBlockPattern.BlockPatternMatch checkStructure(Level level, BlockPos pos, BlockState state) {
    Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
    BlockPos leftBotFront = pos.offset(facing.getClockWise().getNormal());
    return Structures.SMELTERY_PATTERN.matches(level, leftBotFront, facing);
  }
}
