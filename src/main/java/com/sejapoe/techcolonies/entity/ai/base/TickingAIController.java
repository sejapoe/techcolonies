package com.sejapoe.techcolonies.entity.ai.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TickingAIController {
  private final HashMap<IAIState, List<AIElement>> elements;
  private List<AIElement> currentElements;
  private int tickCounter = 0;

  private IAIState state;
  private final IAIState initState;

  public TickingAIController(IAIState initialState) {
    this.initState = initialState;
    this.state = initialState;
    this.elements = new HashMap<>();
    this.currentElements = new ArrayList<>();
    elements.put(initialState, currentElements);
  }

  public void tick() {
    tickCounter++;
    if (tickCounter > 640) {
      tickCounter = 1;
    }

    for (AIElement element : currentElements) {
      if (checkElement(element)) {
        return;
      }
    }
  }

  public boolean checkElement(AIElement element) {
    if ((tickCounter % element.getTickRate()) != element.getTickOffset()) return false;
    if (!element.checkCondition()) return false;
    return performElement(element);
  }

  private boolean performElement(AIElement element) {
    IAIState newState = element.getNextState();
    if (newState != null) {
      if (this.state != newState) {
        this.currentElements = elements.get(newState);
      }
      this.state = newState;
      return true;
    }
    return false;
  }

  public void add(AIElement element) {
    this.elements.computeIfAbsent(element.getState(), k -> new ArrayList<>()).add(element);
  }

  public IAIState getState() {
    return this.state;
  }

  public void reset() {
    state = initState;
    currentElements = elements.get(state);
  }
}
