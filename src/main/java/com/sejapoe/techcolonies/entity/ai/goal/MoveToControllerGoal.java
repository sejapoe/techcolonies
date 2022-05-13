package com.sejapoe.techcolonies.entity.ai.goal;

import com.sejapoe.techcolonies.block.entity.AbstractStructureControllerBlockEntity;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class MoveToControllerGoal extends MoveToBlockGoal {
  private final DwarfEntity dwarf;
  private Block controllerBlock;

  public MoveToControllerGoal(DwarfEntity dwarf, Block controllerBlock) {
    super(dwarf, 1.0f, 128);
    this.dwarf = dwarf;
    this.controllerBlock = controllerBlock;
    this.setFlags(EnumSet.of(Flag.MOVE));
  }

  @Override
  protected int nextStartTick(@NotNull PathfinderMob p_25618_) {
    return 0;
  }

  @Override
  protected boolean isValidTarget(LevelReader levelReader, @NotNull BlockPos blockPos) {
    return levelReader.getBlockState(blockPos).getBlock().equals(this.controllerBlock) &&
            levelReader.getBlockEntity(blockPos) instanceof AbstractStructureControllerBlockEntity;
  }

  @Override
  protected boolean findNearestBlock() {
    if (dwarf.getControllerPos() != null) {
      this.blockPos = dwarf.getControllerPos();
      return true;
    }
    this.blockPos = BlockPos.ZERO;
    return false;
  }
}
