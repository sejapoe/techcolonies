package com.sejapoe.techcolonies.client.event;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.client.renderer.entity.DwarfEntityModel;
import com.sejapoe.techcolonies.client.renderer.entity.DwarfEntityRenderer;
import com.sejapoe.techcolonies.core.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TechColonies.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientRegistryModEvents {
  @SubscribeEvent
  public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    event.registerLayerDefinition(DwarfEntityModel.LAYER_LOCATION, DwarfEntityModel::createBodyLayer);
  }

  @SubscribeEvent
  public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(ModEntities.DWARF_ENTITY.get(), DwarfEntityRenderer::new);
  }
}
