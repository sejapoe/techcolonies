package com.sejapoe.techcolonies.datagen.server;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class BaseCustomRecipeProvider extends RecipeProvider {
  protected final List<GeneratedRecipe> generatedRecipes = new ArrayList<>();

  public BaseCustomRecipeProvider(DataGenerator generator) {
    super(generator);
  }

  protected GeneratedRecipe register(GeneratedRecipe recipe) {
    generatedRecipes.add(recipe);
    return recipe;
  }

  @Override
  protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
    generatedRecipes.forEach(generatedRecipe -> generatedRecipe.register(consumer));
  }
  @FunctionalInterface
  protected interface GeneratedRecipe {
    void register(Consumer<FinishedRecipe> consumer);
  }
}
