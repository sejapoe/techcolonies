package com.sejapoe.techcolonies.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class AbstractStructureControllerBlockEntity extends AbstractStructureElementBlockEntity{
  private List<BlockPos> interfaces;

  public AbstractStructureControllerBlockEntity(BlockEntityType type, BlockPos blockPos, BlockState state) {
    super(type, blockPos, state);
    this.interfaces = new ArrayList<>();
  }

  @Override
  public void load(@NotNull CompoundTag compoundTag) {
    super.load(compoundTag);
    this.interfaces = new ArrayList<>();
    CompoundTag interfacesCompoundTag = compoundTag.getCompound("Interfaces");
    for (String key : interfacesCompoundTag.getAllKeys()) {
      interfaces.add(NbtUtils.readBlockPos(interfacesCompoundTag.getCompound(key)));
    }
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag compoundTag) {
    super.saveAdditional(compoundTag);
    CompoundTag interfacesCompoundTag = new CompoundTag();
    AtomicInteger i = new AtomicInteger();
    this.interfaces.forEach(pos -> {
      interfacesCompoundTag.put("Pos" + i.getAndIncrement(), NbtUtils.writeBlockPos(pos));
    });
    compoundTag.put("Interfaces", interfacesCompoundTag);
  }

  @Override
  public void updateStatus(boolean isComplete, int structureLevel) {
    super.updateStatus(isComplete, structureLevel);
  }

  public void updateInterfaces() {
    for (BlockPos pos : this.getInterfaces()) {
      BlockEntity interfaceEntity = level.getBlockEntity(pos);
      if (interfaceEntity instanceof AbstractStructureElementBlockEntity) {
        ((AbstractStructureElementBlockEntity) interfaceEntity).updateStatus(isComplete(), getStructureLevel());
      }
    }
  }

  public void setInterfaces(List<BlockPos> interfaces) {
    this.interfaces = interfaces;
  }

  public List<BlockPos> getInterfaces() {
    return interfaces;
  }

  public List<AbstractInterfaceBlockEntity> getSerializedInterfaces() {
    return interfaces.stream().map(blockPos -> (AbstractInterfaceBlockEntity) level.getBlockEntity(blockPos)).collect(Collectors.toList());
  }
}
