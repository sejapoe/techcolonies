package com.sejapoe.techcolonies.core;

import com.sejapoe.techcolonies.TechColonies;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
  public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, TechColonies.MOD_ID);

//  public static final RegistryObject<Block>
//  EXAMPLE_BLOCK = BLOCK_REGISTER.register("registry_name", () -> new ExampleBlock(...))

  public static void register(IEventBus bus) {
    BLOCK_REGISTER.register(bus);
  }
}
