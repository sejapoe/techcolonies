package com.sejapoe.techcolonies.entity.ai;

public enum DwarfJobs implements IDwarfJob {
  MELTER("melter");

  private String name;

  DwarfJobs(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }
}
