package com.sejapoe.techcolonies.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.Random;

public abstract class MoltenMetal extends ForgeFlowingFluid {
  protected final double solidificationRatio;

  public MoltenMetal(Properties properties, double solidificationRatio) {
    super(properties);
    this.solidificationRatio = solidificationRatio;
  }

  public static class Source extends MoltenMetal {
    public Source(Properties properties, double solidificationRatio) {
      super(properties, solidificationRatio);
    }

    @Override
    public boolean isSource(FluidState p_76140_) {
      return true;
    }

    @Override
    public int getAmount(FluidState p_164509_) {
      return 8;
    }

    @Override
    protected boolean isRandomlyTicking() {
      return true;
    }


    @Override
    protected void randomTick(Level level, BlockPos blockPos, FluidState fluidState, Random random) {
      if (random.nextDouble() <= this.solidificationRatio) {
        level.setBlockAndUpdate(blockPos, Blocks.STONE.defaultBlockState());
      }
    }
  }

  public static class Flowing extends MoltenMetal {
    public Flowing(Properties properties, double solidificationRatio) {
      super(properties, solidificationRatio);
      registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
    }

    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> stateBuilder) {
      super.createFluidStateDefinition(stateBuilder);
      stateBuilder.add(LEVEL);
    }

    public int getAmount(FluidState fluidState) {
      return fluidState.getValue(LEVEL);
    }

    public boolean isSource(FluidState fluidState) {
      return false;
    }
  }
}
