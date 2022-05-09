package com.sejapoe.techcolonies.block;

import com.sejapoe.techcolonies.block.entity.ItemInterfaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemInterfaceBlock extends AbstractInterfaceBlock {
  public ItemInterfaceBlock(Properties properties) {
    super(properties);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return new ItemInterfaceBlockEntity(pos, state);
  }
}
