package com.sejapoe.techcolonies.block;

import com.sejapoe.techcolonies.block.entity.FluidInterfaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class FluidInterfaceBlock extends AbstractInterfaceBlock {
  public FluidInterfaceBlock(Properties properties) {
    super(properties);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new FluidInterfaceBlockEntity(blockPos, blockState);
  }
}
