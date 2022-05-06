package com.sejapoe.techcolonies.client.model.faceelement.beard;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.function.Function;

public class DefaultBeardModel<T extends DwarfEntity> extends Model {
  public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(TechColonies.MOD_ID, "face/beard/default"), "main");
  private final ModelPart main;

  public DefaultBeardModel(ModelPart root) {
    super(RenderType::entityCutoutNoCull);
    this.main = root.getChild("beard");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    partdefinition.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(0, 37).addBox(3.0F, -1.0F, -5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 37).addBox(-4.0F, -1.0F, -5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 37).addBox(-5.0F, -1.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 37).addBox(4.0F, -1.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 37).addBox(-3.0F, 0.0F, -5.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 37).addBox(-5.0F, -1.0F, -6.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 37).addBox(-4.0F, 0.0F, -6.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 37).addBox(-2.0F, 0.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 37).addBox(-3.0F, -1.0F, -7.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 37).addBox(-1.0F, 1.0F, -7.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 37).addBox(-3.0F, 1.0F, -6.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 37).addBox(-2.0F, 2.0F, -6.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(0, 37).addBox(-2.0F, 3.0F, -5.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

    return LayerDefinition.create(meshdefinition, 64, 64);
  }

  public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    this.main.yRot = netHeadYaw * ((float)(Math.PI / 180f));
    this.main.xRot = headPitch * ((float)(Math.PI / 180f));
  }

  @Override
  public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
    main.render(poseStack, buffer, packedLight, packedOverlay);
  }

}
