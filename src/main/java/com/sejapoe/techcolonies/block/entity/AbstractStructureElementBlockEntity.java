package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.block.SmelteryBlock;
import com.sejapoe.techcolonies.core.properties.ModProperties;
import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractStructureElementBlockEntity extends BlockEntity {
  private boolean isComplete;
  private int structureLevel;

  public AbstractStructureElementBlockEntity(BlockEntityType type, BlockPos blockPos, BlockState state) {
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

  public void updateStatus(boolean isComplete, int structureLevel) {
    BlockState state = Objects.requireNonNull(level).getBlockState(getBlockPos());
    if (this.isComplete != isComplete || this.structureLevel != structureLevel) {
      this.setComplete(isComplete);
      this.setStructureLevel(isComplete ? structureLevel : 0);
      state = state.setValue(ModProperties.PLATING_MATERIAL, Objects.requireNonNull(PlatingMaterial.valueOf(this.structureLevel)));
      level.setBlockAndUpdate(getBlockPos(), state);
      setChanged(level, getBlockPos(), state);
    }
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
