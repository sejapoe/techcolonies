package com.sejapoe.techcolonies.entity.ai.goal;

import com.sejapoe.techcolonies.entity.DwarfEntity;
import com.sejapoe.techcolonies.entity.ai.base.AIElement;
import com.sejapoe.techcolonies.entity.ai.goal.base.JobWithControllerAI;
import com.sejapoe.techcolonies.entity.ai.job.JobMelter;

import static com.sejapoe.techcolonies.entity.ai.base.AIStates.*;

public class MelterAI extends JobWithControllerAI<JobMelter> implements IItemFillerAI {
  public MelterAI(JobMelter job) {
    super(job);
    super.registerTargets(
            new AIElement(IDLE, this::canDo, () -> FILL_INPUT, 20),
            new AIElement(FILL_INPUT, this::canInsertToInterface, this::insertToInterface, 20),
            new AIElement(FILL_INPUT, this::canTakeFromInput, this::takeFromInput, 10),
            new AIElement(FILL_INPUT, ()  -> !canDo(), () -> IDLE, 20)
    );
  }

  @Override
  public DwarfEntity getWorker() {
    return worker;
  }
}
