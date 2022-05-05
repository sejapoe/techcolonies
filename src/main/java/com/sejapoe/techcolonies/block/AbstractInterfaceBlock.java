package com.sejapoe.techcolonies.block;

import com.sejapoe.techcolonies.core.properties.ModProperties;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public abstract class AbstractInterfaceBlock extends BaseEntityBlock {

  public AbstractInterfaceBlock(Properties properties) {
    super(properties);
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(ModProperties.PLATING_MATERIAL, ModProperties.INTERFACE_DIRECTION);
  }
}
