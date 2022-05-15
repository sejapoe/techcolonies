package com.sejapoe.techcolonies.entity.ai.base.goal;

import com.sejapoe.techcolonies.block.entity.AbstractStructureControllerBlockEntity;
import com.sejapoe.techcolonies.block.entity.ItemInterfaceBlockEntity;
import com.sejapoe.techcolonies.core.helper.EntityHelper;
import com.sejapoe.techcolonies.core.helper.ModItemHandlerHelper;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import com.sejapoe.techcolonies.entity.ai.base.IAIState;
import com.sejapoe.techcolonies.recipe.StructureRecipe;
import com.sejapoe.techcolonies.registry.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

import static com.sejapoe.techcolonies.entity.ai.base.AIStates.FILL_INPUT;
import static com.sejapoe.techcolonies.entity.ai.base.AIStates.IDLE;

public interface IItemFillerAI<T extends StructureRecipe<?>> {
  default IAIState insertToInterface() {
    ItemInterfaceBlockEntity interfaceBlockEntity = chooseInterface();
    if (interfaceBlockEntity == null) return IDLE;
    if (isReachedTarget(interfaceBlockEntity.getAccessibilityPos())) {
      IItemHandler workerHandler = getWorkerCapability();
      IItemHandler interfaceHandler = interfaceBlockEntity.getCapability(ModCapabilities.DWARF_ITEM_HANDLER_CAPABILITY).orElse(null);
      ModItemHandlerHelper.moveAllItemStacks(workerHandler, interfaceHandler, stack -> canBePlaced(stack, interfaceHandler));
    } else {
      EntityHelper.moveMobToBlock(getWorker(), interfaceBlockEntity.getBlockPos());
    }
    return FILL_INPUT;
  }
  default ItemInterfaceBlockEntity chooseInterface() {
    return getInterfaces().stream().filter(itemInterfaceBlockEntity -> {
      IItemHandler handler = itemInterfaceBlockEntity.getCapability(ModCapabilities.DWARF_ITEM_HANDLER_CAPABILITY).orElse(null);
      ItemStack stack = ModItemHandlerHelper.getFirstNonEmptyItemStack(getWorkerCapability());
      return stack != ItemHandlerHelper.insertItemStacked(handler, stack, true);
    }).findFirst().orElse(null);
  }
  default boolean canInsertToInterface() {
    return canAct() && chooseInterface() != null;
  }
  default IAIState takeFromInput() {
    BlockEntity input = getInput();
    if (isReachedTarget(input.getBlockPos())) {
      IItemHandler inputHandler = input.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
      IItemHandler workerHandler = getWorkerCapability();
      ModItemHandlerHelper.moveAllItemStacks(inputHandler, workerHandler, stack -> canBePlaced(stack, workerHandler));
    } else {
      EntityHelper.moveMobToBlock(getWorker(), input.getBlockPos());
      return FILL_INPUT;
    }
    return null;
  }
  default boolean canTakeFromInput() {
    return canAct() && getInput().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(iItemHandler -> {
      for (int i = 0; i < iItemHandler.getSlots(); i++) {
        ItemStack stack = iItemHandler.getStackInSlot(i);
        if (canBePlaced(stack, getWorkerCapability())) {
          return true;
        }
      }
      return false;
    }).orElse(false);
  }
  default boolean isReachedTarget(BlockPos pos) {
    return pos.closerToCenterThan(getWorker().position(), 1.5D);
  }

  default boolean isValidItemStack(ItemStack stack) {
    return !stack.isEmpty() && getController().isValidItemInput(stack);
  }

  default boolean canBePlaced(ItemStack stack, IItemHandler itemHandler) {
    return isValidItemStack(stack) && ItemHandlerHelper.insertItemStacked(itemHandler, stack, true) != stack;
  }

  default boolean canAct() {
    return getWorker() != null && getInput() != null && getController() != null && getInterfaces() != null && !getInterfaces().isEmpty();
  }
  default boolean canDo() {
    return canInsertToInterface() || canTakeFromInput();
  }

  default AbstractStructureControllerBlockEntity<T> getController() {
    BlockPos controllerPos = getWorker().getControllerPos();
    if (controllerPos == null) return null;
    BlockEntity blockEntity = getWorker().level.getBlockEntity(controllerPos);
    if (blockEntity instanceof AbstractStructureControllerBlockEntity) {
      return (AbstractStructureControllerBlockEntity<T>) blockEntity;
    }
    return null;
  }

  default BlockEntity getInput() {
    return getWorker().getSerializedInputContainer();
  }

  default List<ItemInterfaceBlockEntity> getInterfaces() {
    return getController().getItemInputInterfaces();
  }

  default IItemHandler getWorkerCapability() {
    return getWorker().getCapability(ModCapabilities.DWARF_ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(null);
  }

  DwarfEntity getWorker();

}
