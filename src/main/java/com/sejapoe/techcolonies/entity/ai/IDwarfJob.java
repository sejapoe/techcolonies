package com.sejapoe.techcolonies.entity.ai;

import com.sejapoe.techcolonies.TechColonies;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.Block;

public interface IDwarfJob {
  String getName();
  boolean hasControllerBlock();
  Block getControllerBlock();

  default TranslatableComponent getTranslatableName() {
    return new TranslatableComponent("dwarf_job." + TechColonies.MOD_ID + "." + getName());
  }
}
