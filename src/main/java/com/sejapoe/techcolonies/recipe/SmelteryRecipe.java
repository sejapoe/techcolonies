package com.sejapoe.techcolonies.recipe;

import com.sejapoe.techcolonies.registry.ModRecipeTypes;

public class SmelteryRecipe extends StructureRecipe {

  public SmelteryRecipe(StructureRecipeBuilder.StructureRecipesParams params) {
    super(ModRecipeTypes.SMELTING, params);
  }

  @Override
  protected int getMaxInputCount() {
    return 1;
  }

  @Override
  protected int getMaxOutputCount() {
    return 0;
  }

  @Override
  protected int getMaxFluidOutputCount() {
    return 1;
  }
}
