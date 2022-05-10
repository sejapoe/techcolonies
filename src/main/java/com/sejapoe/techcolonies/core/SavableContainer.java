package com.sejapoe.techcolonies.core;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SavableContainer extends SimpleContainer {
  public SavableContainer(int i) {
    super(i);
  }


  public void loadAllItems(CompoundTag compoundTag) {
    NonNullList<ItemStack> items = getEmptyItemlist();
    ContainerHelper.loadAllItems(compoundTag, items);
    for (int i = 0; i < getContainerSize(); i++) {
      this.setItem(i, items.get(i));
    }
  }

  public void saveAllItems(CompoundTag compoundTag) {
    ContainerHelper.saveAllItems(compoundTag, this.getItems());
  }

  @NotNull
  private NonNullList<ItemStack> getEmptyItemlist() {
    return NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
  }

  public NonNullList<ItemStack> getItems() {
    NonNullList<ItemStack> items = getEmptyItemlist();
    for (int i = 0; i < getContainerSize(); i++) {
      items.set(i, this.getItem(i));
    }
    return items;
  }
}
