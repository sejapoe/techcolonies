package com.sejapoe.techcolonies.block;

import com.sejapoe.techcolonies.block.entity.ItemInterfaceBlockEntity;
import com.sejapoe.techcolonies.core.properties.InterfaceDirection;
import com.sejapoe.techcolonies.core.properties.ModProperties;
import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ItemInterfaceBlock extends AbstractInterfaceBlock {
  public ItemInterfaceBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.getStateDefinition().any().setValue(ModProperties.PLATING_MATERIAL, PlatingMaterial.NONE).setValue(ModProperties.INTERFACE_DIRECTION, InterfaceDirection.INPUT));
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new ItemInterfaceBlockEntity(pos, state);
  }
}
