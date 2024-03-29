package com.sejapoe.techcolonies.block;

import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import net.minecraft.world.level.block.Block;

public class PlatedBricksBlock extends Block implements IPlatedBlock {
  private final PlatingMaterial platingMaterial;

  public PlatedBricksBlock(PlatingMaterial material, Properties properties) {
    super(properties);
    this.platingMaterial = material;
  }


  @Override
  public PlatingMaterial getPlatingMaterial() {
    return platingMaterial;
  }
}
