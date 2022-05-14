package com.sejapoe.techcolonies.entity.ai.job.base;

import com.sejapoe.techcolonies.entity.DwarfEntity;
import com.sejapoe.techcolonies.entity.ai.goal.MelterAI;
import com.sejapoe.techcolonies.entity.ai.goal.MinerAI;
import com.sejapoe.techcolonies.entity.ai.goal.base.AbstractAI;
import com.sejapoe.techcolonies.entity.ai.job.JobMelter;
import com.sejapoe.techcolonies.entity.ai.job.JobMiner;
import com.sejapoe.techcolonies.registry.ModBlocks;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public enum DwarfJobTypes implements IDwarfJobType {
  MELTER("melter", worker -> new MelterAI(new JobMelter(worker)), ModBlocks.SMELTERY_BLOCK.get()),
//  LUMBERJACK("lumberjack"),
  MINER("miner", worker -> new MinerAI(new JobMiner(worker)));

  private final String name;
  private Function<DwarfEntity, AbstractAI<? extends IJob>> aiFunction;
  private final Block controllerBlock;

  DwarfJobTypes(String name, Function<DwarfEntity, AbstractAI<? extends IJob>> aiFunction, Block controllerBlock) {
    this.name = name;
    this.aiFunction = aiFunction;
    this.controllerBlock = controllerBlock;
  }

//  DwarfJobTypes(String name) {
//    this(name);
//  }

  <T extends IJob> DwarfJobTypes(String name, Function<DwarfEntity, AbstractAI<? extends IJob>> aiFunction) {
    this(name, aiFunction, null);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean hasControllerBlock() {
    return this.controllerBlock != null;
  }

  @Override
  public Block getControllerBlock() {
    return this.controllerBlock;
  }

  @Override
  public AbstractAI<? extends IJob> createGoal(DwarfEntity worker) {
    return aiFunction.apply(worker);
  }

}
