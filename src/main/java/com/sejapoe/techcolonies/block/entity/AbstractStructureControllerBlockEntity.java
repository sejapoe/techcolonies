package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.core.structures.PlatedBlockPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractStructureControllerBlockEntity extends AbstractStructureElementBlockEntity{

  public AbstractStructureControllerBlockEntity(BlockEntityType type, BlockPos blockPos, BlockState state) {
    super(type, blockPos, state);
  }
  @Override
  public void updateStatus(boolean isComplete, int structureLevel) {
    super.updateStatus(isComplete, structureLevel);
  }

  protected void updateStructureStatus(Level level, BlockPos blockPos, BlockState blockState) {
    PlatedBlockPattern.BlockPatternMatch match = this.checkStructure(level, blockPos, blockState);
    if (match != null) {
      this.updateStatus(true, match.getLowestMaterial().getLevel());
    } else {
      this.updateStatus(false, 0);
    }
  }

  protected abstract PlatedBlockPattern.BlockPatternMatch checkStructure(Level level, BlockPos pos, BlockState state);

  public static void tick(@NotNull Level level, BlockPos blockPos, BlockState blockState, AbstractStructureControllerBlockEntity blockEntity) {
    if (level.isClientSide()) return;
    blockEntity.updateStructureStatus(level, blockPos, blockState);
    blockEntity.tick(level, blockPos, blockState);
  }

  public abstract void tick(@NotNull Level level, BlockPos blockPos, BlockState blockState);
}
