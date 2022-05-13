package com.sejapoe.techcolonies.entity.ai.job.base;

import com.sejapoe.techcolonies.registry.ModBlocks;
import net.minecraft.world.level.block.Block;

public enum DwarfJobTypes implements IDwarfJobType {
  MELTER("melter", ModBlocks.SMELTERY_BLOCK.get()),
  LUMBERJACK("lumberjack");

  private final String name;
  private final Block controllerBlock;

  DwarfJobTypes(String name, Block controllerBlock) {
    this.name = name;
    this.controllerBlock = controllerBlock;
  }

  DwarfJobTypes(String name) {
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
