package com.sejapoe.techcolonies.datagen.server;

import com.sejapoe.techcolonies.core.IRecipeTypeInfo;
import com.sejapoe.techcolonies.registry.ModFluids;
import com.sejapoe.techcolonies.registry.ModRecipeTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

public class SmelteryRecipeProvider extends BaseStructureRecipeProvider {
  GeneratedRecipe MOLTEN_COPPER = createMelt("molten_copper", Ingredient.of(Tags.Items.INGOTS_COPPER), new FluidStack(ModFluids.MOLTEN_COPPER.getStillFluid(), 1000), 200, 0);

  private GeneratedRecipe createMelt(String name, Ingredient from, FluidStack to, int processingTime, int structureLevel) {
    return create(name, builder -> builder.withItemIngredients(from).withFluidResults(to).duration(processingTime).structureLevel(structureLevel));
  }

  public SmelteryRecipeProvider(DataGenerator generator) {
    super(generator);
  }

  @Override
  protected IRecipeTypeInfo getRecipeType() {
    return ModRecipeTypes.SMELTING;
  }
}
