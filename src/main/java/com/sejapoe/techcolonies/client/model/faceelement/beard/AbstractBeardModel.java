package com.sejapoe.techcolonies.client.model.faceelement.beard;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractBeardModel<T extends DwarfEntity> extends Model {
  public static ModelLayerLocation LAYER_LOCATION;
  protected final ModelPart main;

  public AbstractBeardModel(ModelPart root) {
    super(RenderType::entityCutoutNoCull);
    this.main = root.getChild("beard");
  }

  protected static ModelLayerLocation createModelLayerLocation(String name) {
    return new ModelLayerLocation(new ResourceLocation(TechColonies.MOD_ID, "face/beard/" + name), "main");
  }

  public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    this.main.yRot = netHeadYaw * ((float)(Math.PI / 180f));
    this.main.xRot = headPitch * ((float)(Math.PI / 180f));
  }

  @Override
  public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
    main.render(poseStack, buffer, packedLight, packedOverlay);
  }
}
