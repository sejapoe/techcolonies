package com.sejapoe.techcolonies.core.event;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.ModEntities;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TechColonies.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryModEvents {
  @SubscribeEvent
  public static void registerAttributes(EntityAttributeCreationEvent event) {
    event.put(ModEntities.DWARF_ENTITY.get(), DwarfEntity.createAttributes().build());
  }
}
