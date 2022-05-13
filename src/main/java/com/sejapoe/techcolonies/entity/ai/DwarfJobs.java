package com.sejapoe.techcolonies.entity.ai;

import com.sejapoe.techcolonies.registry.ModBlocks;
import net.minecraft.world.level.block.Block;

public enum DwarfJobs implements IDwarfJob {
  MELTER("melter", ModBlocks.SMELTERY_BLOCK.get()),
  LUMBERJACK("lumberjack");

  private String name;
  private Block controllerBlock;

  DwarfJobs(String name, Block controllerBlock) {
    this.name = name;
    this.controllerBlock = controllerBlock;
  }

  DwarfJobs(String name) {
    this(name, null);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean hasControllerBlock() {
    return this.controllerBlock != null;
  }

  @Override
  public Block getControllerBlock() {
    return this.controllerBlock;
  }


}
