package com.sejapoe.techcolonies.recipe;

import com.sejapoe.techcolonies.registry.ModRecipeTypes;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

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

  @Override
  public boolean matches(@NotNull Container p_44002_, @NotNull Level p_44003_) {
    return false;
  }
}
