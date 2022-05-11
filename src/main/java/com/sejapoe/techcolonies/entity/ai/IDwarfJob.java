package com.sejapoe.techcolonies.entity.ai;

import com.sejapoe.techcolonies.TechColonies;
import net.minecraft.network.chat.TranslatableComponent;

public interface IDwarfJob {
  String getName();

  default TranslatableComponent getTranslatableName() {
    return new TranslatableComponent("dwarf_job." + TechColonies.MOD_ID + "." + getName());
  }
}
