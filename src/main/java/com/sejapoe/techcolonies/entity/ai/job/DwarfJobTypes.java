package com.sejapoe.techcolonies.entity.ai.job;

import com.sejapoe.techcolonies.entity.DwarfEntity;
import com.sejapoe.techcolonies.entity.ai.job.base.IDwarfJobType;
import com.sejapoe.techcolonies.entity.ai.job.base.IJob;
import com.sejapoe.techcolonies.entity.ai.job.melter.MelterAI;
import com.sejapoe.techcolonies.entity.ai.job.miner.MinerAI;
import com.sejapoe.techcolonies.entity.ai.base.goal.AbstractAI;
import com.sejapoe.techcolonies.entity.ai.job.melter.JobMelter;
import com.sejapoe.techcolonies.entity.ai.job.miner.JobMiner;
import com.sejapoe.techcolonies.registry.ModBlocks;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public enum DwarfJobTypes implements IDwarfJobType {
  MELTER("melter", worker -> new MelterAI(new JobMelter(worker)), ModBlocks.SMELTERY_BLOCK.get()),
//  LUMBERJACK("lumberjack"),
  MINER("miner", worker -> new MinerAI(new JobMiner(worker)));

  private final String name;
  private final Function<DwarfEntity, AbstractAI<? extends IJob>> aiFunction;
  private final Block controllerBlock;

  DwarfJobTypes(String name, Function<DwarfEntity, AbstractAI<? extends IJob>> aiFunction, Block controllerBlock) {
    this.name = name;
    this.aiFunction = aiFunction;
    this.controllerBlock = controllerBlock;
  }

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
