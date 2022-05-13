package com.sejapoe.techcolonies.entity.ai.job;

import com.sejapoe.techcolonies.entity.DwarfEntity;
import com.sejapoe.techcolonies.entity.ai.job.base.IJob;

public class JobMelter implements IJob {
  private DwarfEntity worker;

  public JobMelter(DwarfEntity worker) {
    this.worker = worker;
  }

  @Override
  public DwarfEntity getEntity() {
    return worker;
  }
}
