package com.sejapoe.techcolonies.entity.ai.goal;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.block.entity.AbstractInterfaceBlockEntity;
import com.sejapoe.techcolonies.block.entity.AbstractStructureControllerBlockEntity;
import com.sejapoe.techcolonies.block.entity.ItemInterfaceBlockEntity;
import com.sejapoe.techcolonies.core.ModCapabilities;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;

public class FillInterfaceGoal extends Goal {
  private final DwarfEntity dwarf;
  private ItemInterfaceBlockEntity interfaceBlockEntity;
  private BlockEntity inputBlockEntity;
  private BlockPos currentTarget = BlockPos.ZERO;
  private BlockPos interfaceAccessibilityBlockPos;
  private int tryTicks;

  public FillInterfaceGoal(DwarfEntity dwarf) {
    this.dwarf = dwarf;
    this.setFlags(EnumSet.of(Flag.MOVE));
  }

  private boolean prepare() {
    if (dwarf.getControllerPos() == null || dwarf.getInputContainerPos() == null) return false;
    BlockEntity controller = dwarf.level.getBlockEntity(dwarf.getControllerPos());
    if (controller != null && controller instanceof AbstractStructureControllerBlockEntity && ((AbstractStructureControllerBlockEntity) controller).isComplete()) {
      List<AbstractInterfaceBlockEntity> interfaces = ((AbstractStructureControllerBlockEntity) controller).getSerializedInterfaces();
      this.interfaceBlockEntity = (ItemInterfaceBlockEntity) interfaces.stream().filter(abstractInterfaceBlockEntity -> abstractInterfaceBlockEntity != null && abstractInterfaceBlockEntity.isInput() && abstractInterfaceBlockEntity instanceof ItemInterfaceBlockEntity).findFirst().orElse(null);
      if (this.interfaceBlockEntity == null) return false;
      this.inputBlockEntity = dwarf.level.getBlockEntity(dwarf.getInputContainerPos());
      if (!(inputBlockEntity instanceof Container)) {
        return false;
      }
      return !dwarf.isEmpty() || !isInputEmpty();
    }
    return false;
  }

  protected void moveMobToBlock(BlockPos blockPos) {
    currentTarget = blockPos;
    dwarf.getNavigation().stop();
    dwarf.getNavigation().moveTo((double)((float)blockPos.getX()) + 0.5D, (double)(blockPos.getY()), (double)((float)blockPos.getZ()) + 0.5D, 1.0f);
    
  }

  protected boolean isValidItemStack(ItemStack stack) {
    return !stack.isEmpty() && stack.is(Tags.Items.INGOTS);
  }

  protected boolean canBePlaced(ItemStack stack, ICapabilityProvider to) {
    IItemHandler itemHandler = to.getCapability(ModCapabilities.DWARF_ITEM_HANDLER_CAPABILITY).orElse(null);
    return isValidItemStack(stack) && ItemHandlerHelper.insertItemStacked(itemHandler, stack, true) != stack;
  }

  protected boolean isInputEmpty() {
    boolean res = inputBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(handler -> {
      for (int i = 0; i < handler.getSlots(); i++) {
        ItemStack itemStack = handler.getStackInSlot(i);
        if (canBePlaced(itemStack, interfaceBlockEntity)) {
          return false;
        }
      }
      return true;
    }).orElse(true);
    return res;
  }

  protected boolean isReachedTarget() {
    return currentTarget.closerToCenterThan(dwarf.position(), currentTarget.equals(interfaceBlockEntity.getAccessibilityPos(dwarf)) ? 1.5D : 1.0D);
  }

  @Override
  public boolean canUse() {
    return prepare();
  }

  @Override
  public void start() {
    super.start();
    this.tryTicks = 0;
    moveMobToBlock(dwarf.isEmpty() ? inputBlockEntity.getBlockPos() : interfaceBlockEntity.getAccessibilityPos(dwarf));
  }

  @Override
  public void stop() {
    super.stop();
    this.dwarf.getNavigation().stop();
  }

  @Override
  public boolean requiresUpdateEveryTick() {
    return true;
  }

  @Override
  public void tick() {
    if (isReachedTarget()) {
      if (currentTarget.equals(interfaceBlockEntity.getAccessibilityPos(dwarf))) {
        moveItemStack(dwarf.getCapability(ModCapabilities.DWARF_ITEM_HANDLER_CAPABILITY), interfaceBlockEntity.getCapability(ModCapabilities.DWARF_ITEM_HANDLER_CAPABILITY), stack -> canBePlaced(stack, interfaceBlockEntity));
        moveMobToBlock(inputBlockEntity.getBlockPos());
      } else {
        moveItemStack(dwarf.getCapability(ModCapabilities.DWARF_ITEM_HANDLER_CAPABILITY), inputBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY), stack -> !canBePlaced(stack, interfaceBlockEntity));
        moveItemStack(inputBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY), dwarf.getCapability(ModCapabilities.DWARF_ITEM_HANDLER_CAPABILITY), stack -> canBePlaced(stack, dwarf) && canBePlaced(stack, interfaceBlockEntity));
        moveMobToBlock(interfaceBlockEntity.getAccessibilityPos(dwarf));
      }
    } else {
      ++this.tryTicks;
      if (this.shouldRecalculatePath()) {
        dwarf.getNavigation().moveTo((double)((float)currentTarget.getX()) + 0.5D, (double)currentTarget.getY(), (double)((float)currentTarget.getZ()) + 0.5D, 1.0D);
      }
    }
  }

  private void moveItemStack(LazyOptional<? extends IItemHandler> from, LazyOptional<? extends IItemHandler> to, Function<ItemStack, Boolean> validator) {
    ItemStack stack = from.map(handler -> {
      for (int i = 0; i < handler.getSlots(); i++) {
        ItemStack itemStack = handler.getStackInSlot(i);
        if (validator.apply(itemStack)) {
          return handler.extractItem(i, itemStack.getCount(), false);
        }
      }
      return ItemStack.EMPTY;
    }).orElse(ItemStack.EMPTY);
    IItemHandler handler = to.orElse(null);
    ItemStack remainedStack = ItemHandlerHelper.insertItemStacked(handler, stack, false);
    if (!remainedStack.isEmpty()) {
      ItemHandlerHelper.insertItemStacked(from.orElse(null), remainedStack, false);
    }
  }

  private boolean shouldRecalculatePath() {
    return this.tryTicks % 40 == 0;
  }
}
