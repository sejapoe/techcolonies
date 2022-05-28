package com.sejapoe.techcolonies.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sejapoe.techcolonies.core.helper.FluidHelper;
import com.sejapoe.techcolonies.core.FluidIngredient;
import com.sejapoe.techcolonies.core.helper.ItemHelper;
import com.sejapoe.techcolonies.recipe.StructureRecipeBuilder.StructureRecipeFactory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StructureRecipeSerializer<T extends StructureRecipe> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T> {
  private final StructureRecipeFactory<T> factory;

  public StructureRecipeSerializer(StructureRecipeFactory<T> factory) {
    this.factory = factory;
  }

  public void writeToJson(JsonObject json, T recipe) {
    JsonArray jsonIngredients = new JsonArray();
    JsonArray jsonOutputs = new JsonArray();

    recipe.getIngredients()
            .forEach(i -> jsonIngredients.add(i.toJson()));
    recipe.getFluidIngredients()
            .forEach(i -> jsonIngredients.add(i.serialize()));

    recipe.getResults()
            .forEach(o -> jsonOutputs.add(ItemHelper.serialize(o)));
    recipe.getFluidResults()
            .forEach(o -> jsonOutputs.add(FluidHelper.serializeFluidStack(o)));

    json.add("ingredients", jsonIngredients);
    json.add("results", jsonOutputs);

    int processingDuration = recipe.getProcessingDuration();
    if (processingDuration > 0)
      json.addProperty("processingTime", processingDuration);

    int structureLevel = recipe.getRequiredStructureLevel();
    json.addProperty("structureLevel", structureLevel);
  }

  protected T readFromJson(ResourceLocation id, JsonObject json) {
    StructureRecipeBuilder<T> builder = new StructureRecipeBuilder<>(factory, id);
    NonNullList<Ingredient> ingredients = NonNullList.create();
    NonNullList<FluidIngredient> fluidIngredients = NonNullList.create();
    NonNullList<ItemStack> results = NonNullList.create();
    NonNullList<FluidStack> fluidResults = NonNullList.create();
    for (JsonElement je : GsonHelper.getAsJsonArray(json, "ingredients")) {
      if (FluidIngredient.isFluidIngredient(je)) {
        fluidIngredients.add(FluidIngredient.deserialize(je));
      } else {
        ingredients.add(Ingredient.fromJson(je));
      }
    }

    for (JsonElement je : GsonHelper.getAsJsonArray(json,"results")) {
      JsonObject jsonObject = je.getAsJsonObject();
      if (GsonHelper.isValidNode(jsonObject, "fluid")) {
        fluidResults.add(FluidHelper.deserializeFluidStack(jsonObject));
      } else {
        results.add(ItemHelper.deserialize(je));
      }
    }

    builder.withItemIngredients(ingredients).withItemResults(results).withFluidIngredients(fluidIngredients).withFluidResults(fluidResults);

    if (GsonHelper.isValidNode(json, "processingTime")) {
      builder.duration(GsonHelper.getAsInt(json, "processingTime"));
    }

    if (GsonHelper.isValidNode(json, "structureLevel")) {
      builder.structureLevel(GsonHelper.getAsInt(json, "structureLevel"));
    }

    return builder.build();
  }

  protected void writeToBuffer(FriendlyByteBuf buffer, T recipe) {
    NonNullList<Ingredient> ingredients = recipe.getIngredients();
    NonNullList<FluidIngredient> fluidIngredients = recipe.getFluidIngredients();
    NonNullList<ItemStack> outputs = recipe.getResults();
    NonNullList<FluidStack> fluidOutputs = recipe.getFluidResults();

    buffer.writeVarInt(ingredients.size());
    ingredients.forEach(i -> i.toNetwork(buffer));
    buffer.writeVarInt(fluidIngredients.size());
    fluidIngredients.forEach(i -> i.write(buffer));

    buffer.writeVarInt(outputs.size());
    outputs.forEach(buffer::writeItem);
    buffer.writeVarInt(fluidOutputs.size());
    fluidOutputs.forEach(o -> o.writeToPacket(buffer));

    buffer.writeVarInt(recipe.getProcessingDuration());
    buffer.writeVarInt(recipe.getRequiredStructureLevel());
  }

  protected T readFromBuffer(ResourceLocation recipeId, FriendlyByteBuf buffer) {
    NonNullList<Ingredient> ingredients = NonNullList.create();
    NonNullList<FluidIngredient> fluidIngredients = NonNullList.create();
    NonNullList<ItemStack> results = NonNullList.create();
    NonNullList<FluidStack> fluidResults = NonNullList.create();

    int size = buffer.readVarInt();
    for (int i = 0; i < size; i++)
      ingredients.add(Ingredient.fromNetwork(buffer));

    size = buffer.readVarInt();
    for (int i = 0; i < size; i++)
      fluidIngredients.add(FluidIngredient.read(buffer));

    size = buffer.readVarInt();
    for (int i = 0; i < size; i++)
      results.add(buffer.readItem());

    size = buffer.readVarInt();
    for (int i = 0; i < size; i++)
      fluidResults.add(FluidStack.readFromPacket(buffer));

    return new StructureRecipeBuilder<>(factory, recipeId)
            .withItemIngredients(ingredients)
            .withItemResults(results)
            .withFluidIngredients(fluidIngredients)
            .withFluidResults(fluidResults)
            .duration(buffer.readVarInt())
            .structureLevel(buffer.readVarInt())
            .build();
  }

  @Override
  public @NotNull T fromJson(@NotNull ResourceLocation id, @NotNull JsonObject jsonObject) {
    return readFromJson(id, jsonObject);
  }

  @Nullable
  @Override
  public T fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
    return readFromBuffer(id, buf);
  }

  @Override
  public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull T recipe) {
    writeToBuffer(buf, recipe);
  }

  public StructureRecipeFactory<T> getFactory() {
    return factory;
  }
}
