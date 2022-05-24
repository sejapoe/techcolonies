package com.sejapoe.techcolonies.entity.ai.job.miner;

import com.sejapoe.techcolonies.entity.ai.base.AIElement;
import com.sejapoe.techcolonies.entity.ai.base.IAIState;
import com.sejapoe.techcolonies.entity.ai.base.goal.AbstractBreakerAI;
import net.minecraft.core.BlockPos;

import java.util.List;

import static com.sejapoe.techcolonies.entity.ai.base.AIStates.*;

public class MinerAI extends AbstractBreakerAI<JobMiner> {
  public MinerAI(JobMiner job) {
    super(job);
    super.registerTargets(
            new AIElement(INIT, () -> IDLE, 10), // Safe check?
            new AIElement(IDLE, () -> MINE, 20),
            new AIElement(MINE, () -> MINE, 5)
    );
  }
}
