package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemInterfaceBlockEntity extends AbstractInterfaceBlockEntity implements Container {
  private LazyOptional<IItemHandlerModifiable> handler;
  private NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
  public ItemInterfaceBlockEntity(BlockPos blockPos, BlockState state) {
    super(ModBlockEntities.ITEM_INTERFACE_BE.get(), blockPos, state);
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag compoundTag) {
    super.saveAdditional(compoundTag);
    ContainerHelper.saveAllItems(compoundTag, this.items);
  }

  @Override
  public void load(@NotNull CompoundTag compoundTag) {
    super.load(compoundTag);
    this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    ContainerHelper.loadAllItems(compoundTag, this.items);
  }

  @NotNull
  @Override
  public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      if (this.handler == null)
        this.handler = LazyOptional.of(this::createHandler);
      return this.handler.cast();
    }
    return super.getCapability(cap, side);
  }

  private IItemHandlerModifiable createHandler() {
    return new InvWrapper(this);
  }

  @Override
  public int getContainerSize() {
    return 4;
  }

  @Override
  public boolean isEmpty() {
    return this.items.stream().allMatch(ItemStack::isEmpty);
  }

  public NonNullList<ItemStack> getItems() {
    return items;
  }

  @Override
  public ItemStack getItem(int i) {
    return this.items.get(i);
  }

  @Override
  public ItemStack removeItem(int i, int quantity) {
    ItemStack itemStack = ContainerHelper.removeItem(this.items, i, quantity);
    if (!itemStack.isEmpty()) {
      this.setChanged();
    }
    return itemStack;
  }

  @Override
  public ItemStack removeItemNoUpdate(int i) {
    return ContainerHelper.takeItem(this.items, i);
  }

  @Override
  public void setItem(int i, ItemStack itemStack) {
    this.items.set(i, itemStack);
    if (itemStack.getCount() > this.getMaxStackSize()) {
      itemStack.setCount(this.getMaxStackSize());
    }

    this.setChanged();
  }

  @Override
  public boolean stillValid(Player player) {
    return this.level.getBlockEntity(this.worldPosition) != this;
  }

  @Override
  public void clearContent() {
    this.items.clear();
  }

  public boolean isFull() {
    return this.items.stream().allMatch(itemStack -> !itemStack.isEmpty());
  }
}
