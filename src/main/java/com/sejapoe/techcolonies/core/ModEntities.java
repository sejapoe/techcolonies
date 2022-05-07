package com.sejapoe.techcolonies.core;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class ModEntities {
  public static final DeferredRegister<EntityType<?>> ENTITY_REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, TechColonies.MOD_ID);

  public static final RegistryObject<EntityType<DwarfEntity>> DWARF_ENTITY = register("dwarf_entity",
          () -> EntityType.Builder.of(DwarfEntity::new, MobCategory.CREATURE).sized(0.8f, 1.3f));

  private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> sup) {
    ResourceLocation location = new ResourceLocation(TechColonies.MOD_ID, name);
    return ENTITY_REGISTER.register(name, () -> sup.get().build(location.toString()));
  }

  public static void register(IEventBus bus) {
    ENTITY_REGISTER.register(bus);
  }
}
