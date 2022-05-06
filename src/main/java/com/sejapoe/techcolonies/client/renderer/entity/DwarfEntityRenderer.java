package com.sejapoe.techcolonies.client.renderer.entity;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class DwarfEntityRenderer<T extends DwarfEntity> extends MobRenderer<T, DwarfEntityModel<T>> {

  private static final ResourceLocation TEXTURE = new ResourceLocation(TechColonies.MOD_ID, "textures/entity/dwarf.png");

  public DwarfEntityRenderer(EntityRendererProvider.Context context) {
    super(context, new DwarfEntityModel<>(context.bakeLayer(DwarfEntityModel.LAYER_LOCATION)), .5f);
  }

  @Override
  public ResourceLocation getTextureLocation(T entity) {
    return TEXTURE;
  }
}
