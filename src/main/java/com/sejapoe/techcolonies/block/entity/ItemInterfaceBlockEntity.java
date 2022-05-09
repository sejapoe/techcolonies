package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.core.ModBlockEntities;
import com.sejapoe.techcolonies.core.ModCapabilities;
import com.sejapoe.techcolonies.core.SavableContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemInterfaceBlockEntity extends AbstractInterfaceBlockEntity {
  private LazyOptional<IItemHandlerModifiable> handler;
  private final SavableContainer inv = new SavableContainer(4);
  public ItemInterfaceBlockEntity(BlockPos blockPos, BlockState state) {
    super(ModBlockEntities.ITEM_INTERFACE_BE.get(), blockPos, state);
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag compoundTag) {
    super.saveAdditional(compoundTag);
    inv.saveAllItems(compoundTag);
  }

  @Override
  public void load(@NotNull CompoundTag compoundTag) {
    super.load(compoundTag);
    inv.loadAllItems(compoundTag);
  }

  @NotNull
  @Override
  public LazyOptional<IItemHandler> getCapability(@NotNull Capability cap, @Nullable Direction side) {
    if (cap == ModCapabilities.DWARF_ITEM_HANDLER_CAPABILITY) {
      if (this.handler == null)
        this.handler = LazyOptional.of(this::createHandler);
      return this.handler.cast();
    }
    return super.getCapability(cap, side);
  }

  private @NotNull IItemHandlerModifiable createHandler() {
    return new InvWrapper(this.inv);
  }
}
