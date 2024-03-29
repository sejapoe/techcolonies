package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidInterfaceBlockEntity extends AbstractInterfaceBlockEntity  {
  final FluidTank tank = new FluidTank(8000);
  LazyOptional<IFluidHandler> handler;
  public FluidInterfaceBlockEntity(BlockPos blockPos, BlockState state) {
    super(ModBlockEntities.FLUID_INTERFACE_BE.get(), blockPos, state);
  }

  @Override
  public void load(@NotNull CompoundTag compoundTag) {
    super.load(compoundTag);
    tank.readFromNBT(compoundTag);
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag compoundTag) {
    super.saveAdditional(compoundTag);
    tank.writeToNBT(compoundTag);
  }

  @NotNull
  @Override
  public LazyOptional<IFluidHandler> getCapability(@NotNull Capability cap, @Nullable Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      if (this.handler == null)
        this.handler = LazyOptional.of(this::createHandler);
      return this.handler.cast();
    }
    return super.getCapability(cap, side);
  }

  private @NotNull IFluidHandler createHandler() {
    return this.tank;
  }
}
