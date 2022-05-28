package com.sejapoe.techcolonies.entity;

import com.sejapoe.techcolonies.block.entity.ISentDwarfHolder;
import net.minecraft.nbt.CompoundTag;

public class SentDwarf {
  private final CompoundTag compoundTag;
  private int remainedTicks;
  private final ISentDwarfHolder holder;

  public SentDwarf(DwarfEntity entity, int time, ISentDwarfHolder holder) {
    this(new CompoundTag(), time, holder);
    entity.save(this.compoundTag);
  }

  public SentDwarf(CompoundTag compoundTag1, ISentDwarfHolder holder) {
    this(compoundTag1.getCompound("Entity"), compoundTag1.getInt("RemainedTicks"), holder);
  }

  private SentDwarf(CompoundTag tag, int time, ISentDwarfHolder holder) {
    this.compoundTag = tag;
    this.remainedTicks = time;
    this.holder = holder;
  }

  public CompoundTag getCompoundTag() {
    return compoundTag;
  }

  private void extract() {
   this.holder.extractEntity(this);
  }

  public void tick() {
    this.remainedTicks--;
    if (this.remainedTicks <= 0) {
      this.extract();
    }
  }

  public void save(CompoundTag compoundTag) {
    compoundTag.put("Entity", this.compoundTag);
    compoundTag.putInt("RemainedTicks", this.remainedTicks);
  }
}
