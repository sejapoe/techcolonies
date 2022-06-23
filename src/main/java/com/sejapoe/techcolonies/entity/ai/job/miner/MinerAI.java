package com.sejapoe.techcolonies.entity.ai.job.miner;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.entity.ai.base.AIElement;
import com.sejapoe.techcolonies.entity.ai.base.IAIState;
import com.sejapoe.techcolonies.entity.ai.base.goal.JobPortalAI;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.sejapoe.techcolonies.entity.ai.base.AIStates.*;

public class MinerAI extends JobPortalAI<JobMiner> {
  private VirtualMine virtualMine;
  private int insertionTime;
  public MinerAI(JobMiner job) {
    super(job);
    super.registerTargets(
            new AIElement(INIT, () -> IDLE, 10), // Safe check?
            new AIElement(IDLE, this::hasItemsToGive, this::giveItems, 10),
            new AIElement(IDLE, this::canCreateMine, () -> CREATE_MINE, 200),
            new AIElement(CREATE_MINE, this::createMine, 5)
    );
    // Go to portal
    // Create virtual mine
    // Insert into portal for mining time
    // Wait...
    // Extract and collect mined resources

  }

  private boolean hasItemsToGive() {
    return !job.getVirtualMineResults().isEmpty();
  }

  private void reset() {
    this.virtualMine = null;
  }

  private IAIState giveItems() {
    List<ItemStack> itemStacks = this.job.getVirtualMineResults();
    TechColonies.LOGGER.info("Result size: " + itemStacks.size());
    return IDLE;
  }

  private IAIState createMine() {
    this.virtualMine = new VirtualMine((ServerLevel) level, 1, 20, 25, worker.getMainHandItem());
    return GOING_TO_PORTAL;
  }

  private boolean canCreateMine() {
    return true;
  }

  @Override
  protected boolean isReady() {
    return this.virtualMine != null && this.virtualMine.isVirtualLevelCreated();
  }

  @Override
  protected int getInsertionTime() {
    return this.insertionTime;
  }

  @Override
  protected IAIState enterToPortal() {
    List<ItemStack> itemStacks = this.virtualMine.calculateResult((ServerLevel) level, this.worker.getMainHandItem());
    NonNullList<ItemStack> itemStacks1 = NonNullList.create();
    itemStacks1.addAll(itemStacks);
    this.job.setVirtualMineResults(itemStacks1);
    this.insertionTime = (int) this.virtualMine.getMiningTime(worker.getMainHandItem());
    this.reset();
    return super.enterToPortal();
  }
}
