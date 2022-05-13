package com.sejapoe.techcolonies.entity.ai.base;

import net.minecraft.util.Mth;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class AIElement {
  private IAIState state;
  private BooleanSupplier condition;
  private Supplier<IAIState> action;
  private int tickRate;
  private int tickOffset;

  public AIElement(IAIState state, BooleanSupplier predicate, Supplier<IAIState> action, int tickRate) {
    this.state = state;
    condition = predicate;
    this.action = action;
    this.tickRate = Mth.clamp(tickRate, 1, 640);
    this.tickOffset = 0;
  }

  public AIElement(IAIState state, Supplier<IAIState> action, int tickRate) {
    this(state, () -> true, action, tickRate);
  }

  public IAIState getState() {
    return state;
  }

  public IAIState getNextState() {
    return action.get();
  }

  public boolean checkCondition() {
    return condition.getAsBoolean();
  }

  public int getTickRate() {
    return tickRate;
  }

  public int getTickOffset() {
    return tickOffset;
  }
}
