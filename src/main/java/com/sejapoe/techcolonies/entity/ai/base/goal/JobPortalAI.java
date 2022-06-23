package com.sejapoe.techcolonies.entity.ai.base.goal;

import com.sejapoe.techcolonies.block.entity.PortalControllerBlockEntity;
import com.sejapoe.techcolonies.core.helper.EntityHelper;
import com.sejapoe.techcolonies.entity.ai.base.AIElement;
import com.sejapoe.techcolonies.entity.ai.base.IAIState;
import com.sejapoe.techcolonies.entity.ai.job.base.IJob;
import net.minecraft.world.level.block.entity.BlockEntity;

import static com.sejapoe.techcolonies.entity.ai.base.AIStates.*;

public abstract class JobPortalAI<T extends IJob> extends AbstractAI<T> {
  private static final double DISTANCE_TO_PORTAL = 1.5D;

  public JobPortalAI(T job) {
    super(job);
    super.registerTargets(
            new AIElement(GOING_TO_PORTAL, this::isNotNearPortal, this::goToPortal, 40),
            new AIElement(GOING_TO_PORTAL, () -> !this.isNotNearPortal(), () -> IN_PORTAL, 40),
            new AIElement(IN_PORTAL, this::isReady, this::enterToPortal, 20)
    );
  }
  protected abstract boolean isReady();
  protected abstract int getInsertionTime();

  protected IAIState enterToPortal() {
    this.getPortal().insertEntity(worker, getInsertionTime());
    return IN_PORTAL;
  }


  private IAIState goToPortal() {
    if (isNotNearPortal()) {
      EntityHelper.moveMobToBlock(worker, worker.getControllerPos());
      return GOING_TO_PORTAL;
    }
    return IN_PORTAL;
  }

  private boolean isNotNearPortal() {
    return worker.getControllerPos() != null && !worker.getControllerPos().closerToCenterThan(worker.position(), DISTANCE_TO_PORTAL);
  }

  protected PortalControllerBlockEntity getPortal() {
    assert worker.getControllerPos() != null;
    BlockEntity blockEntity = this.level.getBlockEntity(worker.getControllerPos());
    assert blockEntity instanceof PortalControllerBlockEntity;
    return (PortalControllerBlockEntity) blockEntity;
  }
}
