package com.sejapoe.techcolonies.block;

import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import net.minecraft.world.level.block.WallBlock;

public class PlatedBrickWallBlock extends WallBlock implements IPlatedBlock{
  private final PlatingMaterial platingMaterial;

  public PlatedBrickWallBlock(PlatingMaterial material, Properties properties) {
    super(properties);
    this.platingMaterial = material;
  }

  @Override
  public PlatingMaterial getPlatingMaterial() {
    return platingMaterial;
  }
}
