package com.sejapoe.techcolonies.entity.ai.goal.base;

import com.sejapoe.techcolonies.entity.DwarfEntity;
import com.sejapoe.techcolonies.entity.ai.base.AIElement;
import com.sejapoe.techcolonies.entity.ai.base.IAIState;
import com.sejapoe.techcolonies.entity.ai.base.TickingAIController;
import com.sejapoe.techcolonies.entity.ai.job.base.IJob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.EnumSet;

import static com.sejapoe.techcolonies.entity.ai.base.AIStates.*;

public abstract class AbstractAI<J extends IJob> extends Goal {
  protected final J job;
  protected final Level level;
  protected final TickingAIController controller;
  protected final DwarfEntity worker;

  public AbstractAI(J job) {
    super();
    this.setFlags(EnumSet.of(Flag.MOVE));
    this.job = job;
    this.worker = this.job.getEntity();
    this.level = worker.getLevel();
    this.controller = new TickingAIController(INIT);
    registerTargets(new AIElement(INIT, this::getState, 1));
  }

  public final IAIState getState() {
    return this.controller.getState() == INIT ? IDLE : this.controller.getState(); // TODO: need i any checks in init?
  }

  public void registerTarget(AIElement element) {
    this.controller.add(element);
  }

  public void registerTargets(AIElement... elements) {
    Arrays.stream(elements).forEach(this::registerTarget);
  }

  @Override
  public boolean requiresUpdateEveryTick() {
    return true;
  }

  @Override
  public void tick() {
    controller.tick();
  }

  @Override
  public boolean canUse() {
    return true;
  }

  @Override
  public void stop() {
    controller.reset();
    super.stop();
  }
}
