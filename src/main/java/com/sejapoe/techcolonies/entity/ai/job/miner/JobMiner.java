package com.sejapoe.techcolonies.entity.ai.job.miner;

import com.sejapoe.techcolonies.entity.DwarfEntity;
import com.sejapoe.techcolonies.entity.ai.job.base.IJob;

public class JobMiner implements IJob {
  private final DwarfEntity worker;

  public JobMiner(DwarfEntity worker) {
    this.worker = worker;
  }

  @Override
  public DwarfEntity getEntity() {
    return worker;
  }
}
