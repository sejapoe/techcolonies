package com.sejapoe.techcolonies.entity.ai.base;

public enum AIStates implements IAIState {
  // Common
  INIT,
  IDLE,
  START_WORKING,

  // Filler
  FILL_INPUT,
  // Portal,
  GOING_TO_PORTAL,
  IN_PORTAL,

  // Miner
  MINE,
  CREATE_MINE, MINE_RESET,
}
