package com.sejapoe.techcolonies.entity.ai.goal.base;

import com.sejapoe.techcolonies.core.helper.LevelHelper;
import com.sejapoe.techcolonies.entity.DwarfEntity;
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

  protected void breakBlock(BlockPos pos) {
    BlockState blockState = level.getBlockState(pos);
    Block block = blockState.getBlock();

    //// Unbreakable
    if (block.equals(Blocks.AIR) || block.equals(Blocks.BEDROCK) || block.defaultDestroyTime() < 0) {
      return;
    }

    if (!isBreaking) {
      delay = getBreakingTime(block);
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
    } else {
      LevelHelper.removeBlock(level, pos);
      isBreaking = false;
    }
  }

  private int getBreakingTime(Block block) {
    return (int) (block.defaultDestroyTime() * 20.0F);
  }

  public int getDelay() {
    return delay;
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }
}
