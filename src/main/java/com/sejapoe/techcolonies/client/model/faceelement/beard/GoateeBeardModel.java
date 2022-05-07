package com.sejapoe.techcolonies.client.model.faceelement.beard;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class GoateeBeardModel extends AbstractBeardModel{
  public static final ModelLayerLocation LAYER_LOCATION = createModelLayerLocation("goatee");

  public GoateeBeardModel(ModelPart root) {
    super(root);
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    partdefinition.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(1, 38).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

    return LayerDefinition.create(meshdefinition, 64, 64);
  }
}
