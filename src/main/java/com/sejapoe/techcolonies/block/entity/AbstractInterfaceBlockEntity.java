package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.core.properties.InterfaceDirection;
import com.sejapoe.techcolonies.core.properties.ModProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractInterfaceBlockEntity extends AbstractStructureElementBlockEntity {
  private InterfaceDirection interfaceDirection;
  public AbstractInterfaceBlockEntity(BlockEntityType type, BlockPos blockPos, BlockState state) {
    super(type, blockPos, state);
    this.interfaceDirection = state.getValue(ModProperties.INTERFACE_DIRECTION);
  }

  public BlockPos getAccessibilityPos() {
    return getBlockPos().below();
  }

  public boolean isInput() {
    return interfaceDirection == InterfaceDirection.INPUT;
  }

  public void setInterfaceDirection(InterfaceDirection interfaceDirection) {
    this.interfaceDirection = interfaceDirection;
  }

  @Override
  public void load(@NotNull CompoundTag compoundTag) {
    super.load(compoundTag);
    interfaceDirection = compoundTag.getBoolean("IsInput") ? InterfaceDirection.INPUT : InterfaceDirection.OUTPUT;
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag compoundTag) {
    super.saveAdditional(compoundTag);
    compoundTag.putBoolean("IsInput", interfaceDirection.equals(InterfaceDirection.INPUT));
  }
}
