package com.sejapoe.techcolonies.datagen.server;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.IRecipeTypeInfo;
import com.sejapoe.techcolonies.recipe.StructureRecipe;
import com.sejapoe.techcolonies.recipe.StructureRecipeBuilder;
import com.sejapoe.techcolonies.recipe.StructureRecipeSerializer;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class BaseStructureRecipeProvider extends BaseCustomRecipeProvider {
  public BaseStructureRecipeProvider(DataGenerator generator) {
    super(generator);
  }

  protected <T extends StructureRecipe> GeneratedRecipe create(String name, UnaryOperator<StructureRecipeBuilder<T>> transform) {
    return create(new ResourceLocation(TechColonies.MOD_ID, name), transform);
  }

  protected <T extends StructureRecipe> GeneratedRecipe create(ResourceLocation id, UnaryOperator<StructureRecipeBuilder<T>> transform) {
    return createWithDeferredId(() -> id, transform);
  }

  private <T extends StructureRecipe> GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> id, UnaryOperator<StructureRecipeBuilder<T>> transform) {
    StructureRecipeSerializer<T> serializer = getSerializer();
    GeneratedRecipe recipe = consumer -> transform.apply(new StructureRecipeBuilder<>(serializer.getFactory(), id.get())).build(consumer);
    generatedRecipes.add(recipe);
    return recipe;
  }

  protected abstract IRecipeTypeInfo getRecipeType();

  private <T extends StructureRecipe> StructureRecipeSerializer<T> getSerializer() {
    return getRecipeType().getSerializer();
  }
}
