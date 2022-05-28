package com.sejapoe.techcolonies.recipe;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.FluidIngredient;
import com.sejapoe.techcolonies.core.IRecipeTypeInfo;
import com.sejapoe.techcolonies.recipe.StructureRecipeBuilder.StructureRecipesParams;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Supplier;

public abstract class StructureRecipe implements Recipe<RecipeWrapper> {
  protected final ResourceLocation id;
  protected final NonNullList<Ingredient> ingredients;
  protected final NonNullList<ItemStack> results;
  protected final NonNullList<FluidIngredient> fluidIngredients;
  protected final NonNullList<FluidStack> fluidResults;
  protected final int processingDuration;
  protected final int requiredStructureLevel;

  private final RecipeType<?> type;
  private final IRecipeTypeInfo typeInfo;
  private final RecipeSerializer<?> serializer;
  private final Supplier<ItemStack> forcedResult;

  public StructureRecipe(IRecipeTypeInfo typeInfo, StructureRecipesParams params) {
    this.forcedResult = null;
    this.typeInfo = typeInfo;
    this.ingredients = params.ingredients;
    this.results = params.results;
    this.fluidIngredients = params.fluidIngredients;
    this.fluidResults = params.fluidResults;
    this.processingDuration = params.processingDuration;
    this.requiredStructureLevel = params.structureLevel;
    this.id = params.id;
    this.type = typeInfo.getType();
    this.serializer = typeInfo.getSerializer();

    validate(typeInfo.getId());
  }

  protected abstract int getMaxInputCount();
  protected abstract int getMaxOutputCount();

  protected boolean canSpecifyDuration() {
    return true;
  }

  protected boolean canSpecifyLevel() {
    return true;
  }

  protected int getMaxFluidInputCount() {
    return 0;
  }

  protected int getMaxFluidOutputCount() {
    return 0;
  }

  private void validate(ResourceLocation id) {
    String messageHeader = "Your custom " + id + " recipe (" + id.toString() + ")";
    Logger logger = TechColonies.LOGGER;
    int ingredientCount = ingredients.size();
    int outputCount = results.size();

    if (ingredientCount > getMaxInputCount())
      logger.warn(messageHeader + " has more item inputs (" + ingredientCount + ") than supported ("
              + getMaxInputCount() + ").");

    if (outputCount > getMaxOutputCount())
      logger.warn(messageHeader + " has more item outputs (" + outputCount + ") than supported ("
              + getMaxOutputCount() + ").");

    if (processingDuration > 0 && !canSpecifyDuration())
      logger.warn(messageHeader + " specified a duration. Durations have no impact on this type of recipe.");

    if (requiredStructureLevel != 0 && !canSpecifyLevel())
      logger.warn(
              messageHeader + " specified a heat condition. Heat conditions have no impact on this type of recipe.");

    ingredientCount = fluidIngredients.size();
    outputCount = fluidResults.size();

    if (ingredientCount > getMaxFluidInputCount())
      logger.warn(messageHeader + " has more fluid inputs (" + ingredientCount + ") than supported ("
              + getMaxFluidInputCount() + ").");

    if (outputCount > getMaxFluidOutputCount())
      logger.warn(messageHeader + " has more fluid outputs (" + outputCount + ") than supported ("
              + getMaxFluidOutputCount() + ").");
  }

  @Override
  public @NotNull NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }

  public NonNullList<ItemStack> getResults() {
    return results;
  }

  public NonNullList<FluidIngredient> getFluidIngredients() {
    return fluidIngredients;
  }

  public NonNullList<FluidStack> getFluidResults() {
    return fluidResults;
  }

  public int getProcessingDuration() {
    return processingDuration;
  }

  public int getRequiredStructureLevel() {
    return requiredStructureLevel;
  }

  @Override
  public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
    return true;
  }

  @Override
  public @NotNull ItemStack getResultItem() {
    return getResults().isEmpty() ? ItemStack.EMPTY : getResults().get(0);
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public @NotNull String getGroup() {
    return "structure";
  }

  @Override
  public @NotNull ResourceLocation getId() {
    return id;
  }

  @Override
  public @NotNull RecipeSerializer<?> getSerializer() {
    return serializer;
  }

  @Override
  public @NotNull RecipeType<?> getType() {
    return type;
  }

  public IRecipeTypeInfo getTypeInfo() {
    return typeInfo;
  }

  @Override
  public boolean matches(@NotNull RecipeWrapper pContainer, @NotNull Level pLevel) {
    return false;
  }

  @Override
  public @NotNull ItemStack assemble(@NotNull RecipeWrapper pContainer) {
    return getResultItem();
  }

  public boolean matches(List<ItemStack> input, List<FluidStack> fluidInput) {
    return ingredients.stream().allMatch(ingredient -> input.stream().anyMatch(ingredient)) &&
            fluidIngredients.stream().allMatch(fluidIngredient -> fluidInput.stream().anyMatch(fluidIngredient));
  }

  public Supplier<ItemStack> getForcedResult() {
    return forcedResult;
  }
}
