package com.sejapoe.techcolonies.core;

import com.sejapoe.techcolonies.TechColonies;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public final class ModFluids {
  public static final FluidDeferredRegister FLUID_REGISTER = new FluidDeferredRegister(TechColonies.MOD_ID);
  public static final FluidDeferredRegister.FluidRegistryObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, LiquidBlock, BucketItem> MOLTEN_COPPER = FLUID_REGISTER.registerMoltenMetal(MetalConstants.COPPER);

  public static void register(IEventBus bus) {
    FLUID_REGISTER.register(bus);
  }
}
