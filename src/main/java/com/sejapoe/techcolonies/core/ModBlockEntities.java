package com.sejapoe.techcolonies.core;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.block.entity.SmelteryBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, TechColonies.MOD_ID);

  public static final RegistryObject<BlockEntityType<SmelteryBlockEntity>>
          SMELTERY_BE = BLOCK_ENTITY_REGISTER.register("smeltery_block_entity", () -> BlockEntityType.Builder.of(SmelteryBlockEntity::new, ModBlocks.SMELTERY_BLOCK.get()).build(null));

  public static void register(IEventBus bus) {
    BLOCK_ENTITY_REGISTER.register(bus);
  }
}
