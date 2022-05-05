package com.sejapoe.techcolonies.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractStructureBlockEntity extends BlockEntity {
  private boolean isComplete;
  private int structureLevel;

  public AbstractStructureBlockEntity(BlockEntityType type, BlockPos blockPos, BlockState state) {
    super(type, blockPos, state);
    this.isComplete = false;
    this.structureLevel = 0;
  }

  @Override
  public void load(@NotNull CompoundTag compoundTag) {
    super.load(compoundTag);
    this.isComplete = compoundTag.getBoolean("IsComplete");
    this.structureLevel = compoundTag.getInt("StructureLevel");
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag compoundTag) {
    super.saveAdditional(compoundTag);
    compoundTag.putBoolean("IsComplete", this.isComplete);
    compoundTag.putInt("StructureLevel", this.structureLevel);
  }

  public boolean isComplete() {
    return isComplete;
  }

  public void setComplete(boolean complete) {
    isComplete = complete;
  }

  public int getStructureLevel() {
    return structureLevel;
  }

  public void setStructureLevel(int structureLevel) {
    this.structureLevel = structureLevel;
  }
}
