package com.sejapoe.techcolonies.core.properties;

import net.minecraft.util.StringRepresentable;

public enum InterfaceDirection implements StringRepresentable {
  INPUT("input"),
  OUTPUT("output");
  private final String name;

  InterfaceDirection(String name) {
    this.name = name;
  }


  @Override
  public String getSerializedName() {
    return this.name;
  }

  public InterfaceDirection getOpposite() {
    return this == INPUT ? OUTPUT : INPUT;
  }
}
