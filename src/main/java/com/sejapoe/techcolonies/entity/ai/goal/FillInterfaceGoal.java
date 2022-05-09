package com.sejapoe.techcolonies.entity.ai.goal;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.block.entity.AbstractInterfaceBlockEntity;
import com.sejapoe.techcolonies.block.entity.AbstractStructureControllerBlockEntity;
import com.sejapoe.techcolonies.block.entity.ItemInterfaceBlockEntity;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public class FillInterfaceGoal extends Goal {
  private final DwarfEntity dwarf;
  private ItemInterfaceBlockEntity interfaceBlockEntity;
  private BlockEntity inputBlockEntity;
  private BlockPos currentTarget = BlockPos.ZERO;
  private BlockPos interfaceAccessibilityBlockPos;
  private int tryTicks;

  public FillInterfaceGoal(DwarfEntity dwarf) {
    this.dwarf = dwarf;
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
      return (!dwarf.isEmpty() || !isInputEmpty()) && !this.interfaceBlockEntity.isFull();
    }
    return false;
  }

  protected void moveMobToBlock(BlockPos blockPos) {
    currentTarget = blockPos;
    dwarf.getNavigation().stop();
    dwarf.getNavigation().moveTo((double)((float)blockPos.getX()) + 0.5D, (double)(blockPos.getY()), (double)((float)blockPos.getZ()) + 0.5D, 1.0f);
    
  }

  protected boolean isInputEmpty() {
    return ((Container) inputBlockEntity).isEmpty();
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
  }

  @Override
  public boolean requiresUpdateEveryTick() {
    return true;
  }

  @Override
  public void tick() {
    if (isReachedTarget()) {
      if (currentTarget.equals(interfaceBlockEntity.getAccessibilityPos(dwarf))) {
          ItemStack stack = dwarf.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(handler -> {
          for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack itemStack = handler.getStackInSlot(i);
            if (!itemStack.isEmpty()) {// TODO: check controller recipes
              return handler.extractItem(i, itemStack.getCount(), false);
            }
          }
          return ItemStack.EMPTY;
        }).orElse(ItemStack.EMPTY);
        IItemHandler handler = interfaceBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        for (int i = 0; i < handler.getSlots(); i++) {
          ItemStack itemStack = handler.getStackInSlot(i);
          if (itemStack.isEmpty()) {
            handler.insertItem(i, stack, false);
            break;
          }
        }
        if (!isInputEmpty()) {
          moveMobToBlock(inputBlockEntity.getBlockPos());
        }
      } else {
          ItemStack stack = inputBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(handler -> {
          for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack itemStack = handler.getStackInSlot(i);
            if (!itemStack.isEmpty()) {// TODO: check controller recipes
              return handler.extractItem(i, itemStack.getCount(), false);
            }
          }
          return ItemStack.EMPTY;
        }).orElse(ItemStack.EMPTY);
        IItemHandler handler = dwarf.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        for (int i = 0; i < handler.getSlots(); i++) {
          ItemStack itemStack = handler.getStackInSlot(i);
          if (itemStack.isEmpty()) {
            handler.insertItem(i, stack, false);
            break;
          }
        }
        moveMobToBlock(interfaceBlockEntity.getAccessibilityPos(dwarf));
      }
    } else {
      ++this.tryTicks;
      if (this.shouldRecalculatePath()) {
        dwarf.getNavigation().moveTo((double)((float)currentTarget.getX()) + 0.5D, (double)currentTarget.getY(), (double)((float)currentTarget.getZ()) + 0.5D, 1.0D);
      }
    }
  }

  private boolean shouldRecalculatePath() {
    return this.tryTicks % 40 == 0;
  }
}
