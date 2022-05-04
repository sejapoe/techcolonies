package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.ModBlockEntities;
import com.sejapoe.techcolonies.core.structures.Structures;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;

public class SmelteryBlockEntity extends BlockEntity {

  public SmelteryBlockEntity(BlockPos blockPos, BlockState state) {
    super(ModBlockEntities.SMELTERY_BE.get(), blockPos, state);
  }

  public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T blockEntity) {
    // Do stuff
    checkStructure(level, blockPos);//blockPos.offset(blockState.getValue(HorizontalDirectionalBlock.FACING).getOpposite().getNormal()));
  }

  private static boolean checkStructure(Level level, BlockPos pos) {
    BlockPattern.BlockPatternMatch match = Structures.SMELTERY_PATTERN.find(level, pos);

    return match != null;
  }
}
