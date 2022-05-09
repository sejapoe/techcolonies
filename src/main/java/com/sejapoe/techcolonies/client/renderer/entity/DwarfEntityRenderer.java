package com.sejapoe.techcolonies.client.renderer.entity;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.client.model.DwarfEntityModel;
import com.sejapoe.techcolonies.client.renderer.entity.layer.DwarfFaceElementLayer;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class DwarfEntityRenderer<T extends DwarfEntity> extends MobRenderer<T, DwarfEntityModel<T>> {

  private static final ResourceLocation TEXTURE = new ResourceLocation(TechColonies.MOD_ID, "textures/entity/dwarf.png");

  public DwarfEntityRenderer(EntityRendererProvider.Context context) {
    super(context, new DwarfEntityModel<>(context.bakeLayer(DwarfEntityModel.LAYER_LOCATION)), .5f);
    this.addLayer(new DwarfFaceElementLayer<>(this, context));
  }

  @Override
  public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
    return TEXTURE;
  }
}
