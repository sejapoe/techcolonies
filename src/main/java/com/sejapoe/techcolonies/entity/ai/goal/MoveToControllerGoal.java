package com.sejapoe.techcolonies.entity.ai.goal;

import com.sejapoe.techcolonies.block.entity.AbstractStructureControllerBlockEntity;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;

import java.util.EnumSet;

public class MoveToControllerGoal extends MoveToBlockGoal {
  private final DwarfEntity dwarf;

  public MoveToControllerGoal(DwarfEntity dwarf) {
    super(dwarf, 1.0f, 128);
    this.dwarf = dwarf;
    this.setFlags(EnumSet.of(Flag.MOVE));
  }


  @Override
  protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
    return levelReader.getBlockEntity(blockPos) instanceof AbstractStructureControllerBlockEntity;
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
