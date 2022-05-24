package com.sejapoe.techcolonies.entity.ai.job.base;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import com.sejapoe.techcolonies.entity.ai.base.goal.AbstractAI;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.Block;

public interface IDwarfJobType {
  String getName();
  boolean hasControllerBlock();
  Block getControllerBlock();

  default TranslatableComponent getTranslatableName() {
    return new TranslatableComponent("dwarf_job." + TechColonies.MOD_ID + "." + getName());
  }

  AbstractAI<? extends IJob> createGoal(DwarfEntity worker);

  IJob createJob(DwarfEntity worker);
}
