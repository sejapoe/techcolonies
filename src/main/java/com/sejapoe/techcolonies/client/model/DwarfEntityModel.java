package com.sejapoe.techcolonies.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class DwarfEntityModel<T extends DwarfEntity> extends EntityModel<T> {
  public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(TechColonies.MOD_ID, "dwarf_entity"), "main");
  private final ModelPart body;
  private final ModelPart head;
  private final ModelPart rightArm;
  private final ModelPart leftArm;
  private final ModelPart rightLeg;
  private final ModelPart leftLeg;


  public DwarfEntityModel(ModelPart root) {
    this.body = root.getChild("body");
    this.head = body.getChild("head");
    this.rightArm = body.getChild("right_arm");
    this.leftArm = body.getChild("left_arm");
    this.rightLeg = body.getChild("right_leg");
    this.leftLeg = body.getChild("left_leg");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(25, 11).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(0, 0).addBox(-6.0F, -6.0F, -2.0F, 12.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.0F, 0.0F, 0.0F, 0.0F, 0.0F));

    body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(10, 24).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 3.0F, 0.0F));

    body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(10, 24).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 3.0F, 0.0F));

    body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -4.0F, 0.0F));

    body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 24).mirror().addBox(-2.0F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, -4.0F, 0.0F));

    PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));

    head.addOrReplaceChild("main_r2", CubeListBuilder.create().texOffs(0, 11).addBox(-3.0F, -6.0F, -5.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

    return LayerDefinition.create(meshdefinition, 64, 64);
  }


  @Override
  public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    this.head.yRot = netHeadYaw * ((float)(Math.PI / 180f));
    this.head.xRot = headPitch * ((float)(Math.PI / 180f));
    float legSwing = 1.5F * Mth.triangleWave(limbSwing, 13.f) * limbSwingAmount;
    this.rightLeg.xRot = -legSwing;
    this.leftLeg.xRot = legSwing;
    this.leftLeg.yRot = 0;
    this.rightLeg.yRot = 0;
  }

  @Override
  public void prepareMobModel(@NotNull T entity, float limbSwing, float limbSwingAmount, float partialTick) {
    float armSwing = 1.5f * Mth.triangleWave(limbSwing, 13.f) * limbSwingAmount;
    this.rightArm.xRot = -.2f + armSwing;
    this.leftArm.xRot = -.2f - armSwing;
  }

  @Override
  public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
    body.render(poseStack, buffer, packedLight, packedOverlay);
  }
}
