package com.sejapoe.techcolonies.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.client.model.faceelement.beard.AbstractBeardModel;
import com.sejapoe.techcolonies.core.properties.faceelement.BeardType;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class DwarfFaceElementLayer<T extends DwarfEntity, M extends EntityModel<T>>  extends RenderLayer<T, M> {
  private final Map<BeardType, AbstractBeardModel> beardModelMap = new HashMap<>();

  public DwarfFaceElementLayer(RenderLayerParent<T, M> parent, EntityRendererProvider.Context context) {
    super(parent);
    for (BeardType beardType : BeardType.values()) {
      beardModelMap.put(beardType, beardType.buildModel(context));
    }
  }

  @Override
  public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
    AbstractBeardModel model = beardModelMap.get(entity.getBeardType());
    if (model != null) {
      poseStack.pushPose();
      model.setupAnim(entity,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch);
      VertexConsumer vertexConsumer = bufferSource.getBuffer(model.renderType(new ResourceLocation(TechColonies.MOD_ID, "123")));
      model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, .0f, .0f, .0f, 1.0f);
      poseStack.popPose();
    }
  }
}
