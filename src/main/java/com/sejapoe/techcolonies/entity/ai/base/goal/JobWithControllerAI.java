package com.sejapoe.techcolonies.entity.ai.base.goal;

import com.sejapoe.techcolonies.core.helper.EntityHelper;
import com.sejapoe.techcolonies.entity.ai.base.AIElement;
import com.sejapoe.techcolonies.entity.ai.base.IAIState;
import com.sejapoe.techcolonies.entity.ai.job.base.IJob;


import static com.sejapoe.techcolonies.entity.ai.base.AIStates.IDLE;

public abstract class JobWithControllerAI<J extends IJob> extends AbstractAI<J> {
  private static final double DISTANCE_TO_CONTROLLER = 2.0D;

  public JobWithControllerAI(J job) {
    super(job);
    super.registerTargets(
            new AIElement(IDLE, this::isNotNearController, this::goToController, 40)
    );
  }

  private IAIState goToController() {
    if (isNotNearController()) {
      EntityHelper.moveMobToBlock(worker, worker.getControllerPos());
      return IDLE;
    }
    return null;
  }

  private boolean isNotNearController() {
    return worker.getControllerPos() != null && !worker.getControllerPos().closerToCenterThan(worker.position(), DISTANCE_TO_CONTROLLER);
  }
}
