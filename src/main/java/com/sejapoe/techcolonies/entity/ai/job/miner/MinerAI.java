package com.sejapoe.techcolonies.entity.ai.job.miner;

import com.sejapoe.techcolonies.core.helper.EntityHelper;
import com.sejapoe.techcolonies.entity.ai.base.AIElement;
import com.sejapoe.techcolonies.entity.ai.base.IAIState;
import com.sejapoe.techcolonies.entity.ai.base.goal.AbstractBreakerAI;
import net.minecraft.core.BlockPos;

import static com.sejapoe.techcolonies.entity.ai.base.AIStates.*;

public class MinerAI extends AbstractBreakerAI<JobMiner> {
  public MinerAI(JobMiner job) {
    super(job);
    super.registerTargets(
            new AIElement(INIT, () -> IDLE, 10), // Safe check?
            new AIElement(IDLE, () -> MINE, 20),
            new AIElement(MINE, this::mineEast, 5)
    );
  }

  private IAIState mineEast() {
    BlockPos pos = worker.getOnPos();
    BlockPos pos1 = pos.above().east();
    BlockPos pos2 = pos1.above();
    if (level.getBlockState(pos1).isAir() && level.getBlockState(pos2).isAir()) {
      EntityHelper.moveMobToBlock(worker, pos.below().east(2));
    } else {
      worker.getNavigation().stop();
      breakBlock(level.getBlockState(pos1).isAir() ? pos2 : pos1);
    }
    return getState();
  }
}
