package com.sejapoe.techcolonies.entity.ai.base.goal;

import com.sejapoe.techcolonies.core.helper.BlockHelper;
import com.sejapoe.techcolonies.core.helper.LevelHelper;
import com.sejapoe.techcolonies.entity.ai.job.base.IJob;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractBreakerAI<T extends IJob> extends AbstractAI<T> {
  private int delay;
  private boolean isBreaking = false;

  public AbstractBreakerAI(T job) {
    super(job);
  }

  @Override
  public void tick() {
    if (isBreaking) delay--;
    super.tick();
  }

  protected boolean breakBlock(BlockPos pos) { // Check efficient tool
    BlockState blockState = level.getBlockState(pos);
    Block block = blockState.getBlock();

    //// Unbreakable
    if (block.equals(Blocks.AIR) || block.equals(Blocks.BEDROCK) || block.defaultDestroyTime() < 0) {
      return true;
    }

    if (!isBreaking) {
      delay = getBreakingTime(blockState, pos);
      isBreaking = true;
    }

    worker.getLookControl().setLookAt(pos.getX(), pos.getY(), pos.getZ());
    worker.swing(InteractionHand.MAIN_HAND);

    if (delay > 0) {
      SoundType soundType = block.getSoundType(blockState, level, pos, worker);
      level.playSound(null,
              pos,
              soundType.getBreakSound(),
              SoundSource.BLOCKS,
              soundType.getVolume(),
              soundType.getPitch());
      return false;
    } else {
      LevelHelper.removeBlock(level, pos);
      isBreaking = false;
      return true;
    }
  }

  private int getBreakingTime(BlockState state, BlockPos pos) {
    return BlockHelper.getBreakingTimeForTool(level, state, this.worker.getMainHandItem());
  }
/*
destroy - 1.5
stone/hand - 8* (2)
stone/wood - 1
stone/stone - 0.5

destroy - 0.5
dirt/hand - 1
dirt/wood - 0.5
dirt/stone - 0.25

destroy - 3
copper/hand - 14* (4)
copper/wooden - 8* (2)
copper/stone - 1

x * 3  / 4 = 20 -> x =
x * y * 3 = 300 -> x*y=100
y = 3.5
x = 28.6
  */

  public int getDelay() {
    return delay;
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }
}
