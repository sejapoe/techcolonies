package com.sejapoe.techcolonies.core.properties;

import net.minecraft.util.StringRepresentable;

public enum PlatingMaterial implements StringRepresentable {
  NONE("none"),
  COPPER("copper"),
  DIAMOND("diamond");
  private final String name;

  private PlatingMaterial(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String getSerializedName() {
    return name;
  }
}
