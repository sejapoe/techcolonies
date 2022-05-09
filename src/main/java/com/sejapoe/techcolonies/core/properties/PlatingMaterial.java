package com.sejapoe.techcolonies.core.properties;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PlatingMaterial implements StringRepresentable {
  NONE("none", 0),
  COPPER("copper", 1),
  DIAMOND("diamond", 2);
  private final String name;
  private final int level;

  PlatingMaterial(String name, int level) {
    this.name = name;
    this.level = level;
  }

  public String getName() {
    return name;
  }

  @Override
  public @NotNull String getSerializedName() {
    return name;
  }

  public int getLevel() {
    return level;
  }

  public static PlatingMaterial valueOf(int level) {
    for (PlatingMaterial material : PlatingMaterial.values())
      if (material.getLevel() == level) {
        return material;
      }
    return null;
  }
}
