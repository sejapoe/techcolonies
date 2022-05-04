package com.sejapoe.techcolonies.blocks;

import com.sejapoe.techcolonies.core.properties.ModProperties;
import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class SmelteryBlock extends Block {
  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
  public static final BooleanProperty LIT = BlockStateProperties.LIT;
  public SmelteryBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.getStateDefinition().any()
            .setValue(FACING, Direction.NORTH)
            .setValue(LIT, false)
            .setValue(ModProperties.PLATING_MATERIAL, PlatingMaterial.NONE));
  }



  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
    return this.defaultBlockState().setValue(FACING, placeContext.getHorizontalDirection().getOpposite());
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, LIT, ModProperties.PLATING_MATERIAL);
  }

}
