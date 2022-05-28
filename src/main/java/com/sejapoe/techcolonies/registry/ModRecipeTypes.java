package com.sejapoe.techcolonies.registry;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.IRecipeTypeInfo;
import com.sejapoe.techcolonies.recipe.SmelteryRecipe;
import com.sejapoe.techcolonies.recipe.StructureRecipe;
import com.sejapoe.techcolonies.recipe.StructureRecipeBuilder.StructureRecipeFactory;
import com.sejapoe.techcolonies.recipe.StructureRecipeSerializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.RegistryEvent;

import java.util.Locale;
import java.util.function.Supplier;

public enum ModRecipeTypes implements IRecipeTypeInfo {
  SMELTING(SmelteryRecipe::new);

  private final Supplier<RecipeType<? extends StructureRecipe>> typeSupplier;
  private final Supplier<RecipeSerializer<? extends StructureRecipe>> serializerSuppplier;
  private final ResourceLocation id;
  private RecipeSerializer<? extends StructureRecipe> serializer;
  private RecipeType<? extends StructureRecipe> type;

  ModRecipeTypes(Supplier<RecipeSerializer<? extends StructureRecipe>> serializerSupplier) {
    this.id = new ResourceLocation(TechColonies.MOD_ID, name().toLowerCase(Locale.ROOT));
    this.serializerSuppplier = serializerSupplier;
    this.typeSupplier = () -> simpleType(this.id);
  }

  ModRecipeTypes(StructureRecipeFactory<? extends StructureRecipe> factory) {
    this(() -> new StructureRecipeSerializer<>(factory));
  }


  @Override
  public ResourceLocation getId() {
    return id;
  }

  @Override
  public RecipeSerializer<? extends StructureRecipe> getSerializer() {
    return serializer;
  }

  @Override
  public RecipeType<? extends StructureRecipe> getType() {
    return type;
  }
  public static <T extends Recipe<?>> RecipeType<T> simpleType(ResourceLocation id) {
    String stringId = id.toString();
    return Registry.register(Registry.RECIPE_TYPE, id, new RecipeType<T>(){
      @Override
      public String toString() {
        return stringId;
      }
    });
  }

  public static void register(RegistryEvent.Register<RecipeSerializer<?>> event) {
    for (ModRecipeTypes r : values()) {
      r.serializer = r.serializerSuppplier.get();
      r.type = r.typeSupplier.get();
      r.serializer.setRegistryName(r.id);
      event.getRegistry().register(r.serializer);
    }
  }
}
