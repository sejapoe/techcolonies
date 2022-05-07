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

public class DefaultBeardModel<T extends DwarfEntity> extends AbstractBeardModel<T> {
  public static final ModelLayerLocation LAYER_LOCATION = createModelLayerLocation("default");

  public DefaultBeardModel(ModelPart root) {
    super(root);
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
}
