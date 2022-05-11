package com.sejapoe.techcolonies.registry;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.FluidDeferredRegister;
import com.sejapoe.techcolonies.core.FluidDeferredRegister.FluidRegistryObject;
import com.sejapoe.techcolonies.core.MetalConstants;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public final class ModFluids {
  public static final FluidDeferredRegister FLUID_REGISTER = new FluidDeferredRegister(TechColonies.MOD_ID);
  public static final FluidRegistryObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, LiquidBlock, BucketItem> MOLTEN_COPPER = FLUID_REGISTER.registerMoltenMetal(MetalConstants.COPPER);
  public static final FluidRegistryObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, LiquidBlock, BucketItem> MOLTEN_IRON = FLUID_REGISTER.registerMoltenMetal(MetalConstants.IRON);

  public static void register(IEventBus bus) {
    FLUID_REGISTER.register(bus);
  }
}
