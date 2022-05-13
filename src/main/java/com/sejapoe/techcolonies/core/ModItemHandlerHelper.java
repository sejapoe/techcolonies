package com.sejapoe.techcolonies.core;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModItemHandlerHelper {
  public static ItemStack getFirstNonEmptyItemStack(IItemHandler handler) {
    for (int i = 0; i < handler.getSlots(); i++) {
      ItemStack stack = handler.getStackInSlot(i);
      if (!stack.isEmpty()) {
        return stack;
      }
    }
    return ItemStack.EMPTY;
  }

  public static List<ItemStack> moveAllItemStacks(IItemHandler from, IItemHandler to, Function<ItemStack, Boolean> validator) {
    List<ItemStack> remainedStacks = new ArrayList<>();
    for (int i = 0; i < from.getSlots(); i++) {
      ItemStack stack = from.getStackInSlot(i);
      if (validator.apply(stack)) {
        ItemStack extractedStack = from.extractItem(i, stack.getCount(), false);
        ItemStack remainedStack = ItemHandlerHelper.insertItemStacked(to, extractedStack, false);
        remainedStacks.add(from.insertItem(i, remainedStack, false));
      }
    }
    return remainedStacks;
  }
}
