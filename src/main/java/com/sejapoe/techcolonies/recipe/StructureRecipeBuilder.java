package com.sejapoe.techcolonies.recipe;

import com.google.gson.JsonObject;
import com.sejapoe.techcolonies.core.FluidIngredient;
import com.sejapoe.techcolonies.core.IRecipeTypeInfo;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class StructureRecipeBuilder<T extends StructureRecipe> {
  private final StructureRecipeFactory<T> factory;
  private final StructureRecipesParams params;

  public StructureRecipeBuilder(StructureRecipeFactory<T> factory, ResourceLocation id) {
    this.params = new StructureRecipesParams(id);
    this.factory = factory;
  }

  public StructureRecipeBuilder<T> withItemIngredients(Ingredient... ingredients) {
    return withItemIngredients(NonNullList.of(Ingredient.EMPTY, ingredients));
  }
  public StructureRecipeBuilder<T> withItemIngredients(NonNullList<Ingredient> ingredients) {
    params.ingredients = ingredients;
    return this;
  }

  public StructureRecipeBuilder<T> withItemResults(ItemStack... results) {
    return withItemResults(NonNullList.of(ItemStack.EMPTY, results));
  }
  public StructureRecipeBuilder<T> withItemResults(NonNullList<ItemStack> results) {
    params.results = results;
    return this;
  }

  public StructureRecipeBuilder<T> withFluidIngredients(FluidIngredient... fluidIngredients) {
    return withFluidIngredients(NonNullList.of(FluidIngredient.EMPTY, fluidIngredients));
  }
  public StructureRecipeBuilder<T> withFluidIngredients(NonNullList<FluidIngredient> fluidIngredients) {
    params.fluidIngredients = fluidIngredients;
    return this;
  }

  public StructureRecipeBuilder<T> withFluidResults(FluidStack... fluidResults) {
    return withFluidResults(NonNullList.of(FluidStack.EMPTY, fluidResults));
  }
  public StructureRecipeBuilder<T> withFluidResults(NonNullList<FluidStack> fluidStacks) {
    params.fluidResults = fluidStacks;
    return this;
  }

  public StructureRecipeBuilder<T> duration(int processingTime) {
    params.processingDuration = processingTime;
    return this;
  }

  public StructureRecipeBuilder<T> structureLevel(int structureLevel) {
    params.structureLevel = structureLevel;
    return this;
  }

  public T build() {
    return factory.create(params);
  }

  public void build(Consumer<FinishedRecipe> consumer) {
    consumer.accept(new DataGenResult<>(build()));
  }

  @FunctionalInterface
  public interface StructureRecipeFactory<T extends StructureRecipe> {
    T create(StructureRecipesParams params);
  }

  public static class StructureRecipesParams {
    protected final ResourceLocation id;
    protected NonNullList<Ingredient> ingredients;
    protected NonNullList<ItemStack> results;
    protected NonNullList<FluidIngredient> fluidIngredients;
    protected NonNullList<FluidStack> fluidResults;
    protected int processingDuration;
    protected int structureLevel;


    protected StructureRecipesParams(ResourceLocation id) {
      this.id = id;
      this.ingredients = NonNullList.create();
      this.results = NonNullList.create();
      this.fluidIngredients = NonNullList.create();
      this.fluidResults = NonNullList.create();
      this.processingDuration = 0;
      this.structureLevel = 0;
    }
  }

  public static class DataGenResult<S extends StructureRecipe> implements FinishedRecipe {
    private final StructureRecipeSerializer<S> serializer;
    private final ResourceLocation id;
    private final S recipe;

    public DataGenResult(S recipe) {
      this.recipe = recipe;
      IRecipeTypeInfo recipeType = this.recipe.getTypeInfo();
      ResourceLocation typeId = recipeType.getId();
      if (!(recipeType.getSerializer() instanceof StructureRecipeSerializer))
        throw new IllegalStateException("Cannot datagen ProcessingRecipe of type: " + typeId);

      this.id = new ResourceLocation(recipe.getId().getNamespace(),
              typeId.getPath() + "/" + recipe.getId().getPath());
      this.serializer = (StructureRecipeSerializer<S>) recipe.getSerializer();
    }

    @Override
    public void serializeRecipeData(@NotNull JsonObject jsonObject) {
      serializer.writeToJson(jsonObject, recipe);
    }

    @Override
    public @NotNull ResourceLocation getId() {
      return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getType() {
      return serializer;
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
      return null;
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
      return null;
    }
  }
}
