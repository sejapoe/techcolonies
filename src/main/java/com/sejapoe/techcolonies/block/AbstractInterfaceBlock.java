package com.sejapoe.techcolonies.block;

import com.sejapoe.techcolonies.core.properties.InterfaceDirection;
import com.sejapoe.techcolonies.core.properties.ModProperties;
import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractInterfaceBlock extends BaseEntityBlock {

  public AbstractInterfaceBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.getStateDefinition().any().setValue(ModProperties.PLATING_MATERIAL, PlatingMaterial.NONE).setValue(ModProperties.INTERFACE_DIRECTION, InterfaceDirection.INPUT));
  }

  @Override
  public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(ModProperties.PLATING_MATERIAL, ModProperties.INTERFACE_DIRECTION);
  }
}
