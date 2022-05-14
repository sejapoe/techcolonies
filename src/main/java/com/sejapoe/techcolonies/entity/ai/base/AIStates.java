package com.sejapoe.techcolonies.entity.ai.base;

public enum AIStates implements IAIState {
  // Common
  INIT,
  IDLE,
  START_WORKING,

  // Filler
  FILL_INPUT,
  // Miner
  MINE,
}
