package com.sejapoe.techcolonies.item;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.block.AbstractInterfaceBlock;
import com.sejapoe.techcolonies.block.ItemInterfaceBlock;
import com.sejapoe.techcolonies.block.entity.AbstractStructureControllerBlockEntity;
import com.sejapoe.techcolonies.core.properties.ModProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class StrangeWandItem extends Item {
  public StrangeWandItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext useOnContext) {
    Level level = useOnContext.getLevel();
    BlockPos pos = useOnContext.getClickedPos();
    BlockState state = level.getBlockState(pos);
    BlockEntity blockEntity = level.getBlockEntity(pos);
    if (blockEntity != null && blockEntity instanceof AbstractStructureControllerBlockEntity) {
      TechColonies.LOGGER.info(((AbstractStructureControllerBlockEntity) blockEntity).getInterfaces().toString());
    }
    if (state.getBlock() instanceof AbstractInterfaceBlock) {
      if (level.isClientSide()) return InteractionResult.SUCCESS;
      state = state.setValue(ModProperties.INTERFACE_DIRECTION, state.getValue(ModProperties.INTERFACE_DIRECTION).getOpposite());
      level.setBlockAndUpdate(pos, state);
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.PASS;
  }
}
