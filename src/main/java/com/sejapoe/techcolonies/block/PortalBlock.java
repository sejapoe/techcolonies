package com.sejapoe.techcolonies.block;

import com.sejapoe.techcolonies.block.entity.PortalBlockEntity;
import com.sejapoe.techcolonies.core.properties.ModProperties;
import com.sejapoe.techcolonies.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PortalBlock extends BaseEntityBlock {
  public PortalBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    pBuilder.add(ModProperties.PLATING_MATERIAL);
  }

  @Override
  public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
    return RenderShape.MODEL;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
    return new PortalBlockEntity(pPos, pState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
    return createTickerHelper(pBlockEntityType, ModBlockEntities.PORTAL_BE.get(), PortalBlockEntity::tick);
  }
}
