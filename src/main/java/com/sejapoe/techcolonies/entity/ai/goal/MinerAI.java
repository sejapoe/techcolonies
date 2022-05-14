package com.sejapoe.techcolonies.entity.ai.goal;

import com.sejapoe.techcolonies.entity.ai.base.AIElement;
import com.sejapoe.techcolonies.entity.ai.base.IAIState;
import com.sejapoe.techcolonies.entity.ai.goal.base.AbstractBreakerAI;
import com.sejapoe.techcolonies.entity.ai.job.JobMiner;
import net.minecraft.core.BlockPos;

import static com.sejapoe.techcolonies.entity.ai.base.AIStates.*;

public class MinerAI extends AbstractBreakerAI<JobMiner> {
  public MinerAI(JobMiner job) {
    super(job);
    super.registerTargets(
            new AIElement(INIT, () -> IDLE, 10), // Safe check?
            new AIElement(IDLE, () -> MINE, 20),
            new AIElement(MINE, this::mineBelow, 5)
    );
  }

  private IAIState mineBelow() {
    BlockPos pos = worker.getOnPos();
    breakBlock(pos);
    return getState();
  }
}
