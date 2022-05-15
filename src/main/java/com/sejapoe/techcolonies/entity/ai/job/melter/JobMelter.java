package com.sejapoe.techcolonies.entity.ai.job.melter;

import com.sejapoe.techcolonies.entity.DwarfEntity;
import com.sejapoe.techcolonies.entity.ai.job.base.IJob;

public class JobMelter implements IJob {
  private final DwarfEntity worker;

  public JobMelter(DwarfEntity worker) {
    this.worker = worker;
  }

  @Override
  public DwarfEntity getEntity() {
    return worker;
  }
}
