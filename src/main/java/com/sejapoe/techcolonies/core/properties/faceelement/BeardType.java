package com.sejapoe.techcolonies.core.properties.faceelement;

import com.sejapoe.techcolonies.client.model.faceelement.beard.AbstractBeardModel;
import com.sejapoe.techcolonies.client.model.faceelement.beard.DefaultBeardModel;
import com.sejapoe.techcolonies.client.model.faceelement.beard.GoateeBeardModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public enum BeardType implements StringRepresentable {
  DEFAULT("default", DefaultBeardModel::new, DefaultBeardModel.LAYER_LOCATION),
  GOATEE("goatee", GoateeBeardModel::new, GoateeBeardModel.LAYER_LOCATION);

  private static final int SIZE = values().length;
  private static final Random RANDOM = new Random();
  private final String name;
  private final Function<ModelPart, ? extends AbstractBeardModel> modelFunc;
  private final ModelLayerLocation layerLocation;

  BeardType(String name, Function<ModelPart, ? extends AbstractBeardModel> modelFunc, ModelLayerLocation layerLocation) {
    this.name = name;
    this.modelFunc = modelFunc;
    this.layerLocation = layerLocation;
  }

  public static BeardType getRandom() {
    return values()[RANDOM.nextInt(SIZE)];
  }

  @Override
  public String getSerializedName() {
    return name;
  }

  public static BeardType getByName(String name) {
    for (BeardType beardType : values()) {
      if (beardType.name.equals(name)) {
        return beardType;
      }
    }
    return null;
  }

  public Function<ModelPart, ? extends AbstractBeardModel> getModelFunc() {
    return modelFunc;
  }

  public ModelLayerLocation getLayerLocation() {
    return layerLocation;
  }

  public AbstractBeardModel buildModel(EntityRendererProvider.Context context) {
    return getModelFunc().apply(context.bakeLayer(getLayerLocation()));
  }
}
