package com.sejapoe.techcolonies.entity.ai.job.miner;

import com.sejapoe.techcolonies.entity.DwarfEntity;
import com.sejapoe.techcolonies.entity.ai.job.base.IJob;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JobMiner implements IJob {

  private NonNullList<ItemStack> virtualMineResults = NonNullList.create();
  private final DwarfEntity worker;

  public JobMiner(DwarfEntity worker) {
    this.worker = worker;
  }

  @Override
  public DwarfEntity getEntity() {
    return worker;
  }

  @Override
  public void save(CompoundTag compoundTag) {
    ContainerHelper.saveAllItems(compoundTag, virtualMineResults);
  }

  @Override
  public void load(CompoundTag compoundTag) {
    ContainerHelper.loadAllItems(compoundTag, virtualMineResults);
  }

  public NonNullList<ItemStack> getVirtualMineResults() {
    return virtualMineResults;
  }

  public void setVirtualMineResults(NonNullList<ItemStack> virtualMineResults) {
    this.virtualMineResults = virtualMineResults;
  }
}
