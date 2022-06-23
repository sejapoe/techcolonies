package com.sejapoe.techcolonies.entity.ai.job.base;

import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraft.nbt.CompoundTag;

public interface IJob {
  DwarfEntity getEntity();

  default void save(CompoundTag compoundTag) {};
  default void load(CompoundTag compoundTag) {};
}
