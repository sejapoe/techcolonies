package com.sejapoe.techcolonies.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.sejapoe.techcolonies.client.model.DwarfEntityModel;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DwarfItemInHandLayer extends ItemInHandLayer<DwarfEntity, DwarfEntityModel> {
  public DwarfItemInHandLayer(RenderLayerParent<DwarfEntity, DwarfEntityModel> parent) {
    super(parent);
  }

  @Override
  protected void renderArmWithItem(@NotNull LivingEntity livingEntity, ItemStack stack, ItemTransforms.@NotNull TransformType transformType, @NotNull HumanoidArm arm, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
    if (stack.isEmpty()) return;
    poseStack.pushPose();
    poseStack.scale(0.5F,0.5F,0.5F);
    poseStack.translate(0.0F,1.55F,0.0F);
    this.getParentModel().translateToHand(arm, poseStack);
    boolean flag = arm == HumanoidArm.LEFT;
    poseStack.translate(((float)(flag ? 1 : -1) * 0.45F), 1.0D, -0.1D);
    poseStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
    Minecraft.getInstance().getItemInHandRenderer().renderItem(livingEntity, stack, transformType, flag, poseStack, buffer, packedLight);
    poseStack.popPose();
//    super.renderArmWithItem(livingEntity, stack, transformType, arm, poseStack, buffer, packedLight);
  }
}
