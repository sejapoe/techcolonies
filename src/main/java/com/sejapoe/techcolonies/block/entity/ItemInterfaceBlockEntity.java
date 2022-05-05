package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ItemInterfaceBlockEntity extends AbstractStructureElementBlockEntity {
  public ItemInterfaceBlockEntity(BlockPos blockPos, BlockState state) {
    super(ModBlockEntities.ITEM_INTERFACE_BE.get(), blockPos, state);
  }
}
