package com.sejapoe.techcolonies.core;

public enum MetalConstants implements IMetalConstant{
  COPPER("copper", 0xD67B5B93, 1500, 15, 2);

  private String name;
  private int color;
  private float liquidTemperature;
  private float liquidDensity;
  private int liquidLuminosity;

  MetalConstants(String name, int color, float liquidTemperature, float liquidDensity, int liquidLuminosity) {
    this.name = name;
    this.color = color;
    this.liquidTemperature = liquidTemperature;
    this.liquidDensity = liquidDensity;
    this.liquidLuminosity = liquidLuminosity;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public int getColor() {
    return this.color;
  }

  @Override
  public float getLiquidTemperature() {
    return this.liquidTemperature;
  }

  @Override
  public float getLiquidDensity() {
    return this.liquidDensity;
  }

  @Override
  public int getLiquidLuminosity() {
    return this.liquidLuminosity;
  }
}
